package eu.apenet.dpt.standalone.gui.adhoc;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.Comparator;

/**
 * User: Yoann Moranville
 * Date: Dec 16, 2010
 *
 * @author Yoann Moranville
 */
public class FileNameComparator implements Comparator<File> {
    private static final Logger LOG = Logger.getLogger(FileNameComparator.class);

    public int compare(File fileaf, File filebf) {
        String filea = fileaf.getName();
        String fileb = filebf.getName();

        boolean check = false;
        if(filea.equals("NL-HaNA_1.10.10.ead") || filea.equals("NL-HaNA_1.10.100.ead") || fileb.equals("NL-HaNA_1.10.10.ead") || fileb.equals("NL-HaNA_1.10.100.ead")){
            check = true;
        }

        if(check){
            LOG.debug(filea + " -- " + fileb);
        }

        String[] fileas = filea.split("\\.");
        String[] filebs = fileb.split("\\.");


        for(int i = 0; i < fileas.length; i++){
            int end = 0;
            if(i < filebs.length){
                String sa = fileas[i];
                String sb = filebs[i];

                while(sa.length() > 1 && sa.charAt(0) == '0'){
                    sa = sa.substring(1);
                }
                while(sb.length() > 1 && sb.charAt(0) == '0'){
                    sb = sb.substring(1);
                }

                if(!sb.equals(sa)){
                    try {
                        Integer a, b;
                        if(((a = Integer.parseInt(sa)) > -1) && (b = Integer.parseInt(sb)) > -1){
                            return a.compareTo(b);
                        }
                    }catch (Exception e){
                        return sa.compareToIgnoreCase(sb);
                    }
                }
            }
        }

        return filea.compareToIgnoreCase(fileb);
    }
}
