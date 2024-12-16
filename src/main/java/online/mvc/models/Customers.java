package online.mvc.models;

public class Customers {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String delivery_address;

    public Customers(String id, String firstname, String lastname, String email, String delivery_address) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.delivery_address = delivery_address;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public String get_firstname() {
        return firstname;
    }

    public void set_firstname(String firstname) {
        this.firstname = firstname;
    }

    public String get_lastname() {
        return lastname;
    }

    public void set_lastname(String lastname) {
        this.lastname = lastname;
    }

    public String get_email() {
        return email;
    }

    public void set_email(String email) {
        this.email = email;
    }

    public String get_delivery_address() {
        return delivery_address;
    }

    public void set_delivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    @Override
    public String toString() {
        return id + ";" + firstname + ";" + lastname + ";" + email + ";" + delivery_address;
    }
}
