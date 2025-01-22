package online.mvc.utils;

import javafx.stage.Stage;

public class ScreenManager {
    private static ScreenManager instance;
    private Stage primary_stage;
    private Object current_controller;

    private ScreenManager() {}

    public static ScreenManager get_instance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void set_primary_stage(Stage stage) {
        this.primary_stage = stage;
    }

    public Stage get_primary_stage() {
        return primary_stage;
    }

    public void set_current_controller(Object controller) {
        this.current_controller = controller;
    }

    public Object get_current_controller() {
        return current_controller;
    }
}
