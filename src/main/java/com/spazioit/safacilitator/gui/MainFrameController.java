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
import com.spazioit.safacilitator.Strings;
import com.spazioit.safacilitator.functions.AnalyzersFunctions;
import com.spazioit.safacilitator.functions.CommonFunctions;
import com.spazioit.safacilitator.functions.FileFunctions;
import com.spazioit.safacilitator.functions.FbInferFunctions;
import com.spazioit.safacilitator.functions.JavaFunctions;
import com.spazioit.safacilitator.functions.PythonFunctions;
import com.spazioit.safacilitator.functions.PreprocessingFunctions;
import com.spazioit.safacilitator.functions.SonarQubeFunctions;
import com.spazioit.safacilitator.model.Project;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
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

    private static final String JSON_FILES = "JSON Files";
    private static final String JSON_EXT = "*.json";

    /**
     * Loads project file
     */
    @FXML
    private void loadProject(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter(JSON_FILES, JSON_EXT),
                new ExtensionFilter(Strings.ALL_FILES, "*.*"));
        File selectedFile = fileChooser.showOpenDialog(MainFrame.getMainWindow());
        try {
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.getMyself());
            functions.readProject(fileName);
            SAFacilitator.setFileName(fileName);
            MainFrame.getPrimaryStage().setTitle(Strings.SAFACILITATOR + " - " + fileName);
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
        MainFrame.setTextArea(textArea);
        MainFrame.setApplicationMessage(applicationMessage);
        MainFrame.setStopRunningMenuItem(stopRunningMenuItem);
        textArea.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                textArea.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
        textArea.setPrefRowCount(SAFacilitator.getMAX_LINES());
    }

    /**
     * Pops edit project panel
     */
    @FXML
    private void editProject(ActionEvent event) {
        try {
            applicationMessage.setText("Editing Project.");
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/EditProject.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream(Strings.MARVIN_PNG)));
            Project p = SAFacilitator.getMyself().getCurrentProject();
            if ((p == null)
                    || (p.getProjectName() == null)
                    || (p.getProjectName().equals(""))) {
                stage.setTitle("Editing Project - (Empty)");
            } else {
                stage.setTitle("Editing Project - " + p.getProjectName());
            }
            stage.show();
            MainFrame.setSecondaryStage(stage);
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
            String fileName = SAFacilitator.getFileName();
            if ((fileName == null)
                    || (fileName.equals(""))) {
                saveProjectAs(event);
                return;
            }
            FileFunctions functions = new FileFunctions(SAFacilitator.getMyself());
            functions.saveProject(fileName);
            MainFrame.getPrimaryStage().setTitle(Strings.SAFACILITATOR + " - " + fileName);
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
        SAFacilitator.getMyself().setCurrentProject(new Project());
        SAFacilitator.setFileName("");
        MainFrame.getPrimaryStage().setTitle(Strings.SAFACILITATOR + " - (Empty) Project");
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
                    new ExtensionFilter(JSON_FILES, JSON_EXT),
                    new ExtensionFilter(Strings.ALL_FILES, "*.*"));
            fileChooser.setInitialFileName("compile_commands.json");
            Project p = SAFacilitator.getMyself().getCurrentProject();
            if ((p == null)
                    || (p.getBaseDirectory() == null)
                    || (p.getBaseDirectory().equals(""))) {
                throw new Exception("Project and Base Directory cannot be null");
            }
            if ((p != null)
                    && (p.getBaseDirectory() != null)
                    && (!p.getBaseDirectory().equals(""))) {
                fileChooser.setInitialDirectory(new File(p.getBaseDirectory()));
            }
            File selectedFile = fileChooser.showOpenDialog(MainFrame.getMainWindow());
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.getMyself());
            functions.readCompileCommands(fileName);
            applicationMessage.setText("Compile Commands Imported.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        MainFrame.getPrimaryStage().close();
        System.exit(0);
    }

    /**
     * Shows about dialog
     */
    @FXML
    private void aboutDialog(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream(Strings.MARVIN_PNG)));
        alert.setTitle(Strings.SAFACILITATOR);
        alert.setHeaderText("Static Analysis Facilitator - version " + SAFacilitator.getVersion());
        alert.setContentText("Copyright (©) " + LocalDate.now().getYear() + "\n"
                + "Spazio IT - Soluzioni Informatiche s.a.s.\n"
                + "https://www.spazioit.com\n\n"
                + "Used technologies: Java FX,\n"
                + "Jackson, JSON, CompileDB.\n\n"
                + "This work has been funded by the ESA\n"
                + "Contract # RFP/3-15558/18/NL/FE/as."
        );

        alert.showAndWait();
    }

    /**
     * Shows support dialog
     */
    @FXML
    private void onlineSupport(ActionEvent event) {
        /* Alert alert = new Alert(AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream(Strings.MARVIN_PNG)));
        alert.setTitle(Strings.SAFACILITATOR);
        alert.setHeaderText("Online Support");
        alert.setContentText("Support requests can be posted at\n"
                + "https://support.spazioit.com.");
        alert.showAndWait();
        */
        String url = "https://support.spazioit.com";
        callBrowser(url);
    }

    /**
     * Performs pre-processing
     */
    @FXML
    private void preProcess(ActionEvent event) {
        try {
            PreprocessingFunctions preprocFunctions = new PreprocessingFunctions(SAFacilitator.getMyself());
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
            AnalyzersFunctions analyzersFunctions = new AnalyzersFunctions(SAFacilitator.getMyself());
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
            AnalyzersFunctions analyzersFunctions = new AnalyzersFunctions(SAFacilitator.getMyself());
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
            AnalyzersFunctions analyzersFunctions = new AnalyzersFunctions(SAFacilitator.getMyself());
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
            SonarQubeFunctions sonarQubeFunctions = new SonarQubeFunctions(SAFacilitator.getMyself());
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
            SonarQubeFunctions sonarQubeFunctions = new SonarQubeFunctions(SAFacilitator.getMyself());
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
                new ExtensionFilter(Strings.ALL_FILES, "*.*"));
        Project p = SAFacilitator.getMyself().getCurrentProject();
        if ((p != null)
                && (p.getBaseDirectory() != null)
                && (!p.getBaseDirectory().equals(""))) {
            fileChooser.setInitialDirectory(new File(p.getBaseDirectory()));
        }
        fileChooser.setInitialFileName(Strings.SAFACILITATOR + ".log");
        File selectedFile = fileChooser.showSaveDialog(MainFrame.getMainWindow());
        try {
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.getMyself());
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
                    new ExtensionFilter(JSON_FILES, JSON_EXT),
                    new ExtensionFilter(Strings.ALL_FILES, "*.*"));
            Project p = SAFacilitator.getMyself().getCurrentProject();
            if ((p == null)
                    || (p.getBaseDirectory() == null)
                    || (p.getBaseDirectory().equals(""))
                    || (p.getpFiles() == null)
                    || (p.getpFiles().size() == 0)) {
                throw new Exception("Project, Base Directory and Project Files cannot be null");
            }
            if ((p != null)
                    && (p.getBaseDirectory() != null)
                    && (!p.getBaseDirectory().equals(""))) {
                fileChooser.setInitialDirectory(new File(p.getBaseDirectory()));
            }
            if ((p != null)
                    && (p.getProjectName() != null)
                    && (!p.getProjectName().equals(""))) {
                fileChooser.setInitialFileName("compile_commands.json");
            }
            File selectedFile = fileChooser.showSaveDialog(MainFrame.getMainWindow());
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.getMyself());
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
        Project p = SAFacilitator.getMyself().getCurrentProject();
        if (p == null) {
            return;
        }
        String extensions[] = new String[1];
        extensions[0] = "running";
        if (!p.getPreProcessingEnabled()) {
            for (File file : FileUtils.listFiles(new File(p.getBaseDirectory()), extensions, false)) {
                if (!file.delete()) {
                    System.err.println("Could not delete file " + file.getName() + ".");
                }
            }
        } else {
            for (File file : FileUtils.listFiles(new File(p.getExplodedDirectory()), extensions, false)) {
                if (!file.delete()) {
                    System.err.println("Could not delete file " + file.getName() + ".");
                }
            }
        }
        try {
            MainFrame.getRunningExecutor().cancel();
        } catch (Exception ex) {
            // Absorbs exceptions
        }
        MainFrame.getStopRunningMenuItem().setText("Stop running process...");
        MainFrame.getStopRunningMenuItem().setDisable(true);
    }

    /**
     * Prepares preprocessing
     */
    @FXML
    private void preparePreprocessing(ActionEvent event) {
        try {
            PreprocessingFunctions preprocFunctions = new PreprocessingFunctions(SAFacilitator.getMyself());
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
            PreprocessingFunctions preprocFunctions = new PreprocessingFunctions(SAFacilitator.getMyself());
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
                    new ExtensionFilter(JSON_FILES, JSON_EXT),
                    new ExtensionFilter(Strings.ALL_FILES, "*.*"));
            Project p = SAFacilitator.getMyself().getCurrentProject();
            if ((p == null)
                    || (p.getBaseDirectory() == null)
                    || (p.getBaseDirectory().equals(""))) {
                throw new Exception("Project and Base Directory cannot be null");
            }
            if ((p != null)
                    && (p.getBaseDirectory() != null)
                    && (!p.getBaseDirectory().equals(""))) {
                fileChooser.setInitialDirectory(new File(p.getBaseDirectory()));
            }
            if ((p != null)
                    && (p.getProjectName() != null)
                    && (!p.getProjectName().equals(""))) {
                fileChooser.setInitialFileName(p.getProjectName() + ".json");
            }
            File selectedFile = fileChooser.showSaveDialog(MainFrame.getMainWindow());
            String fileName = selectedFile.getCanonicalPath();
            FileFunctions functions = new FileFunctions(SAFacilitator.getMyself());
            functions.saveProject(fileName);
            MainFrame.getPrimaryStage().setTitle(Strings.SAFACILITATOR + " - " + fileName);
            SAFacilitator.setFileName(fileName);
            applicationMessage.setText("Project Saved.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    @FXML
    private void prepareForMaven(ActionEvent event) {
        JavaFunctions functions = new JavaFunctions(SAFacilitator.getMyself());
        functions.prepareForMaven();
    }

    @FXML
    private void prepareForGradle(ActionEvent event) {
        JavaFunctions functions = new JavaFunctions(SAFacilitator.getMyself());
        functions.prepareForGradle();
    }

    @FXML
    private void prepareForFbInfer(ActionEvent event) {
        FbInferFunctions functions = new FbInferFunctions(SAFacilitator.getMyself());
        functions.prepareForFbInfer();
    }

    @FXML
    private void prepareForPython(ActionEvent event) {
        PythonFunctions functions = new PythonFunctions(SAFacilitator.getMyself());
        functions.prepareForPython();
    }

    @FXML
    private void showHelp(ActionEvent event) {
        String url = "https://www.spazioit.com/SAFeToolsetHelp";
        callBrowser(url);
    }
    
    private void callBrowser(String url) {
        applicationMessage.setText("Consulting Help.");
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                applicationMessage.setText(e.getMessage());
                CommonGuiFunctions.displayError(e.getMessage());
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                applicationMessage.setText(e.getMessage());
                CommonGuiFunctions.displayError(e.getMessage());
            }
        }        
    }
}
