// Helper function to sort a sequence according to DG rules
fun sortAccordingToRules(nums: List<Int>, rules: DG): List<Int> {
    return nums.sortedWith { a, b ->
        // negative number -> keep order. 
        // positive -> reverse
        // 0 -> keep order

        // to remember: most basic use case for ordering is increasing order.
        // a-b < 0 -> keep order

        val aStr = a.toString()
        val bStr = b.toString()

        when {
            // a before b if a is a child of b
            rules.getNode(aStr)?.children?.any { it.value == bStr } == true -> -1
            // b before a if b is a child of a
            rules.getNode(bStr)?.children?.any { it.value == aStr } == true -> 1
            // If no direct relationship, larger numbers should come first
            else -> b.compareTo(a)
        }
    }
}

fun isValidSequence(sequence: List<Int>, rules: DG): Boolean {
    // Convert sequence to strings for comparison with nodes
    val seqStrings = sequence.map { it.toString() }

    // For each pair of numbers in the sequence
    for (i in seqStrings.indices) {
        for (j in i + 1 until seqStrings.size) {
            val first = seqStrings[i]
            val second = seqStrings[j]

            // If there's a rule saying second should come before first,
            // then this sequence is invalid
            if (rules.getNode(second)?.children?.any { it.value == first } == true) {
                return false
            }
        }
    }
    return true
}

data class Node(
    val value: String,
    val children: MutableList<Node> = mutableListOf()
)

class DG {
    private val nodes = mutableMapOf<String, Node>()

    fun addNode(value: String) {
        if (!nodes.containsKey(value)) {
            nodes[value] = Node(value)
        }
    }

    fun addEdge(from: String, to: String) {
        addNode(from)
        addNode(to)
        nodes[from]?.children?.add(nodes[to]!!)
    }

    fun getNode(value: String): Node? = nodes[value]
}


fun main() {

    fun parseInp(input: List<String>): Pair<List<String>, List<String>> {
        val partCutoff = input.indexOf("")
        return input.subList(0, partCutoff) to input.subList(partCutoff + 1, input.size)
    }

    fun parseRules(part1: List<String>): DG {
        val rules = DG()
        for (line in part1) {
            val (from, to) = line.split("|")
            rules.addNode(from.trim())
            rules.addNode(to.trim())
            rules.addEdge(from.trim(), to.trim())
        }

        return rules
    }


    fun part1(input: List<String>): Int {
        val (part1, part2) = parseInp(input)
        val rules = parseRules(part1)
        var tot = 0;

        for (line in part2) {
            val nums = line.split(",").map { it.toInt() }

            if (isValidSequence(nums, rules)) {
                tot += nums[nums.size / 2]
            }

        }

        return tot
    }

    fun part2(input: List<String>): Int {
        val (part1, part2) = parseInp(input)
        val rules = parseRules(part1)
        var tot = 0

        for (line in part2) {
            val nums = line.split(",").map { it.toInt() }

            if (!isValidSequence(nums, rules)) {
                val sorted = sortAccordingToRules(nums, rules)
                tot += sorted[sorted.size / 2]
            }
        }

        return tot
    }


    test("Part 1 (test)", expected = 143, actual = part1(readInput("Day05_test")))
    println("Part 1: " + part1(readInput("Day05")))

    test("Part 2 (test)", expected = 123, actual = part2(readInput("Day05_test")))
    println("Part 2: " + part2(readInput("Day05")))

}
