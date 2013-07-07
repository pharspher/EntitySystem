package spongecake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.IllegalStateException;

public class EntityManager {
    private static final String TAG = EntityManager.class.getSimpleName();

    private static EntityManager mManager = new EntityManager();
    private static int mEntityCount = 0;

    //private HashMap<String, ArrayList<Integer>> mComponentEntities;
    //private HashMap<Integer, ArrayList<Component>> mComponentStores;

    private HashMap<String, HashMap<Integer, ArrayList<Component>>> mComponentStores; 

    public static EntityManager getDefaultManager() {
        return mManager;
    }

    public static Entity obtainEntity() {
        return obtainEntity("unspecified");
    }

    public static Entity obtainEntity(String tag) {
        return new Entity(mEntityCount++, tag);
    }

    public void addComponent(Entity entity, Component component) {
        String componentName = component.getClass().getSimpleName();
        int entityID = entity.mID;

        HashMap<Integer, ArrayList<Component>> store = mComponentStores.get(componentName);
        if (store == null) {
            store = new HashMap<Integer, ArrayList<Component>>();
        }

        ArrayList<Component> instanceList = store.get(entityID);
        if (instanceList == null) {
            instanceList = new ArrayList<Component>();
        }
        instanceList.add(component);

        store.put(entityID, instanceList);
        mComponentStores.put(componentName, store);
    }

    public void dump() {
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

    /*
    public void addComponent(Entity entity, Component component) {
        String componentName = component.getClass().getSimpleName();
        int entityID = entity.mID;

        ArrayList<Integer> entities = mComponentEntities.get(componentName);
        if (entities == null) {
            ArrayList<Integer> newEntityList = new ArrayList<Integer>();
            newEntityList.add(entityID);
            mComponentEntities.put(componentName, newEntityList);
        } else if (!entities.contains(entityID)) {
            entities.add(entityID);
        } else {
            throw new IllegalStateException("Entity " + entity.mTag + "(" + entityID + ")" + " had already been bound to " + componentName);
        }

        ArrayList<Component> components = mComponentStores.get(entityID);
        if (components == null) {
            ArrayList<Component> newComponentList = new ArrayList<Component>();
            newComponentList.add(component);
            mComponentStores.put(entityID, newComponentList);
        } else {
            components.add(component);
        }

        Log.d(TAG, "Bind " + component + " to entity " + entityID);
    }
    */

    private EntityManager() {
        //mComponentEntities = new HashMap<String, ArrayList<Integer>>();
        //mComponentStores = new HashMap<Integer, ArrayList<Component>>();
        mComponentStores = new HashMap<String, HashMap<Integer, ArrayList<Component>>>();
    }

    public static class Entity {
        private Entity(int id) {
            this(id, "");
        }
        private Entity(int id, String tag) {
            mID = id;
            mTag = tag;
        }
        private int mID;
        private String mTag;
    }
}