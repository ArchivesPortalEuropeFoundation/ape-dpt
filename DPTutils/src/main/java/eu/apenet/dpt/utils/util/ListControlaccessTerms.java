package eu.apenet.dpt.utils.util;

import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

/**
 * User: Yoann Moranville
 * Date: Dec 16, 2014
 *
 * @author Yoann Moranville
 */
public class ListControlaccessTerms {
    private static final Logger LOG = Logger.getLogger(ListControlaccessTerms.class);
    private String path;
    private List<File> files;

    private Map<String, Integer> subjects;
    private Map<String, Integer> persnames;
    private Map<String, Integer> famnames;
    private Map<String, Integer> corpnames;
    private ValueComparator bvc;
    private TreeMap<String, Integer> sorted_maps;

    public ListControlaccessTerms(String path){
        subjects = new HashMap<String, Integer>();
        persnames = new HashMap<String, Integer>();
        famnames = new HashMap<String, Integer>();
        corpnames = new HashMap<String, Integer>();
        bvc =  new ValueComparator(subjects);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        this.path = path;
    }

    public ListControlaccessTerms(List<File> files){
        subjects = new HashMap<String, Integer>();
        persnames = new HashMap<String, Integer>();
        famnames = new HashMap<String, Integer>();
        corpnames = new HashMap<String, Integer>();
        bvc =  new ValueComparator(subjects);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        this.files = files;
    }

    public void countTerms(){
        if(files == null) {
            File directory = new File(path);
            if (directory.isDirectory()) {
                files = Arrays.asList(directory.listFiles());
            }
        }
        for(File file : files){
            if(file.getName().endsWith(".xml")){
                analyzeFile(file);
            }
        }
    }

    public void displayLogsResults() {
        LOG.info("We found " + subjects.size() + " subject terms in those files");
        LOG.info("We found " + persnames.size() + " persname terms in those files");
        LOG.info("We found " + famnames.size() + " famname terms in those files");
        LOG.info("We found " + corpnames.size() + " corpname terms in those files");
        LOG.info("================ Subject ================");
        sorted_maps.putAll(subjects);
        for(String term : sorted_maps.keySet()) {
            LOG.info("Term: '" + term + "', size: " + subjects.get(term));
        }

        LOG.info("================ Persname ================");
        bvc =  new ValueComparator(persnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(persnames);
        for(String term : sorted_maps.keySet()) {
            LOG.info("Persname: '" + term + "', size: " + persnames.get(term));
        }

        LOG.info("================ Famname ================");
        bvc =  new ValueComparator(famnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(famnames);
        for(String term : sorted_maps.keySet()) {
            LOG.info("Famname: '" + term + "', size: " + famnames.get(term));
        }

        LOG.info("================ Corpname ================");
        bvc =  new ValueComparator(corpnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(corpnames);
        for(String term : sorted_maps.keySet()) {
            LOG.info("Corpname: '" + term + "', size: " + corpnames.get(term));
        }
    }

    public String retrieveResults() {
        StringBuilder builder = new StringBuilder();
        builder.append("We found ").append(subjects.size()).append(" subject terms in those files\n");
        builder.append("We found ").append(persnames.size()).append(" persname terms in those files\n");
        builder.append("We found ").append(famnames.size()).append(" famname terms in those files\n");
        builder.append("We found ").append(corpnames.size()).append(" corpname terms in those files\n");
        builder.append("================ Subject ================\n");
        sorted_maps.putAll(subjects);
        for(String term : sorted_maps.keySet()) {
            builder.append("Term: '").append(term).append("', size: ").append(subjects.get(term)).append("\n");
        }

        builder.append("================ Persname ================\n");
        bvc =  new ValueComparator(persnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(persnames);
        for(String term : sorted_maps.keySet()) {
            builder.append("Persname: '").append(term).append("', size: ").append(persnames.get(term)).append("\n");
        }

        builder.append("================ Famname ================\n");
        bvc =  new ValueComparator(famnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(famnames);
        for(String term : sorted_maps.keySet()) {
            builder.append("Famname: '").append(term).append("', size: ").append(famnames.get(term)).append("\n");
        }

        builder.append("================ Corpname ================\n");
        bvc =  new ValueComparator(corpnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(corpnames);
        for(String term : sorted_maps.keySet()) {
            builder.append("Corpname: '").append(term).append("', size: ").append(corpnames.get(term)).append("\n");
        }

        return builder.toString();
    }

    private int analyzeFile(File file){
        try {
            return run(file);
        } catch (XMLStreamException e){
            LOG.error("XMLStreamException", e);
            return -1;
        }
    }

    private int run(File file) throws XMLStreamException {
        int count = 0;

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.configureForSpeed();

        XMLStreamReader2 input = xmlif.createXMLStreamReader(file);

        boolean isSubject = false;
        boolean isPersname = false;
        boolean isFamname = false;
        boolean isCorpname = false;
        String term = "";
        while (true) {
            switch (input.getEventType()) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:
                    if(input.getLocalName().equals("subject")){
                        isSubject = true;
                    } else if(input.getLocalName().equals("persname")){
                        isPersname = true;
                    } else if(input.getLocalName().equals("famname")){
                        isFamname = true;
                    } else if(input.getLocalName().equals("corpname")){
                        isCorpname = true;
                    }
                    break;
                case XMLEvent.CHARACTERS:
                case XMLEvent.CDATA:
                    if(isSubject || isPersname || isFamname || isCorpname){
                        term = input.getText().trim();
                        if(!term.equals("")) {
                            if(isSubject) {
                                if (subjects.get(term) == null) {
                                    subjects.put(term, 1);
                                } else {
                                    subjects.put(term, subjects.get(term) + 1);
                                }
                            } else if(isPersname) {
                                if (persnames.get(term) == null) {
                                    persnames.put(term, 1);
                                } else {
                                    persnames.put(term, persnames.get(term) + 1);
                                }
                            } else if(isFamname) {
                                if (famnames.get(term) == null) {
                                    famnames.put(term, 1);
                                } else {
                                    famnames.put(term, famnames.get(term) + 1);
                                }
                            } else if(isCorpname) {
                                if (corpnames.get(term) == null) {
                                    corpnames.put(term, 1);
                                } else {
                                    corpnames.put(term, corpnames.get(term) + 1);
                                }
                            }
                        }
                        isSubject = false;
                        isPersname = false;
                        isFamname = false;
                        isCorpname = false;
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    break;
            }
            if (!input.hasNext())
                break;
            try {
                input.next();
            } catch (Exception e){
                LOG.error("Error when trying to get the next input of XMLStreamReader", e);
                return -1;
            }
        }
        return count;
    }

    public class ValueComparator implements Comparator<String> {
        private Map<String, Integer> base;
        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}