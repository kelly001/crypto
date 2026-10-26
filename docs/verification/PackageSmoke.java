import java.sql.DriverManager;
import java.util.Objects;

/** Run with only target/crypto-1.0-SNAPSHOT.jar on the classpath after packaging. */
public class PackageSmoke {
    public static void main(String[] args) throws Exception {
        Class<?> main = Class.forName("com.zpayment.MainFrame");
        Objects.requireNonNull(main.getResource("/icon.jpg"), "Packaged icon missing");
        Class.forName("com.zpayment.Security");
        var provider = Objects.requireNonNull(java.security.Security.getProvider("BC"));
        // Driver discovery only: never connect to a database in this diagnostic.
        var driver = DriverManager.getDriver("jdbc:mysql://localhost/unused");
        for (Class<?> type : new Class<?>[]{main, provider.getClass(), driver.getClass(),
                com.zpayment.ui.FieldPanel.class}) {
            String location = type.getProtectionDomain().getCodeSource().getLocation().toString();
            if (!location.contains("/target/") || !location.endsWith(".jar")) {
                throw new AssertionError("Expected a packaged build artifact: " + location);
            }
            System.out.println(type.getName() + " loaded from " + location);
        }
        System.out.println("Packaged resources, BC registration and JDBC discovery passed.");
    }
}
