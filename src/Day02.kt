// new stuff:
// .fold, kult at de bruker currying her
// .windowed, litt av et stdlib her


fun main() {

    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line ->
            println(line)
            line.split("\\s".toRegex()).map { it.toInt() }
        }
    }

    fun part1(input: List<String>): Int {
//        The levels are either all increasing or all decreasing.
//        Any two adjacent levels differ by at least one and at most three.
        val lst = parseInput(input)
        return lst.fold(0) { acc, curr ->
            val validIncreasing = curr.windowed(size = 2, step = 1).all { (a, b) -> b > a && b - a <= 3 }
            val validDecreasing = curr.windowed(size = 2, step = 1).all { (a, b) -> b < a && a - b <= 3 }
            if (validIncreasing || validDecreasing) acc + 1 else acc
        }
    }

    fun part2(input: List<String>): Int {
//        The levels are either all increasing or all decreasing.
//        Any two adjacent levels differ by at least one and at most three.

// in addition: one level can be skipped
        val lst = parseInput(input)
        return lst.fold(0) { acc, curr ->
            val combs = mutableListOf<List<Int>>()
            combs.add(curr)
            for (i in 0 until curr.size) {
                combs.add(curr.filterIndexed { index, _ -> index != i })
            }

            val validIncreasing = combs.any { comb -> comb.windowed(size = 2, step = 1).all { (a, b) -> b > a && b - a <= 3 } }
            val validDecreasing = combs.any { comb -> comb.windowed(size = 2, step = 1).all { (a, b) -> b < a && a - b <= 3 } }

            if (validIncreasing || validDecreasing) acc + 1 else acc
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)

    val res2 = part2(testInput)
    println(res2)
   check(res2 == 4)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
