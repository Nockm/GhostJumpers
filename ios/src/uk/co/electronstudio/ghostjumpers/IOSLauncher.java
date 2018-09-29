package uk.co.electronstudio.ghostjumpers;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import uk.me.fantastic.retro.EmptyCallback;
import uk.me.fantastic.retro.SimpleApp;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();


        SimpleApp app = new SimpleApp(new EmptyCallback(), "Ghost Jumpers", new SnakeFactory(), new IOSLogger(), new IOSManualGC(), false);
        return new IOSApplication(app, config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}