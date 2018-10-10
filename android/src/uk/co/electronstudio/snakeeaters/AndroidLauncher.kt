package uk.co.electronstudio.snakeeaters


import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import uk.me.fantastic.retro.EmptyCallback
import uk.me.fantastic.retro.SimpleApp
import uk.me.fantastic.retro.utils.DesktopCallback
import uk.me.fantastic.retro.utils.SimpleLogger
import  uk.me.fantastic.retro.utils.AndroidLogger


class Launcher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {




        val app = SimpleApp(EmptyCallback(), "Ghost Jumpers",  SnakeFactory::class.java, AndroidLogger(BuildConfig.VERSION_NAME,
               ""))
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        initialize(app, config)
    }
}
