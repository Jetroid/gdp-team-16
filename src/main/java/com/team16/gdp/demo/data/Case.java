package com.team16.gdp.demo.data;

/**
 * Created by Andy on 19/10/2017.
 */
public class Case {

    protected int id;
    protected int version;
    protected int authorId;
    protected String caseText;
    protected boolean allowsAnnotations;

    public Case(String text, int authorid){
        caseText = text;
        authorId = authorid;
    }

    public int getId(){ return id; }
    public int getAuthorId(){ return authorId; }
    public String getCaseText(){ return caseText; }
    public boolean canAnnotate(){ return allowsAnnotations; }

}
