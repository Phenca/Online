package online.mvc.models;

public class Options {
    private int id;
    private String name;
    private String type;
    private double price;
    private EMOT emot_ref;

    public Options(int id, String name, String type, double price, EMOT emot_ref) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.emot_ref = emot_ref;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public EMOT getEmot_ref() {
        return emot_ref;
    }

    public void setEmot_ref(EMOT emot_ref) {
        this.emot_ref = emot_ref;
    }

    public String getOptionsName() {
        return getName();
    }

    @Override
    public String toString() {
        return id + ";";
    }
}
