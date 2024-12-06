import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LoopException(message: String) : Exception(message)

fun List<List<Char>>.printGrid() {
    forEach { row ->
        println(row.joinToString(""))
    }
    println("\n")
}

fun main() {
    val GUARD = "^>v<"
    val WALL = '#'
    val EMPTY = '.'

    val directions = mapOf(
        '>' to (0 to 1),    // right
        'v' to (1 to 0),    // down
        '<' to (0 to -1),   // left
        '^' to (-1 to 0)    // up
    )

    fun simulateMoveGuard(grid: MutableList<MutableList<Char>>, maxIterations: Int): MutableList<MutableList<Char>> {
        var its = 0;
        val visited = mutableSetOf<Pair<Char, Pair<Int, Int>>>()
        while (its < maxIterations) {
            its++;
            val guardRow = grid.indexOfFirst { line -> GUARD.any { line.contains(it) } }
            // If no guard is found, we're done
            if (guardRow == -1) return grid

            val guardCol = grid[guardRow].indexOfFirst { col -> GUARD.any { col == it } }


            val guardChar = grid[guardRow][guardCol]

            if (visited.contains(guardChar to (guardRow to guardCol))) {
                throw LoopException("Found loop at row $guardRow column $guardCol")
            }


            val dir = directions[guardChar] ?: throw IllegalArgumentException("Invalid guard character")

            // Check if guard is about to exit the grid
            if (guardRow + dir.first < 0 || guardRow + dir.first >= grid.size ||
                guardCol + dir.second < 0 || guardCol + dir.second >= grid[0].size
            ) {
                grid[guardRow][guardCol] = 'X'
                return grid
            }

            val nextGuardPos = grid[guardRow + dir.first][guardCol + dir.second]

            if (nextGuardPos == WALL) {
                // Rotate guard direction
                val currentIndex = GUARD.indexOf(guardChar)
                val nextIndex = (currentIndex + 1) % GUARD.length
                grid[guardRow][guardCol] = GUARD[nextIndex]
                continue
            }


            // Move guard
            grid[guardRow][guardCol] = 'X'
            grid[guardRow + dir.first][guardCol + dir.second] = guardChar
            visited.add(guardChar to (guardRow to guardCol))
        }
        grid.printGrid()
        throw IllegalArgumentException("Invalid guard character")
    }

    fun part1(input: List<String>): Int {
        val grid = simulateMoveGuard(input.map { line -> line.map { it }.toMutableList() }.toMutableList(), 10000)
        grid.printGrid()
        return grid.sumOf { row -> row.count { it == 'X' } }
    }

    fun part2(input: List<String>): Int = runBlocking {
        var tot = 0
        val m = input.size - 1
        val n = input[0].length - 1
        
        // Generate all possible positions first
        val positions = (0..m).flatMap { row ->
            (0..n).map { col -> row to col }
        }

        // Create a mutex for thread-safe counter updates
        val mutex = Mutex()
        
        // Process positions in parallel using coroutines
        positions.map { (row, col) ->
            async(Dispatchers.Default) {
                val inputCopy = input.map { it.toMutableList() }.toMutableList()
                inputCopy[row][col] = WALL
                try {
                    println("Simulating $row $col")
                    simulateMoveGuard(inputCopy, 100000)
                } catch (e: LoopException) {
                    mutex.withLock {
                        tot += 1
                    }
                } catch (e: Exception) {
                    println("Error at $row:$col ${e.message}")
                }
            }
        }.awaitAll()
        
        return@runBlocking tot
    }


    test("Part 1 (test)", expected = 41, actual = part1(readInput("Day06_test")))
    println("Part 1: " + part1(readInput("Day06")))

    test("Part 2 (test)", expected = 6, actual = part2(readInput("Day06_test")))
    println("Part 2: " + part2(readInput("Day06")))

}
