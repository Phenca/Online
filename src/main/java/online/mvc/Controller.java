package online.mvc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import online.mvc.models.*;
import online.mvc.utils.InstanceManager;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class Controller {
    private BaseModel model;

    @FXML
    public void initialize() throws SQLException {
        this.model = new BaseModel();
        if (InstanceManager.get_instance().get_current_view_name().endsWith("main.fxml")) {
            this.main_frame.setImage(new Image("C:\\Users\\Soran\\IdeaProjects\\Online\\src\\main\\resources\\online\\mvc\\img\\1-color-white.jpg"));
        } else if (InstanceManager.get_instance().get_current_view_name().endsWith("orders-tracking.fxml")) {
            this._set_table_view();
        }
    }

    // main.fxml fields.
    @FXML
    private GridPane grid_option_1;
    @FXML
    private GridPane grid_option_2;
    @FXML
    private GridPane grid_option_3;
    @FXML
    private Text emot_price;
    @FXML
    private Text options_price;
    @FXML
    private Text total_price;
    @FXML
    private Button emot_car;
    @FXML
    private Button emot_bike;
    @FXML
    private Button emot_scooter;
    @FXML
    private ImageView main_frame;

    // main.fxml methods.
    @FXML
    protected void set_emot(ActionEvent evt) throws SQLException {
        if (evt.getSource().equals(emot_car)) {
            this.model.emot_id = 1;
            this.populate_interface(this.model.emot_id, grid_option_2, "rim");
        } else if (evt.getSource().equals(emot_bike)) {
            this.model.emot_id = 2;
            this.populate_interface(this.model.emot_id, grid_option_2, "frame");
        } else if (evt.getSource().equals(emot_scooter)) {
            this.model.emot_id = 3;
            this.populate_interface(this.model.emot_id, grid_option_2, "frame");
        }
        this.populate_interface(this.model.emot_id, grid_option_1, "color");
        this.populate_interface(this.model.emot_id, grid_option_3, "battery");
        this.set_default_image();
    }

    protected void set_default_image() {
        this.main_frame.setImage(new Image(this.model.resources_path+"img/"+this.model.emot_id+"-color-white.jpg"));
    }

    protected void populate_interface(int emot_id, GridPane grid, String option_type) throws SQLException {
        this.reset_main_interface(grid);
        List<Options> options = this.model.get_database().get_emot_options(emot_id, option_type);
        this.emot_price.setText(String.valueOf(options.getFirst().get_emot_ref().get_price()));
        this.total_price.setText(String.valueOf(options.getFirst().get_emot_ref().get_price()));
        int i=0;
        String image_name;
        for (Options option : options) {
            if (option.get_price() == 0) {
                this.model.options_map.put(option_type, option);
            }
            if (grid.getChildren().get(i) instanceof ImageView image) {
                if (option_type.equals("color")){
                    image_name = option.get_type()+"-"+option.get_name()+".jpg";
                } else {
                    image_name = option.get_emot_ref().get_id()+"-"+option.get_type()+"-"+option.get_name()+".jpg";
                }
                image.setImage(new Image(this.model.resources_path+option.get_type()+"/"+image_name));
                i++;
            }
        }
    }
    protected void calculate_price() {
        double price = 0;
        for (Options option : this.model.options_map.values()) {
            price += option.get_price();
        }
        this.options_price.setText(String.valueOf(price));
        this.calculate_total();
    }

    protected void calculate_total() {
        double emot_price = Double.parseDouble(this.emot_price.getText());
        double options_price = Double.parseDouble(this.options_price.getText());
        this.total_price.setText(String.valueOf(emot_price+options_price));
    }

    @FXML
    protected void update(MouseEvent event) throws SQLException {
        if (event.getSource() instanceof ImageView image) {
            if (!Objects.isNull(image.getImage())) {
                String file_name = Paths.get(image.getImage().getUrl()).getFileName().toString();
                List<String> options = new ArrayList<>(List.of(file_name.replace(".jpg", "").split("-")));
                if (file_name.startsWith("color")) {
                    this.main_frame.setImage(new Image(this.model.resources_path + "img/" + this.model.emot_id + "-" + file_name));
                } else {options.removeFirst();}
                options.addFirst(String.valueOf(this.model.emot_id));
                Options option = this.model.get_database().get_emot_option(Integer.parseInt(options.getFirst()), options.get(1), options.getLast());
                System.out.println(option);
                this.model.options_map.replace(option.get_type(), option);
                this.calculate_price();
            }
        }
    }


    protected void reset_main_interface(GridPane grid) {
        for (Node cell : grid.getChildren()) {
            if (cell instanceof ImageView image) {
                image.setImage(null);
            }
        }
        this.main_frame.setImage(null);
        this.emot_price.setText("0");
        this.options_price.setText("0");
        this.total_price.setText("0");
    }

    @FXML
    protected void validate_order() throws SQLException {
        Alert disconnect_popup = new Alert(Alert.AlertType.CONFIRMATION);
        disconnect_popup.setTitle("Confirmation de commande");
        disconnect_popup.setHeaderText("Êtes-vous sûr de vouloir valider votre commande ?");
        disconnect_popup.setContentText("Cliquez sur 'OK' pour confirmer ou 'Annuler' pour annuler l'action.");
        Optional<ButtonType> user_action = disconnect_popup.showAndWait();
        if (user_action.isPresent() && user_action.get() == ButtonType.OK) {
            String tracking_number = this.model.get_order_id();
            Orders order = new Orders(
                    tracking_number,
                    InstanceManager.get_instance().get_logged_user(),
                    this.model.get_database().get_emot_for_id(this.model.emot_id),
                    Double.parseDouble(this.total_price.getText()),
                    this.model.get_database().get_state_for_id(1),
                    tracking_number
            );
            this.model.get_database().add_order(order);
            this.model.get_database().add_orders_options(order.get_id(), this.model.options_map);
        }
    }

    // login.fxml fields.
    @FXML
    private TextField login_email_field;
    @FXML
    private TextField login_password_field;

    // login.fxml methods.
    @FXML
    protected void switch_to_account_creation(ActionEvent evt) {
        _load_screen(evt, "signin.fxml");
    }

    @FXML
    protected boolean log_to_account(ActionEvent evt) throws SQLException {
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
                            InstanceManager.get_instance().set_logged_user(customer);
                            _load_screen(evt, "user-choice.fxml");
                            return true;
                        }
                    }
                }
            } catch (IllegalArgumentException err) {
                System.err.println(err.getMessage());
            }
        }
        throw new IllegalAccessError("Authentication unsuccessful !");
    }

    // signin.fxml fields.
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

    // sign.fxml methods.
    @FXML
    protected void switch_to_account_login(ActionEvent evt) {
        _load_screen(evt, "login.fxml");
    }

    @FXML
    protected void create_account() throws SQLException {
        ArrayList<TextField> fields = new ArrayList<>();
        fields.add(signin_email_field);
        fields.add(signin_1st_password_field);
        fields.add(signin_2nd_password_field);
        try {
            if (_check_fields_not_empty(fields)) {
                if (Objects.equals(signin_1st_password_field.getText(), signin_2nd_password_field.getText())) {
                    Customers customer = new Customers(
                        this.model.get_customer_id(signin_email_field.getText()),
                        signin_firstname_field.getText(),
                        signin_lastname_field.getText(),
                        signin_email_field.getText(),
                        signin_1st_password_field.getText(),
                        signin_delivery_address_field.getText()
                    );
                    this.model.get_database().add_customer(customer);
                }
            }
        } catch (IllegalArgumentException err) {System.err.println(err.getMessage());}
    }

    @FXML
    protected void disconnect(ActionEvent evt) {
        Alert disconnect_popup = new Alert(Alert.AlertType.CONFIRMATION);
        disconnect_popup.setTitle("Confirmation de Déconnexion");
        disconnect_popup.setHeaderText("Êtes-vous sûr de vouloir vous déconnecter ?");
        disconnect_popup.setContentText("Cliquez sur 'OK' pour confirmer ou 'Annuler' pour annuler l'action.");
        Optional<ButtonType> user_action = disconnect_popup.showAndWait();
        if (user_action.isPresent() && user_action.get() == ButtonType.OK) {
            _load_screen(evt, "login.fxml");
        }
    }

    // user-choice methods.
    @FXML
    protected void display_main(ActionEvent evt) {
        _load_screen(evt, "main.fxml");
    }

    @FXML
    protected void display_orders(ActionEvent evt) {
        _load_screen(evt, "orders-tracking.fxml");
    }

    @FXML
    protected void export_orders(ActionEvent evt) throws SQLException {
        List<Orders> orders = this.model.get_database().get_orders_to_export(1);
        System.out.println(orders);
        for (Orders order : orders) {
            List<Orders_Options> orders_options = this.model.get_database().get_orders_options(order.get_id());
            System.out.println(orders_options);
            // Ici, écriture dans le fichiers orders.txt
        }

        System.out.println("Méthode pour exporter les commandes avec le statut 'created'");
    }

    // orders-tracking fields.
    @FXML
    private TableView<Orders> orders_table;
    @FXML
    private TableColumn<Orders, String> id_column = new TableColumn<>("id");
    @FXML
    private TableColumn<Orders, Customers> customer_ref_column = new TableColumn<>("customers_ref");
    @FXML
    private TableColumn<Orders, EMOT> emot_ref_column = new TableColumn<>("emot_ref");
    @FXML
    private TableColumn<Orders, Double> price_column = new TableColumn<>("total_price");
    @FXML
    private TableColumn<Orders, OrderStates> state_column = new TableColumn<>("state");
    @FXML
    private TableColumn<Orders, String> tracking_number_column = new TableColumn<>("tracking_number");


    // orders-tracking methods.
    @FXML
    protected void back_to_menu(ActionEvent evt) {
        _load_screen(evt, "user-choice.fxml");
    }

    private void _set_table_view() throws SQLException {
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        customer_ref_column.setCellValueFactory(new PropertyValueFactory<>("customers_ref"));
        emot_ref_column.setCellValueFactory(new PropertyValueFactory<>("emot_ref"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        state_column.setCellValueFactory(new PropertyValueFactory<>("state"));
        tracking_number_column.setCellValueFactory(new PropertyValueFactory<>("tracking_number"));
        ObservableList<Orders> orders_list = FXCollections.observableArrayList(this.model.get_database().get_orders());
        System.out.println(orders_list);
        this.orders_table.setItems(orders_list);
    }

    // load new screen method.
    private void _load_screen(ActionEvent evt, String name) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(name));
            InstanceManager.get_instance().set_current_view_name(fxmlloader.getLocation().getPath());
            AnchorPane view = fxmlloader.load();
            Stage new_stage = InstanceManager.get_instance().get_primary_stage();
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("style/styles.css").toExternalForm());
            new_stage.setScene(scene);
            new_stage.show();
            if (Objects.equals(name, "main.fxml")) {
                Controller controller = fxmlloader.getController();
                controller.emot_car.fire();
            }
        } catch (Exception err) {
            System.err.println(err.getMessage());
        }
    }

    // validations.

    private boolean _check_fields_not_empty(List<TextField> fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                throw new IllegalArgumentException("Un des champs obligatoire n'est pas renseigné");
            }
        }
        return true;
    }

    private void _check_email_field(TextField email) {
        if (!email.getText().contains("@") && (!email.getText().endsWith(".com") || !email.getText().endsWith(".fr"))) {
            throw new IllegalArgumentException("Le champ email doit contenir un '@' et se terminer par '.com' ou '.fr'");
        }
    }
}