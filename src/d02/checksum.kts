import util.Reader
import java.io.File
import java.io.FileInputStream

val inputStream = FileInputStream(File("input.txt"))
System.setIn(inputStream)
val lines = Reader.readInput(emptyList())

val twoCounts = lines.filter { line -> line.groupingBy { it }.eachCount().values.contains(2)}.size
val threeCounts = lines.filter { line -> line.groupingBy { it }.eachCount().values.contains(3)}.size
val checksum = twoCounts * threeCounts
println(checksum)

