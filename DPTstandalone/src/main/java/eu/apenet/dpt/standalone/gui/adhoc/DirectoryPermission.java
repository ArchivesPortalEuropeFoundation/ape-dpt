package eu.apenet.dpt.standalone.gui.adhoc;

import java.io.File;
import java.io.IOException;

/**
 * User: Yoann Moranville
 * Date: 06/05/13
 */
public class DirectoryPermission {
    public static boolean canWrite(File directory) {
        try {
            File file = writeDummyFile(directory);
            file.delete();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private static File writeDummyFile(File destinationDirectory) throws IOException {
        File newFile = new File(destinationDirectory, "test.txt");
        newFile.createNewFile();
        return newFile;
    }
}
