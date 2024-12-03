import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

// Update the Command enum to represent all command types
enum class Command {
    MUL, DO, DONT;

    companion object {
        fun parse(input: String): List<CommandOperation> {
            val mulRegex = Regex("mul\\((\\d+),(\\d+)\\)")
            val doRegex = Regex("do\\(\\)")
            val dontRegex = Regex("don't\\(\\)")

            val commands = mutableListOf<CommandOperation>()

            // Parse MUL commands
            mulRegex.findAll(input).forEach { match ->
                commands.add(
                    CommandOperation(
                        MUL,
                        listOf(match.groupValues[1].toInt(), match.groupValues[2].toInt()),
                        match.range.first
                    )
                )
            }

            // Parse DO commands
            doRegex.findAll(input).forEach {
                commands.add(CommandOperation(DO, emptyList(), it.range.first))
            }

            // Parse DON'T commands
            dontRegex.findAll(input).forEach {
                commands.add(CommandOperation(DONT, emptyList(), it.range.first))
            }

            return commands.sortedBy { it.position }
        }
    }
}

// New data class to represent a command with its parameters
data class CommandOperation(
    val command: Command,
    val parameters: List<Int>,
    val position: Int
)

// Replace parseMul with a more general parse function
fun parseCommands(input: String): List<CommandOperation> {
    return Command.parse(input)
}


fun parseMul(input: String): List<Pair<Int, Int>> {
    val regex = Regex("mul\\((\\d*),(\\d*)\\)")
    val matches = regex.findAll(input)
    return matches.map { it.groupValues[1].toInt() to it.groupValues[2].toInt() }.toList()
}


class Day03Test {
    @Test
    fun `parseMul extracts single multiplication result`() {
        val input = "mul(2, 3)"
        assertEquals(listOf(2 to 3), parseMul(input))
    }

    @Test
    fun `parseMul extracts multiple multiplication results`() {
        val input = "mul(2,3) mul(4,5)"
        assertEquals(listOf(2 to 3, 4 to 5), parseMul(input))
    }

    @Test
    fun `parseMul handles whitespace variations`() {
        val input = "mul(2,3) mul(4,    5)"
        assertEquals(listOf(2 to 3, 4 to 5), parseMul(input))
    }

    @Test
    fun `parseMul returns empty list for non-matching input`() {
        val input = "add(2, 3) div(4, 2)"
        assertEquals(emptyList(), parseMul(input))
    }

    @Test
    fun `parseMul returns empty list for empty input`() {
        val input = ""
        assertEquals(emptyList(), parseMul(input))
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        // extract all mul(\d*,\d*) from string
        println(input.size)
        val matches = parseMul(input.joinToString())
        var tot = 0
        for (match in matches) {
            tot += match.first * match.second
        }

        return tot
    }

    fun part2(input: List<String>): Int {
        val commands = parseCommands(input.joinToString())
        println("Initial commands: $commands")

        var f = true
        var tot = 0

        commands.forEach {
            when (it.command) {
                Command.MUL -> {
                    if(f) {
                        tot += it.parameters[0] * it.parameters[1]
                        println("MUL: ${it.parameters[0]} * ${it.parameters[1]} = ${it.parameters[0] * it.parameters[1]}, Running total: $tot")
                    } else {
                        println("MUL: Skipped multiplication due to f = false")
                    }
                }
                Command.DO -> {
                    f = true
                    println("DO: Set f to true")
                }
                Command.DONT -> {
                    f = false
                    println("DONT: Set f to false")
                }
            }
        }

        println("Final total: $tot")
        return tot
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
