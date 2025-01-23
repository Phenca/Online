package online.mvc.models;

import online.mvc.utils.Database;

import java.sql.SQLException;
import java.util.HashMap;

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
        int id = Integer.parseInt(this.database.get_id_or_email_existing_error(email).replace("CUS", ""));
        System.out.println("Current last id : " + id);
        String new_last_id = "CUS" + id + 1;
        System.out.println("New last id : " + new_last_id);
        return new_last_id;
    }

    public String get_order_id() {
        return "";
    }
}
