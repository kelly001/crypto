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
import java.sql.Time;
import java.util.logging.Logger;

import com.teacode.swing.dialog
        .OkCancelDialog;
import com.teacode.swing.component.FieldPanel;

public class CertificatePanel extends FieldPanel {

    protected Logger logger = Logger.getLogger();
    protected SystemManager manager;
    protected List<PropertyControl> controls = new ArrayList<PropertyControl>();

   public CertificatePanel () {

    }
}
