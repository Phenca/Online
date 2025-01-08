package online.mvc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import online.mvc.models.BaseModel;

public class Controller {
    private BaseModel model;

    @FXML
    private Text emot_price;

    @FXML
    private Text options_price;

    @FXML
    private Text total_price;

    @FXML
    protected void validate_order() {

    }

    @FXML
    private Button emot_car;

    @FXML
    private Button emot_bike;

    @FXML
    private Button emot_scooter;

    @FXML
    protected void set_emot(){

    }

    @FXML
    private ImageView main_frame;

    @FXML
    private ImageView color_0;

    @FXML
    private ImageView color_1;

    @FXML
    private ImageView color_2;

    @FXML
    private ImageView color_3;

    @FXML
    private ImageView color_4;

    @FXML
    private ImageView color_5;

    @FXML
    private ImageView color_6;

    @FXML
    private ImageView color_7;

    @FXML
    private ImageView color_8;

    @FXML
    private ImageView frame_0;
    @FXML
    private ImageView frame_1;
    @FXML
    private ImageView frame_2;
    @FXML
    private ImageView frame_3;
    @FXML
    private ImageView frame_4;
    @FXML
    private ImageView frame_5;

    @FXML
    private ImageView battery_0;
    @FXML
    private ImageView battery_1;
    @FXML
    private ImageView battery_2;

    @FXML
    protected void log_to_account() {

    }

    @FXML
    protected void switch_to_account_creation() {

    }

    @FXML
    protected void create_account() {

    }

    @FXML
    protected void switch_to_account_login() {

    }


    @FXML
    public void initialize() {
        this.model = new BaseModel();
    }
}