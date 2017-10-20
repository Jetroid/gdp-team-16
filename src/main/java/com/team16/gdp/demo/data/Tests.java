package com.team16.gdp.demo.data;

/**
 * Created by Andy on 19/10/2017.
 */
public class Tests {

    public static void main(String[] args){
        XMLDataObjectFactory cf = new XMLDataObjectFactory("E:/ECS WORK FOLDERS/Year 4/ELEC6200 - GDP/gdp-team-16/DataStore/Cases.xml",
                                                           "E:/ECS WORK FOLDERS/Year 4/ELEC6200 - GDP/gdp-team-16/DataStore/Annotations.xml",
                                                           "E:/ECS WORK FOLDERS/Year 4/ELEC6200 - GDP/gdp-team-16/DataStore/People.xml",
                                                           "E:/ECS WORK FOLDERS/Year 4/ELEC6200 - GDP/gdp-team-16/DataStore/Quotations.xml");


        Case c = cf.buildCase(1);

        if (c != null) {
            System.out.println(c.getId());
            System.out.println(c.getAuthorId());
            System.out.println(c.canAnnotate());
            System.out.println(c.getCaseText());
        }

        Annotation a = cf.buildAnnotation(1);

        if (a != null) {
            System.out.println(a.getId());
            System.out.println(a.getAuthorId());
            System.out.println(a.getCaseId());
            System.out.println(a.getQuote().getStartIndex());
            System.out.println(a.getQuote().getEndIndex());
            System.out.println(a.getText());
        }

        Person p = cf.buildPerson(2);

        if (p != null) {
            System.out.println(p.getId());
            System.out.println(p.getForename());
            System.out.println(p.getSurname());
            System.out.println(p.getEmail());
        }

    }




}
