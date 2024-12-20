package online.mvc.models;

import online.mvc.utils.Database;

public class BaseModel {
    private Database database;

    public BaseModel() {
        this.database = new Database();
    }

    public Database get_database() {
        return database;
    }
}
