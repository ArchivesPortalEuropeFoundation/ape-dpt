package eu.apenet.dpt.utils.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Kostas Stamatis
 * Date: 12/12/2021
 *
 * @author Kostas Stamatis
 */
public class RightsInformationList {
    private static final Logger LOG = Logger.getLogger(RightsInformationList.class);

    public static String[] getRightsInformationStringList(){
        List<String> response = new ArrayList<>();
        response.add("--");
        List<RightsInformation> allRights = getRightsInformationList();
        for (RightsInformation rightsInformation : allRights){
            response.add(rightsInformation.getName());
        }

        return response.toArray(new String[response.size()]);
    }

    public static List<RightsInformation> getRightsInformationList() {
        LinkedList<RightsInformation> rightsList = new LinkedList<RightsInformation>();

        InputStream inputStream = RightsInformationList.class.getResourceAsStream("/rightsInformation.json");
        String jsonString = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        RightsInformation[] allRights = new Gson().fromJson(jsonString, RightsInformation[].class);

        for (RightsInformation rightsInformation : allRights) {
            rightsList.add(rightsInformation);
        }

        return rightsList;
    }

    public static RightsInformation getRightsInformationByName (String name){
        List<RightsInformation> allRights = getRightsInformationList();
        for (RightsInformation rightsInformation : allRights){
            if (rightsInformation.getName().equals(name)){
                return rightsInformation;
            }
        }
        return null;
    }
}
