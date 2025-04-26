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

    @Override
    public void initialise(Cipher cipher, byte[] key, byte[] nonce) {
        // Add your code here
        cipher = new CAST384();
        cipher.initialise(key);
        this.cipher = cipher;
        counter = new byte[8];
        this.nonce = nonce;

    }

    byte[] concatCopy(int blockSize){
        byte[] concat = new byte[Math.max(blockSize, nonce.length + counter.length)];
        System.arraycopy(nonce, 0, concat, 0, nonce.length);
        System.arraycopy(counter, 0, concat, nonce.length, counter.length);
        return concat;
    }

    @Override
    public void encrypt(byte[] data) {
        // Add your code here
        int blockSize = 24;
        int dataLength = data.length;
        int dataLengthBlocked = dataLength/blockSize;
        int dataLengthMod = dataLength % blockSize;

        for(int i = 0; i < dataLengthBlocked; i++) {
            byte[] concatBlock = concatCopy(blockSize);
            cipher.encrypt(concatBlock);
            int offset = i * blockSize;
            for (int j = 0; j < blockSize; j++) {
                data[offset+j] ^= concatBlock[j];
            }

            for (int j = 7; j >= 0; j--) {
                if (++counter[j] != 0) {
                    break;
                }
            }
        }

        if(dataLengthMod > 0){
            byte[] concatBlock = concatCopy(blockSize);
            cipher.encrypt(concatBlock);
            int offset = dataLengthBlocked * blockSize;
            for (int j = 0; j < dataLengthMod; j++) {
                data[offset+j] ^= concatBlock[j];
            }
            for (int j = 7; j >= 0; j--) {
                if (++counter[j] != 0) {
                    break;
                }
            }
        }
    }

    @Override
    public void decrypt(byte[] data) {
        encrypt(data);
    }

    @Override
    public void seek(byte[] counter) {
        // Add your code here
        System.arraycopy(counter, 0, this.counter, this.counter.length-counter.length, counter.length);
    }
}
