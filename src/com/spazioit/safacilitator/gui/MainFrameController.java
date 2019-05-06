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

import com.spazioit.safacilitator.SAFacilitator;
import com.spazioit.safacilitator.functions.AnalyzersFunctions;
import com.spazioit.safacilitator.functions.CommonFunctions;
import com.spazioit.safacilitator.functions.FileFunctions;
import com.spazioit.safacilitator.functions.PreprocessingFunctions;
import com.spazioit.safacilitator.functions.SonarQubeFunctions;
import com.spazioit.safacilitator.model.Project;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;


/**
 * FXML Controller class
 *
 * @author Maurizio Martignano
 */
public class MainFrameController implements Initializable {
    
    @FXML
    private Label applicationMessage;

    @FXML
    private javafx.scene.control.TextArea textArea;
    @FXML
    private MenuItem stopRunningMenuItem;

    /**
     * Loads project file
     */
    @FXML
    private void loadProject(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("JSON Files", "*.json"),
            new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(MainFrame.mainWindow);
        try {
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.myself);
            functions.readProject(fileName);
            SAFacilitator.fileName = fileName;
            MainFrame.primaryStage.setTitle("SAFacilitator - " + fileName);
            applicationMessage.setText("Project Loaded.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainFrame.textArea = textArea;
        MainFrame.applicationMessage = applicationMessage;
        MainFrame.stopRunningMenuItem = stopRunningMenuItem;
        textArea.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                textArea.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
        textArea.setPrefRowCount(SAFacilitator.MAX_LINES);
    }

    /**
     * Pops edit project panel
     */
    @FXML
    private void editProject(ActionEvent event) {
        try {
            applicationMessage.setText("Editing Project.");
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("EditProject.fxml"));
            Scene scene = new Scene(root);        
            stage.setScene(scene);
            stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream( "Marvin.png" )));
            Project p = SAFacilitator.myself.currentProject;
            if ((p == null) || 
                (p.getProjectName() == null) || 
                (p.getProjectName().equals(""))) {
                stage.setTitle("Editing Project - (Empty)");
            } else {
                stage.setTitle("Editing Project - " + p.getProjectName());                
            }
            stage.show();
            MainFrame.secondaryStage = stage;
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }
    
    /**
     * Saves default project file
     */
    @FXML
    private void saveProject(ActionEvent event) {        
        try {
            String fileName = SAFacilitator.fileName;
            if ((fileName == null) || 
                (fileName.equals(""))) {
                saveProjectAs(event);
                return;
            }
            FileFunctions functions = new FileFunctions(SAFacilitator.myself);
            functions.saveProject(fileName);
            MainFrame.primaryStage.setTitle("SAFacilitator - " + fileName);
            applicationMessage.setText("Project Saved.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }
    
    /**
     * Creates new project
     */
    @FXML
    private void newProject(ActionEvent event) {
        SAFacilitator.myself.currentProject = new Project();
        SAFacilitator.fileName = "";
        MainFrame.primaryStage.setTitle("SAFacilitator - (Empty) Project");
        applicationMessage.setText("Working on New (Empty) Project.");
    }

    /**
     * Imports compile commands file
     */
    @FXML
    private void compileCommands(ActionEvent event) {        
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Compile Commands File");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("JSON Files", "*.json"),
                new ExtensionFilter("All Files", "*.*"));
            fileChooser.setInitialFileName("compile_commands.json");
            Project p = SAFacilitator.myself.currentProject;
            if ((p == null) || 
                (p.getBaseDirectory() == null) || 
                (p.getBaseDirectory().equals(""))) {
                throw new Exception("Project and Base Directory cannot be null");
            }
            if ((p != null) && 
                (p.getBaseDirectory() != null) && 
                (!p.getBaseDirectory().equals(""))) {
                fileChooser.setInitialDirectory(new File(p.getBaseDirectory()));
            }
            File selectedFile = fileChooser.showOpenDialog(MainFrame.mainWindow);
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.myself);
            functions.readCompileCommands(fileName);
            applicationMessage.setText("Compile Commands Imported.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        MainFrame.primaryStage.close();
        System.exit(0);
    }

    /**
     * Shows about dialog
     */
    @FXML
    private void aboutDialog(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream( "Marvin.png" ))); 
        alert.setTitle("SAFacilitator");
        alert.setHeaderText("Static Analysis Facilitator - version " + SAFacilitator.version);
        alert.setContentText("Copyright (©) "  + Calendar.getInstance().get(Calendar.YEAR) +  "\n" + 
                             "Spazio IT - Soluzioni Informatiche s.a.s.\n" +
                             "https://www.spazioit.com\n\n" +
                             "Used technologies: Java FX,\n" +
                             "Jackson, JSON, CompileDB.\n\n" +
                             "This work has been funded by the ESA\n" +
                             "Contract # RFP/3-15558/18/NL/FE/as."
        ); 

        alert.showAndWait();
    }

    /**
     * Shows support dialog
     */
    @FXML
    private void supportDialog(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream( "Marvin.png" ))); 
        alert.setTitle("SAFacilitator");
        alert.setHeaderText("Online Support");
        alert.setContentText("Support requests can be posted at\n" + 
                             "https://support.spazioit.com.");
        alert.showAndWait();
    }

    /**
     * Performs pre-processing
     */
    @FXML
    private void preProcess(ActionEvent event) {
        try {
            PreprocessingFunctions preprocFunctions = new PreprocessingFunctions(SAFacilitator.myself);
            preprocFunctions.preProcess();
            applicationMessage.setText("Sources preprocessed.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Prepares analyzers
     */
    @FXML
    private void prepareAnalyzers(ActionEvent event) {
        try {
            AnalyzersFunctions analyzersFunctions = new AnalyzersFunctions(SAFacilitator.myself);
            analyzersFunctions.prepareAnalyzers();
            applicationMessage.setText("Analyzers prepared.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Executes analyzers
     */
    @FXML
    private void executeAnalyzers(ActionEvent event) {
        try {
            AnalyzersFunctions analyzersFunctions = new AnalyzersFunctions(SAFacilitator.myself);
            analyzersFunctions.executeAnalyzers();
            applicationMessage.setText("Analyzer(s) launched.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Post-process analyzers
     */
    @FXML
    private void postProcessAnalyzers(ActionEvent event) {
        try {
            AnalyzersFunctions analyzersFunctions = new AnalyzersFunctions(SAFacilitator.myself);
            analyzersFunctions.postProcessAnalyzers();
            applicationMessage.setText("Analyzers post processed.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    @FXML
    private void prepareSonarQube(ActionEvent event) {
        try {
            SonarQubeFunctions sonarQubeFunctions = new SonarQubeFunctions(SAFacilitator.myself);
            sonarQubeFunctions.prepareSonarQube();
            applicationMessage.setText("SonarQube prepared.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    @FXML
    private void runSonarScanner(ActionEvent event) {
        try {
            SonarQubeFunctions sonarQubeFunctions = new SonarQubeFunctions(SAFacilitator.myself);
            sonarQubeFunctions.runSonarScanner();
            applicationMessage.setText("SonarScanner launched.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Clears Log
     */
    @FXML
    private void clearLog(ActionEvent event) {
        textArea.setText("");
        CommonFunctions.setLines(0);
        applicationMessage.setText("Log Cleared.");
    }

    /**
     * Saves Log
     */
    @FXML
    private void saveLog(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Log File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Log Files", "*.log"),
            new ExtensionFilter("All Files", "*.*"));
        Project p = SAFacilitator.myself.currentProject;
        if ((p != null) && 
            (p.getBaseDirectory() != null) && 
            (!p.getBaseDirectory().equals(""))) {
            fileChooser.setInitialDirectory(new File(p.getBaseDirectory()));
        }
        fileChooser.setInitialFileName("SAFacilitator.log");
        File selectedFile = fileChooser.showSaveDialog(MainFrame.mainWindow);
        try {
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.myself);
            functions.saveLog(fileName, textArea);
            applicationMessage.setText("Log Saved.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Saves Project as compile commands file
     */
    @FXML
    private void saveProjectAsCompileCommands(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Project as Compile Comamnds File");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("JSON Files", "*.json"),
                new ExtensionFilter("All Files", "*.*"));
            Project p = SAFacilitator.myself.currentProject;
            if ((p == null) || 
                (p.getBaseDirectory() == null) || 
                (p.getBaseDirectory().equals("")) ||
                (p.getpFiles() == null) || 
                (p.getpFiles().size() == 0 )) {
                throw new Exception("Project, Base Directory and Project Files cannot be null");
            }
            if ((p != null) && 
            (p.getBaseDirectory() != null) && 
                (!p.getBaseDirectory().equals(""))) {
                fileChooser.setInitialDirectory(new File(p.getBaseDirectory()));
            }
            if ((p != null) && 
                (p.getProjectName() != null) && 
                (!p.getProjectName().equals(""))) {
                fileChooser.setInitialFileName("compile_commands.json");
            }
            File selectedFile = fileChooser.showSaveDialog(MainFrame.mainWindow);
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.myself);
            functions.saveProjectAsCompileCommands(fileName);
            applicationMessage.setText("Project Saved as Compile Commands.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Stops process running in the background
     */
    @FXML
    private void stopRunningProcess(ActionEvent event) {
        Project p = SAFacilitator.myself.currentProject;
        if (p == null) {
            return;
        }
        String extensions[] = new String[1];
        extensions[0] = "running";
        if (!p.getPreProcessingEnabled()) {
            for (File file : FileUtils.listFiles(new File(p.getBaseDirectory()), extensions, false)) {
                file.delete();
            }
        } else {
            for (File file : FileUtils.listFiles(new File(p.getExplodedDirectory()), extensions, false)) {
                file.delete();
            }            
        }
        try {
            MainFrame.runningExecutor.cancel();
        } catch (Exception ex) {
            // Absorbs exceptions
        }
        MainFrame.stopRunningMenuItem.setText("Stop running process...");
        MainFrame.stopRunningMenuItem.setDisable(true);
    }

    /**
     * Prepares preprocessing
     */
    @FXML
    private void preparePreprocessing(ActionEvent event) {
        try {
            PreprocessingFunctions preprocFunctions = new PreprocessingFunctions(SAFacilitator.myself);
            preprocFunctions.preparePreprocess();
            applicationMessage.setText("Preprocessing prepared");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Shows the compiler "predefined" defines
     */
    @FXML
    private void showCompilerDefines(ActionEvent event) {
        try {
            PreprocessingFunctions preprocFunctions = new PreprocessingFunctions(SAFacilitator.myself);
            preprocFunctions.showCompilerDefines();
            applicationMessage.setText("Defines Shown");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Saves default project file
     */
    @FXML
    private void saveProjectAs(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Project File");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("JSON Files", "*.json"),
                new ExtensionFilter("All Files", "*.*"));
            Project p = SAFacilitator.myself.currentProject;
            if ((p == null) || 
                (p.getBaseDirectory() == null) || 
                (p.getBaseDirectory().equals(""))) {
                throw new Exception("Project and Base Directory cannot be null");
            }
            if ((p != null) && 
            (p.getBaseDirectory() != null) && 
                (!p.getBaseDirectory().equals(""))) {
                fileChooser.setInitialDirectory(new File(p.getBaseDirectory()));
            }
            if ((p != null) && 
                (p.getProjectName() != null) && 
                (!p.getProjectName().equals(""))) {
                fileChooser.setInitialFileName(p.getProjectName() + ".json");
            }
            File selectedFile = fileChooser.showSaveDialog(MainFrame.mainWindow);
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.myself);
            functions.saveProject(fileName);
            MainFrame.primaryStage.setTitle("SAFacilitator - " + fileName);
            SAFacilitator.fileName = fileName;
            applicationMessage.setText("Project Saved.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }
}
