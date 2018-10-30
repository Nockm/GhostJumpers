package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.graphics.g2d.Batch
import uk.co.electronstudio.retrowar.AbstractGameFactory
import uk.co.electronstudio.retrowar.App
import uk.co.electronstudio.retrowar.menu.ActionMenuItem
import uk.co.electronstudio.retrowar.menu.EmptyMenuItem
import uk.co.electronstudio.retrowar.menu.Menu
import uk.co.electronstudio.retrowar.menu.MenuController
import uk.co.electronstudio.retrowar.menu.MultiChoiceMenuItem
import uk.co.electronstudio.retrowar.screens.GameSession
import uk.co.electronstudio.retrowar.screens.MenuScreen

class GameOptionsScreen(val game: AbstractGameFactory) : MenuScreen(true) {

    val optionsMenu = Menu("", doubleSpaced = true, quitAction = {
        App.app.showTitleScreen()
    })

    val menuStart = ActionMenuItem("CONTINUE", action = {
        var factory = game
        App.app.screen = GameSession(factory = factory)
    })

    override val controller = MenuController(optionsMenu, WIDTH, HEIGHT, x = 0f, y = HEIGHT - 20f)



    init {
        with(optionsMenu) {
            addAll(game.options)
            add(EmptyMenuItem())
            addAndSelect(menuStart)
        }
    }

    override fun additionalRendering(batch: Batch) {
    }
}
