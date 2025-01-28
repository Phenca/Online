package online.mvc.models;

public class Customers {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String delivery_address;

    public Customers(String id, String firstname, String lastname, String email, String password, String delivery_address) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.delivery_address = delivery_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    @Override
    public String toString() {
        return id;
    }
}
