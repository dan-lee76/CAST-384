package uk.ac.nottingham.cryptography;

/**
 * Implementation of CASTCipher that encrypts and decrypts using the
 * CAST-384 algorithm.
 * <br/>
 * This class is ONE of TWO primary code files in which you can complete
 * your solution to the coursework.
 */
public class CAST384 extends CASTCipher {

    public CAST384() {
        super(192, 384);
    }

    @Override
    public void initialise(byte[] key) {
        CASTKeySet scheduleKeys = generateScheduleKeys(12, 4);
        K = generateRoundKeys(scheduleKeys, key, 12, 4);
    }

    @Override
    public CASTKeySet generateScheduleKeys(int roundCount, int dodecadCount) {
        int cM = 0x5A827999;
        int cR = 19;
        int cTriangleM = 0x6ED9EBA1;
        int cTriangleR = 17;

        int totalPairs = roundCount * dodecadCount * 12;

        int[] tm = new int[totalPairs];
        int[] tr = new int[totalPairs];

        for (int i = 0; i < totalPairs; i++) {
            tm[i] = cM;
            cM += cTriangleM;
            tr[i] = cR;
            cR = (cR + cTriangleR) & 31;
        }
        return new CASTKeySet(tm, tr);
    }

    int[] bytesToInt(byte[] input, int size) { // Converts bytes to integers
        int[] block = new int[size];
        for (int i = 0; i < size; i++) {
            if (i * 4 <= input.length - 1) {
                block[i] = (input[(4 * i)] & 0xff) << 24 |
                        (input[(4 * i) + 1] & 0xff) << 16 |
                        (input[(4 * i) + 2] & 0xff) << 8 |
                        (input[(4 * i) + 3] & 0xff);
            } else {
                block[i] = 0; // Pads with zeros if input is not the required size
            }
        }
        return block;
    }

    void intToBytes(int[] input, byte[] block) { // Converts ints into bytes via pass through reference.
        for (int i = 0; i < (24 / 4); i++) {
            block[4 * i] = (byte) ((input[i] >> 24) & 0xff);
            block[(4 * i) + 1] = (byte) ((input[i] >> 16) & 0xff);
            block[(4 * i) + 2] = (byte) ((input[i] >> 8) & 0xff);
            block[(4 * i) + 3] = (byte) (input[i] & 0xff);
        }
    }

    @Override
    public CASTKeySet generateRoundKeys(CASTKeySet T, byte[] key, int roundCount, int dodecadCount) {
        int totalPairs = roundCount * 6;
        int idxValue = 0;
        int[] km = new int[totalPairs];
        int[] kr = new int[totalPairs];
        int[] keyBlock = bytesToInt(key, 12);

        int[] Tm = T.getM();
        int[] Tr = T.getR();
        for (int i = 0; i < roundCount; i++) {
            for (int d = 1; d <= dodecadCount; d++) {
                dodecad(keyBlock, Tm, Tr, idxValue);
                idxValue += 12;
            }

            for (int j = 0; j < 6; j++) {
                int id = i * 6;
                km[id + j] = keyBlock[11 - (2 * j)];
                kr[id + j] = keyBlock[j * 2] & 31;
            }

        }
        return new CASTKeySet(km, kr);
    }


    static int rotatel(int x, int n) {
        return (x << n) | (x >>> -n); //Rotate left by n bits, then mask off the bits that are not needed
    }

    @Override
    public int f1(int d, int Km, int Kr) {
        int ans = rotatel((Km + d), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 ^ s2) - s3;
        return t2 + s4;
    }

    @Override
    public int f2(int d, int Km, int Kr) {
        int ans = rotatel((Km ^ d), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 - s2) + s3;
        return t2 ^ s4;

    }

    @Override
    public int f3(int d, int Km, int Kr) {
        int ans = rotatel((Km - d), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 + s2) ^ s3;
        return t2 - s4;
    }

    @Override
    public int f4(int d, int Km, int Kr) {
        int ans = rotatel((Km - d), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 ^ s2) + s3;
        return t2 - s4;
    }

