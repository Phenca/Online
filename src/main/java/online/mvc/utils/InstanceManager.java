package online.mvc.utils;

import javafx.stage.Stage;
import online.mvc.models.Customers;

public class InstanceManager {
    private static InstanceManager instance;
    private Stage primary_stage;
    private String current_view;
    private Customers logged_user;

    private InstanceManager() {}

    public static InstanceManager get_instance() {
        if (instance == null) {
            instance = new InstanceManager();
        }
        return instance;
    }

    public void set_primary_stage(Stage stage) {
        this.primary_stage = stage;
    }

    public Stage get_primary_stage() {
        return primary_stage;
    }

    public void set_current_view_name(String view) {
        this.current_view = view;
    }

    public String get_current_view_name() {
        return current_view;
    }

    public Customers get_logged_user() {
        return logged_user;
    }

    public void set_logged_user(Customers logged_user) {
        this.logged_user = logged_user;
    }
}
