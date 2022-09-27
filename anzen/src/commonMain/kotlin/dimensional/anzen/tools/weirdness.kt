package dimensional.anzen.tools

public fun numbersToInts(vararg numbers: Number): IntArray = IntArray(numbers.size) { numbers[it].toInt() }