    @Override
    public int f5(int d, int Km, int Kr) {
        int ans = rotatel((d + Km), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 - s2) ^ s3;
        return t2 + s4;
    }

    @Override
    public int f6(int d, int Km, int Kr) {
        int ans = rotatel((d ^ Km), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 + s2) - s3;
        return t2 ^ s4;
    }

    @Override
    public void dodecad(int[] block, int[] Tm, int[] Tr, int idx) {
        block[10] = block[10] ^ f1(block[11], Tm[idx], Tr[idx]);
        block[9] = block[9] ^ f2(block[10], Tm[idx + 1], Tr[idx + 1]);
        block[8] = block[8] ^ f3(block[9], Tm[idx + 2], Tr[idx + 2]);
        block[7] = block[7] ^ f4(block[8], Tm[idx + 3], Tr[idx + 3]);
        block[6] = block[6] ^ f5(block[7], Tm[idx + 4], Tr[idx + 4]);
        block[5] = block[5] ^ f6(block[6], Tm[idx + 5], Tr[idx + 5]);

        block[4] = block[4] ^ f1(block[5], Tm[idx + 6], Tr[idx + 6]);
        block[3] = block[3] ^ f2(block[4], Tm[idx + 7], Tr[idx + 7]);
        block[2] = block[2] ^ f3(block[3], Tm[idx + 8], Tr[idx + 8]);
        block[1] = block[1] ^ f4(block[2], Tm[idx + 9], Tr[idx + 9]);
        block[0] = block[0] ^ f5(block[1], Tm[idx + 10], Tr[idx + 10]);

        block[11] = block[11] ^ f6(block[0], Tm[idx + 11], Tr[idx + 11]);
    }

    @Override
    public void hexad(int[] block, int[] Km, int[] Kr, int idx) {
        block[4] = block[4] ^ f1(block[5], Km[idx], Kr[idx]);
        block[3] = block[3] ^ f2(block[4], Km[idx + 1], Kr[idx + 1]);
        block[2] = block[2] ^ f3(block[3], Km[idx + 2], Kr[idx + 2]);
        block[1] = block[1] ^ f4(block[2], Km[idx + 3], Kr[idx + 3]);
        block[0] = block[0] ^ f5(block[1], Km[idx + 4], Kr[idx + 4]);

        block[5] = block[5] ^ f6(block[0], Km[idx + 5], Kr[idx + 5]);
    }

    @Override
    public void hexadInv(int[] block, int[] Km, int[] Kr, int idx) {
        block[5] = block[5] ^ f6(block[0], Km[idx + 5], Kr[idx + 5]);

        block[0] = block[0] ^ f5(block[1], Km[idx + 4], Kr[idx + 4]);
        block[1] = block[1] ^ f4(block[2], Km[idx + 3], Kr[idx + 3]);
        block[2] = block[2] ^ f3(block[3], Km[idx + 2], Kr[idx + 2]);
        block[3] = block[3] ^ f2(block[4], Km[idx + 1], Kr[idx + 1]);
        block[4] = block[4] ^ f1(block[5], Km[idx], Kr[idx]);
    }

    @Override
    public void encrypt(byte[] data) {
        int[] dataBlock = bytesToInt(data, 6);
        int[] Km = K.getM();
        int[] Kr = K.getR();
        for (int i = 0; i < 6; i++) {
            hexad(dataBlock, Km, Kr, i * 6);
        }
        for (int i = 6; i < 12; i++) {
            hexadInv(dataBlock, Km, Kr, i * 6);
        }

        intToBytes(dataBlock, data);

    }

    @Override
    public void decrypt(byte[] data) {
        int[] dataBlock = bytesToInt(data, 6);
        int[] Km = K.getM();
        int[] Kr = K.getR();

        for (int i = 0; i < 6; i++) {
            hexad(dataBlock, Km, Kr, 66 - (i * 6));
        }
        for (int i = 6; i < 12; i++) {
            hexadInv(dataBlock, Km, Kr, 66 - (i * 6));
        }

        intToBytes(dataBlock, data);
    }

}
