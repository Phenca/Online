package online.mvc.models;

public class EMOT {
    private int id;
    private String name;
    private double price;

    public EMOT(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getid() {
        return id;
    }

    public void set_id(int id) {
        this.id = id;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public double get_price() {
        return price;
    }

    public void set_price(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return id + ";" + name + ";" + price;
    }
}
