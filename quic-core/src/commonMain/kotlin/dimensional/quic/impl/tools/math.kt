package dimensional.quic.impl.tools

import kotlin.experimental.and

public inline infix fun Short.shl(bitCount: Int): Short = (toInt() shl bitCount).toShort()

public inline infix fun Short.shr(bitCount: Int): Short = (toInt() shr bitCount).toShort()

public inline infix fun Byte.and(other: Int): Byte = (toInt() and other).toByte()

public inline fun Byte.asShort(): Short = asInt().toShort()

public inline fun Byte.asLong(): Long = asInt().toLong()

public inline fun Byte.asInt(): Int = toInt() and 0xFF

public inline fun Short.asByte(): Byte = (this and 0xFF).toByte()

public inline fun Long.asByte(): Byte = (this and 0xFF).toByte()

public inline fun Int.asByte(): Byte = (this and 0xFF).toByte()

/* byte flags */
public inline infix fun Byte.hasFlag(other: Int): Boolean = (toInt() and other) == other

public inline infix fun Byte.hasFlag(other: Byte): Boolean = hasFlag(other.toInt())

/* int flags */
public inline infix fun Int.hasFlag(other: Int): Boolean = (this and other) == other

public inline infix fun Int.withFlag(other: Int): Int = this or other

/* long flags */
public inline fun Long.hasAnyFlags(vararg other: Int): Boolean = other.any { this hasFlag it }

public inline fun Long.hasAllFlags(vararg other: Int): Boolean = other.all { this hasFlag it }

public inline infix fun Long.hasFlag(other: Int): Boolean = hasFlag(other.toLong())

public inline infix fun Long.withFlag(other: Int): Long = withFlag(other.toLong())

public inline infix fun Long.hasFlag(other: Long): Boolean = (this and other) == other

public inline infix fun Long.withFlag(other: Long): Long = this or other
