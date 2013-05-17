package eu.apenet.dpt.utils.util;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * User: Yoann Moranville
 * Date: 08/04/2013
 *
 * @author Yoann Moranville
 */
public class LanguageIsoList {
    private static final Logger LOG = Logger.getLogger(LanguageIsoList.class);
    private static Map<String, String> languagesTemp;
    static {
        String[] isoLanguages = Locale.getISOLanguages();
        languagesTemp = new LinkedHashMap<String, String>(isoLanguages.length);
        for(String isoLanguage : isoLanguages)
            languagesTemp.put(new Locale(isoLanguage).getDisplayLanguage(Locale.ENGLISH), new Locale(isoLanguage).getISO3Language());
    }

    public static List<String> getLanguageIsoList() {
        LinkedList<String> languagesList = new LinkedList<String>();
        List<String> tempList = new LinkedList<String>(languagesTemp.keySet());
        Collections.sort(tempList, String.CASE_INSENSITIVE_ORDER);

        for(String tempLanguage : tempList)
            languagesList.add(tempLanguage);

        return languagesList;
    }

    public static String getIsoCode(String languageStr) {
        return languagesTemp.get(languageStr);
    }

    public static String getLanguageStr(String isoCode) {
        for(String language : languagesTemp.keySet()) {
            if(languagesTemp.get(language).equals(isoCode)) {
                return language;
            }
        }
        return null;
    }

}
