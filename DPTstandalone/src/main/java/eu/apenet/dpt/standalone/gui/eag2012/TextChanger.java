package eu.apenet.dpt.standalone.gui.eag2012;

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
}
