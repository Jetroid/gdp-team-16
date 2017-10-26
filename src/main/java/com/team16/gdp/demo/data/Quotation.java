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

    public Quotation(int id, int caseId, String quote, int startIndex, int endIndex){
        this.id = id;
        this.caseId = caseId;
        this.quote = quote;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getId(){ return id; }
    public String getQuote(){ return quote; }
    public int getStartIndex(){ return startIndex; }
    public int getEndIndex(){ return endIndex; }
    public int getCaseId(){ return caseId; }

    public void setId(int id) {
        this.id = id;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public boolean equals(Object o){
        return o instanceof Quotation
                && ((Quotation) o).getQuote().equals(quote)
                && ((Quotation) o).getStartIndex() == startIndex
                && ((Quotation) o).getEndIndex() == endIndex;
    }

}
