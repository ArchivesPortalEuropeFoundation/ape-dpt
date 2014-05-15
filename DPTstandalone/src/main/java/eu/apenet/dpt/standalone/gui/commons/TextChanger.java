package eu.apenet.dpt.standalone.gui.commons;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithDate;
import eu.apenet.dpt.standalone.gui.eag2012.Eag2012ValidFields;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.TextFieldWithCheckbox;
import eu.apenet.dpt.utils.eag2012.MaintenanceEvent;

/**
 * User: Yoann Moranville
 * Date: 13/03/2013
 *
 * @author Yoann Moranville
 */
public abstract class TextChanger {
    private static final Logger LOG = Logger.getLogger(TextChanger.class);
    public static String getNewText(List<TextFieldWithCheckbox> textFieldWithCheckboxs, String countryCode) {
        String returnValue = "";
        for(TextFieldWithCheckbox textFieldWithCheckbox : textFieldWithCheckboxs) {
            if(Eag2012ValidFields.isRepositoryCodeCorrect(textFieldWithCheckbox.getTextFieldValue()) && textFieldWithCheckbox.getisilOrNotComboValue().equals("yes"))
                return textFieldWithCheckbox.getTextFieldValue();
            else if(StringUtils.isNotEmpty(countryCode))
                returnValue = countryCode + "-" + "99999999999";
        }
        return returnValue;
    }

    public static boolean isDateSetReady(List<TextFieldWithDate> textFieldWithDates, boolean addYear, boolean addRange) {
        int counterDate = 0;
        if(addYear)
            counterDate++;
        int counterDateRange = 0;
        if(addRange)
            counterDateRange++;
        for(TextFieldWithDate textFieldWithDate : textFieldWithDates) {
            if(StringUtils.isNotEmpty(textFieldWithDate.getDate())) {
                counterDate++;
            } else if(StringUtils.isNotEmpty(textFieldWithDate.getFromDate()) && StringUtils.isNotEmpty(textFieldWithDate.getToDate())) {
                counterDateRange++;
            }
        }
        if(counterDate > 1 || counterDateRange > 1 || (counterDate > 0 && counterDateRange > 0)) {
            return true;
        } else {
            if(counterDate == 1) {
                return false;
            } else if(counterDateRange == 1) {
                return false;
            }
        }
        return false;
    }

    public static MaintenanceEvent getMaintenanceEventSaved(Date timeMaintenance, List<MaintenanceEvent> maintenanceEvents) {
        for(MaintenanceEvent maintenanceEvent : maintenanceEvents) {
            SimpleDateFormat formatStandard = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String maintenanceTimeStandardForm = formatStandard.format(timeMaintenance);
            if(maintenanceEvent.getEventDateTime().getStandardDateTime().equals(maintenanceTimeStandardForm)) {
                return maintenanceEvent;
            }
        }
        return null;
    }

    public static eu.apenet.dpt.utils.eaccpf.MaintenanceEvent getEacCpfMaintenanceEventSaved(Date timeMaintenance, List<eu.apenet.dpt.utils.eaccpf.MaintenanceEvent> maintenanceEvents) {
        for(eu.apenet.dpt.utils.eaccpf.MaintenanceEvent maintenanceEvent : maintenanceEvents) {
            SimpleDateFormat formatStandard = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String maintenanceTimeStandardForm = formatStandard.format(timeMaintenance);
            if(maintenanceEvent.getEventDateTime().getStandardDateTime().equals(maintenanceTimeStandardForm)) {
                return maintenanceEvent;
            }
        }
        return null;
    }

//    public static List<Object> transformDatesToDateOrDateRange(DateSet dateSet) {
//        eu.apenet.dpt.standalone.gui.eag2012.data.Date date = dateSet.getDate();
//        DateRange dateRange = dateSet.getDateRange();
//        List<Object> objects = new ArrayList<Object>();
//        objects.add(date);
//        objects.add(dateRange);
//        return objects;
//    }
}
