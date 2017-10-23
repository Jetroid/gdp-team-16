package com.team16.gdp.demo.data;

import java.util.List;

public interface DataAccessor {

    int createCase(String text, int authorID);

    /**
     * Returns a Case when given the caseID.
     * @param caseID ID of case
     * @return Case associated with given ID
     */
    Case readCase(int caseID);

    /**
     * Creates a new Annotation for the given case.
     * @param caseID ID of case
     * @param authorID ID of author
     * @param quoteID ID of quotation
     * @param text Text of the new annotation
     * @return int ID of the newly created Annotation
     */
    int createAnnotation(int caseID, int authorID, int quoteID, String text);

    /**
     * Returns a Annotation when given the annotationID.
     * @param annotationID ID of annotation
     * @return Annotation associated with given ID
     */
    Annotation readAnnotation(int annotationID);

    boolean updateAnnotation(int annotationID, String text);

    boolean deleteAnnotation(int annotationID);

    int createPerson(String forename, String surname, String email);

    Person readPerson(int personID);

    boolean updatePerson(int personID, String forename, String surname, String email);

    boolean deletePerson(int personID);

    int createQuotation(int caseID, int start, int end, String text);

    Quotation readQuotation(int quotationID);

    boolean updateQuotation(int quotationID, int start, int end, String text);

    boolean deleteQuotation(int quotationID);

    /**
     * Returns all annotations for a given case identified by its case ID.
     * @param caseId ID of case
     * @return List of annotations
     */
    List<Annotation> getAnnotationsForCase(int caseId);



    /**
     * Returns a boolean for whether a given case can accept annotations.
     * @param caseId ID of case
     * @return True if able to annotate
     */
    boolean canAnnotate(int caseId);

    /**
     * Attempts to add an annotation to the case. Returns true if successful, false if unsuccessful.
     * Recommended to check with canAnnotate() before this.
     * @param caseId ID of case
     * @param a Annotation Object to add
     * @return True if successfully added
     */
    boolean addAnnotation(int caseId, Annotation a);

    /**
     * Retrieves the case object with all available com.team16.gdp.demo.data about the case given a case ID. Returns null if no case
     * exists with the provided ID.
     * @param caseId ID of case
     * @return Case object for this ID
     */
    Case getCaseData(int caseId);

    /**
     * Returns the person associated with this ID, if no person exists, it returns null.
     * @param personId ID of person
     * @return Person object for this ID
     */
    Person getPersonWithID(int personId);

    /**
     * Overwrites the current text associated with this annotation ID with the provided text.
     * Returns true if operation is successful.
     * @param annoId ID of annotation
     * @param newText Text to replace previous text
     * @return True if edit successful
     */
    boolean editAnnotation(int annoId, String newText);

}
