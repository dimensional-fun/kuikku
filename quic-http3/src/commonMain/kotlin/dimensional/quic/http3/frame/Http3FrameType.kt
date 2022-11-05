package dimensional.quic.http3.frame

public enum class Http3FrameType(public val value: Long) {
    Data       (0x00),
    Headers    (0x01),
    CancelPush (0x02),
    Settings   (0x04),
    PushPromise(0x05),
    GoAway     (0x07),
    MaxPushId  (0x0d),
    ;

    public companion object {
        public operator fun get(value: Long): Http3FrameType? = values()
            .find { it.value == value }

        public operator fun get(value: Int): Http3FrameType? = get(value.toLong())
    }
}
