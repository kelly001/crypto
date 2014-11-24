import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import javax.security.auth.x500.X500Principal;
//import org.bouncycastle.asn1.DEREncodable;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
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
    private KeyPair RootKP;
    private KeyPair UserKP;

    private byte[] realSig;
    private byte[] key;


    public Security() {
        System.out.println("Security class constructor - generate certificates API");
        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();

    }

    public X509Certificate generateRootCertificate() {
        System.out.println("Generate root certificate function");
        X509Certificate rootCert = null;
        RootKP = GenKeys();
        saveEncKey(RootKP, "Root");

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();

        //создание и сохранение root сертификата
        BigInteger RootSerial = BigInteger.valueOf(System.currentTimeMillis());
        try {
            // Rooot certificate
            rootCert = generateX509CertificateRoot("CN=Root CA Certificate", RootSerial, startDate, nextYear, "SHA1withDSA", RootKP, "BC");
            if (rootCert!=null) {
                System.out.println(rootCert);
                //saveCert(rootCert, "sertmy");
                Writer writer = new FileWriter("RootCertificate");
                savePemX509Certificate(rootCert, RootKP.getPrivate(), writer);
            }else { System.out.println("Root certificate is null ");}
        } catch (Exception e) {
            System.out.println("Generate root certificate error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return rootCert;
    }

    public  boolean generateUserCertificate(X509Certificate rootCert) {
        System.out.println("Generate user certificate function");

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();

        //creating signed certificate
        BigInteger signedSerial = BigInteger.valueOf(System.currentTimeMillis());
        UserKP = GenKeys();
        saveEncKey(UserKP, "User");
        try {
            X509Certificate signedCert = generateX509Certificate("CN=Signed Certificate for ...", signedSerial, rootCert, startDate, nextYear, "SHA1withDSA", UserKP, "BC");
            if (signedCert!=null) {
                System.out.println(signedCert);
                Writer writer = new FileWriter("UserCertificate");
                savePemX509Certificate(rootCert, UserKP.getPrivate(), writer);
                return true;
            }else { System.out.println("User certificate is null ");}
        } catch (Exception e) {
            System.out.println("Generate signed certificate error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        Security security = new Security();
        X509Certificate rootcert = security.generateRootCertificate();
        if (rootcert!=null)
            security.generateUserCertificate(rootcert);
    }

    public static X509Certificate generateX509Certificate(String name, BigInteger serial, X509Certificate issuerCert, Date start , Date end, String signAlgorithm, KeyPair keys, String provider)
            throws IOException, OperatorCreationException, CertificateException
    {
        if(serial!=null && name!=null && issuerCert!=null && start!=null && end!=null && signAlgorithm !=null)
        {
            //-----GENERATE THE X509 CERTIFICATE
            ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm).build(keys.getPrivate());

            X500Principal subject = new X500Principal(name);
            X509v3CertificateBuilder certBldr = null;

               certBldr = new JcaX509v3CertificateBuilder(issuerCert, serial, start, end, subject, keys.getPublic());
            /*if(map!=null)
                for(ASN1ObjectIdentifier extension : map.keySet())
                    certBldr.addExtension(extension, map.get(extension).getKey(), map.get(extension).getValue());
            */
            X509Certificate cert = new JcaX509CertificateConverter().setProvider(provider).getCertificate(certBldr.build(signer));
            return cert;
        }
        return null;
    }

    public static X509Certificate generateX509CertificateRoot(String name, BigInteger serial, Date start , Date end, String signAlgorithm, KeyPair keys, String provider)
            throws IOException, OperatorCreationException, CertificateException
    {
        if(serial!=null && name!=null && start!=null && end!=null && signAlgorithm !=null)
        {
            //-----GENERATE THE X509 CERTIFICATE
            ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm).build(keys.getPrivate());

            X500Principal subject = new X500Principal(name);
            X509v3CertificateBuilder certBldr = null;

            certBldr = new JcaX509v3CertificateBuilder(subject, serial, start, end, subject, keys.getPublic());
            /*if(map!=null)
                for(ASN1ObjectIdentifier extension : map.keySet())
                    certBldr.addExtension(extension, map.get(extension).getKey(), map.get(extension).getValue());
            */
            X509Certificate cert = new JcaX509CertificateConverter().setProvider(provider).getCertificate(certBldr.build(signer));
            return cert;
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

    public static void saveCert(X509Certificate cert, String name){
        try {
            System.out.print("saving");
            byte[] encCert = cert.getEncoded();
            System.out.println(encCert.toString());
            FileOutputStream certfos = new FileOutputStream(name);
            certfos.write(encCert);
            certfos.close();
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }


    }

    private KeyPair GenKeys () {
        /* Generate a DSA signature */
        KeyPair pair = null;
        try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            pair = keyGen.generateKeyPair();
            /*
            privKey = pair.getPrivate();
            pubKey = pair.getPublic();
            */
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }

        return pair;
    }

    private void saveEncKey(KeyPair keys, String filename) {
        try {
            byte[] key = keys.getPrivate().getEncoded();
            //System.out.println(key.toString());
            FileOutputStream keyfos = new FileOutputStream(filename + "Key");
            keyfos.write(key);
            keyfos.close();
            //writeFile(filename + "-key.pem", key);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        //writeFile(name, realSig);
    }

    private PublicKey readEncKey(String filename) {
        try {
            FileInputStream keyfis = new FileInputStream(filename);
            //byte[] encKey = new byte[keyfis.available()];
            key = new byte[keyfis.available()];
            keyfis.read(key);
            keyfis.close();


        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "BC");
        return keyFactory.generatePublic(pubKeySpec);
    } catch (InvalidKeySpecException e) {
            System.err.println(e.getLocalizedMessage());
    } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
    }
        return null;
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
            dsa.initSign(RootKP.getPrivate());

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