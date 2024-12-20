package online.mvc;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import online.mvc.models.BaseModel;

public class Controller {
    private BaseModel model;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void initialize() {
        this.model = new BaseModel();
    }
}