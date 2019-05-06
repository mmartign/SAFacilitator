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
public class PrFile {
    
    // File Name
    private String pfFileName = null;
    
    // The "command" used to build the file
    private String pfOriginalBuilder = null;
    
    // Include "directories"
    private List<String> pfIncludeDirectories = null;
    
    // Defines
    private List<String> pfDefines = null;
  
    // Other, additional arguments
    private List<String> pfAdditionalArguments = null;

    // Getters / Setters
    
    public String getPfFileName() {
        return pfFileName;
    }

    public void setPfFileName(String pfFileName) {
        this.pfFileName = pfFileName;
    }

    public String getPfOriginalBuilder() {
        return pfOriginalBuilder;
    }

    public void setPfOriginalBuilder(String pfOriginalBuilder) {
        this.pfOriginalBuilder = pfOriginalBuilder;
    }

    public List<String> getPfIncludeDirectories() {
        return pfIncludeDirectories;
    }

    public void setPfIncludeDirectories(List<String> pfIncludeDirectories) {
        this.pfIncludeDirectories = pfIncludeDirectories;
    }

    public List<String> getPfDefines() {
        return pfDefines;
    }

    public void setPfDefines(List<String> pfDefines) {
        this.pfDefines = pfDefines;
    }

    public List<String> getPfAdditionalArguments() {
        return pfAdditionalArguments;
    }

    public void setPfAdditionalArguments(List<String> pfAdditionalArguments) {
        this.pfAdditionalArguments = pfAdditionalArguments;
    }
    
}
