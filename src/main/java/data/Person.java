package data;

/**
 * Created by Andy on 19/10/2017.
 */
public class Person {

    protected int id;
    protected String forename;
    protected String surname;
    protected String email;

    public Person(int id, String forename, String surname, String email){
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }
}
