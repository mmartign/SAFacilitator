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
package com.spazioit.safacilitator.functions;

import com.spazioit.safacilitator.SAFacilitator;
import com.spazioit.safacilitator.gui.MainFrame;
import java.time.LocalDateTime;
import javafx.application.Platform;

/**
 *
 * @author Maurizio Martignano
 */
@SuppressWarnings("unchecked")
public class CommonFunctions {
    
    private static int lines = 0; 

    public static int getLines() {
        return lines;
    }

    public static void setLines(int lines) {
        CommonFunctions.lines = lines;
    }
    
    /**
     * @param message - the string to be printed
     * print the message either onto the application textarea or to Stdout
     */
    public static void printMessage(String message) {
        if (SAFacilitator.isGuiEnabled()) {
            Platform.runLater(new Runnable() {
                @Override 
                public void run() {
                    MainFrame.getTextArea().appendText(message + "\n");
                }
            });
        } else {
            System.out.println(message);
        }
    }

    /**
     * @param message - the log to be printed
     * print the log message either onto the application textArea or to STDOUT
     */
    public static void printLogMessage(String message) {
        String myMessage = LocalDateTime.now().format(SAFacilitator.getFormatter()) + " - " + message;
        if (SAFacilitator.isGuiEnabled()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MainFrame.getTextArea().appendText(myMessage + "\n");
                }
            });
            if (lines < SAFacilitator.getMAX_LINES()) {
                lines++;
            } else {
                lines = 0;
                MainFrame.getTextArea().setText("");
            }
        } else {
            System.out.println(myMessage);
        }
    }
    

    
}
