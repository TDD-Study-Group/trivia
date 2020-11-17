package com.adaptionsoft.games.trivia

import com.adaptionsoft.games.trivia.runner.GameRunner
import com.adaptionsoft.games.uglytrivia.Game
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut
import org.approvaltests.Approvals
import org.junit.jupiter.api.Test
import java.util.*

class SomeTest {
    private val rand = Random(42)

    @Test
    fun gameWithRandomPlayer() {
        val captured = tapSystemOut {
            repeat(5000) {
                val aGame = Game().apply {
                    val playerCount = rand.nextInt(6)
                    if(playerCount != 0){
                        (1..playerCount).forEach { i -> add("Name $i") }
                    }
                }
                do {
                    aGame.roll(rand.nextInt(5) + 1)
                    if (rand.nextInt(9) == 7) {
                        GameRunner.notAWinner = aGame.wrongAnswer()
                    } else {
                        GameRunner.notAWinner = aGame.wasCorrectlyAnswered()
                    }
                } while (GameRunner.notAWinner)
            }
        }
        Approvals.verify(captured)
    }
}
