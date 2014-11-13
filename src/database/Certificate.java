package database;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by new_name on 11.11.2014.
 */
public class Certificate {
    private Long id;
    private String username;
    private String filename;
    private String department;
    private String email;
    private String type;
    private String comment;
    private Timestamp timestamp;
    private Boolean status;
    private String City;

    public Certificate () {

        /* root cert
        [v3_ca]
        basicConstraints        = CA:true
        nsComment       = "CA certificate of COMPANY_NAME"
        nsCertType      = sslCA, emailCA
        subjectKeyIdentifier=hash
        authorityKeyIdentifier=keyid:always,issuer:always
         */

        /* user cert
        [ v3_req ]
        basicConstraints = CA:FALSE
        keyUsage = "digitalSignature, keyEncipherment"
        nsCertType = "client, email, objsign"
         */
        this.type = "";
        this.comment = "";
        this.status = true;
        java.util.Date now = Calendar.getInstance().getTime();
        this.timestamp = new java.sql.Timestamp(now.getTime());
    }
}
