# Kuikku

> Kotlin multiplatform implementation of the [QUIC protocol][9000] & more!

- **Discord Server:** [Dimensional Fun](https://discord.gg/8R4d8RydT4) 

## 🚀 Usage

> **Warning**  
> This library is in development! Check [#todo](#-todo) to see what's left to be implemented.

_Coming Soon!_

## 📋 TODO

_work in progress list_

**Core Protocol:**
- [ ] TLS (#1)
- [ ] Streams
  - [x] ID Generation
  - [ ] Flow Control 
- [ ] Packets
  - [ ] Packet Coalescing
  - [ ] Congestion Control

**HTTP/3:**
Our http/3 implementation will only provide base functionality for creating HTTP3 connections, in case you want to use it as a legitimate HTTP3 client we implement a Ktor client engine.

_todo_

## 🔗 Acknowledgements

- **RFCs** [9000][9000], [9001][9001], [9002][9002]
- [**pjtr/flupke**](https://bitbucket.org/pjtr/flupke) for HTTP/3 implementation inspiration
- QUIC implementations that inspired **Kuikku** 
  - [**pjtr/kwik**](https://bitbucket.org/pjtr/kwik)
  - [**rmarx/quicker**](https://github.com/rmarx/quicker)

**Note:** I have added comments to any code that was ported from any of the git repositories referenced.

---

[9000]: <https://www.rfc-editor.org/info/rfc9000> "QUIC IETF"
[9001]: <https://www.rfc-editor.org/info/rfc9001> "QUIC TLS IETF"
[9002]: <https://www.rfc-editor.org/info/rfc9002> "QUIC Recovery IETF"
