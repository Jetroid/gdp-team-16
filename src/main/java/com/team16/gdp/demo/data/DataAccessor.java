package com.team16.gdp.demo.data;

import java.util.List;

/**
 * Created by Andy on 18/10/2017.
 */
public interface DataAccessor {

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
