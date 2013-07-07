package spongecake;

import com.badlogic.gdx.Gdx;

public class Log {
    public static void d(String tag, String msg) {
        Gdx.app.log(tag, msg);
    }
}