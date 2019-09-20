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
import com.spazioit.safacilitator.functions.Executor;
import java.time.LocalDate;
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
    
    private static Window mainWindow;
    private static Stage secondaryStage;
    private static Stage primaryStage;
    private static Stage tertiaryStage;
    private static TextArea textArea;
    private static Label applicationMessage;
    private static MenuItem stopRunningMenuItem;
    private static Executor runningExecutor = null;

    public static Window getMainWindow() {
        return mainWindow;
    }

    public static void setMainWindow(Window mainWindow) {
        MainFrame.mainWindow = mainWindow;
    }

    public static Stage getSecondaryStage() {
        return secondaryStage;
    }

    public static void setSecondaryStage(Stage secondaryStage) {
        MainFrame.secondaryStage = secondaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        MainFrame.primaryStage = primaryStage;
    }

    public static Stage getTertiaryStage() {
        return tertiaryStage;
    }

    public static void setTertiaryStage(Stage tertiaryStage) {
        MainFrame.tertiaryStage = tertiaryStage;
    }

    public static TextArea getTextArea() {
        return textArea;
    }

    public static void setTextArea(TextArea textArea) {
        MainFrame.textArea = textArea;
    }

    public static Label getApplicationMessage() {
        return applicationMessage;
    }

    public static void setApplicationMessage(Label applicationMessage) {
        MainFrame.applicationMessage = applicationMessage;
    }

    public static MenuItem getStopRunningMenuItem() {
        return stopRunningMenuItem;
    }

    public static void setStopRunningMenuItem(MenuItem stopRunningMenuItem) {
        MainFrame.stopRunningMenuItem = stopRunningMenuItem;
    }

    public static Executor getRunningExecutor() {
        return runningExecutor;
    }

    public static void setRunningExecutor(Executor runningExecutor) {
        MainFrame.runningExecutor = runningExecutor;
    }
    
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        // Start the splashscreen first
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SplashScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream( Strings.MARVIN_PNG )));
        stage.setTitle(Strings.SAFACILITATOR + " - Copyright (©) " + LocalDate.now().getYear() +  " - " + Strings.SPAZIOIT_WWW);
       
        stage.setScene(scene);
        stage.show();
    }

    public void dynamicLaunch(String[] args) {
        launch(args);
    }    
}
