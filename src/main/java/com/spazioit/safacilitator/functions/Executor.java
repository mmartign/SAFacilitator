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
import java.io.DataInputStream;
import java.io.InputStream;
import javafx.application.Platform;

/**
 *
 * @author Maurizio Martignano
 */
@SuppressWarnings("unchecked")
public class Executor extends Thread {

    private String command = null;
    private String message = null;
    private boolean isLog = false;
    private Process ps = null;

    Executor(String command, String message, boolean isLog) {
        this.command = command;
        this.message = message;
        this.isLog = isLog;
    }

    @Override
    public void run() {
        try {
            // Create process
            ps = Runtime.getRuntime().exec(command);
            // Redirect sub-process I/O
            InputStream is = ps.getInputStream();
            InputStream es = ps.getErrorStream();
            DataInputStream dis = new DataInputStream(is);
            DataInputStream des = new DataInputStream(es);
            Thread inputThread = new Thread() {
                public void run() {
                    try {
                    String istr = dis.readLine();
                    while (istr != null) {
                        if (isInterrupted()) {
                            break;
                        }
                        if (!istr.equals("")) {
                            if (isLog) {
                                CommonFunctions.printLogMessage("(stdout) - " + istr);
                            } else {
                                CommonFunctions.printMessage(istr);
                            }
                        }
                        istr = dis.readLine();
                    }
                    } catch (Exception ex) {
                        // Absorbs exceptions
                    }
                }
            };
            Thread errorThread = new Thread() {
                public void run() {
                    try {
                    String estr = des.readLine();
                    while (estr != null) {
                        if (isInterrupted()) {
                            break;
                        }
                        if (!estr.equals("")) {
                            if (isLog) {
                                CommonFunctions.printLogMessage("(stderr) - " + estr);
                            } else {
                                CommonFunctions.printMessage(estr);
                            }
                        }
                        estr = des.readLine();
                    }
                    } catch (Exception ex) {
                        // Absorbs exceptions
                    }
                }
            };
            inputThread.start();
            errorThread.start();
            inputThread.join();
            errorThread.join();
            CommonFunctions.printLogMessage(message);
            if (!SAFacilitator.isGuiEnabled()) {
                try {
                    notifyAll();
                } catch (Exception ex) {
                    // Absorbs exceptions
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        if (SAFacilitator.isGuiEnabled()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MainFrame.getStopRunningMenuItem().setText("Stop running process...");
                    MainFrame.getStopRunningMenuItem().setDisable(true);
                }
            });
        }
    }

    public void cancel() {
        interrupt();
    }
}
