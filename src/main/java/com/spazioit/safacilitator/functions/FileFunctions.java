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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.spazioit.safacilitator.model.Project;
import com.spazioit.safacilitator.SAFacilitator;
import com.spazioit.safacilitator.model.CcFile;
import com.spazioit.safacilitator.model.PrFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextArea;

import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Maurizio Martignano
 */
public class FileFunctions {

    SAFacilitator safacilitator = null;

    public FileFunctions(SAFacilitator safacilitator) {
        this.safacilitator = safacilitator;
    }

    /**
     *
     * @param fileName - the file to be read
     * @throws Exception in case of application error De-serialize
     * "compileCommands" from "fileName"
     */
    public void readCompileCommands(String fileName) throws Exception {
        FileInputStream file = new FileInputStream(fileName);
        ObjectMapper mapper = new ObjectMapper();
        CcFile[] ccFiles = mapper.readValue(file, CcFile[].class);
        file.close();
        List<CcFile> listCcFiles = new ArrayList<CcFile>();
        for (int i = 0; i < ccFiles.length; i++) {
            listCcFiles.add(ccFiles[i]);
        }
        if (safacilitator.getCurrentProject() == null) {
            throw new Exception("Current Project is null and cannot receive Compile Commands info!");
        }
        List<String> sourceFiles = new ArrayList<String>();
        List<String> sourceDirectories = new ArrayList<String>();
        List<String> includeDirectories = new ArrayList<String>();
        List<String> defines = new ArrayList<String>();
        List<String> additionalArguments = new ArrayList<String>();
        List<String> origBuilders = new ArrayList<String>();
        Project p = safacilitator.getCurrentProject();
        List<PrFile> pfFiles = new ArrayList<PrFile>();
        for (CcFile ccFile : listCcFiles) {
            PrFile pFile = new PrFile();
            String sDirectory = ccFile.getDirectory().replace("\\", "/").replace("c:", "C:");
            String sFile = ccFile.getFile().replace("\\", "/").replace("c:", "C:");
            File fsFile = new File(sDirectory + "/" + sFile);
            String normDirectory = fsFile.getParent().replace("\\", "/").replace("c:", "C:");
            if (normDirectory.equals(p.getBaseDirectory())) {
                normDirectory = ".";
            } else {
                if (normDirectory.startsWith(p.getBaseDirectory())) {
                    normDirectory = normDirectory.substring(p.getBaseDirectory().length() + 1);
                }
            }
            if (!sourceFiles.contains(sDirectory + "/" + sFile)) {
                sourceFiles.add(sDirectory + "/" + sFile);
            }
            pFile.setPfFileName(sDirectory + "/" + sFile);
            if (!sourceDirectories.contains(normDirectory)) {
                sourceDirectories.add(normDirectory);
            }
            if (!origBuilders.contains(ccFile.getArguments().get(0))) {
                origBuilders.add(ccFile.getArguments().get(0));
            }
            pFile.setPfOriginalBuilder(ccFile.getArguments().get(0));
            List<String> arguments = ccFile.getArguments();
            List<String> pfDefines = new ArrayList<String>();
            List<String> pfIncludes = new ArrayList<String>();
            List<String> pfAdditionalArguments = new ArrayList<String>();
            for (int i = 1; i < arguments.size(); i++) {
                if (arguments.get(i).startsWith("-D")
                        || arguments.get(i).startsWith("-U")) {
                    String define = arguments.get(i);
                    if (!defines.contains(define)) {
                        defines.add(define);
                    }
                    if (!pfDefines.contains(define)) {
                        pfDefines.add(define);
                    }
                }
                if (arguments.get(i).equals("-I")) {
                    String includeDirectory = arguments.get(++i);
                    if (!includeDirectories.contains(includeDirectory)) {
                        includeDirectories.add(includeDirectory);
                    }
                    if (!pfIncludes.contains(includeDirectory)) {
                        pfIncludes.add(includeDirectory);
                    }
                    continue;
                }
                if (arguments.get(i).startsWith("-I")) {
                    String includeDirectory = arguments.get(i).substring(2);
                    if (!includeDirectories.contains(includeDirectory)) {
                        includeDirectories.add(includeDirectory);
                    }
                    if (!pfIncludes.contains(includeDirectory)) {
                        pfIncludes.add(includeDirectory);
                    }
                }
                if ((!arguments.get(i).startsWith("-D"))
                        && (!arguments.get(i).startsWith("-U"))
                        && (!arguments.get(i).startsWith("-I"))) {
                    String additionalArgument = arguments.get(i);
                    if ((!additionalArgument.toLowerCase().contains(".o"))
                            && (!additionalArgument.toLowerCase().contains("-c"))
                            && (!additionalArgument.toLowerCase().contains("-o"))
                            && (!additionalArgument.toLowerCase().contains(".c"))
                            && (!additionalArgument.toLowerCase().contains(".cc"))
                            && (!additionalArgument.toLowerCase().contains(".cpp"))
                            && (!additionalArgument.toLowerCase().contains(".cxx"))
                            && (!additionalArgument.toLowerCase().contains(".h"))) {
                        if (!additionalArguments.contains(additionalArgument)) {
                            additionalArguments.add(additionalArgument);
                        }
                        if (!pfAdditionalArguments.contains(additionalArgument)) {
                            pfAdditionalArguments.add(additionalArgument);
                        }
                    }
                }
                pFile.setPfDefines(pfDefines);
                pFile.setPfIncludeDirectories(pfIncludes);
                pFile.setPfAdditionalArguments(pfAdditionalArguments);
            }
            pfFiles.add(pFile);
        }
        p.setpFiles(pfFiles);
        p.setSourceDirectories(sourceDirectories);
        p.setIncludeDirectories(includeDirectories);
        p.setDefines(defines);
        p.setAdditionalArguments(additionalArguments);
        p.setOrigBuilders(origBuilders);
        p.setCompileCommands(fileName.replace("\\", "/").replace("c:", "C:"));
    }

