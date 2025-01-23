package online.mvc.models;

import online.mvc.utils.Database;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BaseModel {
    private Database database;
    public Customers logged_user;
    public int emot_id;
    public HashMap<String, Options> options_map = new HashMap<>();
    public String resources_path;

    public BaseModel() {
        this.database = new Database();
        this.emot_id = 1;
        this.resources_path = "C:/Users/Soran/IdeaProjects/Online/src/main/resources/online/mvc/";
    }

    public Database get_database() {
        return database;
    }

    public String get_customer_id(String email) throws SQLException {
        List<Customers> customers = this.database.get_customers();
        for (Customers customer : customers) {
            if (Objects.equals(customer.get_email(), email)) {
                throw new IllegalArgumentException("Un compte pour l'email '"+email+"' est déjà renseigné.");
            }
            if (Objects.equals(customers.getLast(), customer)) {
                int id = Integer.parseInt(customer.get_id().replace("CUS", ""));
                return "CUS" + id + 1;
            }
        }
        throw new SQLDataException("Aucune données dans la table 'Customers'");
    }

    public String get_order_id() throws SQLException {
        List<Orders> orders = this.database.get_orders();
        if (!orders.isEmpty()) {
            int id = Integer.parseInt(orders.getLast().get_id().replace("CUS", ""));
            return "CMD" + id + 1;
        }
        throw new SQLDataException("Aucune données dans la table 'Orders'");
    }
}
