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
        // Add your code here
    }

    @Override
    public CASTKeySet generateScheduleKeys(int roundCount, int dodecadCount) {
        // Add your code here
        int cm = 0x5A827999;
        int cr = 19;
        int ctm = 0x6ED9EBA1;
        int ctr = 17;

        int totalPairs = roundCount * dodecadCount * 12;

        int[] tm = new int[totalPairs];
        int[] tr = new int[totalPairs];

        for (int i = 0; i < totalPairs; i++) {
            tm[i] = cm;
            cm += ctm;
            tr[i] = cr;
            cr = (cr + ctr) % 32;
        }
        return new CASTKeySet(tm, tr);
    }

    static int[] bytesToInt(byte[] input) {
        int[] block = new int[12];
        for(int i = 0; i < 12; i++){
            block[i] = (input[(4*i)] & 0xff) << 24 |
                    (input[(4*i)+1] & 0xff) << 16 |
                    (input[(4*i)+2] & 0xff) << 8 |
                    (input[(4*i)+3] & 0xff);
        }
        return block;
    }

    @Override
    public CASTKeySet generateRoundKeys(CASTKeySet T, byte[] key, int roundCount, int dodecadCount) {
        // Add your code here
        int totalPairs = roundCount * 6;
        int idxValue = 0;
        int[] km = new int[totalPairs];
        int[] kr = new int[totalPairs];
        int[] keyBlock = bytesToInt(key);

        int[] Tm = T.getM();
        int[] Tr = T.getR();
        for (int i = 0; i < roundCount; i++) {
            for (int d = 1; d <= dodecadCount; d++) {
                dodecad(keyBlock, Tm, Tr, idxValue);
                idxValue+=12;
            }

            for(int j = 0; j < 6; j++){
                int id = i * 6;
                km[id + j] = keyBlock[11-(2*j)];
                kr[id + j] = keyBlock[j*2] & 31;
            }

        }
        return new CASTKeySet(km, kr);
    }


    static int rotatel(int x, int n){
        return (x << n) | (x >>> (32 - n));
    }

    @Override
    public int f1 (int d, int Km, int Kr) {
        // Add your code here
        int ans = rotatel((Km + d), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 ^ s2) - s3 ;
        return t2 + s4;
    }

    @Override
    public int f2 (int d, int Km, int Kr) {
        // Add your code here

        int ans = rotatel((Km ^ d), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 - s2) + s3 ;
        return t2 ^ s4;

    }

    @Override
    public int f3 (int d, int Km, int Kr) {
        // Add your code here
        int ans = rotatel((Km - d), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 + s2) ^ s3;
        return t2 - s4;
    }

    @Override
    public int f4 (int d, int Km, int Kr) {
        // Add your code here
        int ans = rotatel((Km - d), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 ^ s2) + s3;
        return t2 - s4;
    }

    @Override
    public int f5 (int d, int Km, int Kr) {
        // Add your code here
        int ans = rotatel((d + Km), Kr);

        int s1 = S1[(ans >> 24) & 0xFF];
        int s2 = S2[(ans >> 16) & 0xFF];
        int s3 = S3[(ans >> 8) & 0xFF];
        int s4 = S4[ans & 0xFF];

        int t2 = (s1 - s2) ^ s3;
        return t2 + s4;
    }

    @Override
    public int f6 (int d, int Km, int Kr) {
        // Add your code here
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
        // Add your code here
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
        // Add your code here
    }

    @Override
    public void hexadInv(int[] block, int[] Km, int[] Kr, int idx) {
        // Add your code here
    }

    @Override
    public void encrypt(byte[] data) {
        // Add your code here
    }

    @Override
    public void decrypt(byte[] data) {
        // Add your code here
    }

}
