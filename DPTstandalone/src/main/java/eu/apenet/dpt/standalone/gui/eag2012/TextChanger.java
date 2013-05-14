package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.utils.eag2012.DateRange;
import eu.apenet.dpt.utils.eag2012.DateSet;
import eu.apenet.dpt.utils.eag2012.MaintenanceEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

//    public static List<Object> transformDatesToDateOrDateRange(DateSet dateSet) {
//        eu.apenet.dpt.standalone.gui.eag2012.data.Date date = dateSet.getDate();
//        DateRange dateRange = dateSet.getDateRange();
//        List<Object> objects = new ArrayList<Object>();
//        objects.add(date);
//        objects.add(dateRange);
//        return objects;
//    }
}
