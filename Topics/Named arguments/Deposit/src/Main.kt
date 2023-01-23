import kotlin.math.pow
import kotlin.math.roundToInt

fun main() {
    // write your code here
    val nameOfParameter = readln()
    val valueOfParameter = readln().toInt()
    when (nameOfParameter) {
        "amount" -> getFinalAmount(amount = valueOfParameter)
        "percent" -> getFinalAmount(percent = valueOfParameter)
        "years" -> getFinalAmount(years = valueOfParameter)
    }
}

fun getFinalAmount(amount: Int = 1000, percent: Int = 5, years: Int = 10) {
    val calc =amount * ((1 + (percent.toDouble()/100))).pow(years)
    println("${calc.toInt()}")
}