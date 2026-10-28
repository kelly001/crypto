package com.zpayment.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/** Modal content dialog with a close action; callers control when it is shown. */
public class CloseButtonDialog extends JDialog implements ActionListener {
    public CloseButtonDialog(Frame owner, String title, JComponent content) {
        this(owner, title, "Close", content);
    }

    public CloseButtonDialog(Frame owner, String title, String closeText, JComponent content) {
        super(owner, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JButton close = new JButton(closeText);
        close.setActionCommand("close");
        close.addActionListener(this);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(close);
        add(content, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        getRootPane().registerKeyboardAction(event -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        pack();
        setLocationRelativeTo(owner);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if ("close".equals(event.getActionCommand())) {
            dispose();
        }
    }
}
