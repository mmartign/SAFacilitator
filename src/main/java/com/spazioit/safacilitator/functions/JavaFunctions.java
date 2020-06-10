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
 * License along with this library; if not, write to the Free Software
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
import javafx.scene.control.TextArea;

/**
 *
 * @author Maurizio Martignano
 */
public class JavaFunctions {
    
    private SAFacilitator safacilitator = null;

    public JavaFunctions(SAFacilitator safacilitator) {
        this.safacilitator = safacilitator;
    }
    
    /**
     * Provides information on how to set Maven.
     */
    public void prepareForMaven() {
        if (SAFacilitator.isGuiEnabled()) {
            TextArea textArea = MainFrame.getTextArea();
            textArea.clear();
        }
        CommonFunctions.printMessage("Edit the settings.xml file, located in $MAVEN_HOME/conf or ~/.m2, to set the plugin prefix and optionally the SonarQube server URL.");
        CommonFunctions.printMessage("Example:\n");
        CommonFunctions.printMessage("<settings>");
        CommonFunctions.printMessage("    <pluginGroups>");
        CommonFunctions.printMessage("        <pluginGroup>org.sonarsource.scanner.maven</pluginGroup>");
        CommonFunctions.printMessage("    </pluginGroups>");
        CommonFunctions.printMessage("    <profiles>");
        CommonFunctions.printMessage("        <profile>");
        CommonFunctions.printMessage("            <id>sonar</id>");
        CommonFunctions.printMessage("            <activation>");
        CommonFunctions.printMessage("                <activeByDefault>true</activeByDefault>");
        CommonFunctions.printMessage("            </activation>");
        CommonFunctions.printMessage("            <properties>");
        CommonFunctions.printMessage("                <!-- Optional URL to server. Default value is http://localhost:9000 -->");
        CommonFunctions.printMessage("                <sonar.host.url>");
        Project p = safacilitator.getCurrentProject();
        if ((p != null) && p.getSonarQubeUrl() != null) {
            CommonFunctions.printMessage("                  " + p.getSonarQubeUrl()+ "");
            
        } else {
            CommonFunctions.printMessage("                  http://localhost:9000");
        }
        CommonFunctions.printMessage("                </sonar.host.url>");
        CommonFunctions.printMessage("            </properties>");
        CommonFunctions.printMessage("        </profile>");
        CommonFunctions.printMessage("     </profiles>");
        CommonFunctions.printMessage("</settings>\n");
        CommonFunctions.printMessage("To run the SonarQube analysis only:");
        CommonFunctions.printMessage("mvn sonar:sonar");
        CommonFunctions.printMessage("the usual Maven options do apply, e.g.");
        CommonFunctions.printMessage("to skip the tests:");
        CommonFunctions.printMessage("mvn clean install sonar:sonar -DskipTests=true");
        CommonFunctions.printMessage("or, to perform the tests:");
        CommonFunctions.printMessage("mvn clean install sonar:sonar -Dmaven.test.failure.ignore=true");
        CommonFunctions.printMessage("or, to get also coverage information:");
        CommonFunctions.printMessage("mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true");
        CommonFunctions.printMessage("mvn org.jacoco:jacoco-maven-plugin:report");
        CommonFunctions.printMessage("mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml\n");
        CommonFunctions.printMessage("More details at https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Maven");
        
    }

    /**
     * Provides information on how to set Gradle.
     */
    public void prepareForGradle() {
        if (SAFacilitator.isGuiEnabled()) {
            TextArea textArea = MainFrame.getTextArea();
            textArea.clear();
        }
        CommonFunctions.printMessage("Configure the Scanner:");
        CommonFunctions.printMessage("Installation is automatic, but certain global properties should still be configured.");
        CommonFunctions.printMessage("A good place to configure global properties is ~/.gradle/gradle.properties.");
        CommonFunctions.printMessage("Be aware the extension is using System properties so all properties should be prefixed by systemProp.");
        CommonFunctions.printMessage("File: gradle.properties");
        Project p = safacilitator.getCurrentProject();
        if ((p != null) && p.getSonarQubeUrl() != null) {
            CommonFunctions.printMessage("systemProp.sonar.host.url=" + p.getSonarQubeUrl()+ "\n");
            
        } else {
            CommonFunctions.printMessage("systemProp.sonar.host.url=http://localhost:9000\n");
        }
        CommonFunctions.printMessage("Activate the scanner in your build:");
        CommonFunctions.printMessage("File: build.gradle\n");
        CommonFunctions.printMessage("Using the plugins DSL:");
        CommonFunctions.printMessage("plugins {");
        CommonFunctions.printMessage("  id \"org.sonarqube\" version \"3.0\"");
        CommonFunctions.printMessage("}\n");
        CommonFunctions.printMessage("Using legacy plugin application:");
        CommonFunctions.printMessage("buildscript {");
        CommonFunctions.printMessage("  repositories {");
        CommonFunctions.printMessage("    maven {");
        CommonFunctions.printMessage("      url \"https://plugins.gradle.org/m2/\"");
        CommonFunctions.printMessage("    }");
        CommonFunctions.printMessage("  }");
        CommonFunctions.printMessage("  dependencies {");
        CommonFunctions.printMessage("    classpath \"org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.0\"");
        CommonFunctions.printMessage("  }");
        CommonFunctions.printMessage("}");
        CommonFunctions.printMessage("");
        CommonFunctions.printMessage("apply plugin: \"org.sonarqube\"\n");
        CommonFunctions.printMessage("To run the analysis:");
        CommonFunctions.printMessage("gradle sonarqube");
        CommonFunctions.printMessage("or, to get also coverage information:");
        CommonFunctions.printMessage("gradle jacocoTestReport");
        CommonFunctions.printMessage("gradle sonarqube\n");
        CommonFunctions.printMessage("More details at https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle");        
    }   
}
