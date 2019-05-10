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
import com.spazioit.safacilitator.gui.MainFrame;
import com.spazioit.safacilitator.model.Project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Maurizio Martignano
 */
@SuppressWarnings("unchecked")
public class AnalyzersFunctions {
    
    private SAFacilitator safacilitator = null;
    private Executor executor = null;
    
    private String PCLINT_OUT_DIR="pclint-reports";
    private String PCLINT_OUT_REP_UNX="pclint-reports/pclint-result-01.xml";
    private String PCLINT_OUT_REP_WIN="pclint-reports\\pclint-result-01.xml";
    private String CPPCHECK_OUT_DIR="cppcheck-reports";
    private String CPPCHECK_OUT_REP_UNX="cppcheck-reports/cppcheck-result-01.xml";
    private String CPPCHECK_OUT_REP_WIN="cppcheck-reports\\cppcheck-result-01.xml";
    private String CLANGSA_OUT_DIR="clangsa";
    private String CLANGTIDY_OUT_DIR="clangtidy-reports";
    private String CLANGTIDY_OUT_REP_UNX="clangtidy-reports/clangtidy-result-01.txt";
    private String CLANGTIDY_OUT_REP_WIN="clangtidy-reports\\clangtidy-result-01.txt";
    private String COMP_OUT_DIR="comp-reports";
    private String COMP_OUT_REP_UNX="comp-reports/comp-result-01.txt";
    private String COMP_OUT_REP_WIN="comp-reports\\comp-result-01.txt";
    
    public AnalyzersFunctions(SAFacilitator safacilitator) {
        this.safacilitator = safacilitator;
    }
    
    /**
     * @throws Exception in case of application error
     * prepare the various analyzers
     */
    public void prepareAnalyzers() throws Exception {
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        List<String> myList = p.getActiveAnalyzers();
        if (myList == null || myList.isEmpty()) {
            throw new Exception("Current Active Analyzer List is empty!");
        }
        if (myList.contains("PC-Lint")) {
            if (!p.getPreProcessingEnabled()) {
                preparePcLint();
            }
        }
        if (myList.contains("Cppcheck")) {
            if (!p.getPreProcessingEnabled()) {
                prepareCppcheck();
            }
        }
        if (myList.contains("Clang-SA")) {
            prepareClangSa();
        }
        if (myList.contains("Clang-Tidy")) {
            if (!p.getPreProcessingEnabled()) {
                prepareClangTidy();
            }
        }
        if (myList.contains("COMP")) {
            if (!p.getPreProcessingEnabled()) {
                prepareComp();
            }
        }
        
        prepareCompilationDatabase();
    }
    
    /**
     * @throws Exception in case of application error
     * prepare Compilation Database
     */
    void prepareCompilationDatabase() throws Exception {
        Project p = safacilitator.currentProject;
        FileFunctions fileFunctions = new FileFunctions(safacilitator);
        CommonFunctions.printLogMessage("Preparing Compilation Database...");
        if (!p.getPreProcessingEnabled()) {
            fileFunctions.saveProjectAsCompileCommands(p.getBaseDirectory() + "/compile_commands.json");
        } else {
            fileFunctions.saveProjectAsCompileCommands(p.getExplodedDirectory() + "/compile_commands.json");
        }
        CommonFunctions.printLogMessage("Compilation Database prepared.");        
    }
        
