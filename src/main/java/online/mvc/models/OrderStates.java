package online.mvc.models;

public class OrderStates {
    private int id;
    private String name;

    public OrderStates(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int get_id() {
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

    @Override
    public String toString() {
        return id + ";" + name;
    }
}
