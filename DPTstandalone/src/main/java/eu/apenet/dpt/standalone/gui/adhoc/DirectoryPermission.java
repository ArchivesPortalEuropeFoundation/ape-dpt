package eu.apenet.dpt.standalone.gui.adhoc;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */


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
