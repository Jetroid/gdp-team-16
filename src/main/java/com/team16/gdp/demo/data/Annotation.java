package com.team16.gdp.demo.data;

/**
 * Created by Andy on 18/10/2017.
 *
 * Java class representing a single annotation.
 *
 */
public class Annotation {

    protected int id;
    protected int caseId;
    protected int authorId;

    protected Quotation quotation;
    protected String annotationText;


    public Annotation(int authorid, String text, String quote, int start, int end){
        authorId = authorid;
        annotationText = text;
        quotation = new Quotation(quote, start, end);
    }

    public int getId() { return id; }
    public int getCaseId() { return caseId; }
    public int getAuthorId() { return authorId; }

    public String getQuoteText(){ return quotation.getQuote(); }
    public String getText(){ return annotationText; }

    public Quotation getQuote(){ return quotation; }

}
