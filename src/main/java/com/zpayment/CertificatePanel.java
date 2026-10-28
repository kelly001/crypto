package com.zpayment;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zpayment.ui.FieldPanel;
import database.Certificate;

public class CertificatePanel extends FieldPanel {
    protected Logger logger = Logger.getLogger("Certificate panel");

    protected Map<String, JTextField> controls = new HashMap<String, JTextField>();

    protected String[] names = {"email", "username","filename", "organization", "department",  "locality" , "state","country"};

   public CertificatePanel () {
       System.out.println("panel constructor");
       setControls(this);
   }

   protected void setControls(FieldPanel panel) {
       System.out.println("panel setControls");

       for (int i=0; i < names.length; i++) {
           JTextField field = new JTextField();
           field.setName(names[i]);
           controls.put(names[i], field);
           panel.addField(names[i], names[i], field, true);
       }
       panel.addGlue();
       logger.log(Level.FINE, "create Controls");
    }

    public void setValues(Certificate certificate) {
        if (certificate != null) {
            controls.get("username").setText(certificate.getUsername());
            controls.get("organization").setText(certificate.getOrganization());
            controls.get("email").setText(certificate.getEmail());
            controls.get("filename").setText(certificate.getFilename());
            controls.get("state").setText(certificate.getState());
            controls.get("locality").setText(certificate.getLocality());
            controls.get("department").setText(certificate.getDepartment());
            controls.get("country").setText(certificate.getCountry());
        }
    }

    public boolean save (String user) {
        System.out.println("panel save");
        HashMap<String,String> values = new HashMap<String, String>(){};
        for (Map.Entry<String, JTextField> entry : controls.entrySet()) {
            values.put(entry.getKey(), entry.getValue().getText());
        }
        values.put("user_id",user);
        values.put("type","client, email, objsign");
        values.put("comment","Certificate of " + values.get("username"));
        try {
            Certificate.newUser(values);
            return true;
        }catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
            return false;
        }
    }

    public boolean saveRoot (String user) {
        System.out.println("panel save user");
        HashMap<String,String> values = new HashMap<String, String>(){};
        for (Map.Entry<String, JTextField> entry : controls.entrySet()) {
            values.put(entry.getKey(), entry.getValue().getText());
        }
        values.put("user_id",user);
        values.put("type","sslCA, emailCA");
        values.put("comment","CA certificate of " + values.get("organization"));
        try {
            Certificate.newUser(values);
            return true;
        }catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
            return false;
        }
    }

    public Map<String,String> getValues () {
        Map<String,String> values = new HashMap<String, String>(){};
            for (HashMap.Entry<String, JTextField> entry: controls.entrySet()) {
                values.put(entry.getKey(), entry.getValue().getText());
            }
        values.put("type","sslCA, emailCA");
        values.put("comment","CA certificate of " + values.get("organization"));
        return values;
    }
}