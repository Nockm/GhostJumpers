package com.mygdx.game.desktop;

		import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
		import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
		import uk.co.electronstudio.snakeeaters.SnakeFactory;
		import uk.me.fantastic.retro.App;
		import uk.me.fantastic.retro.SimpleApp;
		import uk.me.fantastic.retro.utils.DesktopCallback;
		import uk.me.fantastic.retro.utils.SimpleLogger;


public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopCallback callback = new DesktopCallback();
		LwjglApplicationConfiguration config = callback.getConfig();
		config.fullscreen=false;
		App app = new SimpleApp(callback, "Snake", new SnakeFactory(), new SimpleLogger(), null, false, false);
		new LwjglApplication(app, config);
	}
}
