import javax.crypto.Cipher;

import java.io.Serializable;
import java.security.*;



public class CipherClass implements Serializable {



    public KeyPair keyPair;
    public Cipher cipher;
    public KeyPairGenerator keyPairGenerator;
    public SecureRandom random;
    public Key publicKey;
    public Key privateKey;


    public CipherClass() throws Exception{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        cipher = Cipher.getInstance("ElGamal/None/NoPadding", "BC");
        keyPairGenerator = KeyPairGenerator.getInstance("ElGamal" , "BC");
        random = new SecureRandom();

        //keyPairGenerator.initialize(256, random);

        keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public Key getPublicKey(){
        return publicKey;
    }

    public Key getPrivateKey(){
        return privateKey;
    }

    public String Encrypt(byte[] stringToBeEncrypted, Key publicKey) throws Exception{

        keyPairGenerator.initialize(512, random);


        cipher.init(Cipher.ENCRYPT_MODE, publicKey, random);
        byte[] cipherText = cipher.doFinal(stringToBeEncrypted);

        return new String(cipherText);
    }



    public String Decrypt(byte[] stringToBeDecrypted, Key privateKey) throws Exception{

        keyPairGenerator.initialize(512, random);


        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] plainText = cipher.doFinal(stringToBeDecrypted);

        return new String(plainText);
    }


}
