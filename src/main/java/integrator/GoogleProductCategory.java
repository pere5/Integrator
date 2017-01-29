package integrator;

import org.springframework.data.annotation.Id;

/**
 * Created by Per Eriksson on 2017-01-29.
 */
public class GoogleProductCategory {
    @Id
    private String id;
    private String firstName;
    private String lastName;

    public GoogleProductCategory(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "GoogleProductCategory{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
