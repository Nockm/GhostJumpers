package uk.co.electronstudio.snakeeaters

import uk.me.fantastic.retro.AbstractGameFactory
import uk.me.fantastic.retro.App
import uk.me.fantastic.retro.Game
import uk.me.fantastic.retro.Prefs
import uk.me.fantastic.retro.menu.BinMenuItem
import uk.me.fantastic.retro.menu.BinPrefMenuItem
import uk.me.fantastic.retro.menu.MultiChoiceMenuItem
import uk.me.fantastic.retro.menu.NumberMenuItem
import uk.me.fantastic.retro.screens.GameSession

/*
 * Used by RetroWar to create our main class
 */
class SnakeFactory(path: String) : AbstractGameFactory("Snake", listOf("snake1.png"), path) {


    val suddenDeath = BinMenuItem("Sudden Death", false)
    val maxFoods = NumberMenuItem("Maximum foods on screen", 10, 0, 20)
    val minFoods = NumberMenuItem("Minimum foods on screen", 0, 0, 20)
    val foodGoal = NumberMenuItem("Food required to win", 20, 0, 50)
    val speed = NumberMenuItem("Snake speed", 2, 0, 10)
    val foodValue = NumberMenuItem("Value of each food eaten", 2, 1, 30)


    override val options = listOf(
           suddenDeath
    )


    override val description = "A snake game. Eat the apples, grow as large as you can and trip up other players!"

    override fun create(session: GameSession): Game {
        App.app.configureSessionWithPreSelectedInputDevice(session)
        return SnakeGame(session, pathPrefix, suddenDeath.value,  maxFoods.value, minFoods.value, foodGoal.value,
                speed.value.toFloat()*0.03f,

                foodValue.value, levels!![level])
    }

    override fun createWithDefaultSettings(session: GameSession): Game {
        return SnakeGame(session, pathPrefix)
    }
}
