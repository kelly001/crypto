package com.zpayment;

import database.Certificate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

/**
 * Created by new_name on 05.12.2014.
 */
public class RemoveCertificateActionListener implements ActionListener{

    private Certificate cert;
    private Boolean succeeded = false;

    RemoveCertificateActionListener(Certificate cert) {
        this.cert = cert;
    }

    public void actionPerformed(ActionEvent e) {
        //com.zpayment.CertificateDialog.removeCertificate(this.cert);
        if (this.cert != null && this.cert.getStatus()) {
            String path = "files/" + this.cert.getFilename();
            try {
                Certificate.cancel(this.cert.getId());

                File certFile = new File(path);
                if (certFile.exists() && certFile.isFile() && certFile.delete())
                    this.succeeded = true;

            } catch (SQLException sqle){
                System.out.println("Update db exception catched: " + sqle.getLocalizedMessage());
            } catch (Exception x) {
                System.err.println("deleting certificate files exception catched: " + x.getLocalizedMessage());
            }
        }
    }
    public boolean isSucceeded() {
        return this.succeeded;
    }
}
