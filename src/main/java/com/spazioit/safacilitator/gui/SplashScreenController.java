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

import static com.spazioit.safacilitator.gui.MainFrame.mainWindow;
import static com.spazioit.safacilitator.gui.MainFrame.primaryStage;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maurizio Martignano
 */
public class SplashScreenController implements Initializable {

    @FXML
    private AnchorPane ap;

    class ShowSplashScreen extends Thread{

        @Override
        public void run(){
            try {
                Thread.sleep(5000);

                Platform.runLater(() -> {
                    // Start the main screen
                    Stage stage = new Stage();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/fxml/MainFrame.fxml"));
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                    Scene scene = new Scene(root);      
                    mainWindow = scene.getWindow();
        
                    stage.setScene(scene);
                    primaryStage = stage;
                    stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream("/images/Marvin.png")));
                    // stage.setResizable(false);
                    stage.setTitle("SAFacilitator - Copyright (©) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
                    stage.show();

                    ap.getScene().getWindow().hide();
                });                
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }

        

    }

    

    @Override

    public void initialize(URL url, ResourceBundle rb) {

        new ShowSplashScreen().start();

    }   

}