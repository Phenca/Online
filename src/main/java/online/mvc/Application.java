package online.mvc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import online.mvc.utils.InstanceManager;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        InstanceManager.get_instance().set_primary_stage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        InstanceManager.get_instance().set_current_view_name(fxmlLoader.getLocation().getFile());
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getStylesheets().add(getClass().getResource("style/styles.css").toExternalForm());
        stage.setTitle("Moyens de transport électrique");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}