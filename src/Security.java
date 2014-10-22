import java.io.*;
import java.security.*;

/*
 Класс для создания сертификатов. Используется провайдер (бибилиотека) BouncyCastle
 Подключение: security.provider.11=org.bouncycastle.jce.provider.BouncyCastleProvider
 */
class Security {

    public Signature dsa;
    public PublicKey pub;
    private byte[] realSig;
    private byte[] key;

    public Security(String name) {
        if (!(name.length()>0)) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        } else {
            System.out.println("Cert gen class");
            GenSig();
            saveSert(name);
        }
    }

    public void main(String[] args) {
        if (args.length!=1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else try {
            GenSig();

            byte[] realSig = readFile(args[1]);
            byte[] key = pub.getEncoded();

            writeFile("Signature", realSig);
            writeFile("Key", key);

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

    private void GenSig () {
        /* Generate a DSA signature */
         try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);

            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            pub = pair.getPublic();

            dsa = Signature.getInstance("SHA1withDSA", "BC");
            dsa.initSign(priv);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

    private void saveSert(String filename) {
        byte[] realSig = readFile(filename);
        //System.out.println(realSig.toString());
        byte[] key = pub.getEncoded();
        //System.out.println(key.toString());

        writeFile(filename + "-signature", realSig);
        writeFile(filename + "-key.pem", key);
    }

    private byte[] readFile (String name) {
        byte[] sign;
        try {
            FileInputStream fis = new FileInputStream(name);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufin.read(buffer)) >= 0) {
                dsa.update(buffer, 0, len);
            }
            bufin.close();
            return dsa.sign();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return null;
    }

    private void writeFile (String name,byte[] key) {
        try {
            FileOutputStream keyfos = new FileOutputStream(name);
            keyfos.write(key);
            keyfos.close();
        }catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}