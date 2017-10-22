package com.team16.gdp.demo.data;

import com.sun.org.apache.xpath.internal.operations.Quo;

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
     *Builds a Case Object from the XML store.
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
     * Builds a Person object from the XML file for the ID given. Returns null if no person with this ID exists.
     * @param id Value of the ID
     * @return Person Object
     */
    public Person buildPerson(int id){
        List<String> caseDataStrings = findXMLData(personXMLPath, "Person", "ID", id, true);

        if (caseDataStrings.size() == 0){
            return null;
        }

        String caseDataString = caseDataStrings.get(0);

        String forename = getXMLFieldString(caseDataString, "Forename");
        String surname = getXMLFieldString(caseDataString, "Surname");
        String email = getXMLFieldString(caseDataString, "Email");

        Person p = new Person();
        p.id = id;
        p.forename = forename;
        p.surname = surname;
        p.email = email;

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
        //Check if the quote needs to be added first and get the quote ID we need later:
        int quoteID = addQuotation(annotation.getQuote());

        //Add all existing annotations to a list
        List<String> annoStrings = findXMLData(annotationXMLPath, "Annotation", null, 0, false);
        List<Annotation> annotations = new ArrayList<>();

        for (String s : annoStrings){
            Annotation a = makeAnnotationFromString(s);
            annotations.add(a);
        }

        //Sorting out indexes and the like:
        //add the new annotation to the end of the list with a new index.
        if (annotations.size() == 0){
            annotation.id = 1;
        }else {
            annotation.id = annotations.get(annotations.size() -1).id + 1;
        }
        annotations.add(annotation);

        //Write out, and return success state.
        return writeOutAnnotations(annotations);
    }

    /**
     * Adds a quotation to the xml file if it is not already there, and returns the ID for the given quotation.
     * @param q A quotation to add
     * @return ID of the given Quotation in the XML file.
     */
    public Integer addQuotation(Quotation q){
        //Read all data into an list of data objects
        List<String> annoStrings = findXMLData(quotesXMLPath, "Quotation", null, 0, false);
        List<Quotation> quotes = new ArrayList<>();
        for(String s : annoStrings){
            quotes.add(makeQuotationFromString(s));
        }

        //Now we check if the Quotation is already stored in the XML file. If it is, we don't need to add it again.
        if (quotes.contains(q)){
            //return id of the quotation as it appears in the list.
            q.id = quotes.get(quotes.indexOf(q)).getId();
            return q.getId();

        }else {
            //give the new quote an appropriate ID and add it to the file.
            if (quotes.size() == 0){
                q.id = 1;
            }else {
                q.id = quotes.get(quotes.size() -1).getId() + 1;
            }

            //add to file:
            quotes.add(q);
            writeOutQuotations(quotes);

            //return the new ID.
            return q.getId();
        }
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
        int id = Integer.parseInt(getXMLFieldString(xmlFields, "ID"));
        int caseId = Integer.parseInt(getXMLFieldString(xmlFields, "CaseID"));
        int authorId = Integer.parseInt(getXMLFieldString(xmlFields, "AuthorID"));
        int quoteId = Integer.parseInt(getXMLFieldString(xmlFields, "QuoteID"));
        String text = getXMLFieldString(xmlFields, "Text").replace("\n", "").replace("\t", "");

        //Getting Quote Data
        String quoteDataString = findXMLData(quotesXMLPath, "Quotation", "ID", quoteId, true).get(0);
        Quotation q = makeQuotationFromString(quoteDataString);

        Annotation anno = new Annotation(caseId, authorId, text, q.getQuote(), q.getStartIndex(), q.getEndIndex());
        anno.id = id;
        anno.getQuote().id = quoteId;

        return anno;
    }

    /**
     * Builds a quotation object from a string of fields in xml format.
     * @param xmlFields Quotation fields in xml format.
     * @return Quotation object
     */
    private Quotation makeQuotationFromString(String xmlFields){
        int id = Integer.parseInt(getXMLFieldString(xmlFields, "ID"));
        String quote = getXMLFieldString(xmlFields, "Text");
        int start = Integer.parseInt(getXMLFieldString(xmlFields, "StartIndex"));
        int end = Integer.parseInt(getXMLFieldString(xmlFields, "EndIndex"));

        Quotation q = new Quotation(quote, start, end);
        q.id = id;

        return q;
    }


    /**
     * Builds a case object from a string of case fields in xml format.
     * @param xmlFields Fields in xml format
     * @return Case object
     */
    private Case makeCaseFromString(String xmlFields){
        int id = Integer.parseInt(getXMLFieldString(xmlFields, "ID"));
        int version = Integer.parseInt(getXMLFieldString(xmlFields, "Version"));
        int authorId = Integer.parseInt(getXMLFieldString(xmlFields, "AuthorID"));
        boolean allowsAnnotations = Boolean.parseBoolean(getXMLFieldString(xmlFields, "AllowAnnotations"));
        String caseText = getXMLFieldString(xmlFields, "Text");

        Case c = new Case(caseText, authorId);
        c.id = id;
        c.version = version;
        c.allowsAnnotations = allowsAnnotations;

        return c;
    }

    /**
     * Given a list of annotations, the annotation xml file will be overwritten with this data.
     * @param annotations List of annotations to be written
     * @return True if successful
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

    /**
     * Overwrites the Quotation xml file using the given list of quotations.
     * @param quotations List of Quotations to write.
     * @return True if successful.
     */
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

    /**
     * Returns the XML representation of the given quotation object.
     * @param q Quotation object to convert.
     * @return XML string
     */
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
