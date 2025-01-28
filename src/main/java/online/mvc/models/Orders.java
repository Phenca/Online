package online.mvc.models;

public class Orders {
    private String id;
    private Customers customers_ref;
    private EMOT emot_ref;
    private double total_price;
    private OrderStates state_id;
    private String tracking_number;

    public Orders(String id, Customers customers_ref, EMOT emot_ref, double total_price, OrderStates state_id, String tracking_number) {
        this.id = id;
        this.customers_ref = customers_ref;
        this.emot_ref = emot_ref;
        this.total_price = total_price;
        this.state_id = state_id;
        this.tracking_number = tracking_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customers getCustomers_ref() {
        return customers_ref;
    }

    public void setCustomers_ref(Customers customers_ref) {
        this.customers_ref = customers_ref;
    }

    public EMOT getEmot_ref() {
        return emot_ref;
    }

    public void setEmot_ref(EMOT emot_ref) {
        this.emot_ref = emot_ref;
    }

    public double getTotalPrice() {
        return total_price;
    }

    public void setTotalPrice(double total_price) {
        this.total_price = total_price;
    }

    public OrderStates getStateId() {
        return state_id;
    }

    public void setStateId(OrderStates state_id) {
        this.state_id = state_id;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    public String getCustomerName() {
        return customers_ref.getFirstname() + " " + customers_ref.getLastname();
    }

    public String getEmotName() {
        return emot_ref.getName();
    }

    public String getState() {
        return state_id.getName();
    }

    @Override
    public String toString() {
        return id + ";" + customers_ref + ";" + emot_ref  + ";" + total_price + ";" + state_id + ";" + tracking_number + ";";
    }
}
