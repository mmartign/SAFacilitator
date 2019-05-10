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
package com.spazioit.safacilitator;

import com.spazioit.safacilitator.functions.AnalyzersFunctions;
import com.spazioit.safacilitator.model.Project;
import com.spazioit.safacilitator.functions.FileFunctions;
import com.spazioit.safacilitator.functions.PreprocessingFunctions;
import com.spazioit.safacilitator.functions.SonarQubeFunctions;
import com.spazioit.safacilitator.gui.MainFrame;
import java.util.Calendar;





/**
 *
 * @author Maurizio Martignano
 */
public class SAFacilitator {
    public Project currentProject = null;
    public static SAFacilitator myself = null;
    public static boolean guiEnabled = true;
    public static String version = "0.6";
    public static int MAX_LINES = 24576;
    public static String fileName = "";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        myself = new SAFacilitator();
        if (args.length == 0) {
            myself.startGui(myself, args);
        } else {
            guiEnabled = false;
            myself.processCommandLine(args);
        }
    }
    
    /**
     * Start GUI
     */
    void startGui(SAFacilitator saf, String[] args) {
        try {
            MainFrame mainFrame = new MainFrame();
            mainFrame.dynamicLaunch(args);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
    }
    
    /**
     * 
     * @param args the command line arguments
     * process command lines and instructs the executor
     */
    void processCommandLine(String[] args) {
        boolean checkArgument = true;
        FileFunctions fileFunctions = new FileFunctions(this);
        AnalyzersFunctions analyzersFunctions = new AnalyzersFunctions(this);
        SonarQubeFunctions sonarQubeFunctions = new SonarQubeFunctions(this);
        PreprocessingFunctions preproFunctions = new PreprocessingFunctions(this);
        for(int i = 0; i < args.length; i++) {
            checkArgument = true;
            if (args[i].equals("-ea")) {
                try {
                    analyzersFunctions.executeAnalyzers();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-ep")) {
                try {
                    preproFunctions.preProcess();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-h")) {
                System.out.println("\nStatic Analsys Facilitator - version " + version);
                System.out.println("Copyright (©) "  + Calendar.getInstance().get(Calendar.YEAR));
                System.out.println("Spazio IT - Soluzioni Informatiche s.a.s.");
                System.out.println("https://www.spazioit.com\n");
                System.out.println("Used technologies: Java FX, Jackson, JSON, CompileDB\n");
                System.out.println("Available commands:");
                System.out.println("-ea:               execute analyzers");
                System.out.println("-ep:               execute preprocessing");
                System.out.println("-h:                print this text");
                System.out.println("-l <file name>:    load project file");
                System.out.println("-lcc <file name>:  load compile_commands file into project");
                System.out.println("-pa:               prepare analyzers");
                System.out.println("-pp:               prepare preprocessing");
                System.out.println("-ppa:              post process analyzers");
                System.out.println("-psq:              prepare SonarQube");
                System.out.println("-rss:              run SonarScanner");
                System.out.println("-s <file name>:    save project file");
                System.out.println("-scc <file name>:  save project as compile_commands file");
                System.out.println("-scd:              show compiler defines\n");
                System.out.println("No argument starts the GUI\n");
            }
            if (args[i].equals("-l")) {
                if (i == (args.length - 1)) {
                    System.err.println("Wrong command line!");
                    System.err.println("Arg \"-l\" expects to be followed by a file name.");
                    System.exit(-1);
                }
                try {
                    fileFunctions.readProject(args[++i]);
                    checkArgument = false;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-lcc")) {
                if (i == (args.length - 1)) {
                    System.err.println("Wrong command line!");
                    System.err.println("Arg \"-lcc\" expects to be followed by a file name.");
                    System.exit(-1);
                }
                try {
                    fileFunctions.readCompileCommands(args[++i]);
                    checkArgument = false;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-pa")) {
                try {
                    analyzersFunctions.prepareAnalyzers();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-pp")) {
                try {
                    preproFunctions.preparePreprocess();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-ppa")) {
                try {
                    analyzersFunctions.postProcessAnalyzers();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-psq")) {
                try {
                    sonarQubeFunctions.prepareSonarQube();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-rss")) {
                try {
                    sonarQubeFunctions.runSonarScanner();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-s")) {
                if (i == (args.length - 1)) {
                    System.err.println("Wrong command line!");
                    System.err.println("Arg \"-s\" expects to be followed by a file name.");
                    System.exit(-1);
                }
                try {
                    fileFunctions.saveProject(args[++i]);
                    checkArgument = false;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-scc")) {
                if (i == (args.length - 1)) {
                    System.err.println("Wrong command line!");
                    System.err.println("Arg \"-scc\" expects to be followed by a file name.");
                    System.exit(-1);
                }
                try {
                    fileFunctions.saveProjectAsCompileCommands(args[++i]);
                    checkArgument = false;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-scd")) {
                try {
                    preproFunctions.showCompilerDefines();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if ((!args[i].equals("-ea")) &&
                (!args[i].equals("-ep")) &&
                (!args[i].equals("-h")) &&
                (!args[i].equals("-l")) &&
                (!args[i].equals("-lcc")) &&
                (!args[i].equals("-pa")) && 
                (!args[i].equals("-pp")) && 
                (!args[i].equals("-ppa")) &&
                (!args[i].equals("-psq")) &&
                (!args[i].equals("-rss")) &&
                (!args[i].equals("-s")) &&
                (!args[i].equals("-scc")) &&
                (!args[i].equals("-scd")) &&
                checkArgument) {
                System.err.println("Wrong command line!");
                System.err.println("Available commands:");                    
                System.err.println("-ea:               execute analyzers");
                System.err.println("-ep:               execute preprocessing");
                System.err.println("-h:                print this text");
                System.err.println("-l <file name>:    load project file");
                System.out.println("-lcc <file name>:  load compile_commands file into project");
                System.err.println("-pa:               prepare analyzers");
                System.err.println("-pp:               prepare preprocessing");
                System.err.println("-ppa:              post process analyzers");
                System.err.println("-psq:              prepare SonarQube");
                System.err.println("-rss:              run SonarScanner");
                System.err.println("-s <file name>:    save project file");
                System.err.println("-scc <file name>:  save project as compile_commands file");
                System.out.println("-scd:              show compiler defines\n");
                System.exit(-1);
            }
        }
    }                
}
