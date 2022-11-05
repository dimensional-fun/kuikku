package dimensional.quic.http3

/** [RFC9114](https://www.rfc-editor.org/rfc/rfc9114.html) constants */
public object Http3 {
    /** See [RFC9114#name-http-3-error-codes](https://www.rfc-editor.org/rfc/rfc9114.html#name-http-3-error-codes) */
    public object Errors {
        public const val None:                 Int = 0x0100
        public const val GeneralProtocolError: Int = 0x0101
        public const val InternalError:        Int = 0x0102
        public const val StreamCreationError:  Int = 0x0103
        public const val ClosedCriticalStream: Int = 0x0104
        public const val FrameUnexpected:      Int = 0x0105
        public const val FrameError:           Int = 0x0106
        public const val ExcessiveLoad:        Int = 0x0107
        public const val IdError:              Int = 0x0108
        public const val SettingsError:        Int = 0x0109
        public const val MissingSettings:      Int = 0x010a
        public const val RequestRejected:      Int = 0x010b
        public const val RequestCancelled:     Int = 0x010c
        public const val RequestIncomplete:    Int = 0x010d
        public const val MessageError:         Int = 0x010e
        public const val ConnectError:         Int = 0x010f
        public const val VersionFallback:      Int = 0x0110
    }

    /** See [RFC9114#name-defined-settings-parameters](https://www.rfc-editor.org/rfc/rfc9114.html#name-defined-settings-parameters) */
    public object Settings {
        // HTTP/3
        public const val MaxFieldSectionSize: Int = 0x06

        // QPack, see https://www.rfc-editor.org/rfc/rfc9204.html#name-configuration
        public const val QPackMaxTableCapacity: Int = 0x01
        public const val QPackBlockedStreams: Int = 0x07
    }
}
