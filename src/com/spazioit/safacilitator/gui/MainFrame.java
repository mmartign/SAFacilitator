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

import com.spazioit.safacilitator.functions.Executor;
import java.util.Calendar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Maurizio Martignano
 */
public class MainFrame extends Application {
    
    public static Window mainWindow;
    public static Stage secondaryStage;
    public static Stage primaryStage;
    public static Stage tertiaryStage;
    public static TextArea textArea;
    public static Label applicationMessage;
    public static MenuItem stopRunningMenuItem;
    public static Executor runningExecutor = null;
    
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        // Start the splashscreen first
        Parent root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream( "Marvin.png" )));
        // stage.setResizable(false);
        stage.setTitle("SAFacilitator - Copyright (©) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
       
        stage.setScene(scene);
        stage.show();
    }

    public void dynamicLaunch(String[] args) {
        launch(args);
    }    
}
