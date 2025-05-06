package uk.ac.nottingham.cryptography;

/**
 * Implementation of CipherMode that performs encryption and decryption
 * using Counter mode (CTR) with an underlying Cipher.
 * <br/>
 * This class is TWO of TWO primary code files in which you can complete
 * your solution to the coursework.
 */
public class CTRMode extends CipherMode {

    public CTRMode() {
        super();
    }

    byte[] counter; //8
    byte[] nonce; //16
    int bitsRemaining; //24

    @Override
    public void initialise(Cipher cipher, byte[] key, byte[] nonce) {
        cipher = new CAST384();
        cipher.initialise(key);
        this.cipher = cipher;
        counter = new byte[8];
        this.nonce = nonce;
        bitsRemaining = 24;

    }

    byte[] concatCopy(int blockSize) { //Concatenate nonce and counter into a single byte array
        byte[] concat = new byte[Math.max(blockSize, nonce.length + counter.length)];
        System.arraycopy(nonce, 0, concat, 0, nonce.length);
        System.arraycopy(counter, 0, concat, nonce.length, counter.length);
        return concat;
    }

    @Override
    public void encrypt(byte[] data) {
        int blockSize = Math.min(bitsRemaining, 24);
        int dataLength = data.length;
        byte[] concatBlock = concatCopy(blockSize);
        cipher.encrypt(concatBlock);

        for (int i = 0; i < dataLength; i++) {
            if (bitsRemaining <= 0) {
                for (int j = 7; j >= 0; j--) {
                    if (++counter[j] != 0) {
                        break;
                    }
                }
                bitsRemaining = 24;
                concatBlock = concatCopy(blockSize);
                cipher.encrypt(concatBlock);
            }
            data[i] ^= concatBlock[(24 - bitsRemaining)];
            bitsRemaining--;

        }
    }

    @Override
    public void decrypt(byte[] data) {
        encrypt(data);
    }

    @Override
    public void seek(byte[] counter) {
        bitsRemaining = 24;
        System.arraycopy(counter, 0, this.counter, this.counter.length - counter.length, counter.length);
    }
}
