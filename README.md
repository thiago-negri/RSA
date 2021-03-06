RSA
===

Na�ve RSA implementation, educational purposes.

Block size
----------

### Encoding
* Input (raw): `modulus_bit_length / 8 - 1`
* Output (encoded): `modulus_bit_length / 8`

### Decoding
* Input (encoded): `modulus_bit_length / 8`
* Output (decoded): `modulus_bit_length / 8 - 1`

Padding scheme
--------------
The last input block will be appended with one `0x00` and consecutive `0xFF` to match the block size before encryption.

So, the last decrypted number need to discard last `0xFF` bytes until it finds a `0x00`, the quantity of this bytes is the padding.

Encryption example:
* Block size = raw 4 bytes.
* Last input block `0x0001` (2 bytes).
* Last input block converted to `0x000100FF` (4 bytes).
* Last input as a big number is `0x0100FF` (3 bytes).

Decryption example:
* Block size = raw 4 bytes.
* Last input after decryption as a big number is `0x0100FF`.
* Discard all last `0xFF` bytes leaded by a `0x00`, results in `0x01` with 2 bytes discarded.
* Padding count = discarded bytes = 2 bytes.
* Last block size is "block size" - "padding count" = 4 - 2 = 2.
* Last output number is `0x0001` (2 bytes).

The padding information during encryption is appended to the last block automatically by `NaiveBlockInputStream` class.
In case the last block size has the exact expected size, an additional block of padding information (completely filled with `0xFF`)
is appended by the encryption method at `Main` class. 

The padding information during decryption is handled by an anonymous inner class at `Main`, search for `wrappedBlockOutputStream`.

The `NaiveRSAStrategyTest` also uses this padding scheme for testing.

