# CAST-384 Cryptography Library

This project implements the CAST-384 block cipher and a Counter (CTR) mode of operation in Java. It is designed for educational purposes and includes a comprehensive test suite.

## Features

- **CAST-384 Cipher**: A 384-bit key, 192-bit block symmetric cipher.
- **CTR Mode**: Counter mode implementation for block cipher operation.
- **Test Suite**: Extensive JUnit tests for cipher, mode, and key schedule validation.
- **Utility Functions**: Hex encoding/decoding utilities for test and debug support.

## Project Structure

```
src/
  main/
    java/
      uk/ac/nottingham/cryptography/
        CAST384.java         # CAST-384 cipher implementation
        CASTCipher.java      # Abstract base for CAST ciphers
        CASTKeySet.java      # Key schedule structure
        Cipher.java          # Abstract cipher interface
        CipherMode.java      # Abstract cipher mode interface
        CTRMode.java         # Counter mode implementation
        HexUtils.java        # Hex and array utilities
  test/
    java/
      uk/ac/nottingham/cryptography/
        CAST/               # CAST cipher tests
        Modes/              # CTR mode tests
```

## Building

This project uses Gradle. To build:

```sh
./gradlew build
```

## Running Tests

To run all tests:

```sh
./gradlew test
```

## Usage Example

```java
import uk.ac.nottingham.cryptography.CAST384;
import uk.ac.nottingham.cryptography.CTRMode;

CAST384 cipher = new CAST384();
byte[] key = new byte[48]; // 384-bit key
cipher.initialise(key);

byte[] plaintext = new byte[24]; // 192-bit block
cipher.encrypt(plaintext); // Encrypt in-place

// Using CTR mode
CTRMode mode = new CTRMode();
byte[] nonce = new byte[16];
mode.initialise(cipher, key, nonce);
mode.encrypt(plaintext); // Encrypt in-place using CTR mode
```

## Notes

- The core cipher logic is in [`CAST384.java`](src/main/java/uk/ac/nottingham/cryptography/CAST384.java).
- Do not edit files marked as "Do not edit" in their headers.
- The test suite is located in [`src/test/java/uk/ac/nottingham/cryptography/`](src/test/java/uk/ac/nottingham/cryptography/).

## License

This project is for educational use only.