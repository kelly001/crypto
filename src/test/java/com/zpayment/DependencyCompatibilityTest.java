package com.zpayment;

import java.io.DataInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.sql.DriverManager;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Build/dependency compatibility checks, not the full security regression suite. */
class DependencyCompatibilityTest {
    private static final Map<String, String> SUBJECT = Map.of(
            "country", "FI", "organization", "Disposable test",
            "username", "Compatibility", "email", "test@example.invalid");

    @Test
    void jdbcDriverIsDiscoveredWithoutExplicitClassLoading() throws Exception {
        // Driver lookup only: this deliberately opens no database connection.
        assertEquals("com.mysql.cj.jdbc.Driver",
                DriverManager.getDriver("jdbc:mysql://localhost/unused").getClass().getName());
    }

    @Test
    void applicationResourceAndJava25BytecodeAreAvailable() throws Exception {
        assertNotNull(MainFrame.class.getResource("/icon.jpg"));
        try (var input = new DataInputStream(MainFrame.class.getResourceAsStream("MainFrame.class"))) {
            assertEquals(0xCAFEBABE, input.readInt());
            input.readUnsignedShort();
            assertEquals(69, input.readUnsignedShort());
        }
    }

    @Test
    void legacyCertificatesAndPemExportsWorkWithTheNewProvider() throws Exception {
        Security security = new Security();
        var generator = Security.class.getDeclaredMethod("GenKeys");
        generator.setAccessible(true);
        KeyPair keys = (KeyPair) generator.invoke(security);
        assertNotNull(keys, "Application must register BC without editing the JDK security file");
        Date start = Date.from(Instant.parse("2026-01-01T00:00:00Z"));
        Date end = Date.from(Instant.parse("2027-01-01T00:00:00Z"));
        X509Certificate root = Security.generateX509CertificateRoot(
                SUBJECT, BigInteger.ONE, start, end, "SHA1withDSA", keys, "BC");
        root.verify(keys.getPublic());
        assertEquals(root.getSubjectX500Principal(), root.getIssuerX500Principal());
        assertTrue(root.getBasicConstraints() >= 0);
        assertEquals(start, root.getNotBefore());
        assertEquals(end, root.getNotAfter());

        // Characterize the existing user-self-signing path; it is not a valid CA-chain test.
        X509Certificate user = Security.generateX509Certificate(SUBJECT, BigInteger.TWO, start, end, keys);
        user.verify(keys.getPublic());
        assertEquals(-1, user.getBasicConstraints());

        // Surefire runs in target/test-work, away from committed historical keys.
        Path files = Files.createDirectories(Path.of("files"));
        Path certificate = Files.createTempFile(files, "certificate-", ".pem");
        Path publicKey = Files.createTempFile(files, "public-", ".pem");
        Path privateKey = Files.createTempFile(files, "private-", ".pem");
        try {
            assertTrue(Security.savePemX509Certificate(root, certificate.getFileName().toString()));
            assertTrue(Security.savePublicKey(keys, publicKey.getFileName().toString()));
            assertTrue(Security.savePrivateKey(keys, privateKey.getFileName().toString()));
            assertArrayEquals(root.getEncoded(), ((X509CertificateHolder) readPem(certificate)).getEncoded());
            var converter = new JcaPEMKeyConverter().setProvider("BC");
            assertArrayEquals(keys.getPublic().getEncoded(),
                    converter.getPublicKey((SubjectPublicKeyInfo) readPem(publicKey)).getEncoded());
            assertArrayEquals(keys.getPrivate().getEncoded(),
                    converter.getKeyPair((PEMKeyPair) readPem(privateKey)).getPrivate().getEncoded());
        } finally {
            Files.deleteIfExists(certificate);
            Files.deleteIfExists(publicKey);
            Files.deleteIfExists(privateKey);
        }
    }

    @Test
    void nullPemExportsDoNotCreateOrTruncateFiles() throws Exception {
        Path files = Files.createDirectories(Path.of("files"));
        Path existing = Files.createTempFile(files, "untouched-", ".pem");
        Path missing = files.resolve(existing.getFileName() + ".missing");
        try {
            Files.writeString(existing, "existing contents");
            for (Path destination : new Path[]{existing, missing}) {
                String name = destination.getFileName().toString();
                assertFalse(Security.savePemX509Certificate(null, name));
                assertFalse(Security.savePublicKey(null, name));
                assertFalse(Security.savePrivateKey(null, name));
            }
            assertEquals("existing contents", Files.readString(existing));
            assertFalse(Files.exists(missing));
        } finally {
            Files.deleteIfExists(existing);
            Files.deleteIfExists(missing);
        }
    }

    private Object readPem(Path file) throws Exception {
        try (var parser = new PEMParser(Files.newBufferedReader(file))) {
            return parser.readObject();
        }
    }
}
