import java.io.*;
import java.security.*;

class Security {

    public Signature dsa;
    public PublicKey pub;

    public void GenSig (String filename) {
        /* Generate a DSA signature */
        if (!(filename.length()>0)) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "BC");
            keyGen.initialize(1024, random);

            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            pub = pair.getPublic();

            dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(priv);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

    public void main(String[] args) {
        if (args.length!=1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else try {
            GenSig(args[1]);

            byte[] realSig = readFile(args[1]);
            byte[] key = pub.getEncoded();

            writeFile("Signature", realSig);
            writeFile("Key", key);

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
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