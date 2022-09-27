package dimensional.anzen.digest

public interface ExtendedDigest : Digest {
    /** The size of the current internal buffer (in bytes) */
    public val byteLength: Int
}
