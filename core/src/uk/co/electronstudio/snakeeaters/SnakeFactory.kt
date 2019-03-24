package uk.co.electronstudio.snakeeaters

import uk.co.electronstudio.retrowar.AbstractGameFactory
import uk.co.electronstudio.retrowar.App
import uk.co.electronstudio.retrowar.Game
import uk.co.electronstudio.retrowar.Prefs
import uk.co.electronstudio.retrowar.menu.BinMenuItem
import uk.co.electronstudio.retrowar.menu.BinPrefMenuItem
import uk.co.electronstudio.retrowar.menu.MultiChoiceMenuItem
import uk.co.electronstudio.retrowar.menu.NumberMenuItem
import uk.co.electronstudio.retrowar.screens.GameSession
import java.io.File

/*
 * Used by RetroWar to create our main class
 */
class SnakeFactory(path: String) : AbstractGameFactory("Snake", getAllLevelNames(path+"levels"), path) {


    val suddenDeath = BinMenuItem("Sudden Death: ", false)
    val maxFoods = NumberMenuItem("Maximum foods on screen: ", 5, 0, 20)
    val minFoods = NumberMenuItem("Minimum foods on screen: ", 1, 1, 20)
    val foodGoal = NumberMenuItem("Score required to win: ", 6, 0, 50)
    val speed = NumberMenuItem("Snake speed: ", 2, 0, 10)
    val foodValue = NumberMenuItem("Value of each food eaten: ", 2, 1, 30)
    val speedup = BinMenuItem("Speed increase: ", true)


    override val options = listOf(
           suddenDeath, maxFoods, minFoods, foodGoal, speed, foodValue, speedup
    )


    override val description = "A snake game. Eat the apples, grow as large as you can and trip up other players!"

    override fun create(session: GameSession): Game {
        App.app.configureSessionWithPreSelectedInputDevice(session)
        return SnakeGame(session, pathPrefix, suddenDeath.value,  maxFoods.value, minFoods.value, foodGoal.value,
                speed.value.toFloat()*0.03f, speedup.value,
                foodValue.value, levels!!, level)
    }

    override fun createWithDefaultSettings(session: GameSession): Game {
        return SnakeGame(session, pathPrefix, levelNames = levels!!, levelIndex = level)
    }
}

fun getAllLevelNames(folderName:String): List<String>{
    val folder = File(folderName)
    return folder.list().filter { it.endsWith(".png") }.toList()
}