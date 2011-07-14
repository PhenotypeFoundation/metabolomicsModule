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

    def fileTypes = ["CSV", "TAB", "XLS/XLSX"]
    def platformList = ["Platform1", "Platform2", "Platform 3"]
    def dataColumnsList = ["Column1", "Column2", "Column3", "Column4"]

    /**
     * Renders a dialog which loads the parse configuration controller to
     * configure and display the preview data.
     *
     * @param id id of the HTML element being clicked
     */

    def initParseConfigurationDialog = { attrs, body ->
        out << "<script type=\"text/javascript\" src=\"${resource(dir: 'js', file: 'parseConfiguration.js')}\"></script>"
        out << '<script>'
        out << '$(document).ready(function() {'
        out << 'initParseConfigurationDialog();'
        out << '});'
        out << '</script>'
    }

    def dialog = { attrs, body ->
        out << 'var $link = "parseConfiguration";'
        out << 'var $pcDialog = $("<div></div>")\n'
        out << '.load($link)\n'
        out << '.dialog({'
        out << 'autoOpen: false,'
        out << 'title: "Parse Configuration panel",'
        out << "buttons: { 'close': function() { \$(this).dialog('close');}, 'send': function() { submitForm(); } },"
        out << 'width: 680,'
        out << 'height: 520'
        out << '});'

        out << '$("#' + attrs['id'] + '").click(function() {'
        out << '$pcDialog.dialog("open");'
        out << 'return false;'
        out << '	});'
    }

    /**
     * Dropdown list control to choose the type of data formatting used: tabular, comma separated, Excel et cetera.
     */

    def fileTypeControl = { attrs, body ->
        out << "Filetype: " + g.select(name:"fileType", from:fileTypes)
    }

    /**
     * Dropdown list control to choose the platform used (DCL lipodomics).
     */
    def platformControl = { attrs, body ->
        out << "Platform: " + g.select(name:"platform", from:platformList)
    }

    /**
     * Radio button to switch orientation/transpose data: 1 sample per row or per column.
     */
    def orientationControl = { attrs, body ->
        out << "Sample per "
        out << "<b>row</b>" + g.radio(id:"samplePerRow", name:"orientation", value:"samplePerRow")
        out << "<b>column</b>" + g.radio(id:"samplePerColumn", name:"orientation", value:"samplePerColumn")
    }

    /**
     * Dropdown list control to choose where the sample column starts.
     */
    def sampleColumnControl = { attrs, body ->

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
     * @param studies list of studies with assays
     *
     */
    def assaysControl = { attrs, body ->
        out << "Assay: "
        out << '<select name="assays" multiple size="8" style="width:170px">'

        // if new studygroup create new label
        10.times { out << '<optgroup label="Study' + it + '">'
            5.times {
                out << '<option value="' + it + '">Assay' + it + '</option>'
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
     * Statistics control showing amount of samples found in assay, unassigned samples et cetera.
     */
    def statisticsControl = { attrs, body ->
        out << '<div id="statistics">Statistics: no statistics available</div>'
    }


}
