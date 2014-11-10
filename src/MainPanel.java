/**
 * Created by new_name on 10.11.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teacode.swing.SmartTextArea;
import com.teacode.swing.component.FieldPanel;

public class MainPanel extends FieldPanel {

    protected Logger logger = Logger.getLogger("Certificate panel");
    protected Frame frame;

    // ПОля ораганизациии
    protected String[] names = {"name", "department", "user", "email", "city", "region", "country"};
    protected String[] labels = {"Название", "Отделение", "Имя пользователя", "Email", "Город",
            "Регион", "Страна"};
    protected ArrayList<JTextField> controls = new ArrayList<JTextField>();

    public MainPanel (Frame frame) {
        this.frame = frame;
        System.out.println("Main application panel");
        setControls(this);
    }

    //TODO Objects and get data from bd
    protected void setControls(FieldPanel panel) {
        final JLabel label = new JLabel();
        String companyLabel = "Организация ООО\"Название\"";
        panel.addField(companyLabel, "label", label, true);

        for (int i=0; i < names.length; i++) {
            JTextField field = new JTextField(labels[i]);
            field.setName(names[i]);
            field.setColumns(20);
            field.setEditable(false);
            controls.add(field);
            panel.addField(labels[i], names[i], field, true);
        }

        panel.addGlue();
    }
}
