package online.mvc.utils;

import online.mvc.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database {
    private Connection connection;

    public Database() {
        try {
            connection = this.connect_to_database();
        } catch (Exception err) {
            System.err.println(err.getMessage());
        }
    }
    public Connection connect_to_database() {
        String url = "jdbc:mysql://127.0.0.1:3306/emotmanagement";
        String driver = "com.mysql.jdbc.Driver";
        String login = "root";
        String password = "";
        try {
            Class.forName(driver);
            System.out.println("Connecting to database...");
            Connection connection = DriverManager.getConnection(url, login, password);
            System.out.println("Connected to database !");
            return connection;
        } catch (ClassNotFoundException | SQLException err) {
            System.err.println(err.getMessage());
        }
        return null;
    }

    public List<Customers> get_customers() throws SQLException {
        List<Customers> customers = new ArrayList<>();
        PreparedStatement prepared_statement = connection.prepareStatement("SELECT * FROM customers");
        ResultSet queryset = prepared_statement.executeQuery();
        while (queryset.next()) {
            String id = queryset.getString("id");
            String firstname = queryset.getString("firstname");
            String lastname = queryset.getString("lastname");
            String email = queryset.getString("email");
            String password = queryset.getString("password");
            String delivery_address = queryset.getString("delivery_address");
            customers.add(new Customers(id, firstname, lastname, email, password, delivery_address));
        }
        return customers;
    }

    public String get_id_or_email_existing_error(String email) throws SQLException {
        PreparedStatement prepared_statement = connection.prepareStatement("SELECT * FROM customers");
        ResultSet queryset = prepared_statement.executeQuery();
        while (queryset.next()) {
            if (Objects.equals(queryset.getString("email"), email)) {
                throw new IllegalArgumentException("Un compte pour l'email '"+email+"' est déjà renseigné.");
            }
            if (queryset.last()) {
                return queryset.getString("id").replace("CUS", "");
            }
        }
        throw new SQLDataException("Aucune données dans la table 'Customer'");
    }

    public void add_customer(Customers data) throws SQLException {
        String sql = "INSERT INTO customers (id, firstname, lastname, email, delivery_address) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement prepared_statement = connection.prepareStatement(sql);
        try {
            prepared_statement.setString(1, data.get_id());
            prepared_statement.setString(2, data.get_firstname());
            prepared_statement.setString(3, data.get_lastname());
            prepared_statement.setString(3, data.get_email());
            prepared_statement.setString(3, data.get_password());
            prepared_statement.setString(3, data.get_delivery_address());
            prepared_statement.executeUpdate();
        } catch (Exception err) {
        System.err.println(err.getMessage());
        }
    }

    public List<Orders> get_orders() throws SQLException {
        List<Orders> orders = new ArrayList<>();
        PreparedStatement prepared_statement = connection.prepareStatement("SELECT * FROM orders");
        ResultSet queryset = prepared_statement.executeQuery();
        List<Customers> customers = get_customers();
        while (queryset.next()) {
            String id = queryset.getString("id");
            Customers client_ref = (Customers) queryset.getObject("client_ref");
            EMOT emot_ref = (EMOT) queryset.getObject("emot_ref");
            List<Options> options = (List<Options>) queryset.getObject("options");
            double total_price = queryset.getDouble("total_price");
            OrderStates state = (OrderStates) queryset.getObject("state");
            String tracking_number = queryset.getString("tracking_number");
            orders.add(new Orders(id, client_ref, emot_ref, options, total_price, state, tracking_number));
        }
        return orders;
    }

    public void add_order(ArrayList<Orders> data) throws SQLException {
        String sql = "INSERT INTO orders (id, client_ref, emot_ref, total_price, state, tracking_number) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement prepared_statement = connection.prepareStatement(sql);
        for (Orders order : data) {
            try {
                prepared_statement.setString(1, order.get_id());
                prepared_statement.setString(2, order.get_customers_ref().get_id());
                prepared_statement.setInt(3, order.get_emot_ref().get_id());
                prepared_statement.setDouble(3, order.get_total_price());
                prepared_statement.setInt(3, order.get_state().get_id());
                prepared_statement.setString(3, order.get_tracking_number());
                prepared_statement.executeUpdate();
            } catch (Exception err) {
                System.err.println(err.getMessage());
            }
        }
    }

    public EMOT get_emot(int _id) throws SQLException {
        List<EMOT> emot = new ArrayList<>();
        PreparedStatement prepared_statement = connection.prepareStatement("SELECT * FROM emot WHERE id="+_id);
        ResultSet queryset = prepared_statement.executeQuery();
        if (queryset.next()) {
            int id = queryset.getInt("id");
            String name = queryset.getString("name");
            double price = queryset.getDouble("price");
            emot.add(new EMOT(id, name, price));
            return new EMOT(id, name, price);
        }
        throw new SQLDataException("Aucune données dans la table 'EMOT'");
    }

    public List<Options> get_options_of_emot(int ref) throws SQLException {
        List<Options> options = new ArrayList<>();
        PreparedStatement prepared_statement = connection.prepareStatement("SELECT * FROM options WHERE emot_ref="+ref);
        ResultSet queryset = prepared_statement.executeQuery();
        while (queryset.next()) {
            String id = queryset.getString("id");
            String name = queryset.getString("name");
            String type = queryset.getString("type");
            double price = queryset.getDouble("price");
            int emot_ref = queryset.getInt("emot_ref");
            options.add(new Options(id, name, type, price, this.get_emot(emot_ref)));
        }
        return options;
    }
}
