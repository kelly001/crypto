import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class UserPanel2 extends FieldPanel {
    private final User user;
    // Поля
    private JTextField username;
    private JTextField email;
    private JTextField status;

    public UserPanel2(User user) {
        System.out.println("Users view panel");
        this.user = user;
        setControls(user);
    }

    protected void setControls(User user) {
        System.out.println("set controls with user object");

        username = new JTextField(user.getUsername());  //todo JLabel ???
        username.setEditable(false);
        this.addField("Имя пользователя", "Имя пользователя", username, true);

        email = new JTextField(user.getEmail());
        email.setEditable(false);
        this.addField("Почта пользователя", "Почтовый адрес пользователя", email, true);

        status = new JTextField(user.getStatus()?"Active":"Not");
        status.setEditable(false);
        this.addField("Статус пользователя", "Статус пользователя", status, true);
        this.addGlue();
    }

    public void setValues(User user) {
        if (user != null) {
            username.setText(user.getUsername());
            email.setText(user.getEmail());
            this.addGlue();
            this.repaint();
        }
    }

    public void save(){
        user.setUsername(username.getText());
        user.setEmail(email.getText());
        //user.setPassword(password.getText());    todo where is password field????
        try {
            if (User.newUser(user)){
                CloseButtonDialog infodialog =
                        new CloseButtonDialog(null, "Успех",
                                new JLabel("Новый пользователь создан!"));
                infodialog.setVisible(true);
            }
        } catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
        }
    }
}
