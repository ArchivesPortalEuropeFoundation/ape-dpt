package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.utils.util.extendxsl.DateNormalization;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Yoann Moranville
 * Date: 20/11/2012
 *
 * @author Yoann Moranville
 */
public class Eag2012ValidFields {
    public static boolean isCountryCodeCorrect(String countryCode) {
        Matcher matcher = DateNormalization.PATTERN_COUNTRYCODE.matcher(countryCode);
        return matcher.matches();
    }

    public static boolean isRepositoryCodeCorrect(String repositoryCode) {
        Matcher matcher = DateNormalization.PATTERN_MAINAGENCYCODE.matcher(repositoryCode);
        return matcher.matches();
    }
}
