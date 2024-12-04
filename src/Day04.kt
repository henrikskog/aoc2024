fun main() {

    fun part1(input: List<String>): Int {
        val WORD = "XMAS"

        var tot = 0;
        val n = input.size
        val m = input[0].length

        // Define all 8 directions as (row, col) deltas
        val directions = listOf(
            0 to 1,    // right
            1 to 1,    // right down
            1 to 0,    // down
            1 to -1,   // left down
            0 to -1,   // left
            -1 to -1,  // left up
            -1 to 0,   // up
            -1 to 1    // right up
        )

        fun checkDirectionFromPosition(row: Int, col: Int, dRow: Int, dCol: Int): Boolean {
            val rowEnd = row + (WORD.length - 1) * dRow
            val colEnd = col + (WORD.length - 1) * dCol

//            check if it would be within bounds
            if (rowEnd < 0 || rowEnd >= n || colEnd < 0 || colEnd >= m) return false

            return WORD.withIndex().all { (i, char) ->
                input[row + i * dRow][col + i * dCol] == char
            }
        }

        // Check each starting position
        for (row in 0 until n) {
            for (col in 0 until m) {
                for ((dRow, dCol) in directions) {
                    if (checkDirectionFromPosition(row, col, dRow, dCol)) {
                        tot++
                    }
                }
            }
        }

        return tot

    }

    fun part2(input: List<String>): Int {
        val n = input.size
        val m = input[0].length

        fun checkForCrossFromCenter(row: Int, col: Int): Boolean {
            if (input[row][col] != 'A') return false

            val diag1 = listOf(input[row - 1][col - 1], input[row][col], input[row + 1][col + 1]).joinToString("")
            val diag2 = listOf(input[row - 1][col + 1], input[row][col], input[row + 1][col - 1]).joinToString("")

            val opts = listOf("MAS", "SAM")
            return opts.contains(diag1) && opts.contains(diag2)
        }

        var count = 0

//        don't check from edges in input to avoid having to do bounds check
        for (row in 1 until n - 1) {
            for (col in 1 until m - 1) {
                if (checkForCrossFromCenter(row, col)) count++
            }
        }

        return count
    }

    val testInput = readInput("Day04_test")
    check2(18, part1(testInput))
    check2(9, part2(testInput))

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
