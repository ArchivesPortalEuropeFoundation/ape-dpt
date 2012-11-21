package eu.apenet.dpt.utils.util;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsPSMDetector;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User: Yoann Moranville
 * Date: 3 mars 2010
 */
public class FileUtil {
    public FileUtil(){}

    public InputStream readFileAsInputStream(String filePath/*, String characterSet*/){
        File file = new File(filePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            //InputStreamReader inputStreamReader = new InputStreamReader(fis, characterSet);
        } catch (Exception e) {
            return null;
        }
        return fis;
    }

    public InputStream readFileAsInputStream(File file){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
//            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
//            System.out.println(this.convertStreamToString(inputStreamReader));
        } catch (Exception e) {
            return null;
        }
        return fis;
    }

    /**
     * Prefer using readFileAsString_linebreak for this
     * @param filePath Path of the file to read
     * @return String containing the data of the file
     */
    @Deprecated
    public String readFileAsString(String filePath) {
        File file = new File(filePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            fis = new FileInputStream(file);

            // Here BufferedInputStream is added for fast reading.
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            // dis.available() returns 0 if the file does not have more lines.
            String results = "";
            while (dis.available() != 0) {

                // this statement reads the line from the file and print it to
                // the console.
                results += dis.readLine();
            }

            // dispose all the resources after using them.
            fis.close();
            bis.close();
            dis.close();
            return results;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readFileAsString_linebreak(String filePath){
        File file = new File(filePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            BufferedReader d = new BufferedReader(new InputStreamReader(fis));

            String results = "";
            String thisLine;
            while ((thisLine = d.readLine()) != null) {
                results += thisLine + "\n";
            }

            fis.close();
            d.close();
            return results;
        } catch (Exception e){

        }
        return null;
    }

    public String changeSchema(String xmlString){
        // remove doctype
        Pattern pattern = Pattern.compile("<!DOCTYPE .*>");
        Matcher matcher = pattern.matcher(xmlString);
        xmlString = matcher.replaceFirst("");

        Pattern pattern_xsi = Pattern.compile("xsi:schemaLocation=\".*\"");
        Matcher matcher_xsi = pattern_xsi.matcher(xmlString);
        xmlString = matcher_xsi.replaceFirst("");

        //change xmlns_xlink
        Pattern pattern_xmlns_xlink = Pattern.compile("xmlns:xlink=\"[.A-Za-z0-9:/-]*\"");
        Matcher matcher_xmlns_xlink = pattern_xmlns_xlink.matcher(xmlString);
        xmlString = matcher_xmlns_xlink.replaceFirst("xmlns:xlink=\"http://www.w3.org/1999/xlink\"");

        //change xmlns
        Pattern pattern_xmlns = Pattern.compile("xmlns=\"[.A-Za-z0-9:/-]*\"");
        Matcher matcher_xmlns = pattern_xmlns.matcher(xmlString);

        if(matcher_xmlns.find()){
            xmlString = matcher_xmlns.replaceFirst("xmlns=\"urn:isbn:1-931666-22-9\"");
        } else {
            xmlString = xmlString.replace("<ead ", "<ead xmlns=\"urn:isbn:1-931666-22-9\" ");
            xmlString = xmlString.replace("<ead>", "<ead xmlns=\"urn:isbn:1-931666-22-9\">");
            xmlString = xmlString.replace("<ead >", "<ead xmlns=\"urn:isbn:1-931666-22-9\">");
        }
        return xmlString;
    }

    public String changeSchemaEac(String xmlString){
        // remove doctype
        Pattern pattern = Pattern.compile("<!DOCTYPE .*>");
        Matcher matcher = pattern.matcher(xmlString);
        xmlString = matcher.replaceFirst("");

        // remove xsi:schemaLocation
        Pattern pattern_xsi = Pattern.compile("xsi:schemaLocation=\".*\"");
        Matcher matcher_xsi = pattern_xsi.matcher(xmlString);
        xmlString = matcher_xsi.replaceFirst("");

        xmlString = xmlString.replace("<eac ", "<eac-cpf ");
        xmlString = xmlString.replace("<eac>", "<eac-cpf>");
        xmlString = xmlString.replace("</eac>", "</eac-cpf>");

        //change xmlns
        Pattern pattern_xmlns = Pattern.compile("xmlns=\".*\"");
        Matcher matcher_xmlns = pattern_xmlns.matcher(xmlString);


        if(matcher_xmlns.find()){
            xmlString = matcher_xmlns.replaceFirst("xmlns=\"urn:isbn:1-931666-33-4\"");
        } else {
            xmlString = xmlString.replace("<eac-cpf ", "<eac-cpf xmlns=\"urn:isbn:1-931666-33-4\" ");
            xmlString = xmlString.replace("<eac-cpf>", "<eac-cpf xmlns=\"urn:isbn:1-931666-33-4\">");
            xmlString = xmlString.replace("<eac-cpf >", "<eac-cpf xmlns=\"urn:isbn:1-931666-33-4\">");
        }
        return xmlString;
    }

    public void writeToFile(String stringToWrite, String fileName, boolean append) {
        File aFile = new File(fileName);
        try{
            //aFile.setWritable(true);
            FileWriter out = new FileWriter(aFile, append);
            out.write(stringToWrite);
            out.close();
        } catch (Exception e){
            e.getStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String convertStreamToString(InputStream is) throws IOException{
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                //BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    public void inputStreamWriteToFile(InputStream inputStream, String fileName){
        try {
            writeToFile(slurp(inputStream), fileName, false);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public String slurp (InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }


    public String detectCharset(InputStream is) throws Exception {
        ChardetSniffer chardetSniffer = new ChardetSniffer();

        // Initalize the nsDetector() ;
        nsDetector det = new nsDetector(nsPSMDetector.ALL);
        
        // Set an observer...
        // The Notify() will be called when a matching charset is found.

        det.Init(chardetSniffer);

        BufferedInputStream imp = new BufferedInputStream(is);

        byte[] buf = new byte[1024] ;
        int len;
        boolean done = false ;
        boolean isAscii = true ;

        while( (len=imp.read(buf,0,buf.length)) != -1) {

            // Check if the stream is only ascii.
            if (isAscii)
                isAscii = det.isAscii(buf,len);

            // DoIt if non-ascii and not done yet.
            if (!isAscii && !done)
                done = det.DoIt(buf,len, false);
        }
        det.DataEnd();

        return chardetSniffer.getCharsetName();
    }

    private final String XSD_APE_SCHEMA = "apeEAD.xsd";
    public String retrieveAPEXsdFile(){
        return getClass().getResource("/" + XSD_APE_SCHEMA).getPath();
    }

    public void copyFile(File src, File dest) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
