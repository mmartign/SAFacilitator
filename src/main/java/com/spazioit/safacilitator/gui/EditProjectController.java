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
import com.spazioit.safacilitator.functions.EditFunctions;
import com.spazioit.safacilitator.model.Project;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

/**
 * FXML Controller class
 *
 * @author Maurizio Martignano
 */
public class EditProjectController implements Initializable {
    
    EditFunctions editFunctions = null;
    
    @FXML
    private TextField projectName;
    @FXML
    private TextField projectKey;
    @FXML
    private TextField projectVersion;
    @FXML
    private TextField baseDirectory;
    @FXML
    private TextField explodedDirectory;
    @FXML
    private TextField compileCommands;
    @FXML
    private ListView<String> sourceDirectories;
    @FXML
    private ListView<String> sourceFiles;
    @FXML
    private ListView<String> includeDirectories;
    @FXML
    private TextField defines;
    @FXML
    private TextField additionalArguments;
    @FXML
    private CheckBox preprocessingEnabled;
    @FXML
    private CheckBox pcLintEnabled;
    @FXML
    private CheckBox cppcheckEnabled;
    @FXML
    private CheckBox clangSaEnabled;
    @FXML
    private CheckBox clangTidyEnabled;
    @FXML
    private CheckBox compEnabled;
    @FXML
    private TextField sonarQubeUrl;
    @FXML
    private TextField sonarQubeUser;
    @FXML
    private TextField sonarQubePassword;
    @FXML
    private Label applicationMessage;
    @FXML
    private TextField sonarScanner;
    @FXML
    private TextField originalBuilders;
    @FXML
    private Button removeSourceDitectories;
    @FXML
    private Button removeSourceFiles;
    @FXML
    private Button removeIncludeDirectories;
    @FXML
    private TitledPane titledPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (SAFacilitator.myself.currentProject == null) {
            SAFacilitator.myself.currentProject = new Project();
        }
        editFunctions = new EditFunctions(SAFacilitator.myself);
        if (!System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            AnchorPane.setRightAnchor(removeSourceDitectories, 104.0);
            AnchorPane.setRightAnchor(removeSourceFiles, 104.0);
            AnchorPane.setRightAnchor(removeIncludeDirectories, 98.0);
        }
        Project p = SAFacilitator.myself.currentProject;
        if ((p == null) || 
            (p.getProjectName() == null) || 
            (p.getProjectName().equals(""))) {
            titledPane.setText("Editing Project - (Empty)");
        } else {
            titledPane.setText("Editing Project - " + p.getProjectName());                
        }
        try {
            editFunctions.getProjectName(projectName);
            editFunctions.getProjectKey(projectKey);
            editFunctions.getProjectVersion(projectVersion);
            editFunctions.getBaseDirectory(baseDirectory);
            editFunctions.getExplodedDirectory(explodedDirectory);
            editFunctions.getCompileCommands(compileCommands);
            editFunctions.getSourceDirectories(sourceDirectories);
            editFunctions.getSourceFiles(sourceFiles);
            editFunctions.getOrigBuilders(originalBuilders);
            editFunctions.getDefines(defines);
            editFunctions.getAdditionalArguments(additionalArguments);
            editFunctions.getIncludeDirectories(includeDirectories);
            editFunctions.getPreprocessingEnabled(preprocessingEnabled);
            editFunctions.getActiveAnalyzers(pcLintEnabled, cppcheckEnabled, clangSaEnabled, clangTidyEnabled, compEnabled);
            editFunctions.getSonarQubeUrl(sonarQubeUrl);
            editFunctions.getSonarQubeUserName(sonarQubeUser);
            editFunctions.getSonarQubeUserPassword(sonarQubePassword);
            editFunctions.getSonarScanner(sonarScanner);
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            if ((ex.getMessage() != null) && (!ex.getMessage().equals(""))) {
                CommonGuiFunctions.displayError(ex.getMessage());
            }
        }
    }    

    /**
     * Moves data from GUI to POJO
     */
    @FXML
    private void okEdit(ActionEvent event) {
        try {
            editFunctions.setProjectName(projectName);
            editFunctions.setProjectKey(projectKey);
            editFunctions.setProjectVersion(projectVersion);
            editFunctions.setBaseDirectory(baseDirectory);
            editFunctions.setExplodedDirectory(explodedDirectory);
            editFunctions.setCompileCommands(compileCommands);
            editFunctions.setSourceDirectories(sourceDirectories);
            editFunctions.setSourceFiles(sourceFiles);
            editFunctions.setOrigBuilders(originalBuilders);
            editFunctions.setDefines(defines);
            editFunctions.setAdditionalArguments(additionalArguments);
            editFunctions.setIncludeDirectories(includeDirectories);
            editFunctions.setPreprocessingEnabled(preprocessingEnabled);
            editFunctions.setActiveAnalyzers(pcLintEnabled, cppcheckEnabled, clangSaEnabled, clangTidyEnabled, compEnabled);
            editFunctions.setSonarQubeUrl(sonarQubeUrl);
            editFunctions.setSonarQubeUserName(sonarQubeUser);
            editFunctions.setSonarQubeUserPassword(sonarQubePassword);
            editFunctions.setSonarScanner(sonarScanner);
            MainFrame.secondaryStage.close();
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Closes GUI
     */
    @FXML
    private void cancelEdit(ActionEvent event) {
        MainFrame.secondaryStage.close();
    }

    /**
     * Gets base directory
     */
    @FXML
    private void getBaseDir(ActionEvent event) {
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Base Directory");
            File selectedDirectory = chooser.showDialog(MainFrame.mainWindow);
            baseDirectory.setText(selectedDirectory.getCanonicalPath().replace("\\", "/").replace("c:", "C:"));
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Gets exploded directory
     */
    @FXML
    private void getExplDir(ActionEvent event) {
        try {
            if (baseDirectory == null || baseDirectory.getText() == null || baseDirectory.getText().equals("")) {
                throw new Exception("Base Directory must be defined first.");
            }
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Exploded Directory");
            File selectedDirectory = chooser.showDialog(MainFrame.mainWindow);
            explodedDirectory.setText(selectedDirectory.getCanonicalPath().replace("\\", "/").replace("c:", "C:"));
            if (explodedDirectory.getText().startsWith(baseDirectory.getText() + "/")) {
                explodedDirectory.setText("");
                throw new Exception("Exploded Directory cannot be a child of Base Directory.");
            }
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Adds directory to directories list
     */
    @FXML
    private void addDirectory(ActionEvent event) {
        try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Project cannot be null.");
            } 
            if (baseDirectory.getText() == null) {
                throw new Exception("Base Directory cannot be null.");
            } 
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Add Source Directory");
            chooser.setInitialDirectory(new File(baseDirectory.getText()));
            File selectedDirectory = chooser.showDialog(MainFrame.mainWindow);
            String normDirectory = selectedDirectory.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
            if (normDirectory.equals(baseDirectory.getText())) {
                normDirectory = ".";
            } else {
                if (normDirectory.startsWith(baseDirectory.getText())) {
                    normDirectory = normDirectory.substring(baseDirectory.getText().length()+1);
                }
            }
            sourceDirectories.getItems().add(normDirectory);
            applicationMessage.setText("Directory Added.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Edits selected directory in directories list
     */
    @FXML
    private void editDirectory(ActionEvent event) {
        try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Project cannot be null.");
            } 
            if (baseDirectory.getText() == null) {
                throw new Exception("Base Directory cannot be null.");
            } 
            int selectedIdx = sourceDirectories.getSelectionModel().getSelectedIndex();
            if (selectedIdx != -1) {
                String itemToEdit = sourceDirectories.getSelectionModel().getSelectedItem();
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Edit Source Directory");
                chooser.setInitialDirectory(new File(baseDirectory.getText() + "/" + itemToEdit));
                File selectedDirectory = chooser.showDialog(MainFrame.mainWindow);
                String normDirectory = selectedDirectory.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
                if (normDirectory.equals(baseDirectory.getText())) {
                    normDirectory = ".";
                } else {
                    if (normDirectory.startsWith(baseDirectory.getText())) {
                        normDirectory = normDirectory.substring(baseDirectory.getText().length()+1);
                    }
                }
                sourceDirectories.getItems().set(selectedIdx, normDirectory);
            }
            applicationMessage.setText("Directory Edited.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Edits selected directory in directories list
     */
    @FXML
    private void removeDirectory(ActionEvent event) {
        int selectedIdx = sourceDirectories.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            String itemToRemove = sourceDirectories.getSelectionModel().getSelectedItem();
            int newSelectedIdx = (selectedIdx == sourceDirectories.getItems().size() - 1)
                    ? selectedIdx - 1
                    : selectedIdx;
          sourceDirectories.getItems().remove(selectedIdx);
          sourceDirectories.getSelectionModel().select(newSelectedIdx);
          applicationMessage.setText(itemToRemove + " removed.");
        }
    }

    /**
     * Adds file to sources list
     */
    @FXML
    private void addFile(ActionEvent event) {
        try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Project cannot be null.");
            } 
            if (baseDirectory.getText() == null) {
                throw new Exception("Base Directory cannot be null.");
            } 
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Add Source File");
            fileChooser.setInitialDirectory(new File(baseDirectory.getText()));
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("C Files", "*.c"),
                new FileChooser.ExtensionFilter("C Files", "*.C"),
                new FileChooser.ExtensionFilter("C Files", "*.cc"),
                new FileChooser.ExtensionFilter("C Files", "*.CC"),
                new FileChooser.ExtensionFilter("C Files", "*.cpp"),
                new FileChooser.ExtensionFilter("C Files", "*.CPP"),
                new FileChooser.ExtensionFilter("C Files", "*.cxx"),
                new FileChooser.ExtensionFilter("C Files", "*.CXX"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(MainFrame.mainWindow);
            String fileName = selectedFile.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
            sourceFiles.getItems().add(fileName);
            applicationMessage.setText("File Added.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Edits selected file in sources list
     */
    @FXML
    private void editFile(ActionEvent event) {
        try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Project cannot be null.");
            } 
            if (baseDirectory.getText() == null) {
                throw new Exception("Base Directory cannot be null.");
            } 
            int selectedIdx = sourceFiles.getSelectionModel().getSelectedIndex();
            if (selectedIdx != -1) {
                String itemToEdit = sourceFiles.getSelectionModel().getSelectedItem();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Edit Source File");
                fileChooser.setInitialDirectory(new File(itemToEdit).getParentFile());
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("c files", "*.c"),
                    new FileChooser.ExtensionFilter("C files", "*.C"),
                    new FileChooser.ExtensionFilter("cc files", "*.cc"),
                    new FileChooser.ExtensionFilter("CC files", "*.CC"),
                    new FileChooser.ExtensionFilter("cpp files", "*.cpp"),
                    new FileChooser.ExtensionFilter("CPP files", "*.CPP"),
                    new FileChooser.ExtensionFilter("cxx files", "*.cxx"),
                    new FileChooser.ExtensionFilter("CXX files", "*.CXX"),
                    new FileChooser.ExtensionFilter("All files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(MainFrame.mainWindow);
                String fileName = selectedFile.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
                sourceFiles.getItems().set(selectedIdx, fileName);
            }
            applicationMessage.setText("File Edited.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Removes selected file in sources list
     */
    @FXML
    private void removeFile(ActionEvent event) {
        int selectedIdx = sourceFiles.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            String itemToRemove = sourceFiles.getSelectionModel().getSelectedItem();
            int newSelectedIdx = (selectedIdx == sourceFiles.getItems().size() - 1)
                    ? selectedIdx - 1
                    : selectedIdx;
          sourceFiles.getItems().remove(selectedIdx);
          sourceFiles.getSelectionModel().select(newSelectedIdx);
          applicationMessage.setText(itemToRemove + " removed.");
        }
    }

    /**
     * Rebuilds list of sources from list of directories
     */
    @FXML
    private void getSources(ActionEvent event) {
        try {
            if (sourceDirectories == null) {
                throw new Exception("Source Directory cannot be empty.");
            }
            if (sourceDirectories.getItems().isEmpty()) {
                throw new Exception("Source Directory cannot be empty.");
            }
            if (baseDirectory.getText() == null) {
                throw new Exception("Base Directory cannot be null.");
            }             
            ObservableList<String> ol = FXCollections.observableArrayList();
            String extensions[] = new String[10];
            extensions[0] = "c";
            extensions[1] = "C";
            extensions[2] = "cc";
            extensions[3] = "CC";
            extensions[4] = "cpp";
            extensions[5] = "CPP";
            extensions[6] = "cxx";
            extensions[7] = "CXX";
            extensions[6] = "c++";
            extensions[7] = "C++";
            for (int i = 0; i < sourceDirectories.getItems().size(); i++) {
                File dir = new File(baseDirectory.getText() + "/" + sourceDirectories.getItems().get(i));
                for (File file: FileUtils.listFiles(dir, extensions, true)) {
                  ol.add(file.getCanonicalPath().replace("\\", "/").replace("c:", "C:"));
                }
            }            
            sourceFiles.setItems(null);
            sourceFiles.setItems(ol);
            applicationMessage.setText("Sources Regenerated");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Adds include file to list
     */
    @FXML
    private void addInclude(ActionEvent event) {
        try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Project cannot be null.");
            } 
            if (baseDirectory.getText() == null) {
                throw new Exception("Base Directory cannot be null.");
            } 
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Add Include Directory");
            chooser.setInitialDirectory(new File(baseDirectory.getText()));
            File selectedDirectory = chooser.showDialog(MainFrame.mainWindow);
            String normDirectory = selectedDirectory.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
            if (normDirectory.equals(baseDirectory.getText())) {
                normDirectory = ".";
            } else {
                if (normDirectory.startsWith(baseDirectory.getText())) {
                    normDirectory = normDirectory.substring(baseDirectory.getText().length()+1);
                }
            }
            includeDirectories.getItems().add(normDirectory);
            applicationMessage.setText("Directory Added.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Edits selected include file in list
     */
    @FXML
    private void editInclude(ActionEvent event) {
        try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Project cannot be null.");
            } 
            if (baseDirectory.getText() == null) {
                throw new Exception("Base Directory cannot be null.");
            } 
            int selectedIdx = includeDirectories.getSelectionModel().getSelectedIndex();
            if (selectedIdx != -1) {
                String itemToEdit = includeDirectories.getSelectionModel().getSelectedItem();
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Edit Include Directory");
                chooser.setInitialDirectory(new File(baseDirectory.getText() + "/" + itemToEdit));
                File selectedDirectory = chooser.showDialog(MainFrame.mainWindow);
                String normDirectory = selectedDirectory.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
                if (normDirectory.equals(baseDirectory.getText())) {
                    normDirectory = ".";
                } else {
                    if (normDirectory.startsWith(baseDirectory.getText())) {
                        normDirectory = normDirectory.substring(baseDirectory.getText().length()+1);
                    }
                }
                includeDirectories.getItems().set(selectedIdx, normDirectory);
            }
            applicationMessage.setText("Directory Edited.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Removes selected include file from list
     */
    @FXML
    private void removeInclude(ActionEvent event) {
        int selectedIdx = includeDirectories.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            String itemToRemove = includeDirectories.getSelectionModel().getSelectedItem();
            int newSelectedIdx = (selectedIdx == includeDirectories.getItems().size() - 1)
                    ? selectedIdx - 1
                    : selectedIdx;
          includeDirectories.getItems().remove(selectedIdx);
          includeDirectories.getSelectionModel().select(newSelectedIdx);
          applicationMessage.setText(itemToRemove + " removed.");
        }
   }

    /**
     * Moves up selected include file in list
     */
    @FXML
    private void upInclude(ActionEvent event) {
        int selectedIdx = includeDirectories.getSelectionModel().getSelectedIndex();
        if ((selectedIdx != -1) &&
            (selectedIdx != 0)) {
            ObservableList<String> ol = FXCollections.observableArrayList();
            for (int i = 0; i < (selectedIdx - 1); i++) {
                ol.add(includeDirectories.getItems().get(i));
            }
            ol.add(includeDirectories.getItems().get(selectedIdx));
            ol.add(includeDirectories.getItems().get(selectedIdx - 1));
            for (int i = selectedIdx + 1; i < includeDirectories.getItems().size(); i++) {
                ol.add(includeDirectories.getItems().get(i));
            }
            includeDirectories.setItems(null);
            includeDirectories.setItems(ol);
            applicationMessage.setText("Directory moved up.");
        }
    }

    /**
     * Moves down selected include file in list
     */
    @FXML
    private void downInclude(ActionEvent event) {
        int selectedIdx = includeDirectories.getSelectionModel().getSelectedIndex();
        if ((selectedIdx != -1) &&
            (selectedIdx != (includeDirectories.getItems().size() - 1))) {
            ObservableList<String> ol = FXCollections.observableArrayList();
            for (int i = 0; i < selectedIdx; i++) {
                ol.add(includeDirectories.getItems().get(i));
            }
            ol.add(includeDirectories.getItems().get(selectedIdx + 1));
            ol.add(includeDirectories.getItems().get(selectedIdx));
            for (int i = selectedIdx + 2; i < includeDirectories.getItems().size(); i++) {
                ol.add(includeDirectories.getItems().get(i));
            }
            includeDirectories.setItems(null);
            includeDirectories.setItems(ol);
            applicationMessage.setText("Directory moved down.");
        }
    }

    /**
     * Triggers pre-processing
     */
    @FXML
    private void preprocessingChanged(ActionEvent event) {
        if (explodedDirectory.getText() == null || explodedDirectory.getText().equals("")) {
            preprocessingEnabled.setSelected(false);
            applicationMessage.setText("Cannot set this parameter if Exploded Directory is null.");
            CommonGuiFunctions.displayError("Cannot set this parameter if Exploded Directory is null.");
        }
    }

    @FXML
    private void pcLintChanged(ActionEvent event) {
    }

    @FXML
    private void cppcheckEnabled(ActionEvent event) {
    }

    @FXML
    private void clangSaChanged(ActionEvent event) {
    }

    @FXML
    private void clangTidyChanged(ActionEvent event) {
    }

    @FXML
    private void compChanged(ActionEvent event) {
    }

    /**
     * Clears panel when changing tab
     */
    @FXML
    private void tabChanged(Event event) {
        try {
            applicationMessage.setText("");
        } catch (Exception ex) {
            // Do nothing
        }
    }

    /**
     * Gets Sonar Scanner command
     */
    @FXML
    private void getSonarScanner(ActionEvent event) {
         try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Project cannot be null.");
            } 
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Get Sonar Scanner");
            fileChooser.setInitialFileName("sonar-scanner");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Shell files", "*.sh"),
                new FileChooser.ExtensionFilter("Batch files", "*.bat"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(MainFrame.mainWindow);
            String fileName = selectedFile.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
            sonarScanner.setText(fileName);
            applicationMessage.setText("Sonar Scanner obtained.");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
   }

    /**
     * Takes out from single files info any element, item
     *  not known at general level
     */
    @FXML
    private void normalizeFileDetails(ActionEvent event) {
        try {
            editFunctions.setOrigBuilders(originalBuilders);
            editFunctions.setDefines(defines);
            editFunctions.setAdditionalArguments(additionalArguments);
            editFunctions.setIncludeDirectories(includeDirectories);
            editFunctions.normalizeFileDetails();
            applicationMessage.setText("Project file normalized!");
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Pops out single file panel
     */
    @FXML
    private void editProjectFile(MouseEvent event) {
         try {
            if (event.getClickCount() != 2) {
                return;
            }
            applicationMessage.setText("Editing Single Project File.");
            Project p = SAFacilitator.myself.currentProject;
            int selectedIdx = sourceFiles.getSelectionModel().getSelectedIndex();
            if (selectedIdx != -1) {
                 p.setSelectedFile(sourceFiles.getSelectionModel().getSelectedItem());
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProjectFile.fxml"));
            Scene scene = new Scene(root);        
            stage.setScene(scene);
            stage.getIcons().add(new Image(MainFrame.class.getResourceAsStream( "/images/Marvin.png" )));
            stage.setTitle("Editing Single Project File - " + sourceFiles.getSelectionModel().getSelectedItem());
            stage.show();
            MainFrame.tertiaryStage = stage;
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
   }

    /**
     * Updates Project data,based on data from single file
     */
    @FXML
    private void updateFromFile(ActionEvent event) {
        try {
            editFunctions.getOrigBuilders(originalBuilders);
            editFunctions.getDefines(defines);
            editFunctions.getAdditionalArguments(additionalArguments);
            editFunctions.getIncludeDirectories(includeDirectories);
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Shows analysis info
     */
    @FXML
    private void displayAnalysisInfo(ActionEvent event) {
        try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Project cannot be null.");
            }
            String myProject = p.getProjectName();
            if (myProject == null) {
                throw new Exception("Project Name cannot be null.");                
            }
            String message = null;
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                message = "The configuration files are:\n" + 
                          "PC-lint:\t\t" + myProject + ".lnt, run_pclint.bat\n" +
                          "Cppcheck:\t" + myProject + ".cppcheck, run_cppcheck.bat\n" +
                          "Clang-SA:\t\t" + myProject + "_clang-sa.bat\n" +
                          "Clang-Tidy:\t" + myProject + "_clang-tidy.bat\n" +
                          "Compiler:\t\t" + myProject + "_comp.bat\n" +
                          "SonarQube:\t" + "sonar-project.properties, run_sonar.bat\n";
            } else {
                message = "The configuration files are:\n" + 
                          "PC-lint:\t\t" + myProject + ".lnt, run_pclint.sh\n" +
                          "Cppcheck:\t" + myProject + ".cppcheck, run_cppcheck.bat\n" +
                          "Clang-SA:\t\t" + myProject + "_clang-sa.sh\n" +
                          "Clang-Tidy:\t" + myProject + "_clang-tidy.sh\n" +
                          "Compiler:\t\t" + myProject + "_comp.sh\n" +
                          "SonarQube:\t" + "sonar-project.properties, run_sonar.sh\n";
                
            }
            CommonGuiFunctions.displayMessage(message);
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    /**
     * Moves current folder one level up in source directories
     */
    @FXML
    private void upSourceDirectories(ActionEvent event) {
        int selectedIdx = sourceDirectories.getSelectionModel().getSelectedIndex();
        if ((selectedIdx != -1) &&
            (selectedIdx != 0)) {
            ObservableList<String> ol = FXCollections.observableArrayList();
            for (int i = 0; i < (selectedIdx - 1); i++) {
                ol.add(sourceDirectories.getItems().get(i));
            }
            ol.add(sourceDirectories.getItems().get(selectedIdx));
            ol.add(sourceDirectories.getItems().get(selectedIdx - 1));
            for (int i = selectedIdx + 1; i < sourceDirectories.getItems().size(); i++) {
                ol.add(sourceDirectories.getItems().get(i));
            }
            sourceDirectories.setItems(null);
            sourceDirectories.setItems(ol);
            applicationMessage.setText("Source directory moved up.");
        }
    }

    /**
     * Moves current folder one level down in source directories
     */
    @FXML
    private void downSourceDirectories(ActionEvent event) {
        int selectedIdx = sourceDirectories.getSelectionModel().getSelectedIndex();
        if ((selectedIdx != -1) &&
            (selectedIdx != (sourceDirectories.getItems().size() - 1))) {
            ObservableList<String> ol = FXCollections.observableArrayList();
            for (int i = 0; i < selectedIdx; i++) {
                ol.add(sourceDirectories.getItems().get(i));
            }
            ol.add(sourceDirectories.getItems().get(selectedIdx + 1));
            ol.add(sourceDirectories.getItems().get(selectedIdx));
            for (int i = selectedIdx + 2; i < sourceDirectories.getItems().size(); i++) {
                ol.add(sourceDirectories.getItems().get(i));
            }
            sourceDirectories.setItems(null);
            sourceDirectories.setItems(ol);
            applicationMessage.setText("Source directory moved down.");
        }
    }

    /**
     * Moves current file one level up in source directories
     */
    @FXML
    private void upSources(ActionEvent event) {
        int selectedIdx = sourceFiles.getSelectionModel().getSelectedIndex();
        if ((selectedIdx != -1) &&
            (selectedIdx != 0)) {
            ObservableList<String> ol = FXCollections.observableArrayList();
            for (int i = 0; i < (selectedIdx - 1); i++) {
                ol.add(sourceFiles.getItems().get(i));
            }
            ol.add(sourceFiles.getItems().get(selectedIdx));
            ol.add(sourceFiles.getItems().get(selectedIdx - 1));
            for (int i = selectedIdx + 1; i < sourceFiles.getItems().size(); i++) {
                ol.add(sourceFiles.getItems().get(i));
            }
            sourceFiles.setItems(null);
            sourceFiles.setItems(ol);
            applicationMessage.setText("Source file moved up.");
        }
    }

    /**
     * Moves current file one level down in source directories
     */
    @FXML
    private void downSources(ActionEvent event) {
        int selectedIdx = sourceFiles.getSelectionModel().getSelectedIndex();
        if ((selectedIdx != -1) &&
            (selectedIdx != (sourceFiles.getItems().size() - 1))) {
            ObservableList<String> ol = FXCollections.observableArrayList();
            for (int i = 0; i < selectedIdx; i++) {
                ol.add(sourceFiles.getItems().get(i));
            }
            ol.add(sourceFiles.getItems().get(selectedIdx + 1));
            ol.add(sourceFiles.getItems().get(selectedIdx));
            for (int i = selectedIdx + 2; i < sourceFiles.getItems().size(); i++) {
                ol.add(sourceFiles.getItems().get(i));
            }
            sourceFiles.setItems(null);
            sourceFiles.setItems(ol);
            applicationMessage.setText("Source file moved down.");
        }
    }

    /**
     * Takes "out" child source directories
     */
    @FXML
    private void removeChildren(ActionEvent event) {
        ObservableList<String> ol = FXCollections.observableArrayList();
        for (int i = 0; i < sourceDirectories.getItems().size(); i++) {
            if (ol.size() == 0) {
                ol.add(sourceDirectories.getItems().get(i));
            } else {
                int j;
                for (j = 0; j < ol.size(); j++) {
                    if (ol.get(j).startsWith(sourceDirectories.getItems().get(i))) {
                        ol.set(j, sourceDirectories.getItems().get(i));
                        break;
                    }   
                    if (sourceDirectories.getItems().get(i).startsWith(ol.get(j))) {
                        break;
                    }   
                }
                if (j == ol.size()) {
                    ol.add(sourceDirectories.getItems().get(i));
                }
            }
        }
        sourceDirectories.setItems(null);
        sourceDirectories.setItems(ol);
        applicationMessage.setText("Children taken out from source directories.");
    }
}