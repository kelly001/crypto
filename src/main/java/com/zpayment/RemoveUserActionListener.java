package com.zpayment;

import database.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveUserActionListener implements ActionListener {

    private User user;
    private Boolean succeeded = false;

    RemoveUserActionListener(User user) {
        this.user = user;
    }

    public void actionPerformed(ActionEvent ewt) {
        try {
            if (user != null && user.getStatus()) {
                User.delete(user.getId());
            }
        } catch (Exception exc) {
            System.out.println("Cancel user action listener error: " + exc.getLocalizedMessage());
        }
    }

    public boolean isSucceeded() {
        return this.succeeded;
    }
}
