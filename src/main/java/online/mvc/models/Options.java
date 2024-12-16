package online.mvc.models;

public class Options {
    private String id;
    private String name;
    private String type;
    private double price;
    private EMOT emot_ref;

    public Options(String id, String name, String type, double price, EMOT emot_ref) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.emot_ref = emot_ref;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_type() {
        return type;
    }

    public void set_type(String type) {
        this.type = type;
    }

    public double get_price() {
        return price;
    }

    public void set_price(double price) {
        this.price = price;
    }

    public EMOT get_emot_ref() {
        return emot_ref;
    }

    public void set_emot_ref(EMOT emot_ref) {
        this.emot_ref = emot_ref;
    }

    @Override
    public String toString() {
        return id + ";" + name + ";" + type + ";" + price + ";" + emot_ref;
    }
}