    /**
     * @throws Exception in case of application error
     * prepare PC-Lint
     */
    void preparePcLint() throws Exception {
        CommonFunctions.printLogMessage("Preparing PC-Lint...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String fileName = p.getBaseDirectory() + "/" + p.getProjectName() + ".lnt";
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("//  PC-lint configuration file for project " + p.getProjectName());
        bw.newLine();
        bw.write("//  Generated by SAFacilitator at "  + Calendar.getInstance().getTime());
        bw.newLine();
        bw.write("//  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
        bw.newLine();
        bw.newLine();
        bw.write("//  Spazio IT recommendations and checks recommended by industry bodies");
        bw.newLine();
        bw.write("au-barr10.lnt"); 
        bw.newLine();
        bw.write("au-misra2.lnt"); 
        bw.newLine();
        bw.write("au-misra3.lnt"); 
        bw.newLine();
        bw.write("au-sm123.lnt"); 
        bw.newLine();
        bw.newLine();
        bw.write("// Compiler configuration");
        bw.newLine();
        bw.write("co-gcc.lnt");
        bw.newLine();
        bw.newLine();        
        bw.write("//  PC-lint warning level (0 through 4)");
        bw.newLine();
        bw.write("-w3");                             
        bw.newLine();
        bw.newLine();
        bw.write("//  Spazio IT preferred PC-lint options");
        bw.newLine();
        bw.write("-e309 -e98 -e322 -e952 -e953 -e956 +fqb +e970");
        bw.newLine();
        bw.newLine();
        bw.write("//  Do not stop on error");
        bw.newLine();
        bw.write("+fce");
        bw.newLine();
        bw.newLine();
        bw.write("//  Defines");
        bw.newLine();
        for (int i = 0; i < p.getDefines().size(); i++) {
            bw.write(p.getDefines().get(i));                
            bw.newLine();
        }
        bw.newLine();
        bw.write("//  Include Directories Files");
        bw.newLine();
        for (int i = 0; i < p.getIncludeDirectories().size(); i++) {
            if (p.getIncludeDirectories().get(i).startsWith("/") || 
                p.getIncludeDirectories().get(i).startsWith("C:") ||
                p.getIncludeDirectories().get(i).startsWith("c:")) {
                bw.write("-I" + p.getIncludeDirectories().get(i));
            } else {
                bw.write("-I" + p.getBaseDirectory() + "/" + p.getIncludeDirectories().get(i));                
            }
            bw.newLine();
        }
        bw.newLine();
        bw.write("//  Generate XML");
        bw.newLine();
        bw.write("env-xml.lnt");
        bw.newLine();
        bw.newLine();
        bw.write("//  Source Files");
        bw.newLine();
        for (int i = 0; i < p.getpFiles().size(); i++) {
            bw.write(p.getpFiles().get(i).getPfFileName());
            bw.newLine();
        }
        bw.newLine();
        bw.flush();
        bw.close();
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            fileName = p.getBaseDirectory() + "/run_pclint.bat";            
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("@echo off");
            bw.newLine();
            bw.write("REM  PC-Lint launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("REM  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("REM  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("C:\\lint\\lint-nt.exe -IC:\\lint\\scripts " + p.getProjectName() + 
                     ".lnt > " +  PCLINT_OUT_REP_WIN);
            bw.newLine();
        } else {
            fileName = p.getBaseDirectory() + "/run_pclint.sh";            
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("# !/bin/bash");
            bw.newLine();
            bw.write("#  PC-Lint launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("#  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("#  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("wine /opt/lint/lint-nt.exe -I/opt/lint/scripts " + p.getProjectName() + 
                     ".lnt > " +  PCLINT_OUT_REP_UNX);
            bw.newLine();
        }
        bw.newLine();
        bw.flush();
        bw.close();
        CommonFunctions.printLogMessage("PC-Lint prepared.");
    }
    
    /**
     * @throws Exception in case of application error
     * prepare Cppcheck
     */
    void prepareCppcheck() throws Exception {
        CommonFunctions.printLogMessage("Preparing Cppcheck...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String fileName = p.getBaseDirectory() + "/" + p.getProjectName() + ".cppcheck";
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write("<!-- Cppcheck configuration file for project " + p.getProjectName() + " -->");
        bw.newLine();
        bw.write("<!-- Generated by SAFacilitator at "  + Calendar.getInstance().getTime() + " -->"); 
        bw.newLine();
        bw.write("<!-- Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com -->");
        bw.newLine();
        bw.write("<project version=\"1\">");
        bw.newLine();
        bw.write("    <builddir>" + p.getProjectName() + "-cppcheck-build-dir</builddir>"); 
        bw.newLine();
        bw.write("    <platform>unix64</platform>"); 
        bw.newLine();
        bw.write("    <analyze-all-vs-configs>true</analyze-all-vs-configs>"); 
        bw.newLine();
        bw.write("    <includedir>"); 
        bw.newLine();
        for (int i = 0; i < p.getIncludeDirectories().size(); i++) {
            bw.write("        <dir name=\"" + p.getIncludeDirectories().get(i) + "\"/>");              
            bw.newLine();
        }
        bw.write("    </includedir>"); 
        bw.newLine();
        List<String> myDef = new ArrayList<String>();
        List<String> myUndef = new ArrayList<String>();
        for (String define : p.getDefines()) {
            if (define.startsWith("-D")) {
                myDef.add(define.substring(2));
            } else {
                myUndef.add(define.substring(2));
            }
        }
        bw.write("    <defines>");
        bw.newLine();
        if (myDef.size() > 0) {
            bw.write("<define name=\"");
            for (int i = 0; i < myDef.size(); i++) {
                bw.write(myDef.get(i));
                if (i != (myDef.size() - 1)) {
                    bw.write(",");
                }
            }
            bw.write("\"/>");
        }
        bw.newLine();
        bw.write("    </defines>");
        bw.newLine();        
        bw.write("    <undefines>");
        bw.newLine();
        if (myUndef.size() > 0) {
            bw.write("<undefine name=\"");
            for (int i = 0; i < myUndef.size(); i++) {
                bw.write(myUndef.get(i));
                if (i != (myUndef.size() - 1)) {
                    bw.write(",");
                }
            }
            bw.write("\"/>");
        }
        bw.write("    </undefines>");
        bw.newLine();
        bw.write("    <paths>");
        bw.newLine();
        for (int i = 0; i < p.getSourceDirectories().size(); i++) {
            bw.write("        <dir name=\"" + p.getBaseDirectory() + "/" + p.getSourceDirectories().get(i) + "\"/>");
            bw.newLine();                    
        }
        bw.write("    </paths>");
        bw.newLine();        
        bw.write("    <libraries>");
        bw.newLine();        
        bw.write("        <library>gnu</library>");
        bw.newLine();        
        bw.write("        <library>posix</library>");
        bw.newLine();        
        bw.write("    </libraries>");
        bw.newLine();        
        bw.write("    <addons>");
        bw.newLine();        
        bw.write("        <addon>threadsafety</addon>");
        bw.newLine();        
        bw.write("        <addon>y2038</addon>");
        bw.newLine();        
        bw.write("        <addon>cert</addon>");
        bw.newLine();        
        bw.write("    </addons>");
        bw.newLine();        
        bw.write("</project>");
        bw.newLine();
        bw.newLine();        
        bw.flush();
        bw.close();
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            fileName = p.getBaseDirectory() + "/run_cppcheck.bat";            
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("@echo off");
            bw.newLine();
            bw.write("REM  Cppcheck launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("REM  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("REM  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("\"C:\\Program Files\\Cppcheck\\cppcheck.exe\" --project=" + p.getProjectName() + 
                     ".cppcheck --enable=all --xml --quiet --output-file="  +  CPPCHECK_OUT_REP_WIN);
            bw.newLine();
        } else {
            fileName = p.getBaseDirectory() + "/run_cppcheck.sh";            
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("# !/bin/bash");
            bw.newLine();
            bw.write("#  Cppcheck launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("#  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("#  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("cppcheck --project=" + p.getProjectName() + 
                     ".cppcheck --enable=all --xml --quiet --output-file="  +  CPPCHECK_OUT_REP_UNX);
            bw.newLine();
        }
        bw.newLine();
        bw.flush();
        bw.close();
        CommonFunctions.printLogMessage("Cppcheck prepared.");
    }

    /**
     * @throws Exception in case of application error
     * prepare Clang-SA
     */
    void prepareClangSa() throws Exception {
        CommonFunctions.printLogMessage("Preparing Clang-SA...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            String fileName = null;
            if (!p.getPreProcessingEnabled()) {
                fileName = p.getBaseDirectory() + "/" + p.getProjectName() + "_clang-sa.bat";
            } else {
                fileName = p.getExplodedDirectory() + "/" + p.getProjectName() + "_clang-sa.bat";                
            }
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("@echo off");
            bw.newLine();
            bw.write("REM  Clang-SA launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("REM  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("REM  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            if (!p.getPreProcessingEnabled()) {
                bw.write("cd " + p.getBaseDirectory());
            } else {
                bw.write("cd " + p.getExplodedDirectory());
            }
            bw.newLine();
            bw.write("echo Clang-SA running > Clang-SA.running");
            bw.newLine();
            for (int i = 0; i < p.getpFiles().size(); i++) {
                bw.write("if exist Clang-SA.running (");
                bw.newLine();
                bw.write("clang-check -analyze ");
                if (!p.getPreProcessingEnabled()) {
                    bw.write(p.getpFiles().get(i).getPfFileName() + " ");
                } else {
                    bw.write(p.getpFiles().get(i).getPfFileName().replace(p.getBaseDirectory(), p.getExplodedDirectory()) + " ");
                }
                for (int j = 0; j < p.getpFiles().get(i).getPfDefines().size(); j++) {
                    bw.write("-extra-arg-before=\"" + p.getpFiles().get(i).getPfDefines().get(j) + "\"");
                    bw.write(" ");
                }
                bw.write("-extra-arg-before=\"-ferror-limit=0\" ");                
                for (int j = 0; j < p.getpFiles().get(i).getPfIncludeDirectories().size(); j++) {
                    if (p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("/") || 
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("C:") ||
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("c:")) {
                        bw.write("-extra-arg-before=\"-I" + p.getpFiles().get(i).getPfIncludeDirectories().get(j) + "\"");
                    } else {
                        bw.write("-extra-arg-before=\"-I" + p.getBaseDirectory() + "/" + p.getpFiles().get(i).getPfIncludeDirectories().get(j) + "\"");                
                    }
                    bw.write(" ");
                }
                bw.newLine();
                bw.write(")");
                bw.newLine();
            }
            bw.write("del /F /Q Clang-SA.running");
            bw.newLine();
            bw.newLine();
            bw.flush();
            bw.close();
        } else {
            String fileName = null;
            if (!p.getPreProcessingEnabled()) {
                fileName = p.getBaseDirectory() + "/" + p.getProjectName() + "_clang-sa.sh";
            } else {
                fileName = p.getExplodedDirectory() + "/" + p.getProjectName() + "_clang-sa.sh";                
            }
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("# !/bin/bash");
            bw.newLine();
            bw.write("#  Clang-SA launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("#  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("#  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            if (!p.getPreProcessingEnabled()) {
                bw.write("cd " + p.getBaseDirectory());
            } else {
                bw.write("cd " + p.getExplodedDirectory());
            }
            bw.newLine();
            bw.write("echo Clang-SA running > Clang-SA.running");
            bw.newLine();
            for (int i = 0; i < p.getpFiles().size(); i++) {
                bw.write("if [ -f Clang-SA.running ]; then");
                bw.newLine();
                bw.write("clang-check -analyze ");
                if (!p.getPreProcessingEnabled()) {
                    bw.write(p.getpFiles().get(i).getPfFileName() + " ");
                } else {
                    bw.write(p.getpFiles().get(i).getPfFileName().replace(p.getBaseDirectory(), p.getExplodedDirectory()) + " ");
                }
                for (int j = 0; j < p.getpFiles().get(i).getPfDefines().size(); j++) {
                    bw.write("-extra-arg-before=\"" + p.getpFiles().get(i).getPfDefines().get(j) + "\"");
                    bw.write(" ");
                }
                bw.write("-extra-arg-before=\"-ferror-limit=0\" ");                
                for (int j = 0; j < p.getpFiles().get(i).getPfIncludeDirectories().size(); j++) {
                    if (p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("/") || 
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("C:") ||
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("c:")) {
                        bw.write("-extra-arg-before=\"-I" + p.getpFiles().get(i).getPfIncludeDirectories().get(j) + "\"");
                    } else {
                        bw.write("-extra-arg-before=\"-I" + p.getBaseDirectory() + "/" + p.getpFiles().get(i).getPfIncludeDirectories().get(j) + "\"");                
                    }
                    bw.write(" ");
                }
                bw.newLine();
                bw.write("fi");
                bw.newLine();
            }
            bw.write("rm -f Clang-SA.running");
            bw.newLine();
            bw.newLine();
            bw.flush();
            bw.close();           
        }
        CommonFunctions.printLogMessage("Clang-SA prepared.");
    }

    /**
     * @throws Exception in case of application error
     * prepare Clang-Tidy
     */
    void prepareClangTidy() throws Exception {
        CommonFunctions.printLogMessage("Preparing Clang-Tidy...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            String fileName = p.getBaseDirectory() + "/" + p.getProjectName() + "_clang-tidy.bat";
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("@echo off");
            bw.newLine();
            bw.write("REM  Clang-Tidy launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("REM  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("REM  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("del /F /Q " + CLANGTIDY_OUT_REP_WIN);
            bw.newLine();
            bw.write("echo Clang-Tidy running > Clang-Tidy.running");
            bw.newLine();
            for (int i = 0; i < p.getpFiles().size(); i++) {
                bw.write("if exist Clang-Tidy.running (");
                bw.newLine();
                bw.write("clang-tidy -checks=*,cert-*,clang-analyzer-*,cppcoreguidelines-*,hicpp-* ");
                bw.write(p.getpFiles().get(i).getPfFileName() + " -- ");
                for (int j = 0; j < p.getpFiles().get(i).getPfDefines().size(); j++) {
                    bw.write(p.getpFiles().get(i).getPfDefines().get(j));
                    bw.write(" ");
                }
                bw.write("-ferror-limit=0 ");                
                for (int j = 0; j < p.getpFiles().get(i).getPfIncludeDirectories().size(); j++) {
                    if (p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("/") || 
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("C:") ||
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("c:")) {
                        bw.write("-I" + p.getpFiles().get(i).getPfIncludeDirectories().get(j));
                    } else {
                        bw.write("-I" + p.getBaseDirectory() + "/" + p.getpFiles().get(i).getPfIncludeDirectories().get(j));                
                    }
                    bw.write(" ");
                }
                bw.write(">> " + CLANGTIDY_OUT_REP_WIN);
                bw.newLine();
                bw.write(")");
                bw.newLine();
            }
            bw.write("del /F /Q Clang-Tidy.running");
            bw.newLine();
            bw.newLine();
            bw.flush();
            bw.close();
        } else {
            String fileName = p.getBaseDirectory() + "/" + p.getProjectName() + "_clang-tidy.sh";
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("# !/bin/bash");
            bw.newLine();
            bw.write("#  Clang-Tidy launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("#  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("#  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("rm -f " + CLANGTIDY_OUT_REP_UNX);
            bw.newLine();
            bw.write("echo Clang-Tidy running > Clang-Tidy.running");
            bw.newLine();
            for (int i = 0; i < p.getpFiles().size(); i++) {
                bw.write("if [ -f Clang-Tidy.running ]; then");
                bw.newLine();
                bw.write("clang-tidy -checks=*,cert-*,clang-analyzer-*,cppcoreguidelines-*,hicpp-* ");
                bw.write(p.getpFiles().get(i).getPfFileName() + " -- ");
                for (int j = 0; j < p.getpFiles().get(i).getPfDefines().size(); j++) {
                    bw.write(p.getpFiles().get(i).getPfDefines().get(j));
                    bw.write(" ");
                }
                bw.write("-ferror-limit=0 ");                
                for (int j = 0; j < p.getpFiles().get(i).getPfIncludeDirectories().size(); j++) {
                    if (p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("/") || 
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("C:") ||
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("c:")) {
                        bw.write("-I" + p.getpFiles().get(i).getPfIncludeDirectories().get(j));
                    } else {
                        bw.write("-I" + p.getBaseDirectory() + "/" + p.getpFiles().get(i).getPfIncludeDirectories().get(j));                
                    }
                    bw.write(" ");
                }
                bw.write(">> " + CLANGTIDY_OUT_REP_UNX);
                bw.newLine();
                bw.write("fi");
                bw.newLine();
            }
            bw.write("rm -f Clang-Tidy.running");
            bw.newLine();
            bw.newLine();
            bw.flush();
            bw.close();            
        }
        CommonFunctions.printLogMessage("Clang-Tidy prepared.");
    }

    /**
     * @throws Exception in case of application error
     * prepare COMP
     */
    void prepareComp() throws Exception {
        CommonFunctions.printLogMessage("Preparing COMP...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            String fileName = p.getBaseDirectory() + "/" + p.getProjectName() + "_comp.bat";
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("@echo off");
            bw.newLine();
            bw.write("REM  COMP launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("REM  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("REM  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("del /F /Q " + COMP_OUT_REP_WIN);
            bw.newLine();
            bw.write("echo COMP running > COMP.running");
            bw.newLine();
            for (int i = 0; i < p.getpFiles().size(); i++) {
                bw.write("if exist COMP.running (");
                bw.newLine();
                bw.write(p.getpFiles().get(i).getPfOriginalBuilder() + " ");
                for (int j = 0; j < p.getpFiles().get(i).getPfDefines().size(); j++) {
                    bw.write(p.getpFiles().get(i).getPfDefines().get(j));
                    bw.write(" ");
                }
                for (int j = 0; j < p.getpFiles().get(i).getPfAdditionalArguments().size(); j++) {
                    bw.write(p.getpFiles().get(i).getPfAdditionalArguments().get(j));
                    bw.write(" ");
                }
                for (int j = 0; j < p.getpFiles().get(i).getPfIncludeDirectories().size(); j++) {
                    if (p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("/") || 
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("C:") ||
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("c:")) {
                        bw.write("-I" + p.getpFiles().get(i).getPfIncludeDirectories().get(j));
                    } else {
                        bw.write("-I" + p.getBaseDirectory() + "/" + p.getpFiles().get(i).getPfIncludeDirectories().get(j));                
                    }
                    bw.write(" ");
                }
                bw.write(" -c " + p.getpFiles().get(i).getPfFileName() + " >> " + COMP_OUT_REP_WIN + " 2>&1");
                bw.newLine();
                bw.write(")");
                bw.newLine();
            }
            bw.write("del /F /Q COMP.running");
            bw.newLine();
            bw.newLine();
            bw.flush();
            bw.close();
        } else {
            String fileName = p.getBaseDirectory() + "/" + p.getProjectName() + "_comp.sh";
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("# !/bin/bash");
            bw.newLine();
            bw.write("#  COMP launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("#  Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.write("#  Copyright (C) " + Calendar.getInstance().get(Calendar.YEAR) +  " - Spazio IT - https://www.spazioit.com");
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("rm -f " + COMP_OUT_REP_UNX);
            bw.newLine();
            bw.write("echo COMP running > COMP.running");
            bw.newLine();
            for (int i = 0; i < p.getpFiles().size(); i++) {
                bw.write("if [ -f COMP.running ]; then");
                bw.newLine();
                bw.write(p.getpFiles().get(i).getPfOriginalBuilder() + " ");
                for (int j = 0; j < p.getpFiles().get(i).getPfDefines().size(); j++) {
                    bw.write(p.getpFiles().get(i).getPfDefines().get(j));
                    bw.write(" ");
                }
                for (int j = 0; j < p.getpFiles().get(i).getPfAdditionalArguments().size(); j++) {
                    bw.write(p.getpFiles().get(i).getPfAdditionalArguments().get(j));
                    bw.write(" ");
                }
                for (int j = 0; j < p.getpFiles().get(i).getPfIncludeDirectories().size(); j++) {
                    if (p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("/") || 
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("C:") ||
                        p.getpFiles().get(i).getPfIncludeDirectories().get(j).startsWith("c:")) {
                        bw.write("-I" + p.getpFiles().get(i).getPfIncludeDirectories().get(j));
                    } else {
                        bw.write("-I" + p.getBaseDirectory() + "/" + p.getpFiles().get(i).getPfIncludeDirectories().get(j));                
                    }
                    bw.write(" ");
                }
                bw.write(" -c " + p.getpFiles().get(i).getPfFileName() + " >> " + COMP_OUT_REP_UNX + " 2>&1");
                bw.newLine();
                bw.write("fi");
                bw.newLine();
            }
            bw.write("rm -f COMP.running");
            bw.newLine();
            bw.newLine();
            bw.flush();
            bw.close();            
        }
        CommonFunctions.printLogMessage("COMP prepared.");
    }    

    /**
     * @throws Exception in case of application error
     * execute the various analyzers
     */
    public void executeAnalyzers() throws Exception {
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        List<String> myList = p.getActiveAnalyzers();
        if (myList == null || myList.isEmpty()) {
            throw new Exception("Current Active Analyzer List is empty!");
        }
        if ((SAFacilitator.guiEnabled) && (myList.size() != 1) && (!p.getPreProcessingEnabled())) {
            throw new Exception("Only one analyzer at a time can be launched from the GUI!");            
        }
        if (myList.contains("PC-Lint")) {
            if (!p.getPreProcessingEnabled()) {
                executePcLint();
            }
        }
        if (myList.contains("Cppcheck")) {
            if (!p.getPreProcessingEnabled()) {
                executeCppcheck();
            }
        }
        if (myList.contains("Clang-SA")) {
            executeClangSa();
        }
        if (myList.contains("Clang-Tidy")) {
            if (!p.getPreProcessingEnabled()) {
                executeClangTidy();
            }
        }
        if (myList.contains("COMP")) {
            if (!p.getPreProcessingEnabled()) {
                executeComp();
            }
        }
    }
        
    /**
     * @throws Exception in case of application error
     * execute PC-Lint
     */
    void executePcLint() throws Exception {
        CommonFunctions.printLogMessage("Executing PC-Lint...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String commandLine = null;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            commandLine = p.getBaseDirectory() + "/run_pclint.bat";
        } else {
            commandLine = "bash " + p.getBaseDirectory() + "/run_pclint.sh";
            
        }
        FileUtils.forceMkdir(new File(p.getBaseDirectory() + "/" + PCLINT_OUT_DIR));
        if (SAFacilitator.guiEnabled) {
            MainFrame.stopRunningMenuItem.setText("Stop PC-lint");
            MainFrame.stopRunningMenuItem.setDisable(false);
        }
        executor = new Executor(commandLine, "PC-Lint executed.", true);
        executor.setPriority(Thread.MAX_PRIORITY);
        executor.start();
        if (!SAFacilitator.guiEnabled) {
            executor.join();
        }
    }
    
    /**
     * @throws Exception in case of application error
     * prepare Cppcheck
     */
    void executeCppcheck() throws Exception {
        CommonFunctions.printLogMessage("Executing Cppcheck...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String commandLine = null;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            commandLine = p.getBaseDirectory() + "/run_cppcheck.bat";
        } else {
            commandLine = "bash " + p.getBaseDirectory() + "/run_cppcheck.sh";
        }
        FileUtils.forceMkdir(new File(p.getBaseDirectory() + "/" + CPPCHECK_OUT_DIR));
        if (SAFacilitator.guiEnabled) {
            MainFrame.stopRunningMenuItem.setText("Stop Cppcheck");
            MainFrame.stopRunningMenuItem.setDisable(false);
        }
        executor = new Executor(commandLine, "Cppcheck executed.", true);
        executor.setPriority(Thread.MAX_PRIORITY);
        executor.start();
        if (!SAFacilitator.guiEnabled) {
            executor.join();
        }
    }

    /**
     * @throws Exception in case of application error
     * execute Clang-SA
     */
    void executeClangSa() throws Exception {
        CommonFunctions.printLogMessage("Executing Clang-SA...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String commandLine = null;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            if (!p.getPreProcessingEnabled()) {
                commandLine = p.getBaseDirectory() + "/" + p.getProjectName() + "_clang-sa.bat";
            } else {
                commandLine = p.getExplodedDirectory() + "/" + p.getProjectName() + "_clang-sa.bat";               
            }
        } else {
            if (!p.getPreProcessingEnabled()) {
                commandLine = "bash " + p.getBaseDirectory() + "/" + p.getProjectName() + "_clang-sa.sh";
            } else {
                commandLine = "bash " + p.getExplodedDirectory() + "/" + p.getProjectName() + "_clang-sa.sh";
                
            }
        }
        if (SAFacilitator.guiEnabled) {
            MainFrame.stopRunningMenuItem.setText("Stop Clang-SA");
            MainFrame.stopRunningMenuItem.setDisable(false);
        }
        executor = new Executor(commandLine, "Clang-SA executed.", true);
        executor.setPriority(Thread.MAX_PRIORITY);
        executor.start();
        if (!SAFacilitator.guiEnabled) {
            executor.join();
        }
    }

    /**
     * @throws Exception in case of application error
     * execute Clang-Tidy
     */
    void executeClangTidy() throws Exception {
        CommonFunctions.printLogMessage("Executing Clang-Tidy...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String commandLine = null;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            commandLine = p.getBaseDirectory() + "/" + p.getProjectName() + "_clang-tidy.bat";
        } else {
            commandLine = "bash " + p.getBaseDirectory() + "/" + p.getProjectName() + "_clang-tidy.sh";
        }
        FileUtils.forceMkdir(new File(p.getBaseDirectory() + "/" + CLANGTIDY_OUT_DIR));
        FileUtils.deleteQuietly(new File(p.getBaseDirectory() + "/" + CLANGTIDY_OUT_REP_UNX));
        if (SAFacilitator.guiEnabled) {
            MainFrame.stopRunningMenuItem.setText("Stop Clang-Tidy");
            MainFrame.stopRunningMenuItem.setDisable(false);
        }
        executor = new Executor(commandLine, "Clang-Tidy executed.", true);
        executor.setPriority(Thread.MAX_PRIORITY);
        executor.start();
        if (!SAFacilitator.guiEnabled) {
            executor.join();
        }
    }

    /**
     * @throws Exception in case of application error
     * execute COMP
     */
    void executeComp() throws Exception {
        CommonFunctions.printLogMessage("Executing COMP...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String commandLine = null;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            commandLine = p.getBaseDirectory() + "/" + p.getProjectName() + "_comp.bat";
        } else {
            commandLine = "bash " + p.getBaseDirectory() + "/" + p.getProjectName() + "_comp.sh";
        }
        FileUtils.forceMkdir(new File(p.getBaseDirectory() + "/" + COMP_OUT_DIR));
        FileUtils.deleteQuietly(new File(p.getBaseDirectory() + "/" + COMP_OUT_REP_UNX));
        if (SAFacilitator.guiEnabled) {
            MainFrame.stopRunningMenuItem.setText("Stop COMP");
            MainFrame.stopRunningMenuItem.setDisable(false);
        }
        executor = new Executor(commandLine, "COMP executed.", true);
        executor.setPriority(Thread.MAX_PRIORITY);
        executor.start();
        if (!SAFacilitator.guiEnabled) {
            executor.join();
        }
    }    

    /**
     * @throws Exception in case of application error
     * post-process the various analyzers
     */
    public void postProcessAnalyzers() throws Exception {
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        List<String> myList = p.getActiveAnalyzers();
        if (myList == null || myList.isEmpty()) {
            throw new Exception("Current Active Analyzer List is empty!");
        }
        if (myList.contains("PC-Lint")) {
            if (!p.getPreProcessingEnabled()) {
                postProcessPcLint();
            }
        }
        if (myList.contains("Cppcheck")) {
            if (!p.getPreProcessingEnabled()) {
                postProcessCppcheck();
            }
        }
        if (myList.contains("Clang-SA")) {
            postProcessClangSa();
        }
        if (myList.contains("Clang-Tidy")) {
            if (!p.getPreProcessingEnabled()) {
                postProcessClangTidy();
            }
        }
        if (myList.contains("COMP")) {
            if (!p.getPreProcessingEnabled()) {
                postProcessComp();
            }
        }
    }
        
    /**
     * @throws Exception in case of application error
     * post-process PC-Lint
     */
    void postProcessPcLint() throws Exception {
        CommonFunctions.printLogMessage("Post processing PC-Lint...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        if (!System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            String fileName = p.getBaseDirectory() + "/vi.inp";
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(":1,$s/\\\\/\\//g");
            bw.newLine();
            bw.write(":wq");
            bw.newLine();
            bw.flush();
            bw.close();
            fileName = p.getBaseDirectory() + "/vi_cmd.sh";
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("# !/bin/bash");
            bw.newLine();
            bw.write("# VI launch script for project " + p.getProjectName());
            bw.newLine();
            bw.write("# Generated by SAFacilitator at "  + Calendar.getInstance().getTime()); 
            bw.newLine();
            bw.newLine();
            bw.write("cd " + p.getBaseDirectory());
            bw.newLine();
            bw.write("dos2unix " + PCLINT_OUT_REP_UNX);
            bw.newLine();
            bw.write("vi " + PCLINT_OUT_REP_UNX + " < vi.inp");
            bw.newLine();
            bw.flush();
            bw.close();
            String commandLine = "bash " + p.getBaseDirectory() + "/vi_cmd.sh";
            if (SAFacilitator.guiEnabled) {
                MainFrame.stopRunningMenuItem.setText("Stop Vi");
                MainFrame.stopRunningMenuItem.setDisable(false);
            }
            executor = new Executor(commandLine, "Vi executed", true);
            executor.setPriority(Thread.MAX_PRIORITY);
            executor.start();
            if (!SAFacilitator.guiEnabled) {
                executor.join();
            }
        }
        CommonFunctions.printLogMessage("PC-Lint post processed.");
    }
    
    /**
     * @throws Exception in case of application error
     * post-process Cppcheck
     */
    void postProcessCppcheck() throws Exception {
        CommonFunctions.printLogMessage("Post processing Cppcheck...");
        // Nothing to be done
        CommonFunctions.printLogMessage("Cppcheck post processed.");
    }

    /**
     * @throws Exception in case of application error
     * post-process Clang-SA
     */
    void postProcessClangSa() throws Exception {
        CommonFunctions.printLogMessage("Post processing Clang-SA...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String extensions[] = new String[1];
        extensions[0] = "plist";
        File baseDirectory = null;
        if (!p.getPreProcessingEnabled()) {
            baseDirectory = new File(p.getBaseDirectory());
        } else {
            baseDirectory = new File(p.getExplodedDirectory());            
        }
        File directory = null;
        if (!p.getPreProcessingEnabled()) {
            directory = new File(p.getBaseDirectory() + "/" + CLANGSA_OUT_DIR);
        } else {
            directory = new File(p.getExplodedDirectory() + "/" + CLANGSA_OUT_DIR);            
        }
        FileUtils.forceMkdir(directory);
        for (File file: FileUtils.listFiles(directory, extensions, true)) {
            FileUtils.deleteQuietly(file);
        }
        for (File file: FileUtils.listFiles(baseDirectory, extensions, false)) {
            FileUtils.moveFileToDirectory(file, directory, false);
        }
        /*
        if (p.getPreProcessingEnabled()) {
            for (File file: FileUtils.listFiles(directory, extensions, false)) {
                replaceString(p.getExplodedDirectory(), p.getBaseDirectory(), file, 
                              new File(file.getCanonicalPath().replace(p.getExplodedDirectory(), p.getBaseDirectory())));
            }
        }
        */
        CommonFunctions.printLogMessage("Clang-SA post processed.");
    }
    
    public void replaceString(String oldstring, String newstring, File in, File out) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(in));
        PrintWriter writer = new PrintWriter(new FileWriter(out));
        String line = null;
        while ((line = reader.readLine()) != null) {
            writer.println(line.replace(oldstring, newstring));
        }
        reader.close();
        writer.close();
    }

    /**
     * @throws Exception in case of application error
     * post-process Clang-Tidy
     */
    void postProcessClangTidy() throws Exception {
        CommonFunctions.printLogMessage("Post processing Clang-Tidy...");
        // Nothing to be done
        CommonFunctions.printLogMessage("Clang-Tidy post processed.");
    }

    /**
     * @throws Exception in case of application error
     * post-process COMP
     */
    void postProcessComp() throws Exception {
        CommonFunctions.printLogMessage("Post processing COMP...");
        Project p = safacilitator.currentProject;
        if (p == null) {
            throw new Exception("Current Project is null. Initialize it first!");
        }
        String extensions[] = new String[2];
        extensions[0] = "o";
        extensions[1] = "obj";
        File baseDirectory = new File(p.getBaseDirectory());
        for (File file: FileUtils.listFiles(baseDirectory, extensions, true)) {
            FileUtils.deleteQuietly(file);
        }
        CommonFunctions.printLogMessage("COMP post processed.");
    }    
}
