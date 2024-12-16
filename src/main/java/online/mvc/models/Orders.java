package online.mvc.models;

import java.util.List;

public class Orders {
    private String id;
    private Customers customers_ref;
    private EMOT emot_ref;
    private List<Options> options;
    private double total_price;
    private OrderStates state;
    private String tracking_number;

    public Orders(String id, Customers customers_ref, EMOT emot_ref, List<Options> options, double total_price, OrderStates state, String tracking_number) {
        this.id = id;
        this.customers_ref = customers_ref;
        this.emot_ref = emot_ref;
        this.options = options;
        this.total_price = total_price;
        this.state = state;
        this.tracking_number = tracking_number;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public Customers get_customers_ref() {
        return customers_ref;
    }

    public void set_customers_ref(Customers customers_ref) {
        this.customers_ref = customers_ref;
    }

    public EMOT get_emot_ref() {
        return emot_ref;
    }

    public void set_emot_ref(EMOT emot_ref) {
        this.emot_ref = emot_ref;
    }

    public List<Options> get_options() {
        return options;
    }

    public void set_options(List<Options> options) {
        this.options = options;
    }

    public double get_total_price() {
        return total_price;
    }

    public void set_total_price(double total_price) {
        this.total_price = total_price;
    }

    public OrderStates get_state() {
        return state;
    }

    public void set_state(OrderStates state) {
        this.state = state;
    }

    public String get_tracking_number() {
        return tracking_number;
    }

    public void set_tracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    @Override
    public String toString() {
        return id + ";" + customers_ref + ";" + emot_ref + ";" + options + ";" + total_price + ";" + state + ";" + tracking_number;
    }
}
