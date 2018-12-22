import java.io.File
import java.io.FileInputStream

fun readInput(curList:List<String>): List<String> = readLine()?.let { readInput(curList + it) } ?: curList

var inputStream = FileInputStream(File("input.txt"))
System.setIn(inputStream)

val lines = readInput(emptyList())
val ints = lines.map { line -> line.toInt() }
val totalFreq = ints.sum()

println(totalFreq)
