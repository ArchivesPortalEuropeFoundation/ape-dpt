package eu.apenet.dpt.standalone.gui.edition;

/**
 * User: Yoann Moranville
 * Date: Oct 26, 2010
 *
 * @author Yoann Moranville
 */
public enum ActionNamesEnum {
    INSERT_EADID("edit.insertEltEadid"),
    INSERT_COUNTRYCODE("edit.insertAttrCountrycode"),
    INSERT_MAINAGENCYCODE("edit.insertAttrMainagencycode"),
    INSERT_NORMALATTR("edit.insertAttrNormal"),
    INSERT_LANGUAGETAG("edit.insertEltLanguage"),
    INSERT_TEXT("edit.insertText");


    private String bundleCode;

    public String getBundleCode() {
        return bundleCode;
    }
    
    ActionNamesEnum(String bundleCode){
        this.bundleCode = bundleCode;
    }
}
