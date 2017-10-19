package data;

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

    protected Quotation quoteText;
    protected String annotationText;

    protected int startIndex;
    protected int endIndex;

    public Annotation(int authorid, String text, String quote, int start, int end){
        authorId = authorid;
        annotationText = text;
        quoteText = new Quotation(quote);
        startIndex = start;
        endIndex = end;
    }

    public String getQuote(){ return quoteText.getQuote(); }
    public String getText(){ return annotationText; }

    public int getStartIndex(){ return startIndex; }
    public int getEndIndex(){ return endIndex; }




}
