package eu.apenet.dpt.utils.util;

/**
 * User: Yoann Moranville
 * Date: Aug 10, 2010
 *
 * @author Yoann Moranville
 */
public enum Xsd_enum {
    XSD_EAD_SCHEMA("EAD 2002", "ead_2002.xsd"),
    XSD_APE_SCHEMA("apeEAD", "apeEAD.xsd"),
    XSD1_0_APE_SCHEMA("apeEAD (XSD 1.0)", "apeEAD_XSD1.0.xsd"),
    XSD_EAC_SCHEMA("EAC-CPF", "cpf.xsd"),
    XSD_EAG_SCHEMA("EAG 0.2", "eag.xsd"),
    XSD_EAG_2012_SCHEMA("EAG 2012", "eag_2012.xsd"),
    DTD_EAD_2002("EAD 2002 DTD", "dtd/ead.dtd");

    private String path;
    private String readableName;

    Xsd_enum(String readableName, String path) {
        this.readableName = readableName;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getReadableName(){
        return readableName;
    }

    @Override
    public String toString(){
        return "Schema name: " + readableName + " and path: " + path;
    }
}
