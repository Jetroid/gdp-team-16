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

    CaseRelatedInfo getCaseData(int caseID);

}
