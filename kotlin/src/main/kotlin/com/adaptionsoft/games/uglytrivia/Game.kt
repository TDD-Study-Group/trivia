package com.adaptionsoft.games.uglytrivia

import java.util.*
import kotlin.collections.ArrayList

class Game {
    var players = ArrayList<Player>()
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
        players.add(Player(playerName, place = 0, purse = 0))


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
                movePlayer(roll)

                println(getPlayerName() + "'s new location is " + currentPlace())
                println("The category is " + currentCategory())
                askQuestion()
            } else {
                println(getPlayerName() + " is not getting out of the penalty box")
                isGettingOutOfPenaltyBox = false
            }

        } else {

            movePlayer(roll)

            println(getPlayerName() + "'s new location is " + currentPlace())
            println("The category is " + currentCategory())
            askQuestion()
        }

    }

    private fun movePlayer(roll: Int) {
        val player = players[currentPlayer]
        move(player, roll)
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
        if (currentPlace() == 0) return "Pop"
        if (currentPlace() == 4) return "Pop"
        if (currentPlace() == 8) return "Pop"
        if (currentPlace() == 1) return "Science"
        if (currentPlace() == 5) return "Science"
        if (currentPlace() == 9) return "Science"
        if (currentPlace() == 2) return "Sports"
        if (currentPlace() == 6) return "Sports"
        if (currentPlace() == 10) return "Sports"
        return "Rock"
    }

    private fun currentPlace(): Int {
        return getCurrentPlayer().place
    }

    fun wasCorrectlyAnswered(): Boolean {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                addOneCoin()
                println("${getPlayerName()} now has ${getPlayerCoins()} Gold Coins.")

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
            addOneCoin()
            println("${getPlayerName()} now has ${getPlayerCoins()} Gold Coins.")

            val winner = didPlayerWin()
            currentPlayer++
            if (currentPlayer == playerCount()) currentPlayer = 0

            return winner
        }
    }

    private fun addOneCoin() {
        getCurrentPlayer().purse = getCurrentPlayer().purse + 1
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
        return getPlayerCoins() != 6
    }

    private fun getPlayerCoins(): Int {
        return getCurrentPlayer().purse
    }

}

data class Player(val playerName: String, var place: Int, var purse: Int)

private fun move(player: Player, roll: Int) {
    player.place = player.place + roll
    if (player.place > 11) player.place = player.place - 12
}