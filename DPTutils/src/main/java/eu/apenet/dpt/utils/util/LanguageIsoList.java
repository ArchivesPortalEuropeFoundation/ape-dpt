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
        //Overwrites the ISO 639-2 T codes with the ISO 639-2 B codes
        languagesTemp.put(new Locale("sq").getDisplayLanguage(Locale.ENGLISH), "alb");
        languagesTemp.put(new Locale("hy").getDisplayLanguage(Locale.ENGLISH), "arm");
        languagesTemp.put(new Locale("eu").getDisplayLanguage(Locale.ENGLISH), "baq");
        languagesTemp.put(new Locale("my").getDisplayLanguage(Locale.ENGLISH), "bur");
        languagesTemp.put(new Locale("zh").getDisplayLanguage(Locale.ENGLISH), "chi");
        languagesTemp.put(new Locale("cs").getDisplayLanguage(Locale.ENGLISH), "cze");
        languagesTemp.put(new Locale("nl").getDisplayLanguage(Locale.ENGLISH), "dut");
        languagesTemp.put(new Locale("fr").getDisplayLanguage(Locale.ENGLISH), "fre");
        languagesTemp.put(new Locale("ka").getDisplayLanguage(Locale.ENGLISH), "geo");
        languagesTemp.put(new Locale("de").getDisplayLanguage(Locale.ENGLISH), "ger");
        languagesTemp.put(new Locale("el").getDisplayLanguage(Locale.ENGLISH), "gre");
        languagesTemp.put(new Locale("is").getDisplayLanguage(Locale.ENGLISH), "ice");
        languagesTemp.put(new Locale("mk").getDisplayLanguage(Locale.ENGLISH), "mac");
        languagesTemp.put(new Locale("mi").getDisplayLanguage(Locale.ENGLISH), "mao");
        languagesTemp.put(new Locale("ms").getDisplayLanguage(Locale.ENGLISH), "may");
        languagesTemp.put(new Locale("fa").getDisplayLanguage(Locale.ENGLISH), "per");
        languagesTemp.put(new Locale("ro").getDisplayLanguage(Locale.ENGLISH), "rum");
        languagesTemp.put(new Locale("sk").getDisplayLanguage(Locale.ENGLISH), "slo");
        languagesTemp.put(new Locale("bo").getDisplayLanguage(Locale.ENGLISH), "tib");
        languagesTemp.put(new Locale("cy").getDisplayLanguage(Locale.ENGLISH), "wel");
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
        if("sqi".equals(isoCode)) isoCode = "alb";
        else if("hye".equals(isoCode)) isoCode = "arm";
        else if("eus".equals(isoCode)) isoCode = "baq";
        else if("bod".equals(isoCode)) isoCode = "tib";
        else if("mya".equals(isoCode)) isoCode = "bur";
        else if("ces".equals(isoCode)) isoCode = "cze";
        else if("zho".equals(isoCode)) isoCode = "chi";
        else if("cym".equals(isoCode)) isoCode = "wel";
        else if("deu".equals(isoCode)) isoCode = "ger";
        else if("nld".equals(isoCode)) isoCode = "dut";
        else if("ell".equals(isoCode)) isoCode = "gre";
        else if("fas".equals(isoCode)) isoCode = "per";
        else if("fra".equals(isoCode)) isoCode = "fre";
        else if("kat".equals(isoCode)) isoCode = "geo";
        else if("isl".equals(isoCode)) isoCode = "ice";
        else if("mkd".equals(isoCode)) isoCode = "mac";
        else if("mri".equals(isoCode)) isoCode = "mao";
        else if("msa".equals(isoCode)) isoCode = "may";
        else if("ron".equals(isoCode)) isoCode = "rum";
        else if("slk".equals(isoCode)) isoCode = "slo";
        for(String language : languagesTemp.keySet()) {
            if(languagesTemp.get(language).equals(isoCode)) {
                return language;
            }
        }
        return null;
    }

}
