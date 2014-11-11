import com.teacode.swing.CommonRB;
import com.teacode.swing.dialog.CloseButtonDialog;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Created by new_name on 11.11.2014.
 */
public class UsersViewDialog extends CloseButtonDialog {

    static Logger logger = Logger.getLogger("CertificateDialog log");
    public static Dimension size = new Dimension(500,500);
    public boolean succeeded;


    public UsersViewDialog(Frame parent, String title, JComponent component) {
        super(parent, title, component);
        System.out.println("constructor");
        final CloseButtonDialog dialog = this;
        final UserPanel panel = new UserPanel(parent);
        dialog.getContentPane().add(panel);
        dialog.setSize(size);

        dialog.setVisible(true);
    }
}
