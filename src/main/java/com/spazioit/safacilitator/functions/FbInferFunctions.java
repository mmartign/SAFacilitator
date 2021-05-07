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
public class FbInferFunctions {

    private SAFacilitator safacilitator = null;

    public FbInferFunctions(SAFacilitator safacilitator) {
        this.safacilitator = safacilitator;
    }
    
    /**
     * Provides information on how to set Maven.
     */
    public void prepareForFbInfer() {
        if (SAFacilitator.isGuiEnabled()) {
            TextArea textArea = MainFrame.getTextArea();
            textArea.clear();
        }
        CommonFunctions.printMessage("NOTE: the SAFacilitator itself does not run Infer, you have to do that yourself beforehand.\n");
        CommonFunctions.printMessage("In order to run Infer and generate a fitting report, make sure to call it from the project root directory,");
        CommonFunctions.printMessage("so that the paths in the report fit.\n");
        CommonFunctions.printMessage("Copy the generated file (\"report.json\"), that should be located in the \"infer-out\" directory,");
        CommonFunctions.printMessage("into a subdirectory of the project root directory called \"fbinfer-reports\".");
    }

}
