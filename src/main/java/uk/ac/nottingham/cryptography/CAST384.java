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
        return new CASTKeySet(null, null);
    }

    @Override
    public CASTKeySet generateRoundKeys(CASTKeySet T, byte[] key, int roundCount, int dodecadCount) {
        // Add your code here
        return new CASTKeySet(null, null);
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
