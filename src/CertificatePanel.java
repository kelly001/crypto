/**
 * Created by new_name on 06.11.2014.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teacode.swing.dialog
        .OkCancelDialog;
import com.teacode.swing.component.FieldPanel;
import database.Certificate;

public class CertificatePanel extends FieldPanel {

    protected Logger logger = Logger.getLogger("Certificate panel");
    protected Frame frame;
    protected Map<String, JTextField> controls = new HashMap<String, JTextField>();
    public static Dimension size = new Dimension(500,500);
    protected String[] names = {"email", "username","filename", "organization", "department",  "locality" , "state","country"};//, "type", "comment"};
    protected String[] labels = {"Email","Полное имя пользователя", "Имя файла", "Организация", "Отделение",   "Город",
                    "Регион", "Страна"};//, "Тип сертификата", "Комментарий"};
    private JButton btnCert;

   public CertificatePanel (Frame frame) {
       this.frame = frame;
       System.out.println("panel constructor");
       setControls(this);
   }

   protected void setControls(FieldPanel panel) {
       System.out.println("panel setControls");
       //create and set controls
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