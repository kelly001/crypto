package com.zpayment.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.junit.jupiter.api.Assertions.*;

/** Opt in with -Djava.awt.headless=false on a machine with a graphical desktop. */
@EnabledIfSystemProperty(named = "java.awt.headless", matches = "false")
class DialogCompatibilityTest {
    @Test
    void okNotifiesCallersAndDisposesTheModalDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            OkCancelDialog dialog = new OkCancelDialog(null, "Test", "Save");
            try {
                dialog.setMainPanel(new JLabel("Content"));
                dialog.packAndCenter();
                assertTrue(dialog.isModal());
                assertSame(dialog.getOkButton(), dialog.getRootPane().getDefaultButton());
                int[] callbacks = {0};
                dialog.getOkButton().addActionListener(event -> callbacks[0]++);
                dialog.getOkButton().doClick();
                assertEquals(1, callbacks[0]);
                assertTrue(dialog.isOkPressed());
                assertFalse(dialog.isDisplayable());
            } finally {
                dialog.dispose();
            }
        });
    }

    @Test
    void cancelAndEscapeDoNotAcceptTheForm() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (boolean escape : new boolean[]{false, true}) {
                OkCancelDialog dialog = new OkCancelDialog(null, "Test", "Save");
                try {
                    dialog.pack();
                    if (escape) {
                        dialog.getRootPane().getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0))
                                .actionPerformed(new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "escape"));
                    } else {
                        assertEquals("Cancel", dialog.cancel.getText());
                        dialog.cancel.doClick();
                    }
                    assertFalse(dialog.isOkPressed());
                    assertFalse(dialog.isDisplayable());
                } finally {
                    dialog.dispose();
                }
            }
        });
    }

    @Test
    void closeDialogPacksWithoutShowingAndDisposesOnClose() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            CloseButtonDialog dialog = new CloseButtonDialog(null, "Test", new JLabel("Content"));
            try {
                assertTrue(dialog.isModal());
                assertTrue(dialog.isDisplayable());
                assertFalse(dialog.isVisible());
                dialog.actionPerformed(new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "close"));
                assertFalse(dialog.isDisplayable());
            } finally {
                dialog.dispose();
            }
        });
    }
}
