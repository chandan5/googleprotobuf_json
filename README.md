# googleprotobuf_json

Comparison of Google Protobuf and JSON for serialisation and deserialisation.

See [Assignment1.pdf](Assignment\ 1.pdf) for specifications.

Netbeans 8.1 was used to code.

## Prerequisites
1. Java
2. Netbeans (Or any other IDE which is able to import this project.)

## Run
1. Download directory to your system.
2. Import direcotry to netbeans
3. Click Run button

### Extra (If you want to extend Result.proto)
1. protoc (Run `brew install protobuf` on mac)

To compile `Result.proto` go to src/googleprotobuf_json folder and type `protoc -I=. --java_out=. Result.proto`
