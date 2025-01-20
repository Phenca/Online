package online.mvc.models;

import online.mvc.utils.Database;

import java.sql.SQLException;

public class BaseModel {
    private Database database;

    public BaseModel() {
        this.database = new Database();
    }

    public Database get_database() {
        return database;
    }

    public String get_customer_id() throws SQLException {
        int id = Integer.parseInt(this.database.get_last_customer().replace("CUS", ""));
        System.out.println("Current last id : " + id);
        String new_last_id = "CUS" + id + 1;
        System.out.println("New last id : " + new_last_id);
        return new_last_id;
    }
}
