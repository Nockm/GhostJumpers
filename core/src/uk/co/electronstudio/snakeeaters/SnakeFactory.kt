package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import uk.co.electronstudio.retrowar.AbstractGameFactory
import uk.co.electronstudio.retrowar.App
import uk.co.electronstudio.retrowar.Game
import uk.co.electronstudio.retrowar.LevelLoader
import uk.co.electronstudio.retrowar.menu.BinMenuItem
import uk.co.electronstudio.retrowar.menu.MultiChoiceMenuItem
import uk.co.electronstudio.retrowar.menu.NumberMenuItem
import uk.co.electronstudio.retrowar.screens.GameSession

/*
 * Used by RetroWar to create our main class
 */
class SnakeFactory(path: String) : AbstractGameFactory("SnakeEaters", getAllLevelFiles(path+"levels"), path) {
    override val description = "A snake game. Eat the apples, grow as large as you can and trip up other players!"
    override val image: Texture = Texture("${pathPrefix}snake.png")


    val suddenDeath = BinMenuItem("Sudden Death: ", false)
    val maxFoods = NumberMenuItem("Maximum foods on screen: ", 10, 0, 20)
    val minFoods = NumberMenuItem("Minimum foods on screen: ", 1, 1, 20)
    val foodGoal = NumberMenuItem("Food required to win: ", 10, 2, 20)//6
    val speed = NumberMenuItem("Snake speed: ", 4, 0, 10)
    val foodValue = NumberMenuItem("Value of each food eaten: ", 4, 1, 30)
    val speedup = BinMenuItem("Speed increase: ", false)
    val maxLevelsToPlay = MultiChoiceMenuItem("How many levels to play: ",
        choices = listOf("ALL","1","2","3"),
        intValues = listOf( Int.MAX_VALUE,1,2,3))

    override val options = listOf(
           suddenDeath, maxFoods, minFoods, foodGoal, speed, foodValue, speedup, maxLevelsToPlay
    )

    override fun create(session: GameSession): Game {
        return SnakeGame(session, pathPrefix, suddenDeath.value,  maxFoods.value, minFoods.value, foodGoal.value*foodValue.value,
                speed.value.toFloat()*0.03f, speedup.value,
                foodValue.value, levels!!, level, maxLevelsToPlay.getSelectedInt())
    }

    override fun createWithTournamentSettings(session: GameSession): Game {
        return SnakeGame(session,
            pathPrefix,
            levelFiles = levels!!,
            levelIndex = level,
            suddenDeath = true,
            maxFoods = 10,
            minFoods = 1,
            foodGoal =  Int.MAX_VALUE,
            foodValue = 10,
            speed = 1*0.03f,
            speedup = true,
            maxLevelsToPlay = 1)
    }

    override fun createWithSimpleSettings(session: GameSession): Game {
        return SnakeGame(session,
            pathPrefix,
            levelFiles = levels!!,
            levelIndex = level,
            suddenDeath = false,
            maxFoods = 10,
            minFoods = 1,
            foodGoal = 100,
            foodValue = 10,
            speed = 5*0.03f,
            speedup = false,
            maxLevelsToPlay = Int.MAX_VALUE)
    }
}

fun getAllLevelFiles(folderName:String): List<LevelLoader>{
    val folder = Gdx.files.internal(folderName)
    return folder.list().filter { it.extension()=="png" }.map {
        LevelLoader(it, it, it.nameWithoutExtension())
    }
}
