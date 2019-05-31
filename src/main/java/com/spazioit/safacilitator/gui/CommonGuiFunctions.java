/*
 * Copyright (C) 2019 Spazio IT - Soluzioni Informatiche.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with This program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 * 
 * This work has been funded by the European Space Agency
 * Contract # RFP/3-15558/18/NL/FE/as 
 */
package com.spazioit.safacilitator.gui;

import com.spazioit.safacilitator.Strings;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Maurizio Martignano
 */
public class CommonGuiFunctions {
    
    /**
     * @param message - the message to be displayed
     * show the message in a dialog pane
     */
    public static void displayMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream( Strings.MARVIN_PNG ))); 
        alert.setTitle(Strings.SAFACILITATOR);
        alert.setHeaderText("Application Info");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * @param message - the error message to be displayed
     * show the error message in a dialog pane
     */
    public static void displayError(String message) {
        if ((message == null)|| 
            (message.equals(""))) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream( Strings.MARVIN_PNG ))); 
        alert.setTitle(Strings.SAFACILITATOR);
        alert.setHeaderText("Application Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
