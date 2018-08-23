package tw.realtime.project.rtbaseframework.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import tw.realtime.project.rtbaseframework.LogWrapper;

/**
 * Created by vexonelite on 2016/11/16.
 */

public class CryptUtils {

    private AlgorithmParameterSpec mAlgorithmParameterSpec;
    private SecretKeySpec mSecretKeySpec;
    private Cipher mCipher;

    /**
     * 初始化加解密用到的物件
     */
    private void init (String aesKey, String aesIv) throws Exception {
        mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        mSecretKeySpec = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
        mAlgorithmParameterSpec = new IvParameterSpec(aesIv.getBytes("UTF-8"));
    }

    /**
     * 利用 AES 對明文字串加密
     * @param plainText     明文字串
     * @param aesKey        AES key
     * @param aesIv         AES IV
     * @return              加密字串
     * @throws Exception    例外物件
     */
    public String encrypt (String plainText, String aesKey, String aesIv) throws Exception {
        init (aesKey, aesIv);
        mCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec, mAlgorithmParameterSpec);
        byte[] encryptedByteArray = mCipher.doFinal(plainText.getBytes("UTF-8"));
        //String encryptedText = new String(Base64.encode(encryptedByteArray, Base64.DEFAULT), "UTF-8");
        return new String(Base64.encode(encryptedByteArray, Base64.NO_WRAP), "UTF-8");
    }

    /**
     * 利用 AES 對密文字串作解密
     * @param encryptedText     密文字串
     * @param aesKey            AES key
     * @param aesIv             AES IV
     * @return                  明文字串
     * @throws Exception        例外物件
     */
    public String decrypt (String encryptedText, String aesKey, String aesIv) throws Exception {
        init (aesKey, aesIv);
        mCipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec, mAlgorithmParameterSpec);
        byte[] encryptedByteArray = encryptedText.getBytes("UTF-8");
        byte[] base64ByteArray = Base64.decode(encryptedByteArray, Base64.NO_WRAP);
        byte[] decryptedByteArray = mCipher.doFinal(base64ByteArray);
        //String decryptedText = new String(decryptedByteArray, "UTF-8");
        return new String(decryptedByteArray, "UTF-8");
    }

    /**
     * @param random It must be a SecureRandom instance.
     * @param length The desired length of result.
     * @return Return a randomly generated String with length is exactly 32.
     */
    @NonNull
    public static String generateRandomStringWithLength (@NonNull SecureRandom random, int length) throws Exception {
//        return random.ints(48,122)
//                .filter(i-> (i<57 || i>65) && (i <90 || i>97))
//                .mapToObj(i -> (char) i)
//                .limit(length)
//                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
//                .toString();

        //String randomString = "";
        final StringBuilder builder = new StringBuilder();
        while (builder.length() < length) {
            //randomString = randomString + generateRandomString(random);
            builder.append(generateRandomString(random));
            LogWrapper.showLog(Log.INFO, "CryptUtils", "randomString: " + builder.toString() + ", length: " + builder.length());
        }

        final String randomString = builder.toString();
        final String result = (randomString.length() >= length)
                ? randomString.substring(0, length) : randomString;
        LogWrapper.showLog(Log.INFO, "CryptUtils", "result: " + result + ", length: " + result.length());

        return result;
    }

    /**
     * This works by choosing 130 bits from a cryptographically secure random bit generator,
     * and encoding them in base-32.
     * 128 bits is considered to be cryptographically strong,
     * but each digit in a base 32 number can encode 5 bits,
     * so 128 is rounded up to the next multiple of 5.
     * This encoding is compact and efficient, with 5 random bits per character.
     *
     * Compare this to a random UUID, which only has 3.4 bits per character in standard layout,
     * and only 122 random bits in total.
     *
     * If you allow session identifiers to be easily guessable
     * (too short, flawed random number generator, etc.), attackers can hijack other's sessions.
     *
     * Note that SecureRandom objects are expensive to initialize, so you'll want to keep one around and reuse it.
     *
     * @param random It must be a SecureRandom instance.
     * @return Return a randomly generated String.
     */
    @NonNull
    public static String generateRandomString (@NonNull SecureRandom random) { //throws Exception {
        return new BigInteger(130, random).toString(32);
    }

    /**
     * @return Return a UUID random string plus "_" and time stamp string
     * @throws Exception
     */
    @NonNull
    public static String generateRandomStringViaUuid () {
        return UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
    }

    /**
     * Use SHA-256 message digest algorithm to process the given byte array and
     * generate a fixed-length hashed byte array
     * @param data  The given byte array.
     * @return A fixed-length hashed byte array.
     */
    @Nullable
    public static byte[] messageDigestSHA256 (@NonNull byte[] data) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            LogWrapper.showLog(Log.ERROR, "CryptUtils", "Exception on messageDigestSHA256", e);
            return null;
        }
    }


    public static int getRandomNumber (@NonNull SecureRandom random, int min, int max) throws IllegalArgumentException {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("min or max < 0");
        }
//        if (null == random) {
//            throw new IllegalArgumentException("random is null");
//        }

        //SecureRandom rnd = new SecureRandom();
        final int inclusive = max - min + 1;
        //final int exclusive = max - min;

        //int rndIntIncl = random.nextInt(inclusive) + min;
        //int rndIntExcl = random.nextInt(exclusive) + min;

        return random.nextInt(inclusive) + min;
    }

}
