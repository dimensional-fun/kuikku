package dimensional.quic.tls

public enum class TlsRecordType(
    public val code: Int
) {
    Invalid          ( 0),
    ChangeCipherSpec (20),
    Alert            (21),
    Handshake        (22),
    ApplicationData  (23),
    ;
}
