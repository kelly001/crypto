package com.zpayment.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** A labeled form supporting stacked and side-by-side fields. */
public class FieldPanel extends JPanel {
    public FieldPanel() {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
    }

    public void addField(String text, String tooltip, Component field, boolean stacked) {
        JLabel label = new JLabel(text);
        label.setLabelFor(field);
        String description = tooltip == null ? text : tooltip;
        label.setToolTipText(description);
        if (field instanceof JComponent component) {
            component.setToolTipText(description);
        }

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 8, 0, 8);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = stacked ? GridBagConstraints.REMAINDER : GridBagConstraints.RELATIVE;
        constraints.fill = stacked ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        add(label, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        add(field, constraints);
    }

    public void addGlue() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weighty = 1;
        add(Box.createVerticalGlue(), constraints);
    }
}