    /**
     *
     * @param fileName - the file to be read
     * @throws Exception in case of application error De-serialize
     * "currentProject" from "fileName"
     */
    public void readProject(String fileName) throws Exception {
        FileInputStream file = new FileInputStream(fileName);
        ObjectMapper mapper = new ObjectMapper();
        safacilitator.setCurrentProject(mapper.readValue(file, Project.class));
        file.close();
    }

    /**
     *
     * @param fileName - the file to be saved
     * @throws Exception in case of application error Serialize "currentProject"
     * into "fileName"
     */
    public void saveProject(String fileName) throws Exception {
        if (safacilitator.getCurrentProject() == null) {
            throw new Exception("Current Project is null and cannot be saved!");
        } else {
            FileOutputStream file = new FileOutputStream(fileName);
            PrintStream printer = new PrintStream(file);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String outputString = mapper.writeValueAsString(safacilitator.getCurrentProject());
            printer.println(outputString);
            printer.close();
            file.close();
        }
    }

    /**
     *
     * @param fileName - the file to be read
     * @throws Exception in case of application error Serialize Project into
     * "compileCommands" "fileName"
     */
    public void saveProjectAsCompileCommands(String fileName) throws Exception {
        if (safacilitator.getCurrentProject() == null) {
            throw new Exception("Current Project is null and cannot be saved!");
        } else {
            Project p = safacilitator.getCurrentProject();
            CcFile[] ccFiles = new CcFile[p.getpFiles().size()];
            for (int i = 0; i < ccFiles.length; i++) {
                ccFiles[i] = new CcFile();
                if (!p.getPreProcessingEnabled()) {
                    ccFiles[i].setDirectory(p.getBaseDirectory());
                    ccFiles[i].setFile(p.getpFiles().get(i).getPfFileName());
                } else {
                    ccFiles[i].setDirectory(p.getExplodedDirectory());
                    ccFiles[i].setFile(p.getpFiles().get(i).getPfFileName().replace(p.getBaseDirectory(), p.getExplodedDirectory()));
                }
                List<String> myArgs = new ArrayList<String>();
                myArgs.add(p.getpFiles().get(i).getPfOriginalBuilder());
                for (String arg : p.getpFiles().get(i).getPfDefines()) {
                    myArgs.add(arg);
                }
                for (String arg : p.getpFiles().get(i).getPfAdditionalArguments()) {
                    myArgs.add(arg);
                }
                for (String arg : p.getpFiles().get(i).getPfIncludeDirectories()) {
                    myArgs.add("-I" + arg);
                }
                myArgs.add("-c");
                myArgs.add(ccFiles[i].getFile());
                ccFiles[i].setArguments(myArgs);
            }
            FileOutputStream file = new FileOutputStream(fileName);
            PrintStream printer = new PrintStream(file);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String outputString = mapper.writeValueAsString(ccFiles);
            printer.println(outputString);
            printer.close();
            file.close();
        }
    }

    /**
     *
     * @param fileName - the file in which the text is going to be saved
     * @param textArea - the text to be saved into "fileName"
     * @throws Exception in case of application error Save "textArea" into
     * "fileName"
     */
    public void saveLog(String fileName, TextArea textArea) throws Exception {
        FileOutputStream file = new FileOutputStream(fileName);
        PrintStream printer = new PrintStream(file);
        String outputString = textArea.getText();
        printer.println(outputString);
        printer.close();
        file.close();
    }
}
