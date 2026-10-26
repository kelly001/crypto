package com.zpayment.ui;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FieldPanelTest {
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void labelsAndFieldsKeepTheirLayoutAndAccessibility(boolean stacked) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            FieldPanel panel = new FieldPanel();
            JTextField field = new JTextField(20);
            panel.addField("Name", null, field, stacked);
            panel.addGlue();
            panel.setSize(500, 300);
            panel.doLayout();
            JLabel label = (JLabel) panel.getComponent(0);
            assertSame(field, label.getLabelFor());
            assertEquals("Name", field.getToolTipText());
            assertTrue(field.getWidth() > 0);
            if (stacked) {
                assertTrue(field.getY() >= label.getY() + label.getHeight());
            } else {
                assertTrue(field.getX() >= label.getX() + label.getWidth());
            }
        });
    }
}
