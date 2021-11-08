package Project2;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
 
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
 
public class AES {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
    
	 private static final String SECRET_KEY = "ssshhhhhhhhhhh!!!!";
	 private static final String SALT = "saltlol";
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    
    public static String encryptPlusSalt(String strToEncrypt) 
    {
    	
    	try {
    	      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    	      IvParameterSpec ivspec = new IvParameterSpec(iv);
    	      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    	      KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
    	      SecretKey tmp = factory.generateSecret(spec);
    	      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
    	 
    	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
    	      return Base64.getEncoder()
    	          .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    	    } catch (Exception e) {
    	      System.out.println("Error while encrypting: " + e.toString());
    	    }
    	    return null;
    }
    
    public static String decryptPlusSalt(String strToDecrypt) {
    	try {
    	      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    	      IvParameterSpec ivspec = new IvParameterSpec(iv);
    	 
    	      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    	      KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
    	      SecretKey tmp = factory.generateSecret(spec);
    	      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
    	 
    	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    	      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
    	      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    	    } catch (Exception e) {
    	      System.out.println("Error while decrypting: " + e.toString());
    	    }
    	    return null;
    	  }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
