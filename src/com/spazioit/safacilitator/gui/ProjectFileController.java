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
import com.spazioit.safacilitator.functions.FileFunctions;
import com.spazioit.safacilitator.functions.ProjectFileFunctions;
import com.spazioit.safacilitator.model.PrFile;
import com.spazioit.safacilitator.model.Project;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author Maurizio Martignano
 */
public class ProjectFileController implements Initializable {
    
    ProjectFileFunctions projectFunctions = null;

    @FXML
    private ListView<String> includeDirectories;
    @FXML
    private Button addIncludeButton;
    @FXML
    private Button editIncludeButton;
    @FXML
    private Button removeIncludeButton;
    @FXML
    private Button upIncludeButton;
    @FXML
    private Button downIncludeButton;
    @FXML
    private TextField defines;
    @FXML
    private TextField additionalArguments;
    @FXML
    private Label applicationMessage;
    @FXML
    private TextField originalBuilder;
    
    private PrFile selectedProjectFile = null;
    @FXML
    private AnchorPane removeIncludeDirectories;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            AnchorPane.setRightAnchor(removeIncludeDirectories, 94.0);
        }

        try {
            Project p = SAFacilitator.myself.currentProject;
            if (p == null) {
                throw new Exception("Current project cannot be null.  Initialize it first!");
            }
            if (p.getpFiles() == null) {
                throw new Exception("Project files list cannot be null.  Initialize it first!");
            }
            if (p.getSelectedFile() == null) {
                throw new Exception("First, you need to select a file.");
            }
            for (PrFile pFile : p.getpFiles()) {
                if (pFile.getPfFileName().equals(p.getSelectedFile())) {
                    selectedProjectFile = pFile;
                    break;
                }
            }
            if (selectedProjectFile == null) {
                throw new Exception("The selected file hasn't been saved yet. Press Ok in the edit panel.");                
            }
            projectFunctions = new ProjectFileFunctions(SAFacilitator.myself, selectedProjectFile);
            projectFunctions.getOrigBuilder(originalBuilder);
            projectFunctions.getDefines(defines);
            projectFunctions.getAdditionalArguments(additionalArguments);
            projectFunctions.getIncludeDirectories(includeDirectories);
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
            if (p.getBaseDirectory() == null) {
                throw new Exception("Base Directory cannot be null.");
            } 
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Add Include Directory");
            chooser.setInitialDirectory(new File(p.getBaseDirectory()));
            File selectedDirectory = chooser.showDialog(MainFrame.mainWindow);
            String normDirectory = selectedDirectory.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
            if (normDirectory.equals(p.getBaseDirectory())) {
                normDirectory = ".";
            } else {
                if (normDirectory.startsWith(p.getBaseDirectory())) {
                    normDirectory = normDirectory.substring(p.getBaseDirectory().length()+1);
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
            if (p.getBaseDirectory() == null) {
                throw new Exception("Base Directory cannot be null.");
            } 
            int selectedIdx = includeDirectories.getSelectionModel().getSelectedIndex();
            if (selectedIdx != -1) {
                String itemToEdit = includeDirectories.getSelectionModel().getSelectedItem();
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Edit Include Directory");
                chooser.setInitialDirectory(new File(p.getBaseDirectory() + "/" + itemToEdit));
                File selectedDirectory = chooser.showDialog(MainFrame.mainWindow);
                String normDirectory = selectedDirectory.getCanonicalPath().replace("\\", "/").replace("c:", "C:");
                if (normDirectory.equals(p.getBaseDirectory())) {
                    normDirectory = ".";
                } else {
                    if (normDirectory.startsWith(p.getBaseDirectory())) {
                        normDirectory = normDirectory.substring(p.getBaseDirectory().length()+1);
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


    @FXML
    private void tabChanged(Event event) {
    }

    @FXML
    private void commit(ActionEvent event) {
        try {
            projectFunctions.setOrigBuilder(originalBuilder);
            projectFunctions.setDefines(defines);
            projectFunctions.setAdditionalArguments(additionalArguments);
            projectFunctions.setIncludeDirectories(includeDirectories);
            MainFrame.tertiaryStage.close();
        } catch (Exception ex) {
            applicationMessage.setText(ex.getMessage());
            CommonGuiFunctions.displayError(ex.getMessage());
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        MainFrame.tertiaryStage.close();
    }
    
}
