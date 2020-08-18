package com.wzx.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @author admin
 * @version 1.0
 * @since 2019/12/2
 */
public class RSAUtils {
    private static final String SIGN_TYPE_RSA = "RSA";
    private static final String SIGN_ALGORITHMS_MD5 = "MD5withRSA";
    private static final String SIGN_ALGORITHMS_SHA1 = "SHA1WithRSA";

    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(SIGN_TYPE_RSA);
        keyGen.initialize(1024,new SecureRandom()); //可以理解为：加密后的密文长度，实际原文要小些 越大 加密解密越慢
        return keyGen.generateKeyPair();
    }

    /**
     * 用md5生成内容摘要，再用RSA的私钥加密，进而生成数字签名
     * @param content       内容
     * @param privateKey    私钥
     * @return
     * @throws Exception
     */
    public static String getMd5Sign(String content, PrivateKey privateKey) throws Exception {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS_MD5);
        signature.initSign(privateKey);
        signature.update(contentBytes);
        byte[] signs = signature.sign();
        return Base64.encodeBase64String(signs);
    }

    /**
     * 对用md5和RSA私钥生成的数字签名进行验证
     * @param content       内容
     * @param sign          签名
     * @param publicKey     公钥
     * @return
     * @throws Exception
     */
    public static boolean verifyWhenMd5Sign(String content, String sign, PublicKey publicKey) throws Exception {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS_MD5);
        signature.initVerify(publicKey);
        signature.update(contentBytes);
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 用sha1生成内容摘要，再用RSA的私钥加密，进而生成数字签名
     * @param content           内容
     * @param privateKey        私钥
     * @return
     * @throws Exception
     */
    public static String getSha1Sign(String content, PrivateKey privateKey) throws Exception {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS_SHA1);
        signature.initSign(privateKey);
        signature.update(contentBytes);
        byte[] signs = signature.sign();
        return Base64.encodeBase64String(signs);
    }

    /**
     * 对用md5和RSA私钥生成的数字签名进行验证
     * @param content       内容
     * @param sign          签名
     * @param publicKey     公钥
     * @return
     * @throws Exception
     */
    public static boolean verifyWhenSha1Sign(String content, String sign, PublicKey publicKey) throws Exception {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS_SHA1);
        signature.initVerify(publicKey);
        signature.update(contentBytes);
        return signature.verify(Base64.decodeBase64(sign));
    }


    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(SIGN_TYPE_RSA)
                .generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(SIGN_TYPE_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str 加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(SIGN_TYPE_RSA)
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(SIGN_TYPE_RSA);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

}
