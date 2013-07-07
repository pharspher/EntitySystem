package spongecake;

import java.util.ArrayList;

public abstract class EntityManager {
    private static EntityManager mManager;

    public static EntityManager getDefaultManager() {
        if (mManager == null) {
            mManager = new SimpleEntityManager();
        }
        return mManager;
    }

    public static Entity obtainEntity() {
        if (mManager == null) {
            getDefaultManager();
        }
        return mManager.obtainEntityImpl();
    }

    protected abstract Entity obtainEntityImpl();

    public abstract <T extends Component> EntityManager addComponent(Entity entity, T component);
    //public abstract <T extends Component> ArrayList<T> getComponent(Class<T> componentClass);
    public abstract ArrayList<Component> getComponent(Class<? extends Component> componentClass);
    public abstract ArrayList<Component> getComponent(Entity entity, Class<? extends Component> componentClass);
    public abstract void removeComponent(Entity entity, Component component);
    public abstract void removeComponents(Entity entity, Class<? extends Component> componentClass);
    public abstract void removeEntity(Entity entity);

    public abstract void dump();

    protected EntityManager() {}

    public static class Entity {
        protected Entity(int id) {
            mID = id;
        }
        protected int mID;
    }
}