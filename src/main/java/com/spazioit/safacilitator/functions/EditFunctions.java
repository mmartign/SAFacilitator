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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 *
 * @author Maurizio Martignano
 */
public class EditFunctions {
    private SAFacilitator safacilitator = null;
    
    public EditFunctions(SAFacilitator safacilitator) {
        this.safacilitator = safacilitator;
    }
    
    /**
     * @param projectName - name of the project
     * @throws Exception in case of application error
     * copy "projectName" from GUI to POJO, while doing so
     * normalize it as it was a file name
     */
    public void setProjectName(TextField projectName) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        Path path = Paths.get(projectName.getText());
        p.setProjectName(path.normalize().toString());
    }

    /**
     * @param projectName - name of the project
     * @throws Exception in case of application error
     * copy "projectName" from POJO to GUI
     */
    public void getProjectName(TextField projectName) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        projectName.setText(p.getProjectName());
    }
   
    /**
     * @param projectKey - the project key (useful for SonarQube)
     * @throws Exception in case of application error
     * copy "projectKey" from GUI to POJO
     */
    public void setProjectKey(TextField projectKey) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setProjectKey(projectKey.getText());
    }

    /**
     * @param projectKey - the project key (useful for SonarQube)
     * @throws Exception in case of application error
     * move "projectKey" from POJO to GUI
     */
    public void getProjectKey(TextField projectKey) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        projectKey.setText(p.getProjectKey());
    }
   
    /**
     * @param projectVersion - the project version (useful for SonarQube)
     * @throws Exception in case of application error
     * copy "projectVersion" from GUI to POJO
     */
    public void setProjectVersion(TextField projectVersion) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setProjectVersion(projectVersion.getText());
    }

    /**
     * @param projectVersion - the project version (useful for SonarQube)
     * @throws Exception in case of application error
     * copy "projectVersion" from POJO to GUI
     */
    public void getProjectVersion(TextField projectVersion) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        projectVersion.setText(p.getProjectVersion());
    }
   
    /**
     * @param baseDirectory - the project base directory (where the project is located)
     * @throws Exception in case of application error
     * copy "baseDirectory" from GUI to POJO
     */
    public void setBaseDirectory(TextField baseDirectory) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setBaseDirectory(baseDirectory.getText());
    }

    /**
     * @param baseDirectory - the project base directory (where the project is located)
     * @throws Exception in case of application error
     * copy "baseDirectory" from POJO to GUI
     */
    public void getBaseDirectory(TextField baseDirectory) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        baseDirectory.setText(p.getBaseDirectory());
    }
   
    /**
     * @param explodedDirectory - the directory where the precompiled
     * exploded files are / should be (it should be a sibling of base directory)
     * @throws Exception in case of application error
     * copy "explodedDirectory" from GUI to POJO
     */
    public void setExplodedDirectory(TextField explodedDirectory) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setExplodedDirectory(explodedDirectory.getText());
    }

    /**
     * @param explodedDirectory - the directory where the precompiled
     * exploded files are / should be (it should be a sibling of base directory)
     * @throws Exception in case of application error
     * copy "explodedDirectory" from POJO to GUI
     */
    public void getExplodedDirectory(TextField explodedDirectory) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        explodedDirectory.setText(p.getExplodedDirectory());
    }
   
    /**
     * @param compileCommands - the Compilation Database File as per 
     * https://clang.llvm.org/docs/JSONCompilationDatabase.html)
     * @throws Exception in case of application error
     * copy "compileCommands" from GUI to POJO
     */
    public void setCompileCommands(TextField compileCommands) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setCompileCommands(compileCommands.getText());
    }

    /**
     * @param compileCommands - the Compilation Database File as per 
     * https://clang.llvm.org/docs/JSONCompilationDatabase.html)
     * @throws Exception in case of application error
     * copy "compileCommands" from POJO to GUI
     */
    public void getCompileCommands(TextField compileCommands) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        compileCommands.setText(p.getCompileCommands());
    }
   
    /**
     * @param sourceDirectories - the list of source directories 
     * ARGH: some tools still use directories (and not files)
     * @throws Exception in case of application error
     * copy "sourceDirectories" from GUI to POJO
     */
    public void setSourceDirectories(ListView<String> sourceDirectories) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (sourceDirectories == null) {
            return;
        }
        List<String> listSourceDirectories = new ArrayList<String>();
        for (int i = 0; i < sourceDirectories.getItems().size() ; i++) {
            if (!listSourceDirectories.contains(sourceDirectories.getItems().get(i))) {
                listSourceDirectories.add(sourceDirectories.getItems().get(i));
            }
        }
        p.setSourceDirectories(listSourceDirectories);        
    }

    /**
     * @param sourceDirectories - the list of source directories 
     * ARGH: some tools still use directories (and not files)
     * @throws Exception in case of application error
     * copy "sourceDirectories" from POJO to GUI
     */
    public void getSourceDirectories(ListView<String> sourceDirectories) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (p.getSourceDirectories() == null) {
            return;
        }
        sourceDirectories.getItems().clear();
        for (int i = 0; i < p.getSourceDirectories().size(); i++) {
            sourceDirectories.getItems().add(p.getSourceDirectories().get(i));
        }
    }
    
    /**
     * @param sourceFiles - the list of source files 
     * @throws Exception in case of application error
     * copy "sourceFiles" from GUI to POJO
     */
    public void setSourceFiles(ListView<String> sourceFiles)  throws Exception {         
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (sourceFiles == null) {
            return;
        }
        List<String> listSourceFiles = new ArrayList<String>();
        for (int i = 0; i < sourceFiles.getItems().size() ; i++) {
            if (!listSourceFiles.contains(sourceFiles.getItems().get(i))) {
                listSourceFiles.add(sourceFiles.getItems().get(i));
            }
        }
        List<PrFile> myPrFiles = new ArrayList<PrFile>();
        for (int i = 0; i < listSourceFiles.size(); i++) {
            PrFile pFile = new PrFile();
            pFile.setPfFileName(listSourceFiles.get(i));
            pFile.setPfOriginalBuilder(p.getOrigBuilders().get(0));
            pFile.setPfDefines(p.getDefines());
            pFile.setPfIncludeDirectories(p.getIncludeDirectories());
            pFile.setPfAdditionalArguments(p.getAdditionalArguments());
            myPrFiles.add(pFile);
        } 
        p.setpFiles(myPrFiles);
    }
   
    /**
     * @param sourceFiles - the list of source files 
     * @throws Exception in case of application error
     * copy "sourceFiles" from POJO to GUI
     */
    public void getSourceFiles(ListView<String> sourceFiles) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (p.getpFiles() == null) {
            return;
        }
        sourceFiles.getItems().clear();
        for (int i = 0; i < p.getpFiles().size(); i++) {
            sourceFiles.getItems().add(p.getpFiles().get(i).getPfFileName());
        }
        
    }
   
    /**
     * @param origBuilders - the list of commands (compilers and the like) 
     * @throws Exception in case of application error
     * copy "origBuilders" from GUI to POJO
     */
    public void setOrigBuilders(TextField origBuilders) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        String myBuilders = origBuilders.getText();
        List<String> myList = new ArrayList<String>();
        for (String item : myBuilders.split(",")) {
            myList.add(item);
        }
        p.setOrigBuilders(myList);
    }

    /**
     * @param origBuilders - the list of commands (compilers and the like) 
     * @throws Exception in case of application error
     * copy "origBuilders" from POJO to GUI
     */
    public void getOrigBuilders(TextField origBuilders) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        String myBuilders = "";
        for (int i = 0; i < p.getOrigBuilders().size(); i++) {
            myBuilders += p.getOrigBuilders().get(i);
            if (i < (p.getOrigBuilders().size() - 1)) {
                myBuilders += ",";
            }
        }
        origBuilders.setText(myBuilders);
    }
   
    /**
     * @param defines - the list of defines 
     * @throws Exception in case of application error
     * copy "defines" from GUI to POJO
     */
    public void setDefines(TextField defines) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        String myDefines = defines.getText();
        List<String> myList = new ArrayList<String>();
        for (String item : myDefines.split(",")) {
            myList.add(item);
        }
        p.setDefines(myList);
    }

    /**
     * @param defines - the list of defines 
     * @throws Exception in case of application error
     * copy "defines" from POJO to GUI
     */
    public void getDefines(TextField defines) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        String myDefines = "";
        for (int i = 0; i < p.getDefines().size(); i++) {
            myDefines += p.getDefines().get(i);
            if (i < (p.getDefines().size() - 1)) {
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
     */
    public void setAdditionalArguments(TextField additionalArguments) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        String myArguments = additionalArguments.getText();
        List<String> myList = new ArrayList<String>();
        for (String item : myArguments.split(",")) {
            myList.add(item);
        }
        p.setAdditionalArguments(myList);
    }

    /**
     * @param additionalArguments - the list of additional arguments
     * not defines, not includes
     * @throws Exception in case of application error
     * copy "additionalArguments" from POJO to GUI
     */
    public void getAdditionalArguments(TextField additionalArguments) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        String myArguments = "";
        for (int i = 0; i < p.getAdditionalArguments().size(); i++) {
            myArguments += p.getAdditionalArguments().get(i);
            if (i < (p.getAdditionalArguments().size() - 1)) {
                myArguments += ",";
            }
        }
        additionalArguments.setText(myArguments);
    }
   
    /**
     * @param includeDirectories - the list of include directories
     * @throws Exception in case of application error
     * copy "includeDirectories" from GUI to POJO
     */
    public void setIncludeDirectories(ListView<String> includeDirectories) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
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
        p.setIncludeDirectories(listIncludeDirectories);        
    }

    /**
     * @param includeDirectories - the list of include directories
     * @throws Exception in case of application error
     * copy "includeDirectories" from POJO to GUI
     */
    public void getIncludeDirectories(ListView<String> includeDirectories) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (p.getIncludeDirectories() == null) {
            return;
        }
        includeDirectories.getItems().clear();
        for (int i = 0; i < p.getIncludeDirectories().size(); i++) {
            includeDirectories.getItems().add(p.getIncludeDirectories().get(i));
        }
    }
   
    /**
     * @param preprocessingEnabled - is pre-processing enabled
     * @throws Exception in case of application error
     * copy "preprocessingEnabled" from GUI to POJO
     */
    public void setPreprocessingEnabled(CheckBox preprocessingEnabled) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setPreProcessingEnabled(preprocessingEnabled.isSelected());
    }

    /**
     * @param preprocessingEnabled - is pre-processing enabled
     * @throws Exception in case of application error
     * copy "preprocessingEnabled" from POJO to GUI
     */
    public void getPreprocessingEnabled(CheckBox preprocessingEnabled) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        preprocessingEnabled.setSelected(p.getPreProcessingEnabled());
    }
 
    /**
     * @param pcLintEnabled - is PC-Lint enabled
     * @param cppcheckEnabled - is Cppcheck enabled
     * @param clangSaEnabled - is Clang-SA enabled
     * @param clangTidyEnabled - is Clang-Tidy enabled
     * @param compEnabled - is GCC enabled
     * @throws Exception in case of application error
     * copy "preprocessingEnabled" from GUI to POJO
     */
    public void setActiveAnalyzers(CheckBox pcLintEnabled, CheckBox cppcheckEnabled, 
                            CheckBox clangSaEnabled, CheckBox clangTidyEnabled, CheckBox compEnabled) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        List<String> myList = new ArrayList<String>();
        if (pcLintEnabled.isSelected()) {
            myList.add(Strings.PC_LINT);
        }
        if (cppcheckEnabled.isSelected()) {
            myList.add(Strings.CPPCHECK);
        }
        if (clangSaEnabled.isSelected()) {
            myList.add(Strings.CLANG_SA);
        }
        if (clangTidyEnabled.isSelected()) {
            myList.add(Strings.CLANG_TIDY);
        }
        if (compEnabled.isSelected()) {
            myList.add(Strings.COMP);
        }
        p.setActiveAnalyzers(myList);
    }

    /**
     * @param pcLintEnabled - is PC-Lint enabled
     * @param cppcheckEnabled - is Cppcheck enabled
     * @param clangSaEnabled - is Clang-SA enabled
     * @param clangTidyEnabled - is Clang-Tidy enabled
     * @param compEnabled - is GCC enabled
     * @throws Exception in case of application error
     * copy "preprocessingEnabled" from POJO to GUI
     */
    public void getActiveAnalyzers(CheckBox pcLintEnabled, CheckBox cppcheckEnabled, 
                            CheckBox clangSaEnabled, CheckBox clangTidyEnabled, CheckBox compEnabled) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        List<String> myList = p.getActiveAnalyzers();
        if (myList.contains(Strings.PC_LINT)) {
            pcLintEnabled.setSelected(true);
        } else {
            pcLintEnabled.setSelected(false);            
        }
        if (myList.contains(Strings.CPPCHECK)) {
            cppcheckEnabled.setSelected(true);
        } else {
            cppcheckEnabled.setSelected(false);            
        }
        if (myList.contains(Strings.CLANG_SA)) {
            clangSaEnabled.setSelected(true);
        } else {
            clangSaEnabled.setSelected(false);            
        }
        if (myList.contains(Strings.CLANG_TIDY)) {
            clangTidyEnabled.setSelected(true);
        } else {
            clangTidyEnabled.setSelected(false);            
        }
        if (myList.contains(Strings.COMP)) {
            compEnabled.setSelected(true);
        } else {
            compEnabled.setSelected(false);            
        }
    }

    /**
     * @param sonarQubeUrl - SonarQube URL
     * @throws Exception in case of application error
     * copy "sonarQubeUrl" from GUI to POJO
     */
    public void setSonarQubeUrl(TextField sonarQubeUrl) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setSonarQubeUrl(sonarQubeUrl.getText());
    }

    /**
     * @param sonarQubeUrl - SonarQube URL
     * @throws Exception in case of application error
     * copy "sonarQubeUrl" from POJO to GUI
     */
    public void getSonarQubeUrl(TextField sonarQubeUrl) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        sonarQubeUrl.setText(p.getSonarQubeUrl());
    }
   
    /**
     * @param sonarQubeUserName - SonarQube User Name
     * @throws Exception in case of application error
     * copy "sonarQubeUserName" from GUI to POJO
     */
    public void setSonarQubeUserName(TextField sonarQubeUserName) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setSonarQubeUserName(sonarQubeUserName.getText());
    }

    /**
     * @param sonarQubeUserName - SonarQube User Name
     * @throws Exception in case of application error
     * copy "sonarQubeUserName" from POJO to GUI
     */
    public void getSonarQubeUserName(TextField sonarQubeUserName) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        sonarQubeUserName.setText(p.getSonarQubeUserName());
    }
   
    /**
     * @param sonarQubeUserPassword - SonarQube User Password
     * @throws Exception in case of application error
     * copy "sonarQubeUserPassword" from GUI to POJO
     */
    public void setSonarQubeUserPassword(TextField sonarQubeUserPassword) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setSonarQubeUserPassword(sonarQubeUserPassword.getText());
    }

    /**
     * @param sonarQubeUserPassword - SonarQube User Password
     * @throws Exception in case of application error
     * copy "sonarQubeUserPassword" from POJO to GUI
     */
    public void getSonarQubeUserPassword(TextField sonarQubeUserPassword) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        sonarQubeUserPassword.setText(p.getSonarQubeUserPassword());
    }
   
    /**
     * @param sonarScanner - the location of the sonar scanner command
     * @throws Exception in case of application error
     * copy "sonarScanner" from GUI to POJO
     */
    public void setSonarScanner(TextField sonarScanner) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        p.setSonarScanner(sonarScanner.getText());
    }

    /**
     * @param sonarScanner - the location of the sonar scanner command
     * @throws Exception in case of application error
     * copy "sonarScanner" from POJO to GUI
     */
    public void getSonarScanner(TextField sonarScanner) throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        sonarScanner.setText(p.getSonarScanner());
    }
    
    /**
     * @throws Exception in case of application error
     * normalize the single files data based on what specified
     * at general level (everything not in the general
     * lists is taken out)
     */
    public void normalizeFileDetails() throws Exception {
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (p.getpFiles() == null) {
            throw new Exception("In current project is null the project files list is null. Initialize it first!");
        }
        List<PrFile> tempPrFiles = new ArrayList<PrFile>();
        for (PrFile pFile : p.getpFiles()) {
            if (p.getOrigBuilders() != null) {
                if (pFile.getPfOriginalBuilder() != null) {
                    if (p.getOrigBuilders().contains(pFile.getPfOriginalBuilder())) {
                        tempPrFiles.add(pFile);
                    } else {
                        continue;
                    }
                }
            }
            List<String> temp = new ArrayList<String>();
            for (String item : p.getAdditionalArguments()) {
                temp.add(item);
            }
            pFile.setPfAdditionalArguments(temp);
            temp = new ArrayList<String>();
            for (String item : p.getDefines()) {
                temp.add(item);
            }
            pFile.setPfDefines(temp);
            temp = new ArrayList<String>();
            for (String item : p.getIncludeDirectories()) {
               temp.add(item);
            }
            pFile.setPfIncludeDirectories(temp);
        }
        p.setpFiles(tempPrFiles);
    }
   
}