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
import com.spazioit.safacilitator.Strings;
import com.spazioit.safacilitator.model.PrFile;
import com.spazioit.safacilitator.model.Project;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 *
 * @author Maurizio Martignano
 */
public class ProjectFileFunctions {
    private SAFacilitator safacilitator = null;
    private PrFile selectedFile = null;
    
    public ProjectFileFunctions(SAFacilitator safacilitator, PrFile selectedFile) {
        this.safacilitator = safacilitator;
        this.selectedFile = selectedFile;
    }
    
    /**
     * @param origBuilder - the command used to build the file
     * (compiler and the like) 
     * @throws Exception in case of application error
     * copy "origBuilder" from GUI to POJO
     * (single file level)
     */
    public void setOrigBuilder(TextField origBuilder) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (selectedFile == null) {
            throw new Exception(Strings.SELECTED_PROJECT_FILE_IS_NULL);
        }
        String myBuilder = origBuilder.getText();
        selectedFile.setPfOriginalBuilder(myBuilder);
        if (!p.getOrigBuilders().contains(myBuilder)) {
            p.getOrigBuilders().add(myBuilder);
        }
    }

    /**
     * @param origBuilder - the command used to build the file
     * (compiler and the like) 
     * @throws Exception in case of application error
     * copy "origBuilder" from POJO to GUI
     * (single file level)
     */
    public void getOrigBuilder(TextField origBuilder) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (selectedFile == null) {
            throw new Exception(Strings.SELECTED_PROJECT_FILE_IS_NULL);
        }
        origBuilder.setText(selectedFile.getPfOriginalBuilder());
    }
   
    /**
     * @param defines - the list of defines 
     * @throws Exception in case of application error
     * copy "defines" from GUI to POJO
     * (single file level)
     */
    public void setDefines(TextField defines) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (selectedFile == null) {
            throw new Exception(Strings.SELECTED_PROJECT_FILE_IS_NULL);
        }
        String myDefines = defines.getText();
        List<String> myList = new ArrayList<String>();
        for (String item : myDefines.split(",")) {
            myList.add(item);
        }
        selectedFile.setPfDefines(myList);
        for (String item : myList) {
            if (!p.getDefines().contains(item)) {
                p.getDefines().add(item);
            }
        } 
    }

    /**
     * @param defines - the list of defines 
     * @throws Exception in case of application error
     * copy "defines" from POJO to GUI
     * (single file level)
     */
    public void getDefines(TextField defines) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (selectedFile == null) {
            throw new Exception(Strings.SELECTED_PROJECT_FILE_IS_NULL);
        }
        String myDefines = "";
        for (int i = 0; i < selectedFile.getPfDefines().size(); i++) {
            myDefines += selectedFile.getPfDefines().get(i);
            if (i < (selectedFile.getPfDefines().size() - 1)) {
                myDefines += ",";
            }
        }
        defines.setText(myDefines);
    }
   
    /**
     * @param additionalArguments - the list of additional arguments
     * not defines, not includes
     * @throws Exception in case of application error
     * copy "additionalArguments" from GUI to POJO
     * (single file level)
     */
    public void setAdditionalArguments(TextField additionalArguments) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (selectedFile == null) {
            throw new Exception(Strings.SELECTED_PROJECT_FILE_IS_NULL);
        }
        String myArguments = additionalArguments.getText();
        List<String> myList = new ArrayList<String>();
        for (String item : myArguments.split(",")) {
            myList.add(item);
        }
        selectedFile.setPfAdditionalArguments(myList);
        for (String item : myList) {
            if (!p.getAdditionalArguments().contains(item)) {
                p.getAdditionalArguments().add(item);
            }
        } 
    }

    /**
     * @param additionalArguments - the list of additional arguments
     * not defines, not includes
     * @throws Exception in case of application error
     * copy "additionalArguments" from POJO to GUI
     * (single file level)
     */
    public void getAdditionalArguments(TextField additionalArguments) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (selectedFile == null) {
            throw new Exception(Strings.SELECTED_PROJECT_FILE_IS_NULL);
        }
        String myArguments = "";
        for (int i = 0; i < selectedFile.getPfAdditionalArguments().size(); i++) {
            myArguments += selectedFile.getPfAdditionalArguments().get(i);
            if (i < (selectedFile.getPfAdditionalArguments().size() - 1)) {
                myArguments += ",";
            }
        }
        additionalArguments.setText(myArguments);
    }
   
    /**
     * @param includeDirectories - the list of include directories
     * @throws Exception in case of application error
     * copy "includeDirectories" from GUI to POJO
     * (single file level)
     */
    public void setIncludeDirectories(ListView<String> includeDirectories) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (selectedFile == null) {
            throw new Exception(Strings.SELECTED_PROJECT_FILE_IS_NULL);
        }
        if (includeDirectories == null) {
            return;
        }
        List<String> listIncludeDirectories = new ArrayList<String>();
        for (int i = 0; i < includeDirectories.getItems().size() ; i++) {
            if (!listIncludeDirectories.contains(includeDirectories.getItems().get(i).toString())) {
                listIncludeDirectories.add(includeDirectories.getItems().get(i).toString());
            }
        }
        selectedFile.setPfIncludeDirectories(listIncludeDirectories);        
        for (String item : listIncludeDirectories) {
            if (!p.getIncludeDirectories().contains(item)) {
                p.getIncludeDirectories().add(item);
            }
        } 
    }

    /**
     * @param includeDirectories - the list of include directories
     * @throws Exception in case of application error
     * copy "includeDirectories" from POJO to GUI
     * (single file level)
     */
    public void getIncludeDirectories(ListView<String> includeDirectories) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (selectedFile == null) {
            throw new Exception(Strings.SELECTED_PROJECT_FILE_IS_NULL);
        }
        if (selectedFile.getPfIncludeDirectories() == null) {
            return;
        }
        includeDirectories.getItems().clear();
        for (int i = 0; i < selectedFile.getPfIncludeDirectories().size(); i++) {
            includeDirectories.getItems().add(selectedFile.getPfIncludeDirectories().get(i));
        }
    }
   
    
}
