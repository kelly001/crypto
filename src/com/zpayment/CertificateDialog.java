package com.zpayment;

import java.awt.*;
import java.awt.Frame;
import java.awt.event.*;
import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teacode.swing.dialog
        .OkCancelDialog;
import database.Certificate;
import database.Company;
import database.User;

public class CertificateDialog extends OkCancelDialog {

    static Logger logger = Logger.getLogger("com.zpayment.CertificateDialog log");
    public static Dimension size = new Dimension(500,500);
    public boolean succeeded;
    private Frame frame;
    private static String title = "Окно созднаия и редактирования сертификатов";
    private static String okTitle = "Сохранить";
    final Map<String, String> values = new HashMap<String, String>();

    public CertificateDialog(Frame parent, final Certificate certificate, final User user) throws Exception{
        super(parent, title, okTitle);
        this.frame = parent;
        //final Map<String, String> values = new HashMap<String, String>();

        System.out.println("constructor");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        succeeded = false;
        final OkCancelDialog dialog = this;
        final CertificatePanel panel = new CertificatePanel(parent);
        if (certificate != null) {
            panel.setValues(certificate);
        }
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent we)
            {
                if (isOkPressed())
                {
                    dialog.pressOK();
                    Security security = new Security();
                    String id = null;
                    try{
                        id = user.getId().toString();
                    } catch (NullPointerException ne){
                        System.out.println( "Get user id Null exception: " + ne.getLocalizedMessage());
                        id = User.newUser(user).toString();
                    } catch (Exception e){
                        System.out.print(e.getLocalizedMessage());
                    } finally {
                        if (user instanceof Company) {
                            panel.saveRoot(id);
                            X509Certificate rootcert = security.generateRootCertificate(panel.getValues());
                        } else {
                            panel.save(id);
                            try {
                                //TODO load user's company from class
                                //Certificate.loadByUser((Employer) user.getCompany().getId());
                                User rootCompany = User.loadByName(panel.getValues().get("organization"));
                                Certificate rootCert = rootCompany.getValidCertificate();
                                //get private key of organization
                                PrivateKey CAkey = security.readPrivateKey(rootCert.getFilename());
                                System.out.println(CAkey);
                            } catch (Exception e) {
                                System.out.println("Error to load root certificate: " + e.getLocalizedMessage());
                                System.out.println("User saved, certificate saved to db, closing dialog..");
                            }
                            security.generateUserCertificate(panel.getValues());
                        }
                    }
                }
                dialog.dispose();
                frame.repaint();

            }
        });
        //createControls(panel);
        getContentPane().add(panel);
        dialog.setSize(size);
        dialog.pack();
        dialog.setVisible(true);
        logger.log(Level.FINE, String.valueOf(dialog.isOkPressed()));
    }

    public boolean removeCertificate (Certificate certificate) {
        if (certificate != null && certificate.getStatus()) {
            String path = "files/" + certificate.getFilename();
            try {
                Certificate.cancel(certificate.getId());

                File certFile = new File(path);
                if (certFile.exists() && certFile.isFile() && certFile.delete())
                    return true;
            } catch (SQLException e){
                System.out.println("Update db exception catched: " + e.getLocalizedMessage());
            } catch (Exception x) {
                System.err.println("deleting certificete files exception catched: " + x.getLocalizedMessage());
            }
        }
        return false;
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
