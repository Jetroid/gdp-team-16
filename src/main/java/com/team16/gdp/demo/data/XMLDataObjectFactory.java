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
        List<String> caseDataStrings = findXMLData(caseXMLPath, "Case", "ID", id, true);

        if (caseDataStrings.size() == 0){
            return null;
        }

        return makeCaseFromString(caseDataStrings.get(0));
    }

    /**
     * Builds an Annotation Object from the XML store.
     * @param id ID of the Annotation
     * @return Annotation object for the ID.
     */
    public Annotation buildAnnotation(int id){
        List<String> caseDataStrings = findXMLData(annotationXMLPath, "Annotation", "ID", id, true);

        if (caseDataStrings.size() == 0){
            return null;
        }

        //Annotation building and return
        return makeAnnotationFromString(caseDataStrings.get(0));
    }

    /**
     * Builds a Person object from the XML com.team16.gdp.demo.data for the ID given. Returns null if no com.team16.gdp.demo.data exists.
     * @param id Value of the ID
     * @return Person Object
     */
    public Person buildPerson(int id){
        List<String> caseDataStrings = findXMLData(personXMLPath, "Person", "ID", id, true);

        if (caseDataStrings.size() == 0){
            return null;
        }

        String caseDataString = caseDataStrings.get(0);

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

    /**
     * Adds an annotation entry to the XML file with the same field values as the provided object.
     * Will add to the quotations file automatically if needed.
     * @param annotation Annotation to add.
     * @return True if successful
     */
    public boolean addAnnotation(Annotation annotation){

        List<String> annoStrings = findXMLData(annotationXMLPath, "Annotation", null, 0, false);

        //Add all existing annotations and quotations to a list
        List<Annotation> annotations = new ArrayList<>();
        List<Quotation> quotes = new ArrayList<>();

        for (String s : annoStrings){
            Annotation a = makeAnnotationFromString(s);
            annotations.add(a);
            if(!quotes.contains(annotation.getQuote())) {
                quotes.add(a.getQuote());
            }
        }

        //Sorting out indexes and the like:
        //add new annotation to the end with a new index.
        if (annotations.size() == 0){
            annotation.id = 1;
        }else {
            annotation.id = annotations.get(annotations.size() -1).id + 1;
        }
        annotations.add(annotation);

        //add new quote to the end with a new index.
        if (quotes.size() == 0){
            //If no quotes are stored, this get the id of 1.
            annotation.getQuote().id = 1;
        }else {
            if(!quotes.contains(annotation.getQuote())) {
                //If quote is not already stored, give it new id and store it.
                annotation.getQuote().id = quotes.get(quotes.size() -1).id + 1;
                quotes.add(annotation.getQuote());
            }else{
                //get the ID of the same quote that is already stored.
                annotation.getQuote().id = quotes.get(quotes.indexOf(annotation.getQuote())).getId();
            }
        }


        //Write out, and return success.
        return writeOutAnnotations(annotations) && writeOutQuotations(quotes);
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
        Pattern p = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL);
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
        String phText = getXMLFieldString(xmlFields, "Text").replace("\n", "").replace("\t", "");

        //Getting Quote Data
        String quoteDataString = findXMLData(quotesXMLPath, "Quotation", "ID", phQuoteId, true).get(0);
        String quote = getXMLFieldString(quoteDataString, "Text");
        int phStart =  Integer.parseInt(getXMLFieldString(quoteDataString, "StartIndex"));
        int phEnd =  Integer.parseInt(getXMLFieldString(quoteDataString, "EndIndex"));


        Annotation anno = new Annotation(phCaseId, phAuthorId, phText, quote, phStart, phEnd);
        anno.id = phId;
        anno.getQuote().id = phQuoteId;

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

    /**
     * Given a list of annotations, the annotation xml file will be overwritten with this data.
     * @param annotations
     * @return
     */
    private boolean writeOutAnnotations(List<Annotation> annotations){
        try {
            BufferedWriter bw = new BufferedWriter(new PrintWriter(new File(annotationXMLPath)));

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<Annotations>");
            bw.newLine();

            for (Annotation a : annotations){
                bw.write(makeXMLStringFromAnnotation(a));
                bw.newLine();
            }

            bw.write("</Annotations>");

            bw.flush();
            bw.close();
            return true;
        }catch(IOException e) {
            return false;
        }
    }

    /**
     * Makes an XML string that would represent the annotation provided.
     * i.e. -> <Annotation>...</Annotation>
     * @param a Annotation to encode
     * @return Fields in XML format
     */
    private String makeXMLStringFromAnnotation(Annotation a){
        String output = "";

        output = output + "\t<Annotation>\n";

        output = output + "\t\t<ID>" + a.id + "</ID>\n";
        output = output + "\t\t<CaseID>" + a.caseId + "</CaseID>\n";
        output = output + "\t\t<AuthorID>" + a.authorId + "</AuthorID>\n";
        output = output + "\t\t<QuoteID>" + a.getQuote().id + "</QuoteID>\n";
        output = output + "\t\t<Text>\n\t\t\t" + a.getText() + "\n\t\t</Text>\n";

        output = output + "\t</Annotation>\n";

        return output;
    }


    private boolean writeOutQuotations(List<Quotation> quotations){
        try {
            BufferedWriter bw = new BufferedWriter(new PrintWriter(new File(quotesXMLPath)));

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<Quotations>");
            bw.newLine();

            for (Quotation q : quotations){
                bw.write(makeXMLStringFromQuotation(q));
                bw.newLine();
            }

            bw.write("</Quotations>");

            bw.flush();
            bw.close();
            return true;
        }catch(IOException e) {
            return false;
        }
    }

    private String makeXMLStringFromQuotation(Quotation q){
        String output = "";

        output = output + "\t<Quotation>\n";

        output = output + "\t\t<ID>" + q.id + "</ID>\n";
        output = output + "\t\t<StartIndex>" + q.getStartIndex() + "</StartIndex>\n";
        output = output + "\t\t<EndIndex>" + q.getEndIndex() + "</EndIndex>\n";
        output = output + "\t\t<Text>" + q.getQuote() + "</Text>\n";

        output = output + "\t</Quotation>\n";

        return output;
    }




}
