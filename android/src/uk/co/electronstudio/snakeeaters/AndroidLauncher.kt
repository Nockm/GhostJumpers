package uk.co.electronstudio.snakeeaters


import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import uk.co.electronstudio.retrowar.EmptyCallback
import uk.co.electronstudio.retrowar.SimpleApp
import uk.co.electronstudio.retrowar.utils.DesktopCallback
import uk.co.electronstudio.retrowar.utils.SimpleLogger
import  uk.co.electronstudio.retrowar.utils.AndroidLogger


class Launcher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val app = SimpleApp(EmptyCallback(), "Snake Eaters",  SnakeFactory::class.java, AndroidLogger(BuildConfig
                .VERSION_NAME,
               ""))
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        initialize(app, config)
    }
}
