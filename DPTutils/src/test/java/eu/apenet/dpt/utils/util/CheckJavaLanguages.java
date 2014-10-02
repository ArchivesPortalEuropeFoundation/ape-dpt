package eu.apenet.dpt.utils.util;

import com.neovisionaries.i18n.LanguageCode;
import org.junit.Test;

import java.util.Locale;

/**
 * User: yoannmoranville
 * Date: 01/10/14
 *
 * @author yoannmoranville
 */
public class CheckJavaLanguages {
    @Test
    public void testCheckJavaLanguages() {
        Locale[] locales = Locale.getAvailableLocales();
        for(int i=0; i < locales.length; i++){
            if(!locales[i].getDisplayLanguage().equals(""))
                System.out.println(locales[i].getDisplayLanguage() + " (" + locales[i].getISO3Language() + ") " + " - " + locales[i].getCountry());
        }
    }

    @Test
    public void testNvi18nLanguages() {
        for (LanguageCode code : LanguageCode.values()) {
            System.out.format("[%s] %s\n", code.getAlpha3(), code.getName());
        }
    }
}
