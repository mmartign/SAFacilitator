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
import com.spazioit.safacilitator.functions.JavaFunctions;
import com.spazioit.safacilitator.functions.FbInferFunctions;
import com.spazioit.safacilitator.functions.PythonFunctions;
import com.spazioit.safacilitator.functions.PreprocessingFunctions;
import com.spazioit.safacilitator.functions.SonarQubeFunctions;
import com.spazioit.safacilitator.gui.MainFrame;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;





/**
 *
 * @author Maurizio Martignano
 */
public class SAFacilitator {

    public enum MisraVersion {MisC1998, MisC2004, MisC2012, MisCpp2008, MisC0412};
    public enum PCLintType {PCLint, PCLintPlus};
    private Project currentProject = null;
    private static SAFacilitator myself = null;
    private static boolean guiEnabled = true;
    private static String version = "2.2";
    private static int MAX_LINES = 24576;
    private static String fileName = "";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private MisraVersion misv = MisraVersion.MisC0412;
    private PCLintType pclt = PCLintType.PCLint;

    
    public MisraVersion getMisv() {
        return misv;
    }

    public void setMisv(MisraVersion misv) {
        this.misv = misv;
    }

    public PCLintType getPclt() {
        return pclt;
    }

    public void setPclt(PCLintType pclt) {
        this.pclt = pclt;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public static SAFacilitator getMyself() {
        return myself;
    }

    public static void setMyself(SAFacilitator myself) {
        SAFacilitator.myself = myself;
    }

    public static boolean isGuiEnabled() {
        return guiEnabled;
    }

    public static void setGuiEnabled(boolean guiEnabled) {
        SAFacilitator.guiEnabled = guiEnabled;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        SAFacilitator.version = version;
    }

    public static int getMAX_LINES() {
        return MAX_LINES;
    }

    public static void setMAX_LINES(int MAX_LINES) {
        SAFacilitator.MAX_LINES = MAX_LINES;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        SAFacilitator.fileName = fileName;
    }
    
    public static DateTimeFormatter getFormatter() {
        return formatter;
    }

    public static void setFormatter(DateTimeFormatter formatter) {
        SAFacilitator.formatter = formatter;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        myself = new SAFacilitator();
        if (args.length == 0) {
            myself.startGui(args);
        } else {
            guiEnabled = false;
            myself.processCommandLine(args);
        }
    }
    
    /**
     * Start GUI
     */
    void startGui(String[] args) {
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
        JavaFunctions javaFunctions = new JavaFunctions(this);
        FbInferFunctions fbInferFunctions = new FbInferFunctions(this);
        PythonFunctions pythonFunctions = new PythonFunctions(this);
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
            if (args[i].equals("-fip")) {
                try {
                    fbInferFunctions.prepareForFbInfer();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-h")) {
                System.out.println("\nStatic Analsys Facilitator - version " + version);
                System.out.println("Copyright (©) "  + LocalDate.now().getYear());
                System.out.println("Spazio IT - Soluzioni Informatiche s.a.s.");
                System.out.println("https://spazioit.com\n");
                System.out.println("Used technologies: Java FX, Jackson, JSON, CompileDB\n");
                System.out.println("Available commands:");                    
                System.out.println("-ea: execute analyzers");
                System.out.println("-ep: execute preprocessing");
                System.out.println("-fip: (FB Infer)prepare for FB Infer");                
                System.out.println("-h: print this text");
                System.out.println("-jpg: (Java) prepare for Gradle");
                System.out.println("-jpm: (Java) prepare for Maven");
                System.out.println("-l <file name>: load project file");
                System.out.println("-lcc <file name>: load compile_commands file into project");
                System.out.println("-misv MisC1998 | MisC2004 | MisC2012 | MisC0412 (def): select MISRA C Version");
                System.out.println("-pa: prepare analyzers");
                System.out.println("-pclt PCLint (def) | PClintPlus: select PCLint Type");
                System.out.println("-pp: prepare preprocessing");
                System.out.println("-ppa: post process analyzers");
                System.out.println("-psq: prepare SonarQube");
                System.out.println("-pyp: (Pyhton) prepare for Python analyzers");
                System.out.println("-rss: run SonarScanner");
                System.out.println("-s <file name>: save project file");
                System.out.println("-scc <file name>: save project as compile_commands file");
                System.out.println("-scd: show compiler defines\n");
                System.out.println("No argument starts the GUI\n");
            }
            if (args[i].equals("-jpg")) {
                try {
                    javaFunctions.prepareForGradle();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-jpm")) {
                try {
                    javaFunctions.prepareForMaven();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-l")) {
                if (i == (args.length - 1)) {
                    System.err.println(Strings.WRONG_COMMAND_LINE);
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
                    System.err.println(Strings.WRONG_COMMAND_LINE);
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
            if (args[i].equals("-misv")) {
                if (i == (args.length - 1)) {
                    System.err.println(Strings.WRONG_COMMAND_LINE);
                    System.err.println("Arg \"-misv\" expects to be followed by Misra Version.");
                    System.exit(-1);
                }
                if (args[++i].equals("MisC1998")) {
                    misv = MisraVersion.MisC1998;
                    pclt = PCLintType.PCLint;
                } else if (args[i].equals("MisC2004")) {
                    misv = MisraVersion.MisC2004;
                } else if (args[i].equals("MisC2012")) {
                    misv = MisraVersion.MisC2012;
                } else if (args[i].equals("MisCpp2008")) {
                    misv = MisraVersion.MisCpp2008;
                } else if (args[i].equals("MisC0412")) {
                    misv = MisraVersion.MisC0412;
                } else {
                    misv = MisraVersion.MisC0412;
                
                }
                checkArgument = false;
            }
            if (args[i].equals("-pa")) {
                try {
                    analyzersFunctions.prepareAnalyzers();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (args[i].equals("-pclt")) {
                if (i == (args.length - 1)) {
                    System.err.println(Strings.WRONG_COMMAND_LINE);
                    System.err.println("Arg \"-pclt\" expects to be followed by PClint Type.");
                    System.exit(-1);
                }
                if (args[++i].equals("PCLint")) {
                    pclt = PCLintType.PCLint;
                } else if (args[i].equals("PCLintPlus")) {
                    pclt = PCLintType.PCLintPlus;
                } else {
                    pclt = PCLintType.PCLintPlus;
                
                }
                checkArgument = false;
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
            if (args[i].equals("-pyp")) {
                try {
                    pythonFunctions.prepareForPython();
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
                    System.err.println(Strings.WRONG_COMMAND_LINE);
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
                    System.err.println(Strings.WRONG_COMMAND_LINE);
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
                (!args[i].equals("-fip")) &&
                (!args[i].equals("-h")) &&
                (!args[i].equals("-jpg")) &&
                (!args[i].equals("-jpm")) &&
                (!args[i].equals("-l")) &&
                (!args[i].equals("-lcc")) &&
                (!args[i].equals("-pa")) && 
                (!args[i].equals("-pclt")) && 
                (!args[i].equals("-pp")) && 
                (!args[i].equals("-ppa")) &&
                (!args[i].equals("-psq")) &&
                (!args[i].equals("-pyp")) &&
                (!args[i].equals("-rss")) &&
                (!args[i].equals("-s")) &&
                (!args[i].equals("-scc")) &&
                (!args[i].equals("-scd")) &&
                checkArgument) {
                System.err.println(Strings.WRONG_COMMAND_LINE);
                System.err.println("Available commands:");                    
                System.err.println("-ea: execute analyzers");
                System.err.println("-ep: execute preprocessing");
                System.err.println("-fip: (FB Infer)prepare for FB Infer");                
                System.err.println("-h: print this text");
                System.out.println("-jpg: (Java) prepare for Gradle");
                System.out.println("-jpm: (Java) prepare for Maven");
                System.err.println("-l <file name>: load project file");
                System.err.println("-lcc <file name>: load compile_commands file into project");
                System.err.println("-misv MisC1998 | MisC2004 | MisC2012 | MisC0412 (def): select MISRA C Version");
                System.err.println("-pa: prepare analyzers");
                System.err.println("-pclt PCLint (def) | PClintPlus: select PCLint Type");
                System.err.println("-pp: prepare preprocessing");
                System.err.println("-ppa: post process analyzers");
                System.err.println("-psq: prepare SonarQube");
                System.err.println("-pyp: (Pyhton) prepare for Python analyzers");
                System.err.println("-rss: run SonarScanner");
                System.err.println("-s <file name>: save project file");
                System.err.println("-scc <file name>: save project as compile_commands file");
                System.err.println("-scd: show compiler defines\n");
                System.exit(-1);
            }
        }
    }                
}
