package spongecake;

public abstract class Component {
    private String mTag;

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getTag() {
        return mTag;
    }
}
