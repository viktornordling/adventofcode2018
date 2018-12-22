import java.io.File
import java.io.FileInputStream

fun readInput(curList:List<String>): List<String> = readLine()?.let { readInput(curList + it) } ?: curList
data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int, val id: Int)

fun parseInput(line: String): Rectangle {
    val parts = line.split(" ")
    val id = parts[0].removePrefix("#").toInt()
    val xy = parts[2].split(",")
    val x = xy[0].toInt()
    val y = xy[1].removeSuffix(":").toInt()
    val widthHeight = parts[3].split("x")
    val width = widthHeight[0].toInt()
    val height = widthHeight[1].toInt()
    return Rectangle(x, y, width, height, id)
}

var globalId = 1
var inputStream = FileInputStream(File("input.txt"))
System.setIn(inputStream);
val input = readInput(emptyList())
val rects = input.map { parseInput(it) }

val rowSize = 1000
val cloth = IntArray(rowSize*rowSize)

for (rectangle in rects) {
    for (curY in rectangle.y..(rectangle.y + rectangle.height-1)) {
        for (curX in rectangle.x..(rectangle.x + rectangle.width-1)) {
            val index = curY * rowSize + curX
            if (cloth[index] != 0 || cloth[index] == -1) {
                cloth[index] = -1
            } else {
                cloth[index] = rectangle.id
            }
        }
    }
}

// Find unbroken rect
for (rectangle in rects) {
    var unbroken = true
    for (curY in rectangle.y..(rectangle.y + rectangle.height-1)) {
        for (curX in rectangle.x..(rectangle.x + rectangle.width-1)) {
            val index = curY * rowSize + curX
            if (cloth[index] != rectangle.id) {
                unbroken = false
            }
        }
    }
    if (unbroken) {
        println("Found unbroken rect: ${rectangle.id}")
    }
}

val overlaps = cloth.filter { i -> i == -1 }.count()
println(overlaps)
