fun main() {
    fun part1(input: List<String>): Int {
        // empty list
        val lst1: MutableList<Int> = mutableListOf()
        val lst2: MutableList<Int> = mutableListOf()


        for (line in input) {
        val lst = line.split("\\s+".toRegex())
            lst1.add(lst[0].toInt())
            lst2.add(lst[1].toInt())
        }

        lst1.sort()
        lst2.sort()

        var s: Int = 0

        for (i in 0..lst1.size - 1) {
            val diff = lst1[i] - lst2[i]
            val abs = Math.abs(diff)

            s += abs
        }

        return s
    }

    fun part2(input: List<String>): Int {
        val lst1: MutableList<Int> = mutableListOf()
        val lst2: MutableList<Int> = mutableListOf()


        for (line in input) {
            val lst = line.split("\\s+".toRegex())
            lst1.add(lst[0].toInt())
            lst2.add(lst[1].toInt())
        }

        var s: Int = 0

        for (i in 0..lst1.size - 1) {
            val num = lst1[i]

        val numTimesIn2 = lst2.count { it == num }


            s += numTimesIn2 * num
        }

        return s
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
