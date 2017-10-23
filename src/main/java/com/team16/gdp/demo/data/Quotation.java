package com.team16.gdp.demo.data;

/**
 * Created by Andy on 19/10/2017.
 */
public class Quotation implements DataObject{

    private int id;
    private int caseId;
    private String quote;
    private int startIndex;
    private int endIndex;

    public Quotation(int id, int caseId, String text, int start, int end){
        this.id = id;
        this.caseId = caseId;
        quote = text;
        startIndex = start;
        endIndex = end;
    }

    public int getId(){ return id; }
    public String getQuote(){ return quote; }
    public int getStartIndex(){ return startIndex; }
    public int getEndIndex(){ return endIndex; }
    public int getCaseId(){ return caseId; }

    public boolean equals(Object o){
        return o instanceof Quotation
                && ((Quotation) o).getQuote().equals(quote)
                && ((Quotation) o).getStartIndex() == startIndex
                && ((Quotation) o).getEndIndex() == endIndex;
    }

}
