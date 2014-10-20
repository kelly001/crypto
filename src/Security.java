/**
 * Created by Julia on 21.10.2014.
 */
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V2CRLGenerator;

class Security {

        X509V2CRLGenerator   crlGen = new X509V2CRLGenerator();
        Date now = new Date();
        Date                 nextUpdate = ...;
        X509Certificate      caCrlCert = ...;
        PrivateKey           caCrlPrivateKey = ...;

        crlGen.setIssuerDN(new X500Principal("CN=Test CA"));


        crlGen.setThisUpdate(now);
        crlGen.setNextUpdate(nextUpdate);
        crlGen.setSignatureAlgorithm(signatureAlgorithm);


        crlGen.addCRLEntry(BigInteger.ONE, now, CRLReason.privilegeWithdrawn);


        crlGen.addExtension(X509Extensions.AuthorityKeyIdentifier,
        false, new AuthorityKeyIdentifierStructure(caCrlCert));
        crlGen.addExtension(X509Extensions.CRLNumber,
        false, new CRLNumber(crlNumber));


        X509CRL    crl = crlGen.generateX509CRL(caCrlPrivateKey, "BC");
        }