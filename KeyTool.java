package th.co.ncr.connector.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import th.co.ncr.connector.config.Constants;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Component("KeyTool")
@Slf4j
public class KeyTool {
	
	@Autowired private Environment env;
	
//	@Autowired private Constants constants;
	
	public static void main(String[] args) throws Exception {
		KeyTool keyTool = new KeyTool();
		
		SecretKey key = keyTool.loadKey("Nczr8EyNFuQnlOctrDW4OIwwVYzfeeF61O0xfECLhlQ=".getBytes());

		System.out.println(keyTool.encryptToString("AaBb1234-".getBytes(), key));
		System.out.println(keyTool.encryptToString("Abc1234-".getBytes(), key));
		
		
		
		System.out.println(keyTool.checkEncPwdAndDecrypt("{enc}bhR+tD30iGwn", key));
		System.out.println(keyTool.checkEncPwdAndDecrypt("{enc}bhdf5z71j3U=", key));
		
//		String input = "{\"atmID\": \"ATM0001\",\"IP\": \"153.70.1.1\",\"master\": \"10.2.0\",\"software\": \"10.2.0\",\"screen\": \"22.11.07\",\"firmwareEPP\": \"ZT598H77D32\"}";
//		System.out.println(keyTool.hashText(input));
//
//		String keyB64 = keyTool.generateKeyToString(256);
//		System.out.println("NCRMasterKey: " + keyB64);
//
//		String feature = "sw:10,10,100&img:10,10,100&expired:never";
//		System.out.println("Feature: " + feature);
//
//		SecretKey key = keyTool.loadKey("Nczr8EyNFuQnlOctrDW4OIwwVYzfeeF61O0xfECLhlQ=".getBytes());
//		String encryptedPwd = keyTool.encryptToString("admin1|$|password,admin2|$|password,admin3|$|password".getBytes(), key);
//		System.out.println("License: " + encryptedPwd);
//
//		encryptedPwd = keyTool.decryptFromStringToString(encryptedPwd, key);
//		System.out.println("Decrypted: " + encryptedPwd);
//
//		//Validate License
//		String licenseKey = encryptedPwd;
//		if (licenseKey != null && !licenseKey.equalsIgnoreCase("")) {
//			String[] fields = licenseKey.split("&");
//			for (String field : fields) {
//				if (field.equals("sw")) {
//
//				} else if (field.equals("img")) {
//
//				} else if (field.equals("expired")) {
//
//				}
//			}
//		} else {
//			//Invalid License
//		}

//		byte[] pwdByte = Utils.getBytesFromFile("C:/ATMConf/public.key");
//		String privateKey = Base64.getEncoder().encodeToString(pwdByte);
//		System.out.println(privateKey);
//
//		String secret = "LOGIN_NAME|worakit&PASSWORD|password";
//		String base64 = Base64.getEncoder().encodeToString(secret.getBytes());
//		String encStr = KeyTool.encrypt(base64, "C:/ATMConf/public.key");
//		System.out.println(encStr);
//
//		String plainText = KeyTool.decryptForVerify(encStr, "C:/ATMConf/private.key");
//		System.out.println(plainText);
//
//		HashMap<String,String> paramMap = new HashMap<String,String>();
//        String[] porp = plainText.split("&");
//        for (int i = 0; i < porp.length; i++) {
//            String[] val = porp[i].split("\\|");
//            paramMap.put(val[0], val[1]);
//        }
//        System.out.println(paramMap);
	}
	
	
	public KeyPair createNewKeyPair() throws NoSuchAlgorithmException {
		String algorithm = "RSA";
		int keySize = 2048;
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
		generator.initialize(keySize);
		KeyPair pair = generator.generateKeyPair();
		
		return pair;
	}
	
	public static final String PRV_K = "private";
	public static final String PBL_K = "public";
	
	public Key getKeyFromKeyPair(String type, KeyPair keyPair) {
		if (type.equalsIgnoreCase(PRV_K)) {
			return keyPair.getPrivate();
		} else {
			return keyPair.getPublic();
		}
	}
	
