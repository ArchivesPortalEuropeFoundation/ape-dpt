package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 13/03/2013
 *
 * @author Yoann Moranville
 */
public abstract class TextChanger {
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
}
