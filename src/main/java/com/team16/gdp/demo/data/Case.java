package com.team16.gdp.demo.data;

/**
 * Created by Andy on 19/10/2017.
 */
public class Case implements DataObject{

    private int id;
    private int version;
    private int authorId;
    private String caseText;
    private boolean allowsAnnotations;

    public Case(int id, String text, int authorId){
        this(id, 1, text, authorId, true);
    }

    public Case(int id, String caseText, int authorId, boolean allowsAnnotations){
        this(id, 1, caseText, authorId, allowsAnnotations);
    }

    public Case(int id, int version, String caseText, int authorId, boolean allowsAnnotations){
        this.id = id;
        this.caseText = caseText;
        this.authorId = authorId;
        this.allowsAnnotations = allowsAnnotations;
    }

    public int getId(){ return id; }
    public int getAuthorId(){ return authorId; }
    public String getCaseText(){ return caseText; }
    public boolean canAnnotate(){ return allowsAnnotations; }

}
