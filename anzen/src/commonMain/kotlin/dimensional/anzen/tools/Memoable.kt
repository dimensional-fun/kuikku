package dimensional.anzen.tools

public interface Memoable {
    /** Produce a copy of this object. */
    public fun copy(): Memoable

    /** Restore a copied object state into this object. */
    public fun reset(other: Memoable): Memoable
}
