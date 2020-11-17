package com.adaptionsoft.games.uglytrivia

import java.util.*
import kotlin.collections.ArrayList

class Game {
    var players = ArrayList<Player>()
    var places = IntArray(6)
    var purses = IntArray(6)
    var inPenaltyBox = BooleanArray(6)

    var popQuestions = LinkedList<Any>()
    var scienceQuestions = LinkedList<Any>()
    var sportsQuestions = LinkedList<Any>()
    var rockQuestions = LinkedList<Any>()

    var currentPlayer = 0
    var isGettingOutOfPenaltyBox: Boolean = false

    init {
        for (i in 0..49) {
            popQuestions.addLast("Pop Question " + i)
            scienceQuestions.addLast("Science Question " + i)
            sportsQuestions.addLast("Sports Question " + i)
            rockQuestions.addLast("Rock Question " + i)
        }
    }

    fun isPlayable(): Boolean {
        return playerCount() >= 2
    }

    fun add(playerName: String): Boolean {
        players.add(Player(playerName, place = 0))


        places[playerCount()] = 0
        purses[playerCount()] = 0
        inPenaltyBox[playerCount()] = false

        println(playerName + " was added")
        println("They are player number " + playerCount())
        return true
    }

    fun roll(roll: Int) {
        println(getPlayerName() + " is the current player")
        println("They have rolled a " + roll)

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true

                println(getPlayerName() + " is getting out of the penalty box")
                places[currentPlayer] = places[currentPlayer] + roll
                if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12

                println(getPlayerName() + "'s new location is " + places[currentPlayer])
                println("The category is " + currentCategory())
                askQuestion()
            } else {
                println(getPlayerName() + " is not getting out of the penalty box")
                isGettingOutOfPenaltyBox = false
            }

        } else {

            places[currentPlayer] = places[currentPlayer] + roll
            if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12

            println(getPlayerName() + "'s new location is " + places[currentPlayer])
            println("The category is " + currentCategory())
            askQuestion()
        }

    }

    private fun askQuestion() {
        if (currentCategory() === "Pop")
            println(popQuestions.removeFirst())
        if (currentCategory() === "Science")
            println(scienceQuestions.removeFirst())
        if (currentCategory() === "Sports")
            println(sportsQuestions.removeFirst())
        if (currentCategory() === "Rock")
            println(rockQuestions.removeFirst())
    }

    private fun currentCategory(): String {
        if (places[currentPlayer] == 0) return "Pop"
        if (places[currentPlayer] == 4) return "Pop"
        if (places[currentPlayer] == 8) return "Pop"
        if (places[currentPlayer] == 1) return "Science"
        if (places[currentPlayer] == 5) return "Science"
        if (places[currentPlayer] == 9) return "Science"
        if (places[currentPlayer] == 2) return "Sports"
        if (places[currentPlayer] == 6) return "Sports"
        if (places[currentPlayer] == 10) return "Sports"
        return "Rock"
    }

    fun wasCorrectlyAnswered(): Boolean {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                purses[currentPlayer]++
                println(getPlayerName()
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins.")

                val winner = didPlayerWin()
                currentPlayer++
                if (currentPlayer == playerCount()) currentPlayer = 0

                return winner
            } else {
                currentPlayer++
                if (currentPlayer == playerCount()) currentPlayer = 0
                return true
            }


        } else {

            println("Answer was corrent!!!!")
            purses[currentPlayer]++
            println(getPlayerName()
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.")

            val winner = didPlayerWin()
            currentPlayer++
            if (currentPlayer == playerCount()) currentPlayer = 0

            return winner
        }
    }

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(getPlayerName() + " was sent to the penalty box")
        inPenaltyBox[currentPlayer] = true

        currentPlayer++
        if (currentPlayer == playerCount()) currentPlayer = 0
        return true
    }

    private fun getPlayerName() = getCurrentPlayer().playerName

    private fun getCurrentPlayer() = players[currentPlayer]

    private fun playerCount() = players.size

    private fun didPlayerWin(): Boolean {
        return purses[currentPlayer] != 6
    }
}

data class Player(val playerName: String, var place: Int)
