package data;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andy on 19/10/2017.
 */
public class XMLDataObjectFactory {

    String caseXMLPath;
    String annotationXMLPath;
    String personXMLPath;

    public XMLDataObjectFactory(String casesXML, String annosXML, String peopleXML){
        caseXMLPath = casesXML;
        annotationXMLPath = annosXML;
        personXMLPath = peopleXML;
    }

    /**
     *Builds a Case Object from data stored in the XML store.
     * @param id ID of the case
     * @return Case object for that ID
     */
    public Case buildCase(int id){
        //String caseDataString = findCaseData(id);
        String caseDataString = findXMLData(caseXMLPath, "Case", "ID", id);

        if (caseDataString == null){
            return null;
        }

        int phId = id;
        int phVersion = Integer.parseInt(getXMLFieldString(caseDataString, "Version"));
        int phAuthorId = Integer.parseInt(getXMLFieldString(caseDataString, "AuthorID"));
        boolean phAllowsAnnotations = Boolean.parseBoolean(getXMLFieldString(caseDataString, "AllowAnnotations"));
        String phCaseText = getXMLFieldString(caseDataString, "Text");

        Case c = new Case(phCaseText, phAuthorId);
        c.id = phId;
        c.version = phVersion;
        c.allowsAnnotations = phAllowsAnnotations;

        return c;
    }

    /**
     * Builds an Annotation Object from the XML store.
     * @param id ID of the Annotation
     * @return Annotation object for the ID.
     */
    public Annotation buildAnnotation(int id){
        String caseDataString = findXMLData(annotationXMLPath, "Annotation", "ID", id);

        if (caseDataString == null){
            return null;
        }

        int phId = id;
        int phCaseId = Integer.parseInt(getXMLFieldString(caseDataString, "CaseID"));
        int phAuthorId = Integer.parseInt(getXMLFieldString(caseDataString, "AuthorID"));
        int phQuoteId = Integer.parseInt(getXMLFieldString(caseDataString, "QuoteID"));
        int phStart = Integer.parseInt(getXMLFieldString(caseDataString, "StartIndex"));
        int phEnd = Integer.parseInt(getXMLFieldString(caseDataString, "EndIndex"));
        String phText = getXMLFieldString(caseDataString, "Text");

        //TODO Add quote finding
        Annotation anno = new Annotation(phAuthorId, phText, "", phStart, phEnd);
        anno.id = phId;
        anno.caseId = phCaseId;

        return anno;
    }

    /**
     * Method for searching for an item satisfying a condition for one of it's fields.
     * @param filePath XML file to search
     * @param parentTag Item needed
     * @param idTag Tag for use in identification
     * @param idVal Value for the identification tag to have
     * @return Fields of the item satisfying the condition given in XML format.
     */
    private String findXMLData(String filePath, String parentTag, String idTag, int idVal){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

            String item = "";
            while(br.ready()){
                String line = br.readLine();

                //Start of new Annotation, clear old data
                if (line.contains("<"+parentTag+">")){
                    item = "";
                }

                //End of annotation, check id and return if needed.
                if(line.contains("</"+parentTag+">")){
                    if (getXMLFieldString(item, idTag).equals(idVal+"")){
                        return item;
                    }else {
                        continue;
                    }
                }

                //if not a start or end tag, add to the data.
                item = item + "\n" + line;

            }

        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        return null;
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

}
