import util.Reader.readInput
import java.io.File
import java.io.FileInputStream

val inputStream = FileInputStream(File("input.txt"))
System.setIn(inputStream)
val lines = readInput(emptyList())

fun countDiffs(line1: String, line2: String): Int {
    var index = 0
    var diffs = 0
    while (index < line2.length) {
        if (line1[index] != line2[index]) {
            diffs++
        }
        index++
    }
    return diffs
}

// For each ID, count the number of characters it diffs to all other IDs and return any ID which only has 1 diff.
for (line1 in lines) {
    for (line2 in lines) {
        if (line1 != line2) {
            if (countDiffs(line1, line2) == 1) {
                println("Single diff: $line1 and $line2")
            }
        }
    }
}
