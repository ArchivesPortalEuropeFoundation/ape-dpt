package eu.apenet.dpt.utils.util;

import org.apache.commons.lang.StringUtils;

/**
 * User: Yoann Moranville
 * Date: Aug 10, 2010
 *
 * @author Yoann Moranville
 */
public enum Xsd_enum {
    XSD_APE_SCHEMA("apeEAD", "apeEAD.xsd"),
    XSD_EAD_SCHEMA("EAD 2002", "ead_2002.xsd"),
    XSD_EAD3_SCHEMA("EAD 3", "ead3.xsd"),
    DTD_EAD_2002("EAD 2002 DTD", "dtd/ead.dtd"),
    XSD_APE_EAC_SCHEMA("apeEAC-CPF", "apeEAC-CPF.xsd"),
    XSD_EAC_SCHEMA("EAC-CPF", "cpf.xsd"),
    XSD_EAG_2012_SCHEMA("EAG 2012", "eag_2012.xsd"),
    XSD_METS_SCHEMA("apeMETS", "apeMETS.xsd"),
    XSD_METS_RIGHTS_SCHEMA("apeMETS Rights", "apeMETSRights.xsd"),
    XSD_METS_XLINK_SCHEMA("apeMETS Xlink", "apeMETSxlink.xsd");

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

    public static boolean doesXsdExist(String schema, String stripString) {
        if(StringUtils.isNotEmpty(stripString))
            schema = schema.replaceAll(stripString, "");
        for(Xsd_enum value : Xsd_enum.values()) {
            if(value.getPath().equals(schema)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "Schema name: " + readableName + " and path: " + path;
    }
}
