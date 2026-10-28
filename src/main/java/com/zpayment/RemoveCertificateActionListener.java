package com.zpayment;

import database.Certificate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

public class RemoveCertificateActionListener implements ActionListener{
    private Certificate cert;

    RemoveCertificateActionListener(Certificate cert) {
        this.cert = cert;
    }

    public void actionPerformed(ActionEvent e) {
        if (this.cert != null && this.cert.getStatus()) {
            String path = "files/" + this.cert.getFilename();
            try {
                Certificate.cancel(this.cert.getId());

                File certFile = new File(path);
                if (certFile.exists() && certFile.isFile()) {
                    certFile.delete();
                }
            } catch (SQLException sqle){
                System.out.println("Update db exception catched: " + sqle.getLocalizedMessage());
            } catch (Exception x) {
                System.err.println("deleting certificate files exception catched: " + x.getLocalizedMessage());
            }
        }
    }
}
