import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
//import org.bouncycastle.asn1.DEREncodable;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/*
 Класс для создания сертификатов. Используется провайдер (бибилиотека) BouncyCastle
 Подключение: security.provider.11=org.bouncycastle.jce.provider.BouncyCastleProvider
 */
class Security {

    public Signature dsa; // подписываемые данные
    public PublicKey pubKey;
    public X509Certificate certificate;
    private byte[] realSig;
    private byte[] key;
    private PrivateKey privKey;

    public Security(String name) {
        if (!(name.length()>0)) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        } else {
            System.out.println("Cert gen class");
            GenKeys();
            saveEncKey(name);

            //подпись ключом данных (файла)
            String filename = "data";
            signData(filename);

            //создание и сохранение сертификата
            BigInteger serial = BigInteger.valueOf(1l);
            Date startDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.YEAR, 1); // to get previous year add -1
            Date nextYear = cal.getTime();
            String issuer = "";
            try {
                Writer writer = new FileWriter("my sert");
                // Rooot certificate
                X509Certificate rootCert = this.generateX509CertificateRoot(serial, startDate, nextYear, "SHA1withDSA", privKey, pubKey, "BC");
                System.out.println(rootCert);
                savePemX509Certificate(rootCert, privKey, writer); //TODO write file
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            /*try {
                X509Certificate endCert = generateX509Certificate(serial, "CN=end",issuer, startDate, nextYear, "SHA1withDSA", privKey, pubKey, "BC");
            } catch (Exception e) {
                e.getLocalizedMessage();
            }*/
        }
    }

    public void main(String[] args) {
        if (args.length!=1) {
            System.out.println("Name OfFileToSign required");
        }
        else try {
            GenKeys();

           // byte[] realSig = readFile(args[1]);
            byte[] key = pubKey.getEncoded();

            writeFile("Signature", realSig);
            writeFile("Key", key);

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

    private void GenCertificates (String name) {

    }

    public static X509Certificate generateX509Certificate(BigInteger serialnumber, String subject, String issuer, Date start , Date end, String signAlgorithm, PrivateKey privateKey, PublicKey publicKey, String provider)
            throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException
    {
        if(serialnumber!=null && subject!=null && issuer!=null && start!=null && end!=null && signAlgorithm !=null && privateKey!=null && publicKey!=null)
        {
            //-----GENERATE THE X509 CERTIFICATE
            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            X509Principal dnSubject = new X509Principal(subject);
            //X509Principal dnIssuer = new X509Principal(issuer);

            certGen.setSerialNumber(serialnumber);
            certGen.setSubjectDN(dnSubject);
            //certGen.setIssuerDN(dnIssuer);
            certGen.setNotBefore(start);
            certGen.setNotAfter(end);
            certGen.setPublicKey(publicKey);
            certGen.setSignatureAlgorithm(signAlgorithm);

            /*
            //-----insert extension if needed
            if(map!=null)
                for(ASN1ObjectIdentifier extension : map.keySet())
                    certGen.addExtension(extension, map.get(extension).getKey(), map.get(extension).getValue());
*/
            return certGen.generate(privateKey, provider);
        }
        return null;
    }

    public static X509Certificate generateX509CertificateRoot (BigInteger serialnumber, Date start , Date end, String signAlgorithm, PrivateKey privateKey, PublicKey publicKey, String provider)
            throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException
    {
        if(serialnumber!=null && start!=null && end!=null && signAlgorithm !=null && privateKey!=null && publicKey!=null)
        {
            //-----GENERATE THE X509 CERTIFICATE
            X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
            X509Principal dnSubject = new X509Principal("CN=Test CA Certificate");
            //X509Principal dnIssuer = new X509Principal(issuer);

            certGen.setSerialNumber(serialnumber);
            certGen.setSubjectDN(dnSubject);
            //certGen.setIssuerDN(dnIssuer);
            certGen.setNotBefore(start);
            certGen.setNotAfter(end);
            certGen.setPublicKey(publicKey);
            certGen.setSignatureAlgorithm(signAlgorithm);

            return certGen.generate(privateKey, provider);
        }
        return null;
    }

    public static boolean savePemX509Certificate(X509Certificate cert, PrivateKey key, Writer writer) throws NoSuchAlgorithmException, NoSuchProviderException, CertificateEncodingException, SignatureException, InvalidKeyException, IOException
    {
        if(cert!=null && key!=null && writer!=null)
        {
            PEMWriter pemWriter = new PEMWriter(writer);
            pemWriter.writeObject(cert);
            pemWriter.flush();

            if(key!=null)
            {
                pemWriter.writeObject(key);
                pemWriter.flush();
            }
            pemWriter.close();
            return true;
        }
        return false;
    }

    private void GenKeys () {
        /* Generate a DSA signature */
         try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);

            KeyPair pair = keyGen.generateKeyPair();
            privKey = pair.getPrivate();
            pubKey = pair.getPublic();

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

    private void saveEncKey(String name) {
        try {
            byte[] key = pubKey.getEncoded();
            //System.out.println(key.toString());
            FileOutputStream keyfos = new FileOutputStream(name + "-key");
            keyfos.write(key);
            keyfos.close();
            //writeFile(filename + "-key.pem", key);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        //writeFile(name, realSig);
    }

    private void readEncKey(String filename) {
        try {
            FileInputStream keyfis = new FileInputStream(filename);
            //byte[] encKey = new byte[keyfis.available()];
            key = new byte[keyfis.available()];
            keyfis.read(key);
            keyfis.close();


        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "BC");
        pubKey = keyFactory.generatePublic(pubKeySpec);
    } catch (InvalidKeySpecException e) {
            System.err.println(e.getLocalizedMessage());
    } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
    }
    }

    public void verifySig(String filename){
        try {
            FileInputStream sigfis = new FileInputStream(filename);
            byte[] sigToVerify = new byte[sigfis.available()];
            sigfis.read(sigToVerify);
            sigfis.close();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    // подпись данных приватным ключом
    // возвращает подписанные данные
    public void signData (String name) {

        try {
            // объект для подписываемых данных
            dsa = Signature.getInstance("SHA1withDSA", "BC");
            dsa.initSign(privKey);

            //читаем файл для подписи
            FileInputStream fis = new FileInputStream(name);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufin.read(buffer)) >= 0) {
                dsa.update(buffer, 0, len);
            }
            bufin.close();
            realSig = dsa.sign();

            /* save the signature in a file */
            FileOutputStream sigfos = new FileOutputStream("data_signature");
            sigfos.write(realSig);
            sigfos.close();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void writeFile (String name, byte[] data) {
        try {
            FileOutputStream keyfos = new FileOutputStream(name);
            keyfos.write(data);
            keyfos.close();
        }catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}