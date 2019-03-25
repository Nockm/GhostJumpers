package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import uk.co.electronstudio.retrowar.AbstractGameFactory
import uk.co.electronstudio.retrowar.App
import uk.co.electronstudio.retrowar.Game
import uk.co.electronstudio.retrowar.menu.BinMenuItem
import uk.co.electronstudio.retrowar.menu.MultiChoiceMenuItem
import uk.co.electronstudio.retrowar.menu.NumberMenuItem
import uk.co.electronstudio.retrowar.screens.GameSession
import java.io.File

/*
 * Used by RetroWar to create our main class
 */
class SnakeFactory(path: String) : AbstractGameFactory("Snake", getAllLevelNames(path+"levels"), path) {
    override val description = "A snake game. Eat the apples, grow as large as you can and trip up other players!"
    override val image: Texture = Texture("${pathPrefix}snake.png")


    val suddenDeath = BinMenuItem("Sudden Death: ", false)
    val maxFoods = NumberMenuItem("Maximum foods on screen: ", 5, 0, 20)
    val minFoods = NumberMenuItem("Minimum foods on screen: ", 1, 1, 20)
    val foodGoal = NumberMenuItem("Score required to win: ", 6, 0, 50)
    val speed = NumberMenuItem("Snake speed: ", 2, 0, 10)
    val foodValue = NumberMenuItem("Value of each food eaten: ", 2, 1, 30)
    val speedup = BinMenuItem("Speed increase: ", true)
    val maxLevelsToPlay = MultiChoiceMenuItem("How many levels to play: ",
        choices = listOf("ALL","1","2","3"),
        intValues = listOf( Int.MAX_VALUE,1,2,3))

    override val options = listOf(
           suddenDeath, maxFoods, minFoods, foodGoal, speed, foodValue, speedup, maxLevelsToPlay
    )

    override fun create(session: GameSession): Game {
        App.app.configureSessionWithPreSelectedInputDevice(session)
        return SnakeGame(session, pathPrefix, suddenDeath.value,  maxFoods.value, minFoods.value, foodGoal.value,
                speed.value.toFloat()*0.03f, speedup.value,
                foodValue.value, levels!!, level, maxLevelsToPlay.getSelectedInt())
    }

    override fun createWithDefaultSettings(session: GameSession): Game {
        return SnakeGame(session,
            pathPrefix,
            levelNames = levels!!,
            levelIndex = level,
            maxLevelsToPlay = 1)
    }
}

fun getAllLevelNames(folderName:String): List<String>{
    val folder = Gdx.files.internal(folderName)
    return folder.list().filter { it.extension()=="png" }.map {
        it.name().dropLast(4)
    }
}
