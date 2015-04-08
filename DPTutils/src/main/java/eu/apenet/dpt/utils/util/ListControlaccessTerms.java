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

    private static final String SUBJECT = "subject";
    private static final String PERSNAME = "persname";
    private static final String FAMNAME = "famname";
    private static final String CORPNAME = "corpname";
    private static final String GENREFORM = "genreform";
    private static final String FUNCTION = "function";
    private static final String OCCUPATION = "occupation";

    private Map<String, Integer> subjects;
    private Map<String, Integer> persnames;
    private Map<String, Integer> famnames;
    private Map<String, Integer> corpnames;
    private Map<String, Integer> genreforms;
    private Map<String, Integer> functions;
    private Map<String, Integer> occupations;
    private ValueComparator bvc;
    private TreeMap<String, Integer> sorted_maps;

    public ListControlaccessTerms(String path){
        subjects = new HashMap<String, Integer>();
        persnames = new HashMap<String, Integer>();
        famnames = new HashMap<String, Integer>();
        corpnames = new HashMap<String, Integer>();
        genreforms = new HashMap<String, Integer>();
        functions = new HashMap<String, Integer>();
        occupations = new HashMap<String, Integer>();
        bvc =  new ValueComparator(subjects);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        this.path = path;
    }

    public ListControlaccessTerms(List<File> files){
        subjects = new HashMap<String, Integer>();
        persnames = new HashMap<String, Integer>();
        famnames = new HashMap<String, Integer>();
        corpnames = new HashMap<String, Integer>();
        genreforms = new HashMap<String, Integer>();
        functions = new HashMap<String, Integer>();
        occupations = new HashMap<String, Integer>();
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

    public String retrieveResults(ResourceBundle labels) {
        StringBuilder builder = new StringBuilder();
        builder.append(labels.getString("list.controlaccess.wefound")).append(" ").append(subjects.size()).append(" ").append(SUBJECT).append(" ").append(labels.getString("list.controlaccess.termsinfiles")).append("\n");
        builder.append(labels.getString("list.controlaccess.wefound")).append(" ").append(persnames.size()).append(" ").append(PERSNAME).append(" ").append(labels.getString("list.controlaccess.termsinfiles")).append("\n");
        builder.append(labels.getString("list.controlaccess.wefound")).append(" ").append(famnames.size()).append(" ").append(FAMNAME).append(" ").append(labels.getString("list.controlaccess.termsinfiles")).append("\n");
        builder.append(labels.getString("list.controlaccess.wefound")).append(" ").append(corpnames.size()).append(" ").append(CORPNAME).append(" ").append(labels.getString("list.controlaccess.termsinfiles")).append("\n");
        builder.append(labels.getString("list.controlaccess.wefound")).append(" ").append(genreforms.size()).append(" ").append(GENREFORM).append(" ").append(labels.getString("list.controlaccess.termsinfiles")).append("\n");
        builder.append(labels.getString("list.controlaccess.wefound")).append(" ").append(functions.size()).append(" ").append(FUNCTION).append(" ").append(labels.getString("list.controlaccess.termsinfiles")).append("\n");
        builder.append(labels.getString("list.controlaccess.wefound")).append(" ").append(occupations.size()).append(" ").append(OCCUPATION).append(" ").append(labels.getString("list.controlaccess.termsinfiles")).append("\n");
        builder.append("================ Subject ================\n");
        sorted_maps.putAll(subjects);
        for(String term : sorted_maps.keySet()) {
            builder.append("Subject: '").append(term).append("', ").append(labels.getString("list.controlaccess.size")).append(" ").append(subjects.get(term)).append("\n");
        }

        builder.append("================ Persname ================\n");
        bvc =  new ValueComparator(persnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(persnames);
        for(String term : sorted_maps.keySet()) {
            builder.append("Persname: '").append(term).append("', ").append(labels.getString("list.controlaccess.size")).append(" ").append(persnames.get(term)).append("\n");
        }

        builder.append("================ Famname ================\n");
        bvc =  new ValueComparator(famnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(famnames);
        for(String term : sorted_maps.keySet()) {
            builder.append("Famname: '").append(term).append("', ").append(labels.getString("list.controlaccess.size")).append(" ").append(famnames.get(term)).append("\n");
        }

        builder.append("================ Corpname ================\n");
        bvc =  new ValueComparator(corpnames);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(corpnames);
        for(String term : sorted_maps.keySet()) {
            builder.append("Corpname: '").append(term).append("', ").append(labels.getString("list.controlaccess.size")).append(" ").append(corpnames.get(term)).append("\n");
        }

        builder.append("================ Genreform ================\n");
        bvc =  new ValueComparator(genreforms);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(genreforms);
        for(String term : sorted_maps.keySet()) {
            builder.append("Genreform: '").append(term).append("', ").append(labels.getString("list.controlaccess.size")).append(" ").append(genreforms.get(term)).append("\n");
        }

        builder.append("================ Function ================\n");
        bvc =  new ValueComparator(functions);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(functions);
        for(String term : sorted_maps.keySet()) {
            builder.append("Function: '").append(term).append("', ").append(labels.getString("list.controlaccess.size")).append(" ").append(functions.get(term)).append("\n");
        }

        builder.append("================ Occupation ================\n");
        bvc =  new ValueComparator(occupations);
        sorted_maps = new TreeMap<String, Integer>(bvc);
        sorted_maps.putAll(occupations);
        for(String term : sorted_maps.keySet()) {
            builder.append("Occupation: '").append(term).append("', ").append(labels.getString("list.controlaccess.size")).append(" ").append(occupations.get(term)).append("\n");
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
        boolean isGenreform = false;
        boolean isFunction = false;
        boolean isOccupation = false;
        String term = "";
        while (true) {
            switch (input.getEventType()) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:
                    if(input.getLocalName().equals(SUBJECT)){
                        isSubject = true;
                    } else if(input.getLocalName().equals(PERSNAME)){
                        isPersname = true;
                    } else if(input.getLocalName().equals(FAMNAME)){
                        isFamname = true;
                    } else if(input.getLocalName().equals(CORPNAME)){
                        isCorpname = true;
                    } else if(input.getLocalName().equals(GENREFORM)){
                        isGenreform = true;
                    } else if(input.getLocalName().equals(FUNCTION)){
                        isFunction = true;
                    } else if(input.getLocalName().equals(OCCUPATION)){
                        isOccupation = true;
                    }
                    break;
                case XMLEvent.CHARACTERS:
                case XMLEvent.CDATA:
                    if(isSubject || isPersname || isFamname || isCorpname || isGenreform || isFunction || isOccupation){
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
                            } else if(isGenreform) {
                                if (genreforms.get(term) == null) {
                                    genreforms.put(term, 1);
                                } else {
                                    genreforms.put(term, genreforms.get(term) + 1);
                                }
                            } else if(isFunction) {
                                if (functions.get(term) == null) {
                                    functions.put(term, 1);
                                } else {
                                    functions.put(term, functions.get(term) + 1);
                                }
                            } else if(isOccupation) {
                                if (occupations.get(term) == null) {
                                    occupations.put(term, 1);
                                } else {
                                    occupations.put(term, occupations.get(term) + 1);
                                }
                            }
                        }
                        isSubject = false;
                        isPersname = false;
                        isFamname = false;
                        isCorpname = false;
                        isGenreform = false;
                        isFunction = false;
                        isOccupation = false;
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