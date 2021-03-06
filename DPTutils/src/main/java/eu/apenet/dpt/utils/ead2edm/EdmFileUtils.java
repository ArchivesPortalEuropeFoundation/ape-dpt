package eu.apenet.dpt.utils.ead2edm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public final class EdmFileUtils {

    private static final Logger LOGGER = Logger.getLogger(EdmFileUtils.class);
    private static final String UNDERSCORE = "_";
    private static final String OUTPUT_HTML_DIR_NAME = "EDM-HTML";
    private static final String OUTPUT_XML_DIR_NAME = "EDM";
    private static final String OUTPUT_DIR_NAME = "EUROPEANA";

    public static final String SEPARATOR = "/";

    public static File getOutputEDMDir(String repoDirPath, String countryISOname, int aiId) {
        String path = repoDirPath + SEPARATOR + getRelativeEADDirPath(countryISOname, aiId) + SEPARATOR + OUTPUT_DIR_NAME + SEPARATOR + OUTPUT_XML_DIR_NAME;
        return new File(path);
    }

    public static File getOutputEDMDir(String baseDir, String eadid) {
        return new File(baseDir + SEPARATOR + eadid);
    }

    public static String getOutputEDMDirPath(String repoDirPath, String countryISOname, int aiId) {
        return repoDirPath + SEPARATOR + getRelativeEADDirPath(countryISOname, aiId) + SEPARATOR + OUTPUT_DIR_NAME + SEPARATOR + OUTPUT_XML_DIR_NAME;
    }

    public static File getOutputHTMLDir(String repoDirPath, String countryISOname, int aiId) {
        String path = repoDirPath + SEPARATOR + getRelativeEADDirPath(countryISOname, aiId) + SEPARATOR + OUTPUT_DIR_NAME + SEPARATOR + OUTPUT_HTML_DIR_NAME;
        return new File(path);
    }

    public static String getRelativeEDMFilePath(String countryISOname, int aiId, String edmFilePath) {
        return getRelativeEADDirPath(countryISOname, aiId) + SEPARATOR + OUTPUT_DIR_NAME + SEPARATOR + OUTPUT_XML_DIR_NAME + SEPARATOR + edmFilePath;
    }

    public static String getRelativeEDMHTMLFilePath(String countryISOname, int aiId, String edmFilePath) {
        return getRelativeEADDirPath(countryISOname, aiId) + SEPARATOR + OUTPUT_DIR_NAME + SEPARATOR + OUTPUT_HTML_DIR_NAME + SEPARATOR + edmFilePath;
    }

    public static String getRelativeEADDirPath(String countryISOname, int aiId) {
        return SEPARATOR + countryISOname + SEPARATOR + aiId;
    }

    public static File getTempFile(String tempDirPath, String countryISOname, int aiId, String name) {
        String fileName = countryISOname + UNDERSCORE + aiId + UNDERSCORE + name;
        return new File(tempDirPath + SEPARATOR + fileName);
    }

    public static String getFileName(String fileSeparator, File file) {
        String filePath = file.getAbsolutePath();
        filePath = filePath.replace('\\', '/');
        int lastIndex = filePath.lastIndexOf(fileSeparator);
        filePath = filePath.substring(lastIndex + 1);
        return filePath;
    }

    public static void deleteDir(File tempDir) {
        deleteFiles(tempDir.listFiles());
        tempDir.delete();
    }

    private static void deleteFiles(File[] files) {
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    // delete childs
                    deleteFiles(files[i].listFiles());
                }
                files[i].delete();
            }
        }
    }

    public static File createDir(File dir, String name) {
        File tempDir = new File(dir.getAbsolutePath() + SEPARATOR + name);
        tempDir.mkdirs();
        return tempDir;
    }

    public static File createDir(String dir, String name) {
        return createDir(new File(dir), name);

    }

    public static File getRepoFile(String repoDirPath, String name) {
        File tempFile = new File(repoDirPath + SEPARATOR + name);
        return tempFile;

    }

    public static File getFile(File dir, String name) {
        File tempFile = new File(dir.getAbsolutePath() + SEPARATOR + name);
        return tempFile;
    }

    public static String getRelativePath(File dir, File file) {
        return getRelativePath(dir.getAbsolutePath(), file);
    }

    public static String getRelativePath(String prefix, File file) {
        String filePath = file.getAbsolutePath();
        filePath = filePath.substring(prefix.length() + 1);
        return filePath;
    }

    public static void unzip(File outputDir, File file) throws ZipException, IOException {
        String prefix = outputDir.getAbsolutePath() + EdmFileUtils.SEPARATOR;
        Enumeration<? extends ZipEntry> entries;
        ZipFile zipFile;
        zipFile = new ZipFile(file);

        entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            File extractedFile = new File(prefix + entry.getName());
            if (entry.isDirectory()) {
                extractedFile.mkdirs();
            } else {
                File parentDirectory = extractedFile.getParentFile();
                if (!parentDirectory.exists()) {
                    parentDirectory.mkdirs();
                }
                FileOutputStream fileoutputstream = new FileOutputStream(extractedFile);
                InputStream inputStream = zipFile.getInputStream(entry);
                int n = -1;
                byte[] buf = new byte[1024];
                while ((n = inputStream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }
                fileoutputstream.close();
                zipFile.getInputStream(entry);

            }

        }

        zipFile.close();

    }

    public static void zip(File sourceDir, File zipfile) throws IOException {
        // Check that the directory is a directory, and get its contents
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory:  " + sourceDir.getPath());
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipfile));
        zip(sourceDir.listFiles(), zipOutputStream, sourceDir.getAbsolutePath());
        zipOutputStream.close();
    }

    private static void zip(File[] files, ZipOutputStream zipOutputStream, String sourceDirPath) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) {
                zip(file.listFiles(), zipOutputStream, sourceDirPath);
            } else {
                byte[] buffer = new byte[1024]; // Create a buffer for copying
                int bytesRead;
                ZipEntry entry = new ZipEntry(getRelativePath(sourceDirPath, file));
                zipOutputStream.putNextEntry(entry);
                FileInputStream fileInputStream = new FileInputStream(file); // Stream
                // to
                // read
                // file
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                }
                zipOutputStream.closeEntry();
                fileInputStream.close();

            }
        }
    }

    public static void copyDir(File sourceDir, File destDir) {
        for (File file : sourceDir.listFiles(new NotSVNFilenameFilter())) {
            if (file.isDirectory()) {
                File newDestDir = createDir(destDir, file.getName());
            } else {
                File dest = getFile(destDir, file.getName());
            }
        }
    }

    public static int createDirs(String filePath, int numberOfRecords) {
        double temp = numberOfRecords;
        int recordsPerDirectory = 100;
        int start = 1;
        while (temp > recordsPerDirectory) {
            temp = temp / recordsPerDirectory;
            start = start * recordsPerDirectory;
        }
        if (start == 1) {
            start = 100;
        }
        return createDirs(filePath, numberOfRecords, start, recordsPerDirectory, 0);
    }

    private static int createDirs(String prefix, int numberOfRecords, int currentRecordsPerDirectory,
            int recordsPerDirectory, int startNumber) {
        boolean finished = false;
        int numberOfDirectoryCreated = 0;
        boolean node = currentRecordsPerDirectory > recordsPerDirectory;
        for (int i = 0; i < recordsPerDirectory & !finished; i++) {
            int temp = (i * currentRecordsPerDirectory) + startNumber;
            if (temp > numberOfRecords) {
                finished = true;
            } else {
                String dir = prefix + File.separator + temp;
                File fileDir = new File(dir);
                fileDir.mkdirs();
                numberOfDirectoryCreated++;
                if (node) {
                    numberOfDirectoryCreated += createDirs(dir, numberOfRecords, currentRecordsPerDirectory
                            / recordsPerDirectory, recordsPerDirectory, temp);

                }
            }
        }
        return numberOfDirectoryCreated;
    }

    public static String encodeSpecialCharactersForFilename(String input) {
        if (input != null) {
            try {
                String result = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
                result = result.replaceAll(" ", "%20");
                result = result.replaceAll("\\*", "%2A");
                result = result.replaceAll("\\/", "%2F");
                result = result.replaceAll("\\[", "%5B");
                result = result.replaceAll("\\]", "%5D");
                result = result.replaceAll("\\#", "%23");
                result = result.replaceAll("\\?", "%3F");
                return result;
            } catch (UnsupportedEncodingException ex) {
                LOGGER.log(Priority.ERROR, null, ex);
            }
//            //Step 1: Replace diacritical characters with their standard variants
////            String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
////            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
////            String result = pattern.matcher(nfdNormalizedString).replaceAll("");
//
//            //Step 2: Replace special characters
//            //result = result.replaceAll(":", "_COLON_");
////            result = result.replaceAll("\\*", "_ASTERISK_");
////            result = result.replaceAll("=", "_COMP_");
//            //result = result.replaceAll("/", "_SLASH_");
//            String result = input.replaceAll("/", "_SLASH_");
//            result = result.replaceAll("\\\\", "_BSLASH_");
////            result = result.replaceAll("\\[", "_LSQBRKT_");
////            result = result.replaceAll("\\]", "_RSQBRKT_");
////            result = result.replaceAll("\\+", "_PLUS_");
////            result = result.replaceAll("%", "_PERCENT_");
////            result = result.replaceAll("@", "_ATCHAR_");
////            result = result.replaceAll("\\$", "_DOLLAR_");
////            result = result.replaceAll("#", "_HASH_");
////            result = result.replaceAll("\\^", "_CFLEX_");
////            result = result.replaceAll("&", "_AMP_");
////            result = result.replaceAll("\\(", "_LRDBRKT_");
////            result = result.replaceAll("\\)", "_RRDBRKT_");
////            result = result.replaceAll("!", "_EXCLMARK_");
////            result = result.replaceAll("~", "_TILDE_");
////            result = result.replaceAll("<", "_LT_");
////            result = result.replaceAll(">", "_GT_");
////            result = result.replaceAll("\"", "_QUOTE_");
////            result = result.replaceAll("'", "_SQUOTE_");
////            result = result.replaceAll(",", "_COMMA_");
////            result = result.replaceAll(";", "_SEMICOLON_");
////            result = result.replaceAll("\\{", "_LSCUBRKT_");
////            result = result.replaceAll("\\}", "_RSCUBRKT_");
//            result = result.replaceAll("\\s", "+");
//            return result;
        } else {
            return null;
        }
        return null;
    }

    private static class NotSVNFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File arg0, String name) {
            return !".svn".equalsIgnoreCase(name);
        }
    }
}
