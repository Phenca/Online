package online.mvc;

import javafx.beans.property.SimpleStringProperty;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLDataException;
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
            this.setTableView();
        }
    }

    @FXML
    private Label error_label;

    // login.fxml fields.
    @FXML
    private TextField login_email_field;
    @FXML
    private TextField login_password_field;

    // login.fxml methods.
    @FXML
    protected void switch_to_account_creation(ActionEvent evt) {
        loadScreen(evt, "signin.fxml");
    }

    @FXML
    protected boolean log_to_account(ActionEvent evt) throws SQLException {
        ArrayList<TextField> fields = new ArrayList<>();
        fields.add(login_email_field);
        fields.add(login_password_field);
        try {
            if (checkFieldsNotEmpty(fields)) {
                checkEmailField(login_email_field);
                for (Customers customer : this.model.getDatabase().getCustomers()) {
                    if (Objects.equals(customer.getEmail(), login_email_field.getText())) {
                        if (Objects.equals(customer.getPassword(), login_password_field.getText())) {
                            System.out.println("Authentication successful !");
                            InstanceManager.get_instance().set_logged_user(customer);
                            loadScreen(evt, "user-choice.fxml");
                            return true;
                        }
                    }
                }
            }
        } catch (IllegalArgumentException err) {
            error_label.setText(err.getMessage());
        }
        error_label.setText("Mauvais email ou mot de passe renseigné !");
        return false;
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
        loadScreen(evt, "login.fxml");
    }

    @FXML
    protected void create_account(ActionEvent evt) throws SQLException {
        ArrayList<TextField> fields = new ArrayList<>();
        fields.add(signin_email_field);
        fields.add(signin_1st_password_field);
        fields.add(signin_2nd_password_field);
        try {
            if (checkFieldsNotEmpty(fields)) {
                if (Objects.equals(signin_1st_password_field.getText(), signin_2nd_password_field.getText())) {
                    Customers customer = new Customers(
                            this.model.getCustomerId(signin_email_field.getText()),
                            signin_firstname_field.getText(),
                            signin_lastname_field.getText(),
                            signin_email_field.getText(),
                            signin_1st_password_field.getText(),
                            signin_delivery_address_field.getText()
                    );
                    this.model.getDatabase().addCustomer(customer);
                    Alert popup = new Alert(Alert.AlertType.INFORMATION);
                    popup.setTitle("Compte utilisateur créé !");
                    popup.setHeaderText("Votre compte à bien été enregistré !");
                    popup.showAndWait();
                    loadScreen(evt, "user-choice.fxml");
                }
            }
        } catch (IllegalArgumentException err) {error_label.setText(err.getMessage());}
    }

    // user-choice methods.
    @FXML
    protected void display_main(ActionEvent evt) {
        loadScreen(evt, "main.fxml");
    }

    @FXML
    protected void display_orders(ActionEvent evt) {
        System.out.println("DIsplay orders");
        loadScreen(evt, "orders-tracking.fxml");
        System.out.println("orders displayed");
    }

    @FXML
    protected void export_orders(ActionEvent evt) {
        try {
            this.model.writeFile("orders.csv");
        } catch (SQLException | IOException err) {
            error_label.setText(err.getMessage());
        }
    }

    @FXML
    protected void import_orders_advancement() {
        try {
            this.model.readFile("advancement.csv");
        } catch (FileNotFoundException | SQLException err) {
            error_label.setText(err.getMessage());
        } catch (NumberFormatException err) {
            error_label.setText("Les données du fichier d'import sont incorrecte !");
        }
    }

    // orders-tracking fields.
    @FXML
    private TableView<Orders> orders_table;
    @FXML
    private TableColumn<Orders, String> id_column = new TableColumn<>("id");
    @FXML
    private TableColumn<Orders, String> customer_ref_column;
    @FXML
    private TableColumn<Orders, String> emot_ref_column;
    @FXML
    private TableColumn<Orders, Double> price_column;
    @FXML
    private TableColumn<Orders, String> state_column;
    @FXML
    private TableColumn<Orders, String> tracking_number_column;

    // orders-tracking methods.
    @FXML
    protected void back_to_menu(ActionEvent evt) {
        loadScreen(evt, "user-choice.fxml");
    }

    @FXML
    private void setTableView() throws SQLException {
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        customer_ref_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomerName()));
        emot_ref_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmotName()));
        price_column.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        state_column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOrderState()));
        tracking_number_column.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        try {
            ObservableList<Orders> ordersList = FXCollections.observableArrayList(this.model.getDatabase().getOrders(InstanceManager.get_instance().get_logged_user()));
            if (!ordersList.isEmpty()) {
                this.orders_table.setItems(ordersList);
            }
        } catch (SQLDataException err) {
            System.err.println("No orders for user " + InstanceManager.get_instance().get_logged_user() + ", Message : " + err.getMessage());
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
            this.model.emotId = 1;
            this.populateMainInterface(this.model.emotId, grid_option_2, "rim");
        } else if (evt.getSource().equals(emot_bike)) {
            this.model.emotId = 2;
            this.populateMainInterface(this.model.emotId, grid_option_2, "frame");
        } else if (evt.getSource().equals(emot_scooter)) {
            this.model.emotId = 3;
            this.populateMainInterface(this.model.emotId, grid_option_2, "frame");
        }
        this.populateMainInterface(this.model.emotId, grid_option_1, "color");
        this.populateMainInterface(this.model.emotId, grid_option_3, "battery");
        this.setDefaultImage();
    }

    protected void setDefaultImage() {
        this.main_frame.setImage(new Image(this.model.resourcesPath +"img/"+this.model.emotId +"-color-white.jpg"));
    }

    protected void populateMainInterface(int emot_id, GridPane grid, String option_type) throws SQLException {
        this.resetMainInterface(grid);
        List<Options> options = this.model.getDatabase().getEmotOptionsForType(emot_id, option_type);
        this.emot_price.setText(String.valueOf(options.getFirst().getEmot_ref().getPrice()));
        this.total_price.setText(String.valueOf(options.getFirst().getEmot_ref().getPrice()));
        int i=0;
        String imageName;
        for (Options option : options) {
            if (option.getPrice() == 0) {
                this.model.optionsMap.put(option_type, option);
            }
            if (grid.getChildren().get(i) instanceof ImageView image) {
                if (option_type.equals("color")){
                    imageName = option.getType()+"-"+option.getName()+".jpg";
                } else {
                    imageName = option.getEmot_ref().getId()+"-"+option.getType()+"-"+option.getName()+".jpg";
                }
                image.setImage(new Image(this.model.resourcesPath +option.getType()+"/"+imageName));
                i++;
            }
        }
    }

    protected void calculatePrice() {
        double price = 0;
        for (Options option : this.model.optionsMap.values()) {
            price += option.getPrice();
        }
        this.options_price.setText(String.valueOf(price));
        this.calculateTotal();
    }

    protected void calculateTotal() {
        double emotPrice = Double.parseDouble(this.emot_price.getText());
        double optionsPrice = Double.parseDouble(this.options_price.getText());
        this.total_price.setText(String.valueOf(emotPrice+optionsPrice));
    }

    @FXML
    protected void update_main_interface(MouseEvent event) throws SQLException {
        if (event.getSource() instanceof ImageView image) {
            if (!Objects.isNull(image.getImage())) {
                String fileName = Paths.get(image.getImage().getUrl()).getFileName().toString();
                List<String> options = new ArrayList<>(List.of(fileName.replace(".jpg", "").split("-")));
                if (fileName.startsWith("color")) {
                    this.main_frame.setImage(new Image(this.model.resourcesPath + "img/" + this.model.emotId + "-" + fileName));
                } else {options.removeFirst();}
                options.addFirst(String.valueOf(this.model.emotId));
                Options option = this.model.getDatabase().getOptionForParameters(null, Integer.parseInt(options.getFirst()), options.get(1), options.getLast());
                this.model.optionsMap.replace(option.getType(), option);
                this.calculatePrice();
            }
        }
    }

    @FXML
    protected void validate_order() throws SQLException {
        error_label.setText("");
        Alert disconnectPopup = new Alert(Alert.AlertType.CONFIRMATION);
        disconnectPopup.setTitle("Confirmation de commande");
        disconnectPopup.setHeaderText("Êtes-vous sûr de vouloir valider votre commande ?");
        disconnectPopup.setContentText("Cliquez sur 'OK' pour confirmer ou 'Annuler' pour annuler l'action.");
        Optional<ButtonType> userAction = disconnectPopup.showAndWait();
        if (userAction.isPresent() && userAction.get() == ButtonType.OK) {
            String[] orderIds = this.model.getOrderIds();
            try {
                Orders order = new Orders(
                        orderIds[0],
                        InstanceManager.get_instance().get_logged_user(),
                        this.model.getDatabase().getEmotForId(this.model.emotId),
                        Double.parseDouble(this.total_price.getText()),
                        this.model.getDatabase().getStateForId(1),
                        orderIds[1]
                );
                this.model.getDatabase().addOrder(order);
                this.model.getDatabase().addOrdersOptions(order.getId(), this.model.optionsMap);
            } catch (SQLException err) {
                error_label.setText(err.getMessage());
            }
        }
    }

    protected void resetMainInterface(GridPane grid) {
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
    protected void disconnect(ActionEvent evt) {
        Alert disconnectPopup = new Alert(Alert.AlertType.CONFIRMATION);
        disconnectPopup.setTitle("Confirmation de Déconnexion");
        disconnectPopup.setHeaderText("Êtes-vous sûr de vouloir vous déconnecter ?");
        disconnectPopup.setContentText("Cliquez sur 'OK' pour confirmer ou 'Annuler' pour annuler l'action.");
        Optional<ButtonType> userAction = disconnectPopup.showAndWait();
        if (userAction.isPresent() && userAction.get() == ButtonType.OK) {
            loadScreen(evt, "login.fxml");
        }
    }

    // load new screen method.
    private void loadScreen(ActionEvent evt, String name) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(name));
            System.out.println(getClass().getResource(name));
            InstanceManager.get_instance().set_current_view_name(fxmlloader.getLocation().getPath());
            AnchorPane view = fxmlloader.load();
            Stage new_stage = InstanceManager.get_instance().get_primary_stage();
            Scene scene = new Scene(view);
            if (!Objects.equals(name, "orders-tracking.fxml")){
                scene.getStylesheets().add(getClass().getResource("style/styles.css").toExternalForm());
            }
            new_stage.setScene(scene);
            new_stage.show();
            if (Objects.equals(name, "main.fxml")) {
                Controller controller = fxmlloader.getController();
                controller.emot_car.fire();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    // validations.
    private boolean checkFieldsNotEmpty(List<TextField> fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                throw new IllegalArgumentException("Un des champs obligatoire n'est pas renseigné");
            }
        }
        return true;
    }

    private void checkEmailField(TextField email) {
        if (!email.getText().contains("@") && (!email.getText().endsWith(".com") || !email.getText().endsWith(".fr"))) {
            throw new IllegalArgumentException("Le champ email doit contenir un '@' et se terminer par '.com' ou '.fr'");
        }
    }
}