fun main() {
    // put your code here
    val stringInitial = readln()
    var counter = 0
    for (i in stringInitial) {
        if (stringInitial.lastIndexOf(i) == stringInitial.indexOf(i)) {
            counter++
        }
    }
    println(counter)
}