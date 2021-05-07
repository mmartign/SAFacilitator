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
import com.spazioit.safacilitator.gui.MainFrame;
import com.spazioit.safacilitator.model.Project;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Maurizio Martignano
 */
public class SonarQubeFunctions {

    private SAFacilitator safacilitator = null;
    private Executor executor = null;

    private static final String PCLINT_REP_PATH = "pclint-reports/*";
    private static final String CPPCHECK_REP_PATH = "cppcheck-reports/*";
    private static final String CLANGSA_REP_PATH = "clangsa/*";
    private static final String CLANGTIDY_REP_PATH = "clangtidy-reports/*";
    private static final String COMP_REP_PATH = "comp-reports/*";
    private static final String FBINFER_REP_PATH = "fbinfer-reports/*";
    private static final String BANDIT_REP_PATH = "bandit-reports/*";
    private static final String PYLINT_REP_PATH = "pylint-reports/*";
    private static final String FLAKE8_REP_PATH = "flake8-reports/*";

    private static final String EXPLODED = "_EXPLODED";

    private static final String RUN_SONAR_UNX = "/run_sonar.sh";
    private static final String RUN_SONAR_WIN = "/run_sonar.bat";

    public SonarQubeFunctions(SAFacilitator safacilitator) {
        this.safacilitator = safacilitator;
    }

    /**
     * @throws Exception in case of application error prepare SonarQube
     */
    public void prepareSonarQube() throws Exception {
        CommonFunctions.printLogMessage("Preparing SonarQube...");
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        if (p.getProjectName() == null || p.getProjectName().equals("")) {
            throw new Exception("Project name cannot be null. Initialize it first!");
        }
        if (p.getProjectKey() == null || p.getProjectKey().equals("")) {
            throw new Exception("Project key cannot be null. Initialize it first!");
        }
        if (p.getProjectVersion() == null || p.getProjectVersion().equals("")) {
            throw new Exception("Project version cannot be null. Initialize it first!");
        }
        if (p.getSonarQubeUrl() == null || p.getSonarQubeUrl().equals("")) {
            throw new Exception("SonarQube URL cannot be null. Initialize it first!");
        }
        if (p.getSonarQubeUrl() == null || p.getSonarQubeUrl().equals("")) {
            throw new Exception("SonarQube URL cannot be null. Initialize it first!");
        }
        if (p.getSonarScanner() == null || p.getSonarScanner().equals("")) {
            throw new Exception("SonarScanner command cannot be null. Initialize it first!");
        }
        String fileName = null;
        if (!p.getPreProcessingEnabled()) {
            fileName = p.getBaseDirectory() + "/sonar-project.properties";
        } else {
            fileName = p.getExplodedDirectory() + "/sonar-project.properties";
        }
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("#  SonarQube configuration file for project " + p.getProjectName());
        bw.newLine();
        bw.write("#  Generated by SAFacilitator at " + LocalDateTime.now().format(SAFacilitator.getFormatter()));
        bw.newLine();
        bw.write("#  Copyright (C) " + LocalDate.now().getYear() + " - " + Strings.SPAZIOIT_WWW);
        bw.newLine();
        bw.newLine();
        bw.write("#  Required metadata");
        bw.newLine();
        if (!p.getPreProcessingEnabled()) {
            bw.write("sonar.projectName=" + p.getProjectName());
            bw.newLine();
            bw.write("sonar.projectKey=" + p.getProjectKey());
            bw.newLine();
            bw.write("sonar.projectVersion=" + p.getProjectVersion());
            bw.newLine();
        } else {
            bw.write("sonar.projectName=" + p.getProjectName() + EXPLODED);
            bw.newLine();
            bw.write("sonar.projectKey=" + p.getProjectKey() + EXPLODED);
            bw.newLine();
            bw.write("sonar.projectVersion=" + p.getProjectVersion() + EXPLODED);
            bw.newLine();
        }
        bw.newLine();
        bw.write("#  Default macros");
        bw.newLine();
        bw.write("sonar.cxx.defines=");
        for (int i = 0; i < p.getDefines().size(); i++) {
            if (p.getDefines().get(i).length() > 2 && p.getDefines().get(i).startsWith("-D")) { 
                bw.write(p.getDefines().get(i).substring(2));
                if (i < (p.getDefines().size() - 1)) {
                    bw.write(",");
                }
            }
        }
        bw.newLine();
        bw.newLine();
        bw.write("#  Source files information");
        bw.newLine();
        if (!p.getPreProcessingEnabled()) {
            bw.write("sonar.projectBaseDir=" + p.getBaseDirectory());
        } else {
            bw.write("sonar.projectBaseDir=" + p.getExplodedDirectory());
        }
        bw.newLine();
        bw.write("sonar.sources=");
        if (p.getSourceDirectories() == null || p.getSourceDirectories().size() == 0) {
            bw.write(".");
        } else {
            for (int i = 0; i < p.getSourceDirectories().size(); i++) {
                bw.write(p.getSourceDirectories().get(i));
                if (i < (p.getSourceDirectories().size() - 1)) {
                    bw.write(",");
                }
            }
        }
        bw.newLine();
        bw.write("sonar.cxx.includeDirectories=");
        for (int i = 0; i < p.getIncludeDirectories().size(); i++) {
            bw.write(p.getIncludeDirectories().get(i));
            if (i < (p.getIncludeDirectories().size() - 1)) {
                bw.write(",");
            }
        }
        bw.newLine();
        bw.newLine();
        bw.write("#  Report files information");
        bw.newLine();
        if (!p.getPreProcessingEnabled()) {
            bw.write("sonar.cxx.pclint.reportPaths=" + p.getBaseDirectory() + "/" + PCLINT_REP_PATH);
            bw.newLine();
            bw.write("sonar.cxx.cppcheck.reportPaths=" + p.getBaseDirectory() + "/" + CPPCHECK_REP_PATH);
            bw.newLine();
            bw.write("sonar.cxx.clangsa.reportPaths=" + p.getBaseDirectory() + "/" + CLANGSA_REP_PATH);
            bw.newLine();
            bw.write("sonar.cxx.clangtidy.reportPatsh=" + p.getBaseDirectory() + "/" + CLANGTIDY_REP_PATH);
            bw.newLine();
            bw.write("sonar.cxx.gcc.reportPaths=" + p.getBaseDirectory() + "/" + COMP_REP_PATH);
            bw.newLine();
            bw.write("sonar.cxx.infer.reportPaths=" + p.getBaseDirectory() + "/" + FBINFER_REP_PATH);
            bw.newLine();
            bw.write("sonar.python.bandit.reportPaths=" + p.getBaseDirectory() + "/" + BANDIT_REP_PATH);
            bw.newLine();
            bw.write("sonar.python.pylint.reportPaths=" + p.getBaseDirectory() + "/" + PYLINT_REP_PATH);
            bw.newLine();
            bw.write("sonar.python.flake8.reportPaths=" + p.getBaseDirectory() + "/" + FLAKE8_REP_PATH);
            bw.newLine();
        } else {
            bw.write("sonar.cxx.clangsa.reportPaths=" + p.getExplodedDirectory() + "/" + CLANGSA_REP_PATH);
            bw.newLine();
        }
        bw.newLine();
        bw.write("#  Language information");
        bw.newLine();
        bw.write("sonar.language=java,js,c++,c,py,python");
        bw.newLine();
        bw.write("sonar.cxx.file.suffixes=.h,.hpp,.hxx,.c,.cpp,.cxx");
        bw.newLine();
        bw.newLine();
        bw.write("#  Sonar URL and login");
        bw.newLine();
        bw.write("sonar.host.url=" + p.getSonarQubeUrl());
        bw.newLine();
        if ((p.getSonarQubeUserName()) != null && (!p.getSonarQubeUserName().equals(""))) {
            bw.write("sonar.login=" + p.getSonarQubeUserName());
            bw.newLine();
        }
        if ((p.getSonarQubeUserPassword()) != null && (!p.getSonarQubeUserPassword().equals(""))) {
            bw.write("sonar.password=" + p.getSonarQubeUserPassword());
            bw.newLine();
        }
        bw.newLine();
        bw.flush();
        bw.close();
        if (System.getProperty(Strings.OS_NAME).toLowerCase().startsWith(Strings.WINDOWS)) {
            if (!p.getPreProcessingEnabled()) {
                fileName = p.getBaseDirectory() + RUN_SONAR_WIN;
            } else {
                fileName = p.getExplodedDirectory() + RUN_SONAR_WIN;
            }
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("@echo off");
            bw.newLine();
            bw.write("REM  SonarScanner launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("REM  Generated by SAFacilitator at " + LocalDateTime.now().format(SAFacilitator.getFormatter()));
            bw.newLine();
            bw.write("REM  Copyright (C) " + LocalDate.now().getYear() + " - " + Strings.SPAZIOIT_WWW);
            bw.newLine();
            bw.newLine();
            if (!p.getPreProcessingEnabled()) {
                bw.write("cd " + p.getBaseDirectory());
            } else {
                bw.write("cd " + p.getExplodedDirectory());
            }
            bw.newLine();
            bw.write(p.getSonarScanner());
            bw.newLine();
        } else {
            if (!p.getPreProcessingEnabled()) {
                fileName = p.getBaseDirectory() + RUN_SONAR_UNX;
            } else {
                fileName = p.getExplodedDirectory() + RUN_SONAR_UNX;
            }
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("# !/bin/bash");
            bw.newLine();
            bw.write("#  SonarScanner launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("#  Generated by SAFacilitator at " + LocalDateTime.now().format(SAFacilitator.getFormatter()));
            bw.newLine();
            bw.write("#  Copyright (C) " + LocalDate.now().getYear() + " - " + Strings.SPAZIOIT_WWW);
            bw.newLine();
            bw.newLine();
            if (!p.getPreProcessingEnabled()) {
                bw.write("cd " + p.getBaseDirectory());
            } else {
                bw.write("cd " + p.getExplodedDirectory());
            }
            bw.newLine();
            bw.write(p.getSonarScanner());
            bw.newLine();
        }
        bw.newLine();
        bw.flush();
        bw.close();
        CommonFunctions.printLogMessage("SonarQube prepared.");
    }

    /**
     * @throws Exception in case of application error run SonarScanner
     */
    public void runSonarScanner() throws Exception {
        CommonFunctions.printLogMessage("Running SonarScanner...");
        Project p = safacilitator.getCurrentProject();
        if (p == null) {
            throw new Exception(Strings.CURRENT_PROJECT_IS_NULL);
        }
        String commandLine = null;
        if (System.getProperty(Strings.OS_NAME).toLowerCase().startsWith(Strings.WINDOWS)) {
            if (!p.getPreProcessingEnabled()) {
                commandLine = p.getBaseDirectory() + "/run_sonar.bat";
            } else {
                commandLine = p.getExplodedDirectory() + "/run_sonar.bat";
            }
        } else {
            if (!p.getPreProcessingEnabled()) {
                commandLine = Strings.BASH + p.getBaseDirectory() + RUN_SONAR_UNX;
            } else {
                commandLine = Strings.BASH + p.getExplodedDirectory() + RUN_SONAR_UNX;
            }
        }
        if (SAFacilitator.isGuiEnabled()) {
            MainFrame.getStopRunningMenuItem().setText("Stop SonarScanner");
            MainFrame.getStopRunningMenuItem().setDisable(false);
        }
        executor = new Executor(commandLine, "SonarScanner executed.", true);
        executor.setPriority(Thread.MAX_PRIORITY);
        executor.start();
        if (!SAFacilitator.isGuiEnabled()) {
            executor.join();
        }
    }

}
