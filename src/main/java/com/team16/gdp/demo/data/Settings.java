package com.team16.gdp.demo.data;

public class Settings {

    private String caseXMLLocation;
    private String peopleXMLLocation;
    private String quotationXMLLocation;
    private String annotationXMLLocation;

    public Settings(String caseXMLLocation, String peopleXMLLocation, String quotationXMLLocation, String annotationXMLLocation) {
        this.caseXMLLocation = caseXMLLocation;
        this.peopleXMLLocation = peopleXMLLocation;
        this.quotationXMLLocation = quotationXMLLocation;
        this.annotationXMLLocation = annotationXMLLocation;
    }

    public String getCaseXMLLocation() {
        return caseXMLLocation;
    }

    public String getPeopleXMLLocation() {
        return peopleXMLLocation;
    }

    public String getQuotationXMLLocation() {
        return quotationXMLLocation;
    }

    public String getAnnotationXMLLocation() {
        return annotationXMLLocation;
    }
}
