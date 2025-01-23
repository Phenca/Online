package online.mvc.models;

public class Orders_Options {
    private Orders order_id;
    private Options option_id;

    public Orders_Options(Orders order_id, Options option_id) {
        this.order_id = order_id;
        this.option_id = option_id;
    }

    public Orders get_order_id() {
        return order_id;
    }

    public void set_order_id(Orders order_id) {
        this.order_id = order_id;
    }

    public Options get_option_id() {
        return option_id;
    }

    public void set_option_id(Options option_id) {
        this.option_id = option_id;
    }

    @Override
    public String toString() {
        return order_id + ";" + option_id;
    }
}
