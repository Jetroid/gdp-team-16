package com.team16.gdp.demo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataModel implements DataAccessor {

    private static int caseCount = 0;
    private static HashMap<Integer, Case> cases = new HashMap<>();
    private static int peopleCount = 0;
    private static HashMap<Integer, Person> people = new HashMap<>();
    private static int quotationCount = 0;
    private static HashMap<Integer, Quotation> quotations = new HashMap<>();
    private static int annotationCount = 0;
    private static HashMap<Integer, Annotation> annotations = new HashMap<>();
    private static XMLDataObjectFactory persistence;

    public DataModel(Settings settings){
        if (persistence == null) {
            persistence = new XMLDataObjectFactory(
                    settings.getCaseXMLLocation(),
                    settings.getAnnotationXMLLocation(),
                    settings.getPeopleXMLLocation(),
                    settings.getQuotationXMLLocation()
            );
            readInAllData();
        }
    }

    @Override
    public int createCase(String text, int authorID) {
        return -1;
    }

    @Override
    public Case readCase(int caseID) {
        return cases.get(caseID);
    }

    @Override
    public int createAnnotation(int caseID, int authorID, int quoteID, String text) {
        int annotationID = annotationCount;
        Annotation annotation = new Annotation(annotationID, caseID, authorID, quoteID, text);
        boolean successfulWrite = persistence.addAnnotation(annotation);
        if (successfulWrite){
            annotationCount++;
            annotations.put(annotationID, annotation);
            return annotationID;
        }
        return -1;
    }

    @Override
    public Annotation readAnnotation(int annotationID) {
        return annotations.get(annotationID);
    }

    @Override
    public boolean updateAnnotation(int annotationID, String text) {
        return false;
    }

    @Override
    public boolean deleteAnnotation(int annotationID) {
        return false;
    }

    @Override
    public int createPerson(String forename, String surname, String email) {
        return -1;
    }

    @Override
    public Person readPerson(int personID) {
        return people.get(personID);
    }

    @Override
    public boolean updatePerson(int personID, String forename, String surname, String email) {
        return false;
    }

    @Override
    public boolean deletePerson(int personID) {
        return false;
    }

    @Override
    public int createQuotation(int caseID, int start, int end, String text) {
        int quotationId = quotationCount;
        Quotation quotation = new Quotation(quotationId, caseID, text, start, end);
        boolean successfulWrite = persistence.addQuotation(quotation);
        if (successfulWrite){
            quotationCount++;
            quotations.put(quotationId, quotation);
            return quotationId;
        }
        return -1;
    }

    @Override
    public Quotation readQuotation(int quotationID) {
        return quotations.get(quotationID);
    }

    @Override
    public boolean updateQuotation(int quotationID, int start, int end, String text) {
        return false;
    }

    @Override
    public boolean deleteQuotation(int quotationID) {
        return false;
    }

    @Override
    public CaseRelatedInfo getCaseData(int caseId) {
        Case caseObj = cases.get(caseId);
        HashMap<Integer, Annotation> annotations = new HashMap<>();
        HashMap<Integer, Quotation> quotations = new HashMap<>();
        HashMap<Integer, Person> people = new HashMap<>();
        for(Annotation annotation : DataModel.annotations.values()){
            if(annotation.getCaseId() == caseId) {
                int quoteId = annotation.getQuoteId();
                quotations.put(quoteId, DataModel.quotations.get(quoteId));
                int personId = annotation.getAuthorId();
                people.put(personId, DataModel.people.get(personId));
                annotations.put(annotation.getId(), annotation);
            }
        }
        return new CaseRelatedInfo(caseObj, annotations, people, quotations);
    }

    private void readInAllData(){
        List<Case> cases = persistence.getAllCases();
        caseCount = getNextID((List) cases);
        for(Case caseObj : cases){
            this.cases.put(caseObj.getId(), caseObj);
        }
        List<Person> people = persistence.getAllPeople();
        peopleCount = getNextID((List) people);
        for(Person person : people){
            this.people.put(person.getId(), person);
        }
        List<Quotation> quotations = persistence.getAllQuotations();
        quotationCount = getNextID((List) quotations);
        for(Quotation quotation : quotations){
            this.quotations.put(quotation.getId(), quotation);
        }
        List<Annotation> annotations = persistence.getAllAnnotations();
        annotationCount = getNextID((List) annotations);
        for(Annotation annotation : annotations){
            this.annotations.put(annotation.getId(), annotation);
        }
    }

    private int getNextID(List<DataObject> dataObjects){
        int highest = 0;
        for (DataObject dataObject : dataObjects) {
            highest = Math.max(highest, dataObject.getId());
        }
        return highest == 0 ? 0 : highest + 1;
    }
}
