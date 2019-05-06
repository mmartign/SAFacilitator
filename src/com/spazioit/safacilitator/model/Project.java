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
package com.spazioit.safacilitator.model;

import java.util.List;

/**
 *
 * @author Maurizio Martignano
 */
public class Project {
    
    // Project Name
    private String projectName = null;
    
    // Project Key (SonarQube)
    private String projectKey = null;
    
    // Project Version (SonarQube)
    private String projectVersion = null;

    // Base Directory
    private String baseDirectory = null;

    // Includes (@ global devel)
    private List<String> includeDirectories = null;
    
    // Sources (@ global devel)
    private List<String> sourceDirectories = null;
    
    // Defines (@ global devel)
    private List<String> defines = null;
    
    // Additional arguments  (@ global devel)
    private List<String> additionalArguments = null;
    
    // List of project files
    private List<PrFile> pFiles = null;
    
    // the Compile Commands file
    private String compileCommands = null;
    
    // Builders list  (@ global devel)
    private List<String> origBuilders = null;
    
    // Active Anayzers
    private List<String> activeAnalyzers = null;
    
    // Is Pre-Processing Enabled
    private Boolean preProcessingEnabled = false;
    
    // Directory containing the exploded files
    // (a sibling og the base directory)
    private String explodedDirectory = null;
    
    // SonarQube URL
    private String sonarQubeUrl = null;

    // SonarQube User Name
    private String sonarQubeUserName = null;

    // SonarQube User Password
    private String sonarQubeUserPassword = null;
    
    // Sonar Scanner Command
    private String sonarScanner = null;

    // Currently selected project file
    // (not relevant)
    private String selectedFile = null;
    
    // Getters / Setters

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public List<String> getIncludeDirectories() {
        return includeDirectories;
    }

    public void setIncludeDirectories(List<String> includeDirectories) {
        this.includeDirectories = includeDirectories;
    }

    public List<String> getSourceDirectories() {
        return sourceDirectories;
    }

    public void setSourceDirectories(List<String> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    public List<String> getDefines() {
        return defines;
    }

    public void setDefines(List<String> defines) {
        this.defines = defines;
    }

    public List<String> getAdditionalArguments() {
        return additionalArguments;
    }

    public void setAdditionalArguments(List<String> additionalArguments) {
        this.additionalArguments = additionalArguments;
    }

    public List<PrFile> getpFiles() {
        return pFiles;
    }

    public void setpFiles(List<PrFile> pFiles) {
        this.pFiles = pFiles;
    }

    public String getCompileCommands() {
        return compileCommands;
    }

    public void setCompileCommands(String compileCommands) {
        this.compileCommands = compileCommands;
    }

    public List<String> getOrigBuilders() {
        return origBuilders;
    }

    public void setOrigBuilders(List<String> origBuilders) {
        this.origBuilders = origBuilders;
    }

    public List<String> getActiveAnalyzers() {
        return activeAnalyzers;
    }

    public void setActiveAnalyzers(List<String> activeAnalyzers) {
        this.activeAnalyzers = activeAnalyzers;
    }

    public Boolean getPreProcessingEnabled() {
        return preProcessingEnabled;
    }

    public void setPreProcessingEnabled(Boolean preProcessingEnabled) {
        this.preProcessingEnabled = preProcessingEnabled;
    }

    public String getExplodedDirectory() {
        return explodedDirectory;
    }

    public void setExplodedDirectory(String explodedDirectory) {
        this.explodedDirectory = explodedDirectory;
    }

    public String getSonarQubeUrl() {
        return sonarQubeUrl;
    }

    public void setSonarQubeUrl(String sonarQubeUrl) {
        this.sonarQubeUrl = sonarQubeUrl;
    }

    public String getSonarQubeUserName() {
        return sonarQubeUserName;
    }

    public void setSonarQubeUserName(String sonarQubeUserName) {
        this.sonarQubeUserName = sonarQubeUserName;
    }

    public String getSonarQubeUserPassword() {
        return sonarQubeUserPassword;
    }

    public void setSonarQubeUserPassword(String sonarQubeUserPassword) {
        this.sonarQubeUserPassword = sonarQubeUserPassword;
    }

    public String getSonarScanner() {
        return sonarScanner;
    }

    public void setSonarScanner(String sonarScanner) {
        this.sonarScanner = sonarScanner;
    }

    public String getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }

}
