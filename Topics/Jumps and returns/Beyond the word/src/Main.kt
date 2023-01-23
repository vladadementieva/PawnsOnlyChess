fun main() {
    // put your code here
    val stringIni = readln()
    loop1@ for (i in 0..25) {
        val letter = 'a' + i
        if (letter in stringIni) {
            continue@loop1
        } else
            print(letter)
    }
}