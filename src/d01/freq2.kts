val lines = mutableListOf<String>()
var currentLine:String? = ""
while(currentLine != null) {
    currentLine = readLine()
    if (currentLine != null) {
        lines.add(currentLine!!)
    }
}
val ints = lines.map { line -> line.toInt() }
var sum = 0
val sums = mutableSetOf<Int>()
while(true) {
    for (int in ints) {
        sum += int
        if (sums.contains(sum)) {
            println("double sum: ${sum}")
        }
        sums.add(sum)
    }
}
println("No double sum")
