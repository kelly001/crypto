package com.zpayment;
        import database.*;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERBMPString;
        import org.bouncycastle.asn1.DEROctetString;
        import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
        import org.bouncycastle.asn1.x500.RDN;
        import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
        import org.bouncycastle.asn1.x500.style.IETFUtils;
        import org.bouncycastle.asn1.x509.*;
        import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
        import org.bouncycastle.crypto.engines.RC2Engine;
        import org.bouncycastle.crypto.modes.CBCBlockCipher;
        import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.pkcs.PKCS12PfxPdu;
import org.bouncycastle.pkcs.PKCS12PfxPduBuilder;
import org.bouncycastle.pkcs.PKCS12SafeBag;
import org.bouncycastle.pkcs.PKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCS12MacCalculatorBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEOutputEncryptorBuilder;
        import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;

        import javax.security.auth.x500.X500Principal;
        import javax.swing.*;
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
import java.util.HashMap;
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
        System.out.println("com.zpayment.Security class constructor - generate certificates API");
        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();

    }

    public X509Certificate generateRootCertificate(Map<String, String> values) {
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
            rootCert = generateX509CertificateRoot(values, RootSerial, startDate, nextYear, "SHA1withDSA", RootKP, "BC");
            if (rootCert!=null) {
                System.out.println(rootCert);
                //saveCert(rootCert, "sertmy");
                savePemX509Certificate(rootCert, "RootCertificate");
                savePublicKey(RootKP,values.get("filename")+"RootPublicKey");
                savePrivateKey(RootKP,values.get("filename")+"RootPrivateKey");
                //TODO show message dialog to input password
                transformToPKS12(rootCert, RootKP);

            }else { System.out.println("Root certificate is null ");}
        } catch (Exception e) {
            System.out.println("Generate root certificate error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return rootCert;
    }

    public  boolean generateUserCertificate(X509Certificate rootCert) {
        System.out.println("Generate user certificate by certificate function");

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
            X509Certificate signedCert = generateX509Certificate("CN=Signed Certificate for", signedSerial, rootCert, startDate, nextYear, "SHA1withDSA", UserKP, "BC");
            if (signedCert!=null) {
                System.out.println(signedCert);
                savePemX509Certificate(signedCert, "UserCertificate");
                savePublicKey(UserKP, "UserKey");
                transformToPKS12(signedCert, UserKP);
                return true;
            }else { System.out.println("User certificate is null ");}
        } catch (Exception e) {
            System.out.println("Generate signed certificate error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }


     /*
     * Создание сертификата пользователя с параметрами, передвавемыми в массиве с ключами - values
     * Подпись секретным ключом компании, имя компании.
     * Чем подписывать сертификат - паблик ключ пользователя/компании? Нужно пользователем,
     * Компания - секртный ключ подписывает контейнер как то так
     * TODO generate container with company private key
     *
     *  */
    public  boolean generateUserCertificate( Map<String, String> values) {
        System.out.println("Generate user certificate by key function");

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();

        //creating signed certificate
        BigInteger signedSerial = BigInteger.valueOf(System.currentTimeMillis());
        UserKP = GenKeys();
        //saveEncKey(UserKP, "User");
        try {
            X509Certificate signedCert = generateX509Certificate(values,
                    signedSerial, startDate, nextYear, UserKP);
            if (signedCert!=null) {
                System.out.println(signedCert);
                savePemX509Certificate(signedCert, values.get("filename"));
                savePublicKey(UserKP, values.get("filename")+"PublicKey");
                savePrivateKey(UserKP, values.get("filename")+"PrivateKey");
                transformToPKS12(signedCert, UserKP);
                return true;
            }else { System.out.println("User certificate is null ");}
        } catch (Exception e) {
            System.out.println("Generate signed certificate error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        final Map<String, String> values = new HashMap<String, String>();
        values.put("username", "USER");
        values.put("organization", "ROOT");
        values.put("filename", "test");

        Security security = new Security();
        X509Certificate rootcert = security.generateRootCertificate(values);
            //rootcert.getSubjectDN().toString();
        if (rootcert!=null)
            security.generateUserCertificate(values);
        //Boolean user = security.generateUserCertificate(values, rootcert);
    }

    public static X509Certificate generateX509Certificate(Map<String, String> values,
            BigInteger serial, Date start , Date end, KeyPair userKeys)
            throws IOException, OperatorCreationException, CertificateException
    {
        if(serial!=null && start!=null && end!=null)
        {
            //-----GENERATE THE X509 CERTIFICATE
            ContentSigner signer = new JcaContentSignerBuilder("SHA1withDSA").build(userKeys.getPrivate());

            X500NameBuilder namebuilder = new X500NameBuilder(X500Name.getDefaultStyle());
            namebuilder.addRDN(BCStyle.C, values.get("country")!=null?values.get("country"):"");
            namebuilder.addRDN(BCStyle.O, values.get("locality")!=null?values.get("locality"):"");
            namebuilder.addRDN(BCStyle.ST, values.get("state")!=null?values.get("state"):"");
            namebuilder.addRDN(BCStyle.O, values.get("organization")!=null?values.get("organization"):"");
            namebuilder.addRDN(BCStyle.OU, values.get("department")!=null?values.get("department"):"");
            namebuilder.addRDN(BCStyle.CN, values.get("username")!=null?values.get("username"):"");
            namebuilder.addRDN(BCStyle.EmailAddress, values.get("email")!=null?values.get("email"):"");
            X500Name subject  = namebuilder.build();

            //X500Name subject = new X500Name("CN=Signed Certificate for " + values.get("username"));
            X500Name issuerName = new X500Name("CN = Root CA Certificate of " + (values.get("organization")!=null?values.get("organization"):""));

            X509v3CertificateBuilder certBldr = null;

            // Чем подписывать сертификат - паблик ключ пользователя/компании? Нужно пользователем,
            // Компания - секртный ключ подписывает контейнер как то так
            // пока нет контейнера - паблик ключ компании
            certBldr = new JcaX509v3CertificateBuilder(issuerName, serial, start, end, subject, userKeys.getPublic());
            certBldr.addExtension( X509Extension.basicConstraints, true, new BasicConstraints(false));
            certBldr.addExtension(X509Extension.keyUsage, true,
                    new KeyUsage(KeyUsage.keyCertSign|KeyUsage.digitalSignature));
            //certBldr.addExtension(X509Extension.authorityKeyIdentifier, true,
            //        new DEROctetString(new AuthorityKeyIdentifierStructure(issuerCert)));
            X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
            return cert;
        }
        return null;
    }


    public static X509Certificate generateX509Certificate(String name, BigInteger serial, X509Certificate issuerCert,
                                                         Date start , Date end, String signAlgorithm, KeyPair keys, String provider)
            throws IOException, OperatorCreationException, CertificateException
    {
        if(serial!=null && name!=null && issuerCert!=null && start!=null && end!=null && signAlgorithm !=null)
        {
            //-----GENERATE THE X509 CERTIFICATE
            ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm).build(keys.getPrivate());
            X500Name subject = new X500Name(name);
            X509v3CertificateBuilder certBldr = null;
            certBldr = new JcaX509v3CertificateBuilder(issuerCert, serial, start, end, subject, keys.getPublic());
            certBldr.addExtension( X509Extension.basicConstraints, true, new BasicConstraints(false));
            certBldr.addExtension(X509Extension.keyUsage, true,
                    new KeyUsage(KeyUsage.keyCertSign|KeyUsage.digitalSignature));

            X509Certificate cert = new JcaX509CertificateConverter().setProvider(provider).getCertificate(certBldr.build(signer));
            return cert;
        }
        return null;
    }

    public static X509Certificate generateX509CertificateRoot(Map<String, String> values,BigInteger serial,
                                                              Date start , Date end, String signAlgorithm, KeyPair keys, String provider)
            throws IOException, OperatorCreationException, CertificateException
    {
        if(serial!=null && start!=null && end!=null && signAlgorithm !=null)
        {
            //-----GENERATE THE X509 CERTIFICATE
            ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm).build(keys.getPrivate());
            //X500Name subject = new X500Name("CN = CA Root certificate of  " + values.get("organization"));
            X509v3CertificateBuilder certBldr = null;

            X500NameBuilder namebuilder = new X500NameBuilder(X500Name.getDefaultStyle());
            namebuilder.addRDN(BCStyle.C, values.get("country")!=null?values.get("country"):"");
            namebuilder.addRDN(BCStyle.O, values.get("locality")!=null?values.get("locality"):"");
            namebuilder.addRDN(BCStyle.ST, values.get("state")!=null?values.get("state"):"");
            namebuilder.addRDN(BCStyle.O, values.get("organization")!=null?values.get("organization"):"");
            namebuilder.addRDN(BCStyle.OU, values.get("department")!=null?values.get("department"):"");
            namebuilder.addRDN(BCStyle.CN, values.get("username")!=null?values.get("username"):"");
            namebuilder.addRDN(BCStyle.EmailAddress, values.get("email")!=null?values.get("email"):"");
            X500Name subject  = namebuilder.build();

            certBldr = new JcaX509v3CertificateBuilder(subject, serial, start, end, subject, keys.getPublic());
            certBldr.addExtension( X509Extension.basicConstraints, true, new BasicConstraints(true));
            certBldr.addExtension(X509Extension.keyUsage, true,
                    new KeyUsage(KeyUsage.keyCertSign|KeyUsage.digitalSignature));
            X509Certificate cert = new JcaX509CertificateConverter().setProvider(provider).getCertificate(certBldr.build(signer));
            return cert;
        }
        return null;
    }

    public static boolean savePemX509Certificate(X509Certificate cert, String name) throws NoSuchAlgorithmException, NoSuchProviderException, CertificateEncodingException, SignatureException, InvalidKeyException, IOException
    {
        Writer writer = new FileWriter (new File("files/", name));
        if(cert!=null)
        {
            PEMWriter pemWriter = new PEMWriter(writer);
            pemWriter.writeObject(cert);
            pemWriter.flush();
            pemWriter.close();
            return true;
        }
        return false;
    }

    public static boolean savePublicKey(KeyPair key, String name) throws NoSuchAlgorithmException, NoSuchProviderException, CertificateEncodingException, SignatureException, InvalidKeyException, IOException
    {
        Writer writer = new FileWriter (new File("files/", name));
        if(key!=null)
        {
            PEMWriter pemWriter = new PEMWriter(writer);
            pemWriter.writeObject(key.getPublic());
            pemWriter.flush();
            pemWriter.close();
            return true;
        }
        return false;
    }

    public static boolean savePrivateKey(KeyPair key, String name) throws NoSuchAlgorithmException, NoSuchProviderException, CertificateEncodingException, SignatureException, InvalidKeyException, IOException
    {
        Writer writer = new FileWriter (new File("files/", name));
        if(key!=null)
        {
            PEMWriter pemWriter = new PEMWriter(writer);
            pemWriter.writeObject(key.getPrivate());
            pemWriter.flush();
            pemWriter.close();
            return true;
        }
        return false;
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

    public PublicKey readEncKey(String filename) {
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

    public PrivateKey readPrivateKey(String filename) {
        try {
            FileInputStream keyfis = new FileInputStream(filename);
            //byte[] encKey = new byte[keyfis.available()];
            key = new byte[keyfis.available()];
            keyfis.read(key);
            keyfis.close();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "BC");
            return keyFactory.generatePrivate(pubKeySpec);
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
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
        private void transformToPKS12(X509Certificate user, KeyPair userKey)
                throws NoSuchProviderException, KeyStoreException{
         try {
             //get user's name
             X500Principal principal = user.getSubjectX500Principal();
             X500Name x500name = new X500Name( principal.getName() );
             RDN cn = x500name.getRDNs(BCStyle.CN)[0];
             String username = IETFUtils.valueToString(cn.getFirst().getValue());
             String filename = "files/" + username + ".p12";

             // how input dialog to get password
             String password =
                     JOptionPane.showInputDialog("Введите пароль для контейнера сертификата:");
             if(password == null)
             {
                 JOptionPane.showMessageDialog(null,
                         "Будет использован стандартный пароль: certificate_password", "Password Required",
                         JOptionPane.INFORMATION_MESSAGE);

             }
             PKCS12SafeBagBuilder eeCertBagBuilder = new JcaPKCS12SafeBagBuilder(user);
             eeCertBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new DERBMPString("User's Certificate"));

             JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
             SubjectKeyIdentifier pubKeyId = extUtils.createSubjectKeyIdentifier(userKey.getPublic());
             eeCertBagBuilder.addBagAttribute(PKCS12SafeBag.localKeyIdAttribute, pubKeyId);

             //OutputEncryptor encOut = new JcePKCSPBEOutputEncryptorBuilder(NISTObjectIdentifiers.id_aes256_CBC).setProvider("BC").build(JcaUtils.KEY_PASSWD);
             PKCS12SafeBagBuilder keyBagBuilder = new JcaPKCS12SafeBagBuilder(userKey.getPrivate());
             keyBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new DERBMPString("User's Key"));
             keyBagBuilder.addBagAttribute(PKCS12SafeBag.localKeyIdAttribute, pubKeyId);

             PKCS12PfxPduBuilder builder = new PKCS12PfxPduBuilder();
             builder.addData(keyBagBuilder.build());
             builder.addEncryptedData(new JcePKCSPBEOutputEncryptorBuilder(
                             PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC).setProvider("BC").build(password.toCharArray()),
                    eeCertBagBuilder.build());

             PKCS12PfxPdu pfx = builder.build(new JcePKCS12MacCalculatorBuilder(), password.toCharArray());
             writeFile(filename, pfx.getEncoded());
        } catch (IOException ioe) {System.out.println("IOException error: " + ioe.getLocalizedMessage());}
        catch (Exception exc ) {System.out.println("Generate p12 error: " + exc.getLocalizedMessage());}
    }

    private void transformToPKS12_CommadLine(String user, String rootKey){
        try {
            String certificate = "files/" + user +"Certificate";
            String key = "files/"+rootKey+"PublicKey";
            String command = "openssl pkcs12 -export -in " + certificate +" -inkey "+key+" -out client.p12 -name \"Client certificate from our organization\"";
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
        } catch (Exception exc ) {System.out.println("Generate p12 error: " + exc.getLocalizedMessage());}
    }
}