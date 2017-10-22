package com.team16.gdp.demo.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andy on 19/10/2017.
 */
public class XMLDataObjectFactory {

    String caseXMLPath;
    String annotationXMLPath;
    String personXMLPath;
    String quotesXMLPath;

    public XMLDataObjectFactory(String casesXML, String annosXML, String peopleXML, String quotesXML){
        caseXMLPath = casesXML;
        annotationXMLPath = annosXML;
        personXMLPath = peopleXML;
        quotesXMLPath = quotesXML;
    }

    /**
     *Builds a Case Object from com.team16.gdp.demo.data stored in the XML store.
     * @param id ID of the case
     * @return Case object for that ID
     */
    public Case buildCase(int id){
        String caseDataString = findXMLData(caseXMLPath, "Case", "ID", id, true).get(0);

        if (caseDataString == null){
            return null;
        }

        return makeCaseFromString(caseDataString);
    }

    /**
     * Builds an Annotation Object from the XML store.
     * @param id ID of the Annotation
     * @return Annotation object for the ID.
     */
    public Annotation buildAnnotation(int id){
        String caseDataString = findXMLData(annotationXMLPath, "Annotation", "ID", id, true).get(0);

        if (caseDataString == null){
            return null;
        }

        //Annotation building and return
        return makeAnnotationFromString(caseDataString);
    }

    /**
     * Builds a Person object from the XML com.team16.gdp.demo.data for the ID given. Returns null if no com.team16.gdp.demo.data exists.
     * @param id Value of the ID
     * @return Person Object
     */
    public Person buildPerson(int id){
        String caseDataString = findXMLData(personXMLPath, "Person", "ID", id, true).get(0);

        if (caseDataString == null){
            return null;
        }

        int phId = id;
        String phForename = getXMLFieldString(caseDataString, "Forename");
        String phSurname = getXMLFieldString(caseDataString, "Surname");
        String phEmail = getXMLFieldString(caseDataString, "Email");

        Person p = new Person();
        p.id = phId;
        p.forename = phForename;
        p.surname = phSurname;
        p.email = phEmail;

        return p;
    }

    /**
     * Returns a list of all the annotations associated with a case in order of first submission (ID).
     * @param caseID ID of the case
     * @return List of annotations
     */
    public List<Annotation> getAnnotationsForCase(int caseID){
        ArrayList<Annotation> annotations = new ArrayList<>();

        List<String> xmlDataStrings = findXMLData(annotationXMLPath, "Annotation", "CaseID", caseID, false);

        for (String s : xmlDataStrings){
            annotations.add(makeAnnotationFromString(s));
        }

        return annotations;
    }

    /**
     * Returns all cases stored within the XML file as a list of case objects.
     * @return List of all cases.
     */
    public List<Case> getAllCases(){
        ArrayList<Case> cases = new ArrayList<>();

        List<String> xmlDataStrings = findXMLData(caseXMLPath, "Case", null, 0, false);

        for (String s : xmlDataStrings){
            cases.add(makeCaseFromString(s));
        }

        return cases;
    }

    //TODO This method :L
    public boolean addAnnotation(Annotation annotation){

        return false;
    }







    //  ------  Helping Functions  -------


    /**
     * Method for searching for an item satisfying a condition for one of it's fields.
     * Supplying a null value to idTag will return all items.
     * @param filePath XML file to search
     * @param parentTag Item needed
     * @param idTag Tag for use in identification
     * @param idVal Value for the identification tag to have
     * @param first Flag fo only returning first value found
     * @return Fields of the item satisfying the condition given in XML format.
     */
    private List<String> findXMLData(String filePath, String parentTag, String idTag, int idVal, boolean first){
        ArrayList<String> items = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

            String item = "";
            while(br.ready()){
                String line = br.readLine();

                //Start of new Annotation, clear old com.team16.gdp.demo.data
                if (line.contains("<"+parentTag+">")){
                    item = "";
                }

                //End of annotation, check id and return if needed.
                if(line.contains("</"+parentTag+">")){
                    if (idTag == null || getXMLFieldString(item, idTag).equals(idVal+"")){
                        items.add(item);
                        if(first){
                            return items;
                        } else {
                            continue;
                        }
                    }else {
                        continue;
                    }
                }

                //if not a start or end tag, add to the com.team16.gdp.demo.data.
                item = item + "\n" + line;

            }

        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        return items;
    }


    /**
     * Extracts the value of a field from an XML String
     * @param XMLdata String in XML format
     * @param tag Tag of field needed.
     * @return Value of first field with the tag
     */
    private String getXMLFieldString(String XMLdata, String tag){
        Pattern p = Pattern.compile("<"+ tag +">(.*?)</"+ tag +">", Pattern.DOTALL);
        Matcher m = p.matcher(XMLdata);
        m.find();
        return m.group(1);
    }

    /**
     * Builds an annotation object from a string of fields in xml format.
     * @param xmlFields Fields in xml format
     * @return Annotation object
     */
    private Annotation makeAnnotationFromString(String xmlFields){
        int phId = Integer.parseInt(getXMLFieldString(xmlFields, "ID"));
        int phCaseId = Integer.parseInt(getXMLFieldString(xmlFields, "CaseID"));
        int phAuthorId = Integer.parseInt(getXMLFieldString(xmlFields, "AuthorID"));
        int phQuoteId = Integer.parseInt(getXMLFieldString(xmlFields, "QuoteID"));
        String phText = getXMLFieldString(xmlFields, "Text");

        //Getting Quote Data
        String quoteDataString = findXMLData(quotesXMLPath, "Quotation", "ID", phQuoteId, true).get(0);
        String quote = getXMLFieldString(quoteDataString, "Text");
        int phStart =  Integer.parseInt(getXMLFieldString(quoteDataString, "StartIndex"));
        int phEnd =  Integer.parseInt(getXMLFieldString(quoteDataString, "EndIndex"));


        Annotation anno = new Annotation(phAuthorId, phText, quote, phStart, phEnd);
        anno.id = phId;
        anno.caseId = phCaseId;

        return anno;
    }

    /**
     * Builds a case object from a string of case fields in xml format.
     * @param xmlFields Fields in xml format
     * @return Case object
     */
    private Case makeCaseFromString(String xmlFields){
        int phId = Integer.parseInt(getXMLFieldString(xmlFields, "ID"));
        int phVersion = Integer.parseInt(getXMLFieldString(xmlFields, "Version"));
        int phAuthorId = Integer.parseInt(getXMLFieldString(xmlFields, "AuthorID"));
        boolean phAllowsAnnotations = Boolean.parseBoolean(getXMLFieldString(xmlFields, "AllowAnnotations"));
        String phCaseText = getXMLFieldString(xmlFields, "Text");

        Case c = new Case(phCaseText, phAuthorId);
        c.id = phId;
        c.version = phVersion;
        c.allowsAnnotations = phAllowsAnnotations;

        return c;
    }


}
