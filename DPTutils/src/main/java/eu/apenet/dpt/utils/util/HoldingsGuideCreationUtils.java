package eu.apenet.dpt.utils.util;

/**
 * User: Yoann Moranville
 * Date: 21/09/2011
 *
 * @author Yoann Moranville
 */
public class HoldingsGuideCreationUtils {

    public static String eadDeclaration(String title, String id, String countrycode, String globalIdentifier, String versionNb){
        String string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        string += "<ead xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:isbn:1-931666-22-9\" xsi:schemaLocation=\"urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD_XSD1.0.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd\" audience=\"external\">\n";
        string += "    <eadheader countryencoding=\"iso3166-1\" dateencoding=\"iso8601\" langencoding=\"iso639-2b\" repositoryencoding=\"iso15511\" scriptencoding=\"iso15924\" relatedencoding=\"MARC21\">\n";
        string += "        <eadid countrycode=\"" + countrycode + "\" mainagencycode=\"" + globalIdentifier + "\" identifier=\"" + globalIdentifier + "_" + id + "\">" + id + "</eadid>\n";
        string += "        <filedesc><titlestmt><titleproper>" + title + "</titleproper></titlestmt></filedesc>\n";
        string += "        <revisiondesc><change>\n";
        string += "         <date />\n";
        string += "         <item>Converted_apeEAD_version_" + versionNb + "</item>\n";
        string += "        </change></revisiondesc>\n";
        string += "    </eadheader>\n";
        return string;
    }

    public static String endDeclaration(){
        return "    </ead>";
    }

}
