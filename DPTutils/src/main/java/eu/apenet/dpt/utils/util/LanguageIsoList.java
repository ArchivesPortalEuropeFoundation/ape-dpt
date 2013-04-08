package eu.apenet.dpt.utils.util;

import java.util.*;

/**
 * User: Yoann Moranville
 * Date: 08/04/2013
 *
 * @author Yoann Moranville
 */
public class LanguageIsoList {

    public static List<String> getLanguageIsoList() {
        String[] isoLanguages = Locale.getISOLanguages();
        Map<String, String> languagesTemp = new LinkedHashMap<String, String>(isoLanguages.length);
        LinkedList<String> languagesList = new LinkedList<String>();
        for(String isoLanguage : isoLanguages)
            languagesTemp.put(new Locale(isoLanguage).getISO3Language(), isoLanguage);//DisplayLanguage(Locale.ENGLISH), isoLanguage);

        List<String> tempList = new LinkedList<String>(languagesTemp.keySet());
        Collections.sort(tempList, String.CASE_INSENSITIVE_ORDER);

        for(String tempLanguage : tempList)
            languagesList.add(tempLanguage);

        return languagesList;
    }

}
