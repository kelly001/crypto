/**
 * Created by Julia on 21.10.2014.
 */
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.x509.X509V2CRLGenerator;
import sun.security.x509.X500Name;

class BouncySecurity {
    AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA");
    AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);

    /* Создание сертификатов алгоритмом ECSDA библиотекой Баунси
    ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(lwPrivKey);
    SubjectPublicKeyInfo subPubKeyInfo;

    Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000);

    X509v1CertificateBuilder v1CertGen = new X509v1CertificateBuilder(
            new X500Name("CN=Test"),
            BigInteger.ONE,
            startDate, endDate,
            new X500Name("CN=Test"),
            subPubKeyInfo);

    X509CertificateHolder certHolder = v1CertGen.build(sigGen);
    */
}