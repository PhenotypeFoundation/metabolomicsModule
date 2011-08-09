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

    /**
     * Dropdown list control to choose the platform used (DCL lipodomics, et cetera).
     *
     * @param platformVersionID selected platform version identifier
     */
    def platformControl = { attrs, body ->
        //out << "Platform: " + g.select(name:"platform", from:platformList)
        out << "Platform:  <br />"
        out << '<select name="platformVersionId" size="8" style="width:100%;">'

        measurementFactoryService.findAllMeasurementPlatforms().each { platform ->
        // if new studygroup create new label
        out << '<optgroup label="' + platform.name + '">'
            measurementFactoryService.findAllMeasurementPlatformVersions(measurementPlatform:platform).each { platformVersion ->

                out << '<option value="' + platformVersion.id + '" ' + ((platformVersion.id == attrs.platformVersionId) ? 'selected' : '') + '>' + platformVersion.versionnumber + '</option>'
            }

            out << '</optgroup>'
        }

        out << '</select>'
    }

    /**
     * Multiselect control to choose assays.
     *
     * @param assayId selected assay identifier
     *
     */
    def assaysControl = { attrs, body ->
        out << "Assay: <br />"
        out << '<select name="assayId" size="8" style="width:100%;">'

        // if new studygroup create new label
        assayService.getAssaysReadableByUserAndGroupedByStudy(session.user).each { assaysGroupedByStudy ->
            out << '<optgroup label="' + assaysGroupedByStudy.key.name + '">'

            assaysGroupedByStudy.value.each { assay ->
                out << '<option value="' + assay.id + '" ' + ((assay.id == attrs.assayId) ? 'selected' : '')  + '>' + assay.name + '</option>'
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
        out << "<b>row</b>" + g.radio(id:"samplePerRow", name:"isColumnOriented", value:"${false}", checked: !attrs.isColumnOriented, disabled: attrs.disabled)
        out << "<b>column</b>" + g.radio(id:"samplePerColumn", name:"isColumnOriented", value:"${true}", checked: attrs.isColumnOriented, disabled: attrs.disabled)
    }

    /**
     * Shows human readable delimiter names in a select box. The value will be the actual delimiter (eg. '\t' or ',').
     * @param name
     * @param delimiterNameMap a map of human readable delimiter names as keys with delimiters as values
     * @param value currently selected value
     */
    def delimiterControl = { attrs, body ->
        out << "Delimiter: "
        out << g.select(name: "delimiter",
                from: "${attrs.delimiterNameMap.entrySet()}",
                value: "${attrs.value}",
                optionKey: "value",
                optionValue: "key")
    }

    /**
     * Dropdown list control to choose excel sheet.
     * @param numberOfSheets
     * @param sheetIndex currently selected sheet index
     */
    def sheetSelectControl = { attrs, body ->
        out << pc.rangeSelectWithLabel(label: 'Sheet', name: 'sheetIndex', maxIndex: attrs.numberOfSheets, value: attrs.sheetIndex)
    }

    /**
     * Dropdown list control to choose the feature row.
     * @param featureRowIndex currently selected feature row
     */
    def featureRowControl = { attrs, body ->
        out << pc.rangeSelectWithLabel(label: 'Feature row', name: 'featureRowIndex', maxIndex: 5, value: attrs.featureRowIndex)
    }

    /**
     * Dropdown list control to choose where the sample column starts.
     * @param sampleColumnIndex currently selected sample column
     */
    def sampleColumnControl = { attrs, body ->
        out << pc.rangeSelectWithLabel(label: 'Sample column', name: 'sampleColumnIndex', maxIndex: 5, value: attrs.sampleColumnIndex)
    }

    /**
     * Helper tag to for controls with a label and a drop down select box. Select box is zero based. Example: maxIndex:3
     * then options will be 0,1,2 but visible values are 1,2,3.
     * @param name
     * @param maxIndex the maximum index of the range
     * @param value currently selected value
     */
    def rangeSelectWithLabel = { attrs, body ->

        def rangeMap = [:]

        (0..attrs.maxIndex-1).each { rangeMap[it+1] = it }

        out << "${attrs.label}: "
        out << g.select(name: attrs.name, from: rangeMap, value: attrs.value, optionKey: 'value', optionValue: 'key')
    }

    /**
     * Checkbox control for normalized data.
     */
    def normalizedControl = { attrs, body ->
        out << "Normalized: " + g.checkBox(name:"normalized")
    }

    /**
     * Status control showing amount of samples found in assay, unassigned samples et cetera.
     */
    def statusControl = { attrs, body ->
        out << "<div id=\"status\">Status: ${attrs.initialStatus ?: 'no status available'}</div>"
    }
}
