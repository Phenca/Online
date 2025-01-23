package online.mvc.models;

public class Orders_Options {
    private String order_id;
    private String option_id;

    public Orders_Options(String order_id, String option_id) {
        this.order_id = order_id;
        this.option_id = option_id;
    }

    public String get_order_id() {
        return order_id;
    }

    public void set_order_id(String order_id) {
        this.order_id = order_id;
    }

    public String get_option_id() {
        return option_id;
    }

    public void set_option_id(String option_id) {
        this.option_id = option_id;
    }

    @Override
    public String toString() {
        return order_id + ";" + option_id;
    }
}
