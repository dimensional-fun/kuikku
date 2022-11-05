package dimensional.quic.tls.handshake


public enum class TlsHandshakeType(public val code: Int) {
    ClientHello         (  1),
    ServerHello         (  2),
    NewSessionTicket    (  4),
    EndOfEarlyData      (  5),
    EncryptedExtensions (  8),
    Certificate         ( 11),
    CertificateRequest  ( 13),
    CertificateVerify   ( 15),
    Finished            ( 20),
    KeyUpdate           ( 24),
    MessageHash         (254),
    ;
}
