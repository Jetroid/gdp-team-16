package com.team16.gdp.demo.data;

/**
 * Created by Andy on 18/10/2017.
 *
 * Java class representing a single annotation.
 *
 */
public class Annotation implements DataObject{

    private int id;
    private int caseId;
    private int authorId;
    private int quoteId;
    private String text;

    public Annotation(int annotationID, int caseId, int authorId, int quoteId, String text){
        this.id = annotationID;
        this.caseId = caseId;
        this.authorId = authorId;
        this.quoteId = quoteId;
        this.text = text;
    }

    public int getId() { return id; }
    public int getCaseId() { return caseId; }
    public int getAuthorId() { return authorId; }
    public String getText(){ return text; }
    public int getQuoteId(){ return quoteId; }

}
