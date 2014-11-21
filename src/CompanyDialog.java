import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import database.Company;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by new_name on 20.11.2014.
 */
public class CompanyDialog extends CloseButtonDialog {
    static Logger logger = Logger.getLogger("Users View Dialog log");
    public static Dimension size = new Dimension(480,480);
    protected Frame frame;
    public Company company;
    protected String[] labels = {"Название", "Отделение", "Имя пользователя", "Email", "Город",
            "Регион", "Страна"};
    protected ArrayList<JTextField> controls = new ArrayList<JTextField>();

    public CompanyDialog(Frame parent, String title, FieldPanel panel) {
        super(parent, title,panel);
    }

    public void setCompany(String name) {
        try {
            System.out.println(name);
            company = Company.loadByEmail(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGUI(Frame parent, FieldPanel panel) {
        //FieldPanel panel = new FieldPanel();
        if (company == null) {
            for( String label: labels) {
                JTextField field = new JTextField(label);
                field.setName(label);
                field.setColumns(20);
                field.setEditable(true);
                controls.add(field);
                panel.addField(label, label, field, true);
            }
        } else {
            final JLabel label = new JLabel();
            String companyLabel = "Организация ООО\""+ company.getUsername() + "\"";
            panel.addField(companyLabel, "label", label, true);
        }

        final JButton saveCompanyButton = new JButton("Сохранить");
        saveCompanyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.addField("", "", saveCompanyButton, false);

        panel.addGlue();
        this.pack();
        this.setContentPane(panel);
        //this.setSize(size);
    }

}
