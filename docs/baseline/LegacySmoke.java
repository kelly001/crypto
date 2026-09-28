package com.zpayment;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/** Diagnostic of historical binaries, not a source-build or regression test. */
public class LegacySmoke {
    public static void main(String[] args) throws Exception {
        Security legacy = new Security();
        var generate = Security.class.getDeclaredMethod("GenKeys");
        generate.setAccessible(true);
        System.out.println("BC initially registered: " +
            (java.security.Security.getProvider("BC") != null));
        System.out.println("Legacy key generation without setup: " + generate.invoke(legacy));
        java.security.Security.addProvider(new BouncyCastleProvider());
        KeyPair keys = (KeyPair) generate.invoke(legacy);
        System.out.println("Keys after explicit BC registration: " + keys.getPublic().getAlgorithm());
        Map<String, String> values = new HashMap<>();
        values.put("country", "FI"); values.put("locality", "Baseline");
        values.put("state", "Baseline"); values.put("organization", "Baseline");
        values.put("department", "Test"); values.put("username", "Disposable");
        values.put("email", "baseline@example.invalid");
        Date start = new Date(System.currentTimeMillis() - 60000);
        Date end = new Date(System.currentTimeMillis() + 3600000);
        var root = Security.generateX509CertificateRoot(values, BigInteger.ONE,
            start, end, "SHA1withDSA", keys, "BC");
        root.verify(keys.getPublic());
        System.out.println("Root certificate signature verified: " + root.getSigAlgName());
        var user = Security.generateX509Certificate(values, BigInteger.TWO, start, end, keys);
        user.verify(keys.getPublic());
        System.out.println("User certificate verified with its own key; CA chain not tested");
        Path keyFile = Files.createTempFile("crypto-baseline-private-", ".der");
        try {
            Files.write(keyFile, keys.getPrivate().getEncoded());
            System.out.println("Private-key reload returned null: " +
                (legacy.readPrivateKey(keyFile.toString()) == null));
        } finally { Files.deleteIfExists(keyFile); }
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Legacy JDBC driver loads");
        try (var connection = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/crypto?connectTimeout=2000&socketTimeout=2000",
                "crypto", "crypto")) {
            System.out.println("Database connection established; no SQL executed");
        } catch (java.sql.SQLException error) {
            System.out.println("Database connection: " + error.getClass().getSimpleName());
        }
    }
}
