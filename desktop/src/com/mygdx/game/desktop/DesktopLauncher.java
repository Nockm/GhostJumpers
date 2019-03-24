package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uk.co.electronstudio.snakeeaters.SnakeFactory;
import uk.co.electronstudio.retrowar.App;
import uk.co.electronstudio.retrowar.SimpleApp;
import uk.co.electronstudio.retrowar.utils.DesktopCallback;
import uk.co.electronstudio.retrowar.utils.SimpleLogger;


public class DesktopLauncher {
    public static void main(String[] arg) {
        DesktopCallback callback = new DesktopCallback();
        LwjglApplicationConfiguration config = callback.getConfig();
       // config.fullscreen = true;
        App app = new SimpleApp(callback, "Snake", SnakeFactory.class, new SimpleLogger(), null, false, false);
        new LwjglApplication(app, config);
    }
}
