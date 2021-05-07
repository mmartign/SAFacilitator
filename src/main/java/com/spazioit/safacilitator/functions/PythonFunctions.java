/*
 * Copyright (C) 2021 Spazio IT - Soluzioni Informatiche.
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
public class PythonFunctions {

    private SAFacilitator safacilitator = null;

    public PythonFunctions(SAFacilitator safacilitator) {
        this.safacilitator = safacilitator;
    }
    
    /**
     * Provides information on how to set Maven.
     */
    public void prepareForPython() {
        if (SAFacilitator.isGuiEnabled()) {
            TextArea textArea = MainFrame.getTextArea();
            textArea.clear();
        }
        CommonFunctions.printMessage("NOTE: the SAFacilitator itself does not run the Python static analyzers, you have to do that yourself beforehand.\n");
        CommonFunctions.printMessage("In order to run these alyzers and generate fitting reports, make sure to call them from the project root directory,");
        CommonFunctions.printMessage("so that the paths in the report fit.\n");
        CommonFunctions.printMessage("Run Bandit as follows:");
        CommonFunctions.printMessage("bandit --format json --recursive <source_directory> --output ./bandit-reports/bandit-report-01.json\n");
        CommonFunctions.printMessage("Run Pylint as follows:");
        CommonFunctions.printMessage("pylint <pyton file name> -r n --msg-template=\"{path}:{line}: [{msg_id}({symbol}), {obj}] {msg}\" > ./pylint-reports/pylint-report-01.txt");
        CommonFunctions.printMessage("(an utility applying the above command recurdively to all python files in a directory could be very useful).\n");
        CommonFunctions.printMessage("Run Flake8 as follows:");
        CommonFunctions.printMessage("flake8 > ./flake8-reports/flake8-report-01.json.");
    }

}
