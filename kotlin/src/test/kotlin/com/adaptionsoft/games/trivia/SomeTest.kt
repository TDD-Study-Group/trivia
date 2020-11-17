package com.adaptionsoft.games.trivia

import com.adaptionsoft.games.trivia.runner.GameRunner
import com.adaptionsoft.games.uglytrivia.Game
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut
import org.approvaltests.Approvals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class SomeTest {
    @Test
    fun true_is_true() {
        assertTrue(true)
    }

    private val rand = Random(42)

    @Test
    fun EXPLORE() {
        val captured = tapSystemOut {
            val aGame = Game()

            aGame.add("Chet")
            aGame.add("Pat")
            aGame.add("Sue")


            do {

                aGame.roll(rand.nextInt(5) + 1)

                if (rand.nextInt(9) == 7) {
                    GameRunner.notAWinner = aGame.wrongAnswer()
                } else {
                    GameRunner.notAWinner = aGame.wasCorrectlyAnswered()
                }

            } while (GameRunner.notAWinner)
        }
        Approvals.verify(captured)
    }

    @Test
    fun gameWithOnePlayer() {
        val captured = tapSystemOut {
            val aGame = Game()

            aGame.add("Chet")

            do {

                aGame.roll(rand.nextInt(5) + 1)

                if (rand.nextInt(9) == 7) {
                    GameRunner.notAWinner = aGame.wrongAnswer()
                } else {
                    GameRunner.notAWinner = aGame.wasCorrectlyAnswered()
                }

            } while (GameRunner.notAWinner)
        }
        Approvals.verify(captured)
    }

    @Test
    fun gameWithTwoPlayer() {
        val captured = tapSystemOut {
            val aGame = Game()

            aGame.add("Whatever")
            aGame.add("Chet")

            do {

                aGame.roll(rand.nextInt(5) + 1)

                if (rand.nextInt(9) == 7) {
                    GameRunner.notAWinner = aGame.wrongAnswer()
                } else {
                    GameRunner.notAWinner = aGame.wasCorrectlyAnswered()
                }

            } while (GameRunner.notAWinner)
        }
        Approvals.verify(captured)
    }

    @Test
    fun gameWithRandomPlayer() {
        val captured = tapSystemOut {
            repeat(1000){
                val aGame = Game().apply {
                    val nextInt = rand.nextInt(10)
                    (0..nextInt).forEach { i -> add("Name $i") }
                }

                do {

                    aGame.roll(rand.nextInt(5) + 1)

                    if (rand.nextInt(9) == 7) {
                        GameRunner.notAWinner = aGame.wrongAnswer()
                    } else {
                        GameRunner.notAWinner = aGame.wasCorrectlyAnswered()
                    }

                } while (GameRunner.notAWinner)
            }        }
        Approvals.verify(captured)
    }

}
