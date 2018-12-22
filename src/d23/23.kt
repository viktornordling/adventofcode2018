package d23

import util.Reader
import java.util.*

object Solution {
    data class Pos(val x: Int, val y: Int, val z: Int)
    data class Star(val index: Int, val pos: Pos, val radius: Int)
    data class Box(val minX: Int, val minY: Int, val minZ: Int, val maxX: Int, val maxY: Int, val maxZ: Int) {
        fun corners() = listOf(
            Pos(minX, minY, minZ),
            Pos(minX, minY, maxZ),
            Pos(minX, maxY, minZ),
            Pos(minX, maxY, maxZ),
            Pos(maxX, minY, minZ),
            Pos(maxX, minY, maxZ),
            Pos(maxX, maxY, minZ),
            Pos(maxX, maxY, maxZ)
        )

        fun countIntersectingStars(stars: List<Star>): Int = stars.filter { intersectsWith(it) }.count()

        private fun intersectsWith(star: Star): Boolean {
            val x = when {
                star.pos.x > maxX -> maxX
                star.pos.x < minX -> minX
                else -> star.pos.x
            }

            val y = when {
                star.pos.y > maxY -> maxY
                star.pos.y < minY -> minY
                else -> star.pos.y
            }

            val z = when {
                star.pos.z > maxZ -> maxZ
                star.pos.z < minZ -> minZ
                else -> star.pos.z
            }

            return manhattanDistance(star.pos, Pos(x, y, z)) < star.radius
        }
    }

    private fun boxIsOnePixel(box: Box): Boolean =
        Math.abs(box.maxX - box.minX) <= 1 && Math.abs(box.maxY - box.minY) <= 1 && Math.abs(box.maxZ - box.minZ) <= 1

    var counter = 0
    private fun findMaxOverlap(prioQueue: PriorityQueue<Box>): Box {
        // Take the first box from the queue, split it in 8 and create sub boxes, add them all to the queue and recurse.
        counter++
        val box = prioQueue.poll()

        if (boxIsOnePixel(box)) {
            return box
        }

        val midX = (box.minX + box.maxX) / 2
        val midY = (box.minY + box.maxY) / 2
        val midZ = (box.minZ + box.maxZ) / 2

        val box1 = box.copy(maxX = midX, maxY = midY, maxZ = midZ)
        val box2 = box.copy(minX = midX, maxY = midY, maxZ = midZ)
        val box3 = box.copy(maxX = midX, maxY = midY, minZ = midZ)
        val box4 = box.copy(minX = midX, maxY = midY, minZ = midZ)

        val box5 = box.copy(maxX = midX, minY = midY, maxZ = midZ)
        val box6 = box.copy(minX = midX, minY = midY, maxZ = midZ)
        val box7 = box.copy(maxX = midX, minY = midY, minZ = midZ)
        val box8 = box.copy(minX = midX, minY = midY, minZ = midZ)

        val allBoxes = listOf(box1, box2, box3, box4, box5, box6, box7, box8)
        allBoxes.forEach { prioQueue.add(it) }
        return findMaxOverlap(prioQueue)
    }

    fun manhattanDistance(start: Pos, end: Pos) = Math.abs(start.x - end.x) + Math.abs(start.y - end.y) + Math.abs(start.z - end.z)

    private fun distanceToOrigo(box: Box): Int {
        return box.corners().map { manhattanDistance(Pos(0, 0, 0), it) }.min()!!
    }

    private fun compareBoxes(box1: Box, box2: Box, stars: List<Star>): Int {
        val overlapDiff = box2.countIntersectingStars(stars) - box1.countIntersectingStars(stars)
        if (overlapDiff == 0) {
            return distanceToOrigo(box1) - distanceToOrigo(box2)
        }
        return overlapDiff
    }

    private fun countStars(pos: Pos, stars: List<Star>):Int = stars.filter { manhattanDistance(pos, it.pos) <= it.radius }.count()

    fun solve() {
        var starIndex = 0

        fun parse(string: String): Star {
            val p = string.substringAfter("pos=<").substringBefore(">").split(",").map { it.toInt() }
            val pos = Pos(p[0], p[1], p[2])
            val r = string.substringAfter("r=").toInt()
            return Star(starIndex++, pos, r)
        }

        val input = Reader.readInput("input.txt")
        val stars = input.map { parse(it) }

        val minX = { val s = stars.minBy { it.pos.x - it.radius }!!; s.pos.x - s.radius }()
        val minY = { val s = stars.minBy { it.pos.y - it.radius }!!; s.pos.y - s.radius }()
        val minZ = { val s = stars.minBy { it.pos.z - it.radius }!!; s.pos.z - s.radius }()

        val maxX = { val s = stars.maxBy { it.pos.x + it.radius }!!; s.pos.x + s.radius }()
        val maxY = { val s = stars.maxBy { it.pos.y + it.radius }!!; s.pos.y + s.radius }()
        val maxZ = { val s = stars.maxBy { it.pos.z + it.radius }!!; s.pos.z + s.radius }()

        val box = Box(minX, minY, minZ, maxX, maxY, maxZ)
        val prioQueue = PriorityQueue<Box> { box1: Box, box2: Box -> compareBoxes(box1, box2, stars) }
        prioQueue.add(box)
        val maxOverlap = findMaxOverlap(prioQueue)
        val maxPoint = maxOverlap.corners().maxBy { countStars(it, stars) }
        println(manhattanDistance(Pos(0, 0, 0), maxPoint!!))
        print("iterations: $counter")
    }
}

fun main(args: Array<String>) {
    Solution.solve()
}