package com.team16.gdp.demo;

import com.team16.gdp.demo.data.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;

public class DataAccessorTests {

    private static DataModel dataAccessor;
    private static String firstname = "Greg";
    private static String surname = "Thompson";
    private static String email = "gt@participediademo.net";
    private static String caseText = "This is a case about people attending town halls.";
    private static int quoteStart = 1;
    private static int quoteEnd = 14;
    private static String quoteText = "his is a case";
    private static String annotationText = "Annotation about the quote";
    private static Settings settings = new Settings(
            System.getProperty("user.dir") + "/DataStore/TempTestCases.xml",
            System.getProperty("user.dir") + "/DataStore/TempTestPeople.xml",
            System.getProperty("user.dir") + "/DataStore/TempTestQuotations.xml",
            System.getProperty("user.dir") + "/DataStore/TempTestAnnotations.xml"
    );

    @BeforeClass
    public static void setUp(){

        File annotationsSource = new File(System.getProperty("user.dir") + "/DataStore/TestAnnotations.xml");
        File annotationsDest = new File(settings.getAnnotationXMLLocation());
        copyFileUsingStream(annotationsSource, annotationsDest);

        File peopleSource = new File(System.getProperty("user.dir") + "/DataStore/TestPeople.xml");
        File peopleDest = new File(settings.getPeopleXMLLocation());
        copyFileUsingStream(peopleSource, peopleDest);

        File caseSource = new File(System.getProperty("user.dir") + "/DataStore/TestCases.xml");
        File caseDest = new File(settings.getCaseXMLLocation());
        copyFileUsingStream(caseSource, caseDest);

        File quotationSource = new File(System.getProperty("user.dir") + "/DataStore/TestQuotations.xml");
        File quotationDest = new File(settings.getQuotationXMLLocation());
        copyFileUsingStream(quotationSource, quotationDest);

        dataAccessor = new DataModel(settings);
    }

    private static void copyFileUsingStream(File source, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            try {
                is = new FileInputStream(source);
                os = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                is.close();
                os.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown(){
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(settings.getCaseXMLLocation()));
        files.add(new File(settings.getPeopleXMLLocation()));
        files.add(new File(settings.getQuotationXMLLocation()));
        files.add(new File(settings.getAnnotationXMLLocation()));
        for(File file : files){
            file.delete();
        }
    }

    @Test
    public void readPerson(){
        Person person = dataAccessor.readPerson(1);
        assertEquals(person.getId(), 1);
        assertEquals(person.getForename(), "Henry");
        assertEquals(person.getSurname(), "Thomson");
        assertEquals(person.getEmail(), "h.thompson@example.com");
        Person person2 = dataAccessor.readPerson(2);
        assertEquals(person2.getId(), 2);
        assertEquals(person2.getForename(), "James");
        assertEquals(person2.getSurname(), "Jefferson");
        assertEquals(person2.getEmail(), "jamesinator@example.com");
    }

    @Test
    public void createPerson(){
        int personID = dataAccessor.createPerson(firstname,surname,email);
        assert personID >= 0;
    }

    @Test
    public void createAndReadPerson(){
        int personID = dataAccessor.createPerson(firstname,surname,email);
        Person person = dataAccessor.readPerson(personID);
        assertPersonCorrect(person);
    }

    @Test
    public void readCase(){
        Case caseObj = dataAccessor.readCase(1);
        assertEquals(caseObj.getId(), 1);
        assertEquals(caseObj.getAuthorId(), 1);
        assertEquals(caseObj.getCaseText(), "This is a case.");
    }

    @Test
    public void createCase(){
        int authorID = dataAccessor.createPerson(firstname,surname,email);
        int caseID = dataAccessor.createCase(caseText,authorID);
        assert caseID >= 0;
    }

    @Test
    public void createAndReadCase(){
        int authorID = dataAccessor.createPerson(firstname,surname,email);
        int caseID = dataAccessor.createCase(caseText,authorID);
        Case caseObj = dataAccessor.readCase(caseID);
        assertCaseCorrect(caseObj,authorID);
    }

    @Test
    public void readQuote(){
        Quotation quotation = dataAccessor.readQuotation(1);
        assertEquals(quotation.getId(), 1);
        assertEquals(quotation.getCaseId(), 1);
        assertEquals(quotation.getStartIndex(), 20);
        assertEquals(quotation.getEndIndex(), 30);
        assertEquals(quotation.getQuote(), "In 2015 the South Australian government established a Royal Commission");
        Quotation quotation2 = dataAccessor.readQuotation(2);
        assertEquals(quotation2.getId(), 2);
        assertEquals(quotation2.getCaseId(), 1);
        assertEquals(quotation2.getStartIndex(), 30);
        assertEquals(quotation2.getEndIndex(), 45);
        assertEquals(quotation2.getQuote(), "nuclear fuel cycle");
    }

    @Test
    public void createQuote(){
        int authorID = dataAccessor.createPerson(firstname,surname,email);
        int caseID = dataAccessor.createCase(caseText,authorID);
        int quoteID = dataAccessor.createQuotation(caseID,quoteStart,quoteEnd,quoteText);
        assert quoteID >= 0;
    }

    @Test
    public void createAndReadQuote(){
        int authorID = dataAccessor.createPerson(firstname,surname,email);
        int caseID = dataAccessor.createCase(caseText,authorID);
        int quoteID = dataAccessor.createQuotation(caseID,quoteStart,quoteEnd,quoteText);
        Quotation quotation = dataAccessor.readQuotation(quoteID);
        assertQuotationCorrect(quotation,caseID);
    }

    @Test
    public void readAnnotation(){
        Annotation annotation = dataAccessor.readAnnotation(1);
        assertEquals(annotation.getId(), 1);
        assertEquals(annotation.getAuthorId(), 2);
        assertEquals(annotation.getQuoteId(), 1);
        assertEquals(annotation.getText().trim(), "Your study is bad and you should feel bad.");
    }

    @Test
    public void createAnnotation(){
        int authorID = dataAccessor.createPerson(firstname,surname,email);
        int caseID = dataAccessor.createCase(caseText,authorID);
        int quoteID = dataAccessor.createQuotation(caseID,quoteStart,quoteEnd,quoteText);
        int annotationID = dataAccessor.createAnnotation(caseID,authorID,quoteID,annotationText);
        assert annotationID >= 0;
    }

    @Test
    public void createAndReadAnnotation() {
        int authorID = dataAccessor.createPerson(firstname,surname,email);
        int caseID = dataAccessor.createCase(caseText,authorID);
        int quoteID = dataAccessor.createQuotation(caseID,quoteStart,quoteEnd,quoteText);
        int annotationID = dataAccessor.createAnnotation(caseID,authorID,quoteID,annotationText);
        Annotation annotation = dataAccessor.readAnnotation(annotationID);
        assertAnnotationCorrect(annotation,caseID,authorID,quoteID);
    }

    private void assertPersonCorrect(Person person){
        assertEquals(person.getForename(), firstname);
        assertEquals(person.getSurname(), surname);
        assertEquals(person.getEmail(), email);
    }

    private void assertCaseCorrect(Case caseObj, int authorID){
        assertEquals(caseObj.getCaseText(),caseText);
        assertEquals(caseObj.getAuthorId(),authorID);
    }

    private void assertQuotationCorrect(Quotation quotation, int caseID){
        assertEquals(quotation.getStartIndex(), quoteStart);
        assertEquals(quotation.getEndIndex(), quoteEnd);
        assertEquals(quotation.getQuote(),quoteText);
        assertEquals(quotation.getCaseId(),caseID);
    }

    private void assertAnnotationCorrect(Annotation annotation, int caseID, int authorID, int quoteID){
        assertEquals(annotation.getText(), annotationText);
        assertEquals(annotation.getCaseId(), caseID);
        assertEquals(annotation.getAuthorId(), authorID);
        assertEquals(annotation.getQuoteId(), quoteID);
    }

}
