package spongecake;

public abstract class EntityManager {
    private static EntityManager mManager;

    public static EntityManager getDefaultManager() {
        if (mManager == null) {
            mManager = new SimpleEntityManager();
        }
        return mManager;
    }

    public abstract void addComponent(Entity entity, Component component);
    public abstract void removeComponent(Entity entity, Component component);
    public abstract void removeComponents(Entity entity, Class<? extends Component> componentClass);
    public abstract void removeEntity(Entity entity);
    public abstract void dump();

    protected abstract Entity obtainEntityImpl();

    public static Entity obtainEntity() {
        return mManager.obtainEntityImpl();
    }

    protected EntityManager() {}

    public static class Entity {
        protected Entity(int id) {
            mID = id;
        }
        protected int mID;
    }
}