package com.team16.gdp.demo.data;

import java.util.HashMap;
import java.util.List;

public class CaseRelatedInfo {

    private Case caseObj;
    private HashMap<Integer, Annotation> annotations;
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Quotation> quotations;

    public CaseRelatedInfo(Case caseObj, HashMap<Integer, Annotation> annotations, HashMap<Integer, Person> people, HashMap<Integer, Quotation> quotations) {
        this.caseObj = caseObj;
        this.annotations = annotations;
        this.people = people;
        this.quotations = quotations;
    }

    public Case getCaseObj() {
        return caseObj;
    }

    public HashMap<Integer, Annotation> getAnnotations() {
        return annotations;
    }

    public HashMap<Integer, Person> getPeople() {
        return people;
    }

    public HashMap<Integer, Quotation> getQuotations() {
        return quotations;
    }
}
