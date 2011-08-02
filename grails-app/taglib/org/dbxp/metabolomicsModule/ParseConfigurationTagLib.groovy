/**
 *  ParseConfigurationTagLib, a tag library delivering controls for handling
 *  configuration settings for the parser
 *
 *  Copyright (C) 2011 Tjeerd Abma
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author Tjeerd Abma
 *  @since 20110710
 *
 *  Revision information:
 *
 *  $Author$
 *  $Rev$
 *  $Date$
 */

package org.dbxp.metabolomicsModule

class ParseConfigurationTagLib {
    static namespace = "pc"
    def measurementFactoryService
    def assayService

    def fileTypes = ["CSV", "TAB", "XLS/XLSX"]
    def dataColumnsList = ["Column1", "Column2", "Column3", "Column4"]

    /**
     * Method showing a popup dialog to configure the parse settings
     *
     * @uploadedFileId identifier of the uploaded file
     */
    def popupDialog = { attrs, body ->
        out << body()
    }

    /**
     * Dropdown list control to choose the type of data formatting used: tabular, comma separated, Excel et cetera.
     */
    def fileTypeControl = { attrs, body ->
        out << "Filetype: " + g.select(name:"fileType", from:fileTypes)
    }

    /**
     * Dropdown list control to choose the platform used (DCL lipodomics, et cetera).
     *
     * @param platformVersionID selected platform version identifier
     */
    def platformControl = { attrs, body ->
        //out << "Platform: " + g.select(name:"platform", from:platformList)
        out << "Platform: "
        out << '<select name="platformVersionID" size="6" style="width:210px">'

        measurementFactoryService.findAllMeasurementPlatforms().each { platform ->
        // if new studygroup create new label
        out << '<optgroup label="' + platform.name + '">'
            measurementFactoryService.findAllMeasurementPlatformVersions(measurementPlatform:platform).each { platformversion ->
                out << '<option value="' + platformversion.id + ((platformversion.id == attrs.platformVersionID) ? 'selected' : '') + '">' + platformversion.versionnumber + '</option>'
            }

            out << '</optgroup>'
        }

        out << '</select>'
    }

    /**
     * Radio button to switch orientation/transpose data: 1 sample per row or per column.
     *
     * @param isColumnOriented true if samples are stored per column, false if samples are stored per row
     */
    def orientationControl = { attrs, body ->
        out << "Sample per "
        out << "<b>row</b>" + g.radio(id:"samplePerRow", name:"isColumnOriented", value:"${false}", checked: !attrs.isColumnOriented)
        out << "<b>column</b>" + g.radio(id:"samplePerColumn", name:"isColumnOriented", value:"${true}", checked: attrs.isColumnOriented)
    }

    /**
     * Dropdown list control to choose where the sample column starts.
     */
    def sampleColumnControl = { attrs, body ->
        out << "Sample column: "
        //out <<

    }

    /**
     * Dropdown list control to choose one parameter (not clear).
     */
    def oneParameterControl = { attrs, body ->

    }

    /**
     * Checkbox control for normalized data.
     */
    def normalizedControl = { attrs, body ->
        out << "Normalized: " + g.checkBox(name:"normalized")
    }

    /**
     * Dropdown list control to choose the feature row.
     */
    def featureRowControl = { attrs, body ->

    }

    /**
     * Multiselect control to choose assays.
     *
     * @param assayID selected assay identifier
     *
     */
    def assaysControl = { attrs, body ->
        out << "Assay: "

        out << '<select name="assayID" size="8" style="width:170px">'

        // if new studygroup create new label
        assayService.getAssaysReadableByUserAndGroupedByStudy(session.user).each { assaysGroupedByStudy ->
            out << '<optgroup label="' + assaysGroupedByStudy.key.name + '">'

            assaysGroupedByStudy.value.each { assay ->
                out << '<option value="' + assay.id + '" ' + ((assay.id == attrs.assayID) ? 'selected' : '')  + '>' + assay.name + '</option>'
            }

            out << '</optgroup>'
        }

        out << '</select>'
    }

    /**
     * Checkbox list control to select the data columns.
     *
     * TODO: jQuery plugin at http://code.google.com/p/dropdown-check-list/
     */
    def dataColumnsControl = { attrs, body ->
        //out << "Datacolumns: " + g.select (name:"datacolumns", from:dataColumnsList)
        out << "Datacolumns: " + '<select name="datacolumns" id="datacolumns" multiple="multiple"><option>Column1</option><option>Column2</option></select>'
    }

    /**
     * Preview control displaying a preview of the data according to the settings made by other controls.
     * @param datamatrix two dimensional array with data
     *
     */
    def dataMatrixControl = { attrs, body ->
        out << '<table id="datamatrix"></table>'
    }

    /**
     * Status control showing amount of samples found in assay, unassigned samples et cetera.
     */
    def statusControl = { attrs, body ->
        out << "<div id=\"status\">${attrs.initialStatus ?: 'Status: no status available'}</div>"
    }
}
