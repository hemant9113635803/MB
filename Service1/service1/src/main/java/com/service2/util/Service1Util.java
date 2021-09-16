package com.service2.util;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class Service1Util {

    private static final String ALGO = "AES";

    public Map<String, String> encryptData(UserDto userDto) {
        Map<String, String> keyStringMap = new HashMap<>();
        try {
            byte[] data = SerializationUtils.serialize(userDto);
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            keyStringMap.put(Base64.getEncoder().encodeToString(SerializationUtils.serialize(key)),Base64.getEncoder().encodeToString(cipher.doFinal(data)));

            return keyStringMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance(ALGO);
        keygen.init(128);
        return keygen.generateKey();
    }

    public static UserDto decryptData(String encry,Key key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] pt = cipher.doFinal(Base64.getDecoder().decode(encry));
            return  (UserDto) SerializationUtils.deserialize(pt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
