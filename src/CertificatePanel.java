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
    protected String[] names = {"email", "username","filename", "organization", "department",  "locality" , "state", "type", "comment"};
    protected String[] labels = {"Имя файла", "Организация", "Отделение", "Полное имя пользователя", "Email", "Месторасположение",
                    "Тип сертификата", "Комментарий"};
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
           JTextField field = new JTextField(labels[i]);
           field.setName(names[i]);
           controls.put(names[i], field);
           panel.addField(names[i], names[i], field, true);
       }
       panel.addGlue();
       logger.log(Level.FINE, "create Controls");
    }

    public void save () {
        System.out.println("panel save");
        HashMap<String,String> values = new HashMap<String, String>(){};
        for (Map.Entry<String, JTextField> entry : controls.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            values.put(entry.getKey(), entry.getValue().getText());
        }
        values.put("user_id","1");
        try {
            Certificate.newUser(values);
        }catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
            }

    }
}
