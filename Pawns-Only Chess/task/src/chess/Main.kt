package chess

private const val SIZE = 8
private const val SPACE = ' '
private const val BLACK = 'B'
private const val WHITE = 'W'
private const val BORDER = "  +---+---+---+---+---+---+---+---+"
var chess = mutableListOf(
    MutableList(SIZE) { SPACE },
    MutableList(SIZE) { BLACK },
    MutableList(SIZE) { SPACE },
    MutableList(SIZE) { SPACE },
    MutableList(SIZE) { SPACE },
    MutableList(SIZE) { SPACE },
    MutableList(SIZE) { WHITE },
    MutableList(SIZE) { SPACE }

)
var logOfSteps = mutableListOf("")
fun main() {
//    write your code here
    println("""Pawns-Only Chess""")
    println("""First Player's name:""")
    //print("> ")
    val firstPlayer = readln()

    println("""Second Player's name:""")
    //print("> ")

    val secondPlayer = readln()

    printChess(chess)
    val regexTurn = Regex("[a-h][1-8][a-h][1-8]")
    var currentPlayer: Pair<String, Char> = Pair(firstPlayer, WHITE)
    while (true) {

        val turnStep = turn(currentPlayer.first)
        when (checkStep(turnStep, regexTurn, currentPlayer.second)) {
            1 -> {

                return
            }

            2 -> continue

            else -> {
                logOfSteps.add(turnStep)
                currentPlayer = switchPlayer(currentPlayer, secondPlayer, firstPlayer)
            }
        }

    }
}

fun printChess(chess: MutableList<MutableList<Char>>) {
    var i = SIZE
    for (elem in chess) {
        println(BORDER)
        print(i)
        println(elem.joinToString(" | ", " | ", " | "))
        i--
    }
    println(BORDER)
    print("$SPACE$SPACE$SPACE$SPACE")
    for (j: Int in 0 until SIZE) {
        print('a' + j)
        print("$SPACE$SPACE$SPACE")
    }
    println()
}


private fun switchPlayer(
    currentPlayer: Pair<String, Char>,
    secondPlayer: String,
    firstPlayer: String
): Pair<String, Char> {
    return if (currentPlayer.first == secondPlayer) {
        Pair(firstPlayer, WHITE)
    } else {
        Pair(secondPlayer, BLACK)
    }
}

private fun checkStep(
    turnStep: String,
    regexTurn: Regex,
    color: Char
): Int {
    if (turnStep.contentEquals("exit")) {
        println("Bye")
        return 1
    } else {
        if (!turnStep.matches(regexTurn)) {
            println("Invalid Input")
            return 2
        } else {
            //THE GAME
            val start1 = mapLetterOnNumber(turnStep[0])
            val start2 = mapNumberOnNumber(turnStep[1].digitToInt() - 1)
            val end1 = mapLetterOnNumber(turnStep[2])
            val end2 = mapNumberOnNumber(turnStep[3].digitToInt() - 1)
            val lastStepString = logOfSteps.last()
            val lastStep1 = if (lastStepString.isNotEmpty()) {
                lastStepString[1].digitToInt()
            } else {
                0
            }
            val lastStep2 = if (lastStepString.isNotEmpty()) {
                mapLetterOnNumber(lastStepString[0])
            } else {
                0
            }
            val step = if (((color == BLACK) && (start2 == 1)) || ((color == WHITE) && (start2 == 6))) {
                2
            } else {
                1
            }
            //println("$start1 $start2 $end1 $end2 $step")
            //println(logOfSteps.last())
            if (chess[(start2)][start1] == color) {
                /*
                There are several moves that a pawn can make — a forward move and a capture.
                In this stage, you need to implement forwards moves only.
                A forward move is a move that propels your pawn by one rank
                (horizontal line) if the square is not taken by another pawn,
                 or by two ranks if this is the first move of the pawn:
                */

                /*
                                if (start1 != end1) {
                                    println("Invalid Input")
                                    return 2
                                }*/
                // Нельзя ходить назад
                if ((((end2 > start2)) && (color == WHITE)) || (((start2 > end2)) && (color == BLACK))) {
                    println("Invalid Input")
                    return 2
                }
                //Проверка первого шага
                if ((((end2 - start2) > step) && (color == BLACK)) || (((start2 - end2) > step) && (color == WHITE))) {
                    println("Invalid Input")
                    return 2
                }


                when (chess[end2][end1]) {
                    SPACE ->
                        //change
                    {

                        //добавить проверку на en passant
                        if ((color == BLACK) && (lastStep1 == 2 && lastStep2 == start1 + 1) && (start1 != 7 && chess[start2][start1 + 1] == WHITE) && (((start2 - end2) == 1) || ((end2 - start2) == 1))) {
                            println(chess[start2][start1 + 1])
                            chess[end2][end1] = color
                            chess[start2][start1] = SPACE
                            chess[start2][start1 + 1] = SPACE


                        }
                        if ((color == WHITE) && (lastStep1 == 7 && lastStep2 == start1 - 1) && (start1 != 0 && chess[start2][start1 - 1] == BLACK) && (((start2 - end2) == 1) || ((end2 - start2) == 1))) {

                            chess[end2][end1] = color
                            chess[start2][start1] = SPACE
                            chess[start2][start1 - 1] = SPACE


                        } else {
                            // проверка на то, что ход по прямой
                            if (start1 != end1) {
                                println("Invalid Input")
                                return 2
                            } else {
                                chess[end2][end1] = color
                                chess[start2][start1] = SPACE

                            }
                        }
                    }

                    color -> {
                        println("Invalid Input")
                        return 2
                    }

                    else -> {
                        //захват противника
                        if ((((start2 - end2) == 1) || ((end2 - start2) == 1)) && ((((start1 - end1) == 1) || ((end1 - start1) == 1)))) {
                            chess[end2][end1] = color
                            chess[start2][start1] = SPACE

                        } else {
                            println("Invalid Input")
                            return 2
                        }
                    }


                }


            } else {
                println("No ${textColor(color)} pawn at ${turnStep[0]}${turnStep[1]}")
                return 2
            }

            printChess(chess)

        }
    }
    if (checkEndOfTheGame(turnStep[3].digitToInt(), color)) {
        return 1
    }
    //add STALEMATE
    //Stalemate (draw) occurs when a player can't make any valid move on their turn.
    //check all opportunities ближайшие три к стартовой позиции и края
    if (stalemate(color)) {
        return 1
    }
    return 0
}

//проверка конца игры
// The game is over when either player succeeds in moving their pawn to the last opposite rank
// — rank 8 for White, rank 1 for Black.
// The game is also over when all opposite pawns are captured.
private fun checkEndOfTheGame(end2: Int, color: Char): Boolean {
    if ((((end2 == 8) && (color == WHITE)) || ((end2 == 1) && (color == BLACK))) || ((noBlack() && (color == WHITE)) || (noWhite() && (color == BLACK)))) {
        println("${textColor(color)} wins!")
        println("Bye")
        return true
    }

    return false

}

private fun stalemate(color: Char): Boolean {
    if (((oneRisk(BLACK) && (color == WHITE)) || (oneRisk(WHITE) && (color == BLACK)))) {
        println("Stalemate!")
        println("Bye!")
        return true
    }
    return false
}

fun noWhite(): Boolean {
    return (colorOccurrences(WHITE) == 0)
}

fun noBlack(): Boolean {
    return (colorOccurrences(BLACK) == 0)
}

fun oneRisk(color: Char): Boolean {
    //здесь проверить координаты оставшейся фигуры и возможные ее шаги три варианта
    var result = 0
    // i j cycle to do
    for (i in 0 until 8) {
        for (j in 0 until 8) {
            if (chess[i][j] == color) {
                result += result + 1
                if (result > 1) {
                    break
                }
            }
        }
    }
    if (result == 1) {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                if (chess[i][j] == color) {
                    if (color == WHITE) {
                        if (i != 0 && (chess[i - 1][j] != SPACE && ((j != 0 && chess[i - 1][j - 1] == SPACE) || (j != 7 && chess[i - 1][j + 1] == SPACE)))) {
                            return true
                        }
                    }
                    if (color == BLACK) {
                        if (i != 7 && (chess[i + 1][j] != SPACE && ((j != 0 && chess[i + 1][j - 1] == SPACE) || (j != 7 && chess[i + 1][j + 1] == SPACE)))) {
                            return true
                        }
                    }
                }
            }
        }
    }



    return false
}


fun colorOccurrences(color: Char): Int {
    var result = 0
    for (elem in chess) {
        if (elem.contains(color)) {
            result++
        }
    }
    return result
}

private fun textColor(color: Char): String = if (color == BLACK) "black" else "white"


private fun turn(player: String): String {
    println("${player}'s turn:")
    //print("> ")
    return readln()
}

private fun mapLetterOnNumber(letter: Char): Int {
    val aChar = 'a'
    return (letter - aChar)
}

private fun mapNumberOnNumber(letter: Int): Int {
    return when (letter) {
        0 -> 7
        1 -> 6
        2 -> 5
        3 -> 4
        4 -> 3
        5 -> 2
        6 -> 1
        7 -> 0
        else -> {
            0
        }
    }
}