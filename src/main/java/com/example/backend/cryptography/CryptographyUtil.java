package com.example.backend.cryptography;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import com.example.backend.Models.abdm.Keys;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptographyUtil {
    private String key = "2954f37d428edf2695fd2cecd7af2ace";
    private String initVector = "ecddb0c2238799af";
    private String algo = "AES/CBC/PKCS5PADDING";

    private Keys keys;

    public String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte encrypted[] = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        }
        catch (Exception e) {
            System.out.println("Error in encryption: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));

            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

            byte original[] = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        }
        catch (Exception e) {
            System.out.println("Error in decrpytion: " + e.getLocalizedMessage());
            e.printStackTrace();;
        }
        return null;
    }

    public byte[] encrypt(byte value[]) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte encrypted[] = cipher.doFinal(value);
            return encrypted;
        }
        catch (Exception e) {
            System.out.println("Error in encryption: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte encrypted[]) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));

            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

            byte original[] = cipher.doFinal(encrypted);
            return original;
        }
        catch (Exception e) {
            System.out.println("Error in decrpytion: " + e.getLocalizedMessage());
            e.printStackTrace();;
        }
        return null;
    }

    public void generateKeySet() {
        if(keys != null) {
            return;
        }
        try {
            Runtime rt = Runtime.getRuntime();
            String commands[] = {"bash", "-c", "cd src/main/resources/fidelius-cli-1.2.0/bin && sh fidelius-cli gkm"};
            Process proc = rt.exec(commands);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            StringBuilder output = new StringBuilder();
            String s;
            while((s = stdInput.readLine()) != null) {
                output.append(s);
            }
            String outputStr = output.toString().replace(" ", "");
            Gson gson = new Gson();
            this.keys = gson.fromJson(outputStr, Keys.class);
        }
        catch (Exception e) {
            System.out.println("Error in CryptographyUtil->generateKeySet: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public String fideliusDecrypt(String encryptedData, String othersPublicKey, String othersNonce) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String decryptCmd = "sh fidelius-cli d " + 
            encryptedData + 
            " " + 
            this.keys.getNonce() + 
            " " + 
            othersNonce + 
            " " + 
            this.keys.getPrivateKey() + 
            " " + 
            othersPublicKey;

        String commands[] = {"bash", "-c", "cd src/main/resources/fidelius-cli-1.2.0/bin && " + decryptCmd};

        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        StringBuilder output = new StringBuilder();
        String sout;
        while((sout = stdInput.readLine()) != null) {
            output.append(sout + "\n");
        }

        BufferedReader errInput = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        StringBuilder err = new StringBuilder();
        while((sout = errInput.readLine()) != null) {
            err.append(sout);
        }
        
        System.out.println("Error in decrypting: " + err.toString());

        String decrypted = output.toString();
        decrypted = decrypted.replace("\n", "");
        return decrypted;
    }

    public String fideliusEncrypt(String plainText, String requesterNonce, String requesterPublicKey) throws IOException {
        plainText = plainText.replace("\"", "\\\"");

        Runtime rt = Runtime.getRuntime();
        String encryptCmd = "sh fidelius-cli e " + 
            '"' + 
            plainText + 
            '"' + 
            " " + 
            this.keys.getNonce() + 
            " " + 
            requesterNonce + 
            " " + 
            this.keys.getPrivateKey() + 
            " " + 
            requesterPublicKey;

        String commands[] = {"bash", "-c", "cd src/main/resources/fidelius-cli-1.2.0/bin && " + encryptCmd};
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader errInput = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        StringBuilder output = new StringBuilder();
        String sout;

        while((sout = stdInput.readLine()) != null) {
            output.append(sout + "\n");
        }

        StringBuilder err = new StringBuilder();
        while((sout = errInput.readLine()) != null) {
            err.append(sout);
        }
        
        System.out.println("Error in encrypting: " + err.toString());
        
        String encrypted = output.toString();
        String arr[] = encrypted.split("\n");
        encrypted = arr[1].trim().split(":")[1].trim().replace("\"", "");
        return encrypted;
    }
}