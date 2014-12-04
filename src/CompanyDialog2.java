import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import database.Company;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by new_name on 20.11.2014.
 */
public class CompanyDialog2 extends CloseButtonDialog {
    static Logger logger = Logger.getLogger("Users View Dialog log");
    public static Dimension size = new Dimension(480,480);
    protected Frame frame;
    protected JPanel panel;

    public CompanyDialog2(Frame parent, String title, User user, Company company) {
        super(parent, title, getPanel(user, company));
        this.frame = parent;
    }

    public static JPanel getPanel(User user, Company company) {
        //FieldPanel panel = new FieldPanel();
        if (user instanceof Company){
            CompanyPanel companyPanel = new CompanyPanel(user, null); // TODO лучше избавиться от передачи в панель диалога.
            companyPanel.setControls();
            return companyPanel;
        } else  {
            UserPanel2 panel = new UserPanel2(user); //лучше избавиться от передачи в панель диалога.
            panel.setValues(user);
            //видишь не однородность. Не единообразно передаешь пользователя в панель.
            return panel;
        }
    }

    // обработка нажатия на close - закрытие диалога
    public void dispose() {
        if (panel instanceof UserPanel2){
            UserPanel2 up2 = (UserPanel2) panel;
            up2.save();
        }
        //todo for CompanyPanel
        //закрываем диалог
        super.dispose();
    }
}
