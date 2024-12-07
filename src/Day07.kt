import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


fun calculate(nums: List<Int>, operations: List<Char>): Long {
    // First pass: handle concatenations
    var result = nums[0].toLong()
    for ((idx, op) in operations.withIndex()) {
            when (op) {
                '+' -> result += nums[idx + 1]
                '*' -> result *= nums[idx + 1]
                '|' -> result  = (result.toString() + nums[idx + 1].toString()).toLong()
            }
    }
    return result
}

class Day07Test {
    @Test
    fun `test mixed operations`() {
        // Complex mixed operations
        assertEquals(81L, calculate(listOf(2, 3, 4, 3), listOf('|', '+', '*')))
        assertEquals(162L, calculate(listOf(2, 3, 4, 3), listOf('+', '|', '*')))
    }
} 

fun main() {
    // Generate all possible combinations of operations
    fun generateOperations(size: Int): List<List<Char>> {
        if (size == 1) return listOf(emptyList())
        
        val results = mutableListOf<List<Char>>()
        val operations = listOf('+', '*', '|')
        
        // We need size-1 operations for size numbers
        fun generate(current: List<Char>) {
            if (current.size == size - 1) {
                results.add(current)
                return
            }
            
            for (op in operations) {
                generate(current + op)
            }
        }
        
        generate(emptyList())
        return results
    }

    fun part1(input: List<String>): Long {
        var tot = 0L
        
        for (x in input) {
            val (res_, nums_) = x.split(":")
            val res = res_.toLong()
            val nums = nums_.trim().split("\\s".toRegex()).map(String::toInt)

            // Generate all possible operation combinations
            val operations = generateOperations(nums.size)
            
            // Check if any combination gives the target result
            if (operations.any { ops -> calculate(nums, ops) == res }) {
                tot+=res
            }
        }
        
        return tot
    }

    fun part2(input: List<String>): Int {
        return 0
    }


//    test2("Part 1 (test)", expected = 3749, actual = part1(readInput("Day07_test")))
//    println("Part 1: " + part1(readInput("Day07")))

    test2("Part 2 (test)", expected = 11387, actual = part1(readInput("Day07_test")))
    println("Part 2: " + part1(readInput("Day07"))) //54.224ms

}

