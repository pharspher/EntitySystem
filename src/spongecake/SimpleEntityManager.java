package spongecake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import spongecake.EntityManager.Entity;

public class SimpleEntityManager extends EntityManager {
    private static final String TAG = SimpleEntityManager.class.getSimpleName();

    private static int mEntityCount = 0;

    private HashMap<String, HashMap<Integer, ArrayList<Component>>> mComponentStores;

    @Override
    public Entity obtainEntityImpl() {
        return new Entity(mEntityCount++);
    }

    @Override
    public <T extends Component> EntityManager addComponent(Entity entity, T component) {
        String componentName = component.getClass().getSimpleName();
        int entityID = entity.mID;

        HashMap<Integer, ArrayList<Component>> componentStore = mComponentStores.get(componentName);
        if (componentStore == null) {
            componentStore = new HashMap<Integer, ArrayList<Component>>();
        }

        ArrayList<Component> componentList = componentStore.get(entityID);
        if (componentList == null) {
            componentList = new ArrayList<Component>();
        }

        componentList.add(component);
        componentStore.put(entityID, componentList);
        mComponentStores.put(componentName, componentStore);

        return this;
    }

    @Override
    public ArrayList<Component> getComponent(Class<? extends Component> componentClass) {
        ArrayList<Component> components = new ArrayList<Component>();

        HashMap<Integer, ArrayList<Component>> componentStore = mComponentStores.get(componentClass.getSimpleName());
        Set<Integer> entityIDs = componentStore.keySet();
        for (Integer entityID : entityIDs) {
            components.addAll(componentStore.get(entityID));
        }
        return components;
    }

    public ArrayList<Component> getComponent(Entity entity, Class<? extends Component> componentClass) {
        ArrayList<Component> components = new ArrayList<Component>();

        HashMap<Integer, ArrayList<Component>> componentStore = mComponentStores.get(componentClass.getSimpleName());
        components.addAll(componentStore.get(entity.mID));
        return components;
    }

    @Override
    public void removeComponent(Entity entity, Component component) {
        String componentName = component.getClass().getSimpleName();
        int entityID = entity.mID;

        HashMap<Integer, ArrayList<Component>> componentStore = mComponentStores.get(componentName);
        if (componentStore == null) {
            throw new IllegalStateException(componentName + " is not component of entity " + entityID);
        }

        ArrayList<Component> componentList = componentStore.get(entityID);
        if (componentList == null) {
            throw new IllegalStateException(componentName + " is not component of entity " + entityID);
        }

        int componentIndex = componentList.indexOf(component);
        if (componentIndex == -1) {
            throw new IllegalStateException(componentName + " is not component of entity " + entityID);
        }

        componentList.remove(componentIndex);
        if (componentList.isEmpty()) {
            componentStore.remove(entityID);
        }

        if (componentStore.isEmpty()) {
            mComponentStores.remove(componentName);
        }
    }

    @Override
    public void removeComponents(Entity entity, Class<? extends Component> componentClass) {
        String componentName = componentClass.getSimpleName();
        int entityID = entity.mID;

        HashMap<Integer, ArrayList<Component>> componentStore = mComponentStores.get(componentName);
        if (componentStore == null) {
            throw new IllegalStateException(componentName + " is not component of entity " + entityID);
        }

        ArrayList<Component> componentList = componentStore.get(entityID);
        if (componentList == null) {
            throw new IllegalStateException(componentName + " is not component of entity " + entityID);
        }

        componentStore.remove(entityID);

        if (componentStore.isEmpty()) {
            mComponentStores.remove(componentName);
        }
    }

    @Override
    public void removeEntity(Entity entity) {
        int entityID = entity.mID;

        /*
        Iterator<Entry<String, HashMap<Integer, ArrayList<Component>>>> storeIter = mComponentStores.entrySet().iterator();
        while (storeIter.hasNext()) {
            Entry<String, HashMap<Integer, ArrayList<Component>>> pairs = (Entry<String, HashMap<Integer, ArrayList<Component>>>)storeIter.next();
            HashMap<Integer, ArrayList<Component>> store = pairs.getValue();
            store.remove(entityID);
            if (store.isEmpty()) {
                storeIter.remove();
            }
        }
        */

        Set<String> componentSet = mComponentStores.keySet();
        for (String componentName : componentSet) {
            HashMap<Integer, ArrayList<Component>> componentStore = mComponentStores.get(componentName);
            componentStore.remove(entityID);
            if (componentStore.isEmpty()) {
                mComponentStores.remove(componentName);
            }
        }
    }

    @Override
    public void dump() {
        Log.d(TAG, "==============");
        Iterator<Map.Entry<String, HashMap<Integer, ArrayList<Component>>>> componentIter = mComponentStores.entrySet().iterator();
        while (componentIter.hasNext()) {
            Map.Entry<String, HashMap<Integer, ArrayList<Component>>> pairs = (Map.Entry<String, HashMap<Integer, ArrayList<Component>>>)componentIter.next();
            Log.d(TAG, (String)pairs.getKey());
            
            HashMap<Integer, ArrayList<Component>> store = (HashMap<Integer, ArrayList<Component>>)(pairs.getValue());
            Iterator<Map.Entry<Integer, ArrayList<Component>>> entityIter = store.entrySet().iterator();
            while (entityIter.hasNext()) {
                Map.Entry<Integer, ArrayList<Component>> entity = (Map.Entry<Integer, ArrayList<Component>>)entityIter.next();
                StringBuilder builder = new StringBuilder();
                builder.append("\t" + entity.getKey() + ": ");
                ArrayList<Component> c = (ArrayList<Component>)(entity.getValue());
                builder.append("[");
                for (Component cccc : c) {
                    builder.append(cccc + ", ");
                }
                builder.append("]");
                Log.d(TAG, builder.toString());
            }
        }
    }

    protected SimpleEntityManager() {
        mComponentStores = new HashMap<String, HashMap<Integer, ArrayList<Component>>>();
    }
}
