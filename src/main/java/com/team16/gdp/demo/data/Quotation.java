package com.team16.gdp.demo.data;

/**
 * Created by Andy on 19/10/2017.
 */
public class Quotation {

    protected int id;
    private String quote;
    protected int startIndex;
    protected int endIndex;

    public Quotation(String text, int start, int end){
        quote = text;
        startIndex = start;
        endIndex = end;
    }

    public int getId(){ return id; }
    public String getQuote(){ return quote; }
    public int getStartIndex(){ return startIndex; }
    public int getEndIndex(){ return endIndex; }

    public boolean equals(Object o){
        return o instanceof Quotation
                && ((Quotation) o).getQuote().equals(quote)
                && ((Quotation) o).getStartIndex() == startIndex
                && ((Quotation) o).getEndIndex() == endIndex;
    }

}
