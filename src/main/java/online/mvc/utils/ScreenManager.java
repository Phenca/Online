package online.mvc.utils;

import javafx.stage.Stage;

public class ScreenManager {
    private static ScreenManager instance;
    private Stage primary_stage;
    private String current_view;

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

    public void set_current_view_name(String view) {
        this.current_view = view;
    }

    public String get_current_view_name() {
        return current_view;
    }
}
