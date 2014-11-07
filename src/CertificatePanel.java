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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teacode.swing.dialog
        .OkCancelDialog;
import com.teacode.swing.component.FieldPanel;

public class CertificatePanel extends FieldPanel {

    protected Logger logger = Logger.getLogger("Certificate panel");
    protected Frame frame;
    protected ArrayList<JTextField> controls = new ArrayList<JTextField>();
    public static Dimension size = new Dimension(500,500);
    protected String[] names = {"name", "organization", "department", "user", "email", "localty", "type", "comment"};
    protected String[] labels = {"Имя файла", "Организация", "Отделение", "Полное имя пользователя", "Email", "Месторасположение",
                    "Тип сертификата", "Комментарий"};
    private JTextField tfName;
    private JTextField tfOrganization;
    private JTextField tfDepartment;
    private JTextField tfUsername;
    private JTextField tfEmail;
    private JTextField tfType;
    private JTextField tfComment;
    private JTextField tfLocalty;
    private Time timestamp;
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
           controls.add(field);
           panel.addField(labels[i], names[i], field, true);
       }
       panel.addGlue();
       logger.log(Level.FINE, "create Controls");
    }

    public void save () {
        System.out.println("panel save");
        for(JTextField control: controls) {
            String value = control.getName() + control.getText();
            System.out.println(value);
        }
    }
}
