package com.zpayment;

import database.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by new_name on 05.12.2014.
 */
public class RemoveUserActionListener implements ActionListener {

    private User user;
    private Boolean succeeded = false;

    RemoveUserActionListener(User user) {
        this.user = user;
    }

    public void actionPerformed(ActionEvent ewt) {

    }
}
