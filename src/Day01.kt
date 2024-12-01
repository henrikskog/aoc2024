import kotlin.math.abs

fun main() {

    fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
        return input.map { line ->
            val numbers = line.split("\\s+".toRegex())
            Pair(numbers[0].toInt(), numbers[1].toInt())
        }.unzip()
    }

    fun part1(input: List<String>): Int {
        val (lst1, lst2) = parseInput(input)
        val sortedLst1 = lst1.sorted()
        val sortedLst2 = lst2.sorted()

        return sortedLst1.zip(sortedLst2).sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Int {
        val (lst1, lst2) = parseInput(input)

        return lst1.sumOf { a -> a * lst2.count { it == a } }
    }

//    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
