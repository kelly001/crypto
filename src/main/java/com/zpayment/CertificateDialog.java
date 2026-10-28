package com.zpayment;

import java.awt.*;
import java.awt.Frame;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zpayment.ui.OkCancelDialog;
import database.Certificate;
import database.Company;
import database.User;

public class CertificateDialog extends OkCancelDialog {
    static Logger logger = Logger.getLogger("com.zpayment.CertificateDialog log");
    public static Dimension size = new Dimension(500,500);

    private static String title = "Окно созднаия и редактирования сертификатов";
    private static String okTitle = "Сохранить";

    public CertificateDialog(Frame parent, final Certificate certificate, final User user) throws Exception{
        super(parent, title, okTitle);

        System.out.println("constructor");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        final OkCancelDialog dialog = this;
        final CertificatePanel panel = new CertificatePanel();
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
                            security.generateRootCertificate(panel.getValues());
                        } else {
                            panel.save(id);
                            try {
                                User rootCompany = User.loadByName(panel.getValues().get("organization"));
                                // Preserve the existing company lookup before generating the certificate.
                                rootCompany.getValidCertificate();

                                security.generateUserCertificate(panel.getValues());
                            } catch (Exception e) {
                                System.out.println("Error to load root certificate: " + e.getLocalizedMessage());
                                System.out.println("User saved, certificate saved to db, closing dialog..");
                            }
                        }
                    }
                }
                dialog.dispose();
                 }
        });

        getContentPane().add(panel);
        dialog.setSize(size);
        dialog.pack();
        dialog.setVisible(true);
        logger.log(Level.FINE, String.valueOf(dialog.isOkPressed()));
    }
}
