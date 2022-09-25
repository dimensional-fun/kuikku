package dimensional.quic

/** [RFC9000](https://www.rfc-editor.org/rfc/rfc9000.html) constants */
public object Quic {
    //
    public object Stream {
        public const val ClientBidi: Int = 0x00
        public const val ServerBidi: Int = 0x01
        public const val ClientUni:  Int = 0x02
        public const val ServerUni:  Int = 0x03
    }

    public object Frame {
        // See https://www.rfc-editor.org/rfc/rfc9000.html#name-streams_blocked-frames
        public const val StreamOffsetFlag: Int = 0x04
        public const val StreamLengthFlag: Int = 0x02
        public const val StreamFinishFlag: Int = 0x01
    }
}
