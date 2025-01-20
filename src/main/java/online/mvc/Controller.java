package online.mvc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import online.mvc.models.BaseModel;
import online.mvc.models.Customers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    // login.fxml view
    @FXML
    private TextField login_email_field;
    @FXML
    private TextField login_password_field;

    @FXML
    protected void log_to_account(ActionEvent evt) throws SQLException {
        ArrayList<TextField> fields = new ArrayList<>();
        fields.add(login_email_field);
        fields.add(login_password_field);
        if (_check_fields_not_empty(fields)) {
            try {
                _check_email_field(login_email_field);
                for (Customers customer : this.model.get_database().get_customers()) {
                    if (Objects.equals(customer.get_email(), login_email_field.getText())) {
                        if (Objects.equals(customer.get_password(), login_password_field.getText())) {
                            System.out.println("Authentication successful !");
                            _load_screen(evt, "main.fxml");
                            break;
                        }
                    }
                }
            } catch (IllegalArgumentException err) {
                System.err.println(err.getMessage());
            }
        }
        throw new IllegalAccessError("Authentication unsuccessful !");
    }
    private void _check_email_field(TextField email) {
        if (!email.getText().contains("@") && (!email.getText().endsWith(".com") || !email.getText().endsWith(".fr"))) {
            throw new IllegalArgumentException("Le champ email doit contenir un '@' et se terminer par '.com' ou '.fr'");
        }
    }
    @FXML
    protected void switch_to_account_creation(ActionEvent evt) {
        _load_screen(evt, "signin.fxml");
    }

    // signin.fxml view
    @FXML
    private TextField signin_firstname_field;
    @FXML
    private TextField signin_lastname_field;
    @FXML
    private TextField signin_delivery_address_field;
    @FXML
    private TextField signin_email_field;
    @FXML
    private TextField signin_1st_password_field;
    @FXML
    private TextField signin_2nd_password_field;

    private boolean _check_fields_not_empty(List<TextField> fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                throw new IllegalArgumentException("Un des champs obligatoire n'est pas renseigné");
            }
        }
        return true;
    }
    @FXML
    protected void create_account() throws SQLException {
        ArrayList<TextField> fields = new ArrayList<>();
        fields.add(signin_email_field);
        fields.add(signin_1st_password_field);
        fields.add(signin_2nd_password_field);
        if (_check_fields_not_empty(fields)) {
            if (Objects.equals(signin_1st_password_field.getText(), signin_2nd_password_field.getText())) {
                System.out.println("SUPER");
                Customers customer = new Customers(
                        this.model.get_customer_id(),
                        signin_firstname_field.getText(),
                        signin_lastname_field.getText(),
                        signin_email_field.getText(),
                        signin_1st_password_field.getText(),
                        signin_delivery_address_field.getText()
                );
                this.model.get_database().add_customer(customer);
            }
        }
    }

    @FXML
    protected void switch_to_account_login(ActionEvent evt) {
        _load_screen(evt, "login.fxml");
    }

    @FXML
    protected void disconnect(ActionEvent evt) {
        _load_screen(evt, "login.fxml");
    }

    private void _load_screen(ActionEvent evt, String name) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(name));
            AnchorPane view = fxmlloader.load();
            Stage new_stage = (Stage) ((Button) evt.getSource()).getScene().getWindow();
            new_stage.setScene(new Scene(view));
            new_stage.show();
        } catch (Exception err) {
            System.err.println(err.getMessage());
        }
    }

    @FXML
    public void initialize() {
        this.model = new BaseModel();
    }
}