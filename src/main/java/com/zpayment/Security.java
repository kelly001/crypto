package com.zpayment;

import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS12PfxPdu;
import org.bouncycastle.pkcs.PKCS12PfxPduBuilder;
import org.bouncycastle.pkcs.PKCS12SafeBag;
import org.bouncycastle.pkcs.PKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCS12MacCalculatorBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEOutputEncryptorBuilder;

import javax.security.auth.x500.X500Principal;
import javax.swing.*;
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

/** Legacy certificate operations using the application-managed Bouncy Castle provider. */
class Security {
    static {
        if (java.security.Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            java.security.Security.addProvider(new BouncyCastleProvider());
        }
    }

    private KeyPair RootKP;
    private KeyPair UserKP;

    private byte[] key;

    public Security() {
        System.out.println("com.zpayment.Security class constructor - generate certificates API");
    }

    public X509Certificate generateRootCertificate(Map<String, String> values) {
        System.out.println("Generate root certificate function");
        X509Certificate rootCert = null;
        RootKP = GenKeys();
        saveEncKey(RootKP, "Root");

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.YEAR, 1);
        Date nextYear = cal.getTime();

        BigInteger RootSerial = BigInteger.valueOf(System.currentTimeMillis());
        try {
            rootCert = generateX509CertificateRoot(values, RootSerial, startDate, nextYear, "SHA1withDSA", RootKP, "BC");
            if (rootCert!=null) {
                System.out.println(rootCert);

                savePemX509Certificate(rootCert, "RootCertificate");
                savePublicKey(RootKP,values.get("filename")+"RootPublicKey");
                savePrivateKey(RootKP,values.get("filename")+"RootPrivateKey");

                transformToPKS12(rootCert, RootKP);
            }else { System.out.println("Root certificate is null ");}
        } catch (Exception e) {
            System.out.println("Generate root certificate error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return rootCert;
    }

    public  boolean generateUserCertificate( Map<String, String> values) {
        System.out.println("Generate user certificate by key function");

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.YEAR, 1);
        Date nextYear = cal.getTime();

        BigInteger signedSerial = BigInteger.valueOf(System.currentTimeMillis());
        UserKP = GenKeys();

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

    public static X509Certificate generateX509Certificate(Map<String, String> values,
            BigInteger serial, Date start , Date end, KeyPair userKeys)
            throws IOException, OperatorCreationException, CertificateException
    {
        if(serial!=null && start!=null && end!=null)
        {
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

            // Legacy behavior: the user key signs this certificate despite the company issuer name.
            X500Name issuerName = new X500Name("CN = Root CA Certificate of " + (values.get("organization")!=null?values.get("organization"):""));

            X509v3CertificateBuilder certBldr = null;

            certBldr = new JcaX509v3CertificateBuilder(issuerName, serial, start, end, subject, userKeys.getPublic());
            certBldr.addExtension( Extension.basicConstraints, true, new BasicConstraints(false));
            certBldr.addExtension(Extension.keyUsage, true,
                    new KeyUsage(KeyUsage.keyCertSign|KeyUsage.digitalSignature));
            X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
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
            ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm).build(keys.getPrivate());

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
            certBldr.addExtension( Extension.basicConstraints, true, new BasicConstraints(true));
            certBldr.addExtension(Extension.keyUsage, true,
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
            JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
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
            JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
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
            JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
            pemWriter.writeObject(key.getPrivate());
            pemWriter.flush();
            pemWriter.close();
            return true;
        }
        return false;
    }

    private KeyPair GenKeys () {
        KeyPair pair = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            pair = keyGen.generateKeyPair();
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }

        return pair;
    }

    private void saveEncKey(KeyPair keys, String filename) {
        try {
            byte[] key = keys.getPrivate().getEncoded();

            FileOutputStream keyfos = new FileOutputStream(filename + "Key");
            keyfos.write(key);
            keyfos.close();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public PrivateKey readPrivateKey(String filename) {
        // Retained for the historical baseline diagnostic; the legacy key-spec defect remains.
        try {
            FileInputStream keyfis = new FileInputStream(filename);

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
             X500Principal principal = user.getSubjectX500Principal();
             X500Name x500name = new X500Name( principal.getName() );
             RDN cn = x500name.getRDNs(BCStyle.CN)[0];
             String username = IETFUtils.valueToString(cn.getFirst().getValue());
             String filename = "files/" + username + ".p12";

             String password =
                     JOptionPane.showInputDialog("Enter a password for the certificate container:");
             if(password == null)
             {
                 JOptionPane.showMessageDialog(null,
                         "The default password will be used: certificate_password", "Password Required",
                         JOptionPane.INFORMATION_MESSAGE);
             }
             PKCS12SafeBagBuilder eeCertBagBuilder = new JcaPKCS12SafeBagBuilder(user);
             eeCertBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new DERBMPString("User's Certificate"));

             JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
             SubjectKeyIdentifier pubKeyId = extUtils.createSubjectKeyIdentifier(userKey.getPublic());
             eeCertBagBuilder.addBagAttribute(PKCS12SafeBag.localKeyIdAttribute, pubKeyId);
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
}
