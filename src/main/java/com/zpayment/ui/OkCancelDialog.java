package com.zpayment.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/** Shared modal form shell retaining the application's OK/dispose contract. */
public class OkCancelDialog extends JDialog {
    public static final String OK_ACTION_COMMAND = "OK";
    protected final Frame frame;
    protected final JButton ok;
    protected final JButton cancel;
    protected boolean okPressed;

    public OkCancelDialog(Frame owner, String title, String okText) {
        super(owner, title, true);
        frame = owner;
        ok = new JButton(okText);
        ok.setActionCommand(OK_ACTION_COMMAND);
        ok.addActionListener(event -> pressOK());
        cancel = new JButton("Cancel");
        cancel.addActionListener(event -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(ok);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(ok);
        getRootPane().registerKeyboardAction(event -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void setMainPanel(JComponent panel) {
        add(panel, BorderLayout.CENTER);
    }

    public void packAndCenter() {
        pack();
        setLocationRelativeTo(frame);
    }

    public void pressOK() {
        okPressed = true;
        dispose();
    }

    public boolean isOkPressed() {
        return okPressed;
    }

    public JButton getOkButton() {
        return ok;
    }
}