	public static String encrypt(String plainText,String publicKeyPath) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyPath));
        byte[] plaintextByte = plainText.getBytes("UTF-8");
        byte[] cipherByte = cipher.doFinal(plaintextByte);
        String encodeCipherText = Base64.getEncoder().encodeToString(cipherByte);
        return encodeCipherText;
    }


    public static String decrypt(String cipherText,String privateKeyPath) throws Exception{
        byte[] cipherByte = Base64.getDecoder().decode(cipherText);
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyPath));
        byte[] plaintextByte = cipher.doFinal(cipherByte);
        String plaintextStr = new String(plaintextByte, "UTF-8");
        return plaintextStr;
    }
    
    public static String decryptForVerify(String cipherText,String privateKeyPath) throws Exception{
        byte[] cipherByte = Base64.getDecoder().decode(cipherText);
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyPath));
        byte[] plaintextByte = cipher.doFinal(cipherByte);
        String plaintextStr = new String(plaintextByte, "UTF-8");
        plaintextStr = new String(Base64.getDecoder().decode(plaintextStr), "UTF-8");
        return plaintextStr;
    }
    
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        File f = new File(filename);
        byte[] keyBytes = new byte[(int) f.length()];
        
        try (
        		FileInputStream fis = new FileInputStream(f);
        		DataInputStream dis = new DataInputStream(fis);
        ) {
        	dis.readFully(keyBytes);
        }
        
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);

    }

    public static PublicKey getPublicKey(String filename) throws Exception {
        File f = new File(filename);
        byte[] keyBytes = new byte[(int) f.length()];
        
        try (
        		FileInputStream fis = new FileInputStream(f);
        		DataInputStream dis = new DataInputStream(fis);
        ) {
        	dis.readFully(keyBytes);
        }
        
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
	
	public byte[] generateKey(int keysize) throws NoSuchAlgorithmException, IOException{
//		AES/CBC/NoPadding (128)
//		AES/CBC/PKCS5Padding (128)
//		AES/ECB/NoPadding (128)
//		AES/ECB/PKCS5Padding (128)
//		DES/CBC/NoPadding (56)
//		DES/CBC/PKCS5Padding (56)
//		DES/ECB/NoPadding (56)
//		DES/ECB/PKCS5Padding (56)
//		DESede/CBC/NoPadding (168)
//		DESede/CBC/PKCS5Padding (168)
//		DESede/ECB/NoPadding (168)
//		DESede/ECB/PKCS5Padding (168)
//		RSA/ECB/PKCS1Padding (1024, 2048)
//		RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
//		RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
		
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(keysize); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		
		return raw;
	}
	
	public String generateKeyToString(int keysize) throws NoSuchAlgorithmException, IOException{
		return Base64.getEncoder().encodeToString(generateKey(keysize));
	}

	public byte[] encrypt(byte[] data, SecretKey sKey) throws Exception{
		if (sKey == null) return data;
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE,sKey,generateIv());
		byte[] output = cipher.doFinal(data);
		return output;
	}
	
	public byte[] encrypt(byte[] data) throws Exception{
		return encrypt(data,loadDefaultKey());
	}
	
	public String encryptToString(byte[] data) throws Exception{
		byte[] cipher = encrypt(data,loadDefaultKey());
		String cipherText = Base64.getEncoder().encodeToString(cipher);
		return new String(cipherText);
	}
	
	public String encryptToString(byte[] data, SecretKey key) throws Exception{
		byte[] cipher = encrypt(data, key);
		String cipherText = Base64.getEncoder().encodeToString(cipher);
		return new String(cipherText);
	}
	
	public byte[] decrypt(byte[] data, SecretKey sKey) throws Exception{
		if (sKey == null) return data;
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE,sKey,generateIv());
		byte[] output = cipher.doFinal(data);
		return output;
	}
	
	public byte[] decrypt(byte[] data) throws Exception{
		return decrypt(data,loadDefaultKey());
	}
	
	public String decryptToString(byte[] data) throws Exception{
		return new String(decrypt(data,loadDefaultKey()));
	}
	
	public String decryptToString(byte[] data, SecretKey key) throws Exception{
		return new String(decrypt(data, key));
	}
	
	public String decryptFromStringToString(String cipherText, SecretKey key) throws Exception{
		byte[] cipher = Base64.getDecoder().decode(cipherText);
		return new String(decrypt(cipher, key));
	}
	
	public String decryptFromStringToString(String cipherText) throws Exception{
		byte[] cipher = Base64.getDecoder().decode(cipherText);
		return new String(decrypt(cipher,loadDefaultKey()));
	}
	
	private SecretKey loadDefaultKey() throws Exception{
		String keyPath = env.getProperty("atm.web.secret-key");
		
		byte[] raw = null;
		File file = null;
		
		if (keyPath == null) {
			log.info("use default key from classpath");
			
			try (InputStream is = getClass().getResourceAsStream("/AES.key")) {
				raw = is.readAllBytes();
			} catch (Exception e) {}
			
		} else {
			log.info("use key from atm.web.secret-key: " + keyPath);
			
			file = new File(keyPath);
			
			try (FileInputStream fis = new FileInputStream(file)) {
				raw = fis.readAllBytes();
			} catch (Exception e) {}
		}
		
		SecretKey sKey = new SecretKeySpec(Base64.getDecoder().decode(raw),0,16,"AES");
		return sKey;
	}
	
	public SecretKey loadKey(byte[] key) throws Exception {
		SecretKey sKey = new SecretKeySpec(Base64.getDecoder().decode(key),0,16,"AES");
		return sKey;
	}
	
	public String checkEncPwdAndDecrypt(final String encPwd, SecretKey key) throws Exception{
		if (encPwd == null) return null;
		String password = encPwd;
		if (password.startsWith(Constants.PS_ENCRYPTED_PREFIX)) {
			password = encPwd.substring(Constants.PS_ENCRYPTED_PREFIX.length());
			byte[] cipher = Base64.getDecoder().decode(password);
			password = decryptToString(cipher, key);
		} 
		return password;
	}
	
	public String hashText(String text) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(Constants.HASH_PS_ALGORITHM);
		byte[] hash = digest.digest(text.getBytes());
		return Base64.getEncoder().encodeToString(hash);
	}
	
//	public String generateApiSecret() {
//		SecureRandom secureRandom = new SecureRandom();
//		byte[] randomBytes = new byte[24];
//	    secureRandom.nextBytes(randomBytes);
//		
//	    String firstChar = RandomStringUtils.randomAlphabetic(1);
//	    String token = Base64.getEncoder().encodeToString(randomBytes);
//	    
//	    return firstChar + "." + token;
//	}
//	
//	public String generateApiToken() {
//		return Utils.generateUUID();
//	}

	private static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		for (int i = 0; i < iv.length; i++) {
			iv[i] = 0;
		}
		return new IvParameterSpec(iv);
	}
}
