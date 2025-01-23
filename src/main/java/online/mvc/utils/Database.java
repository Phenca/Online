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

    public Customers get_customer_for_id(String customer_id) throws SQLException {
        PreparedStatement prepared_statement = connection.prepareStatement(
            "SELECT * FROM customers WHERE id'"+customer_id+"'"
        );
        ResultSet queryset = prepared_statement.executeQuery();
        if (queryset.next()) {
            String id = queryset.getString("id");
            String firstname = queryset.getString("firstname");
            String lastname = queryset.getString("lastname");
            String email = queryset.getString("email");
            String password = queryset.getString("password");
            String delivery_address = queryset.getString("delivery_address");
            return new Customers(id, firstname, lastname, email, password, delivery_address);
        }
        throw new SQLDataException("Aucune données dans la table 'Customer'");
    }

    public OrderStates get_state_for_id(String state_id) throws SQLException {
        PreparedStatement prepared_statement = connection.prepareStatement(
                "SELECT * FROM ordersstates WHERE id'"+state_id+"'"
        );
        ResultSet queryset = prepared_statement.executeQuery();
        if (queryset.next()) {
            int id = queryset.getInt("id");
            String name = queryset.getString("name");
            return new OrderStates(id, name);
        }
        throw new SQLDataException("Aucune données dans la table 'OrdersStates'");
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
        while (queryset.next()) {
            String id = queryset.getString("id");
            Customers client_ref = this.get_customer_for_id(queryset.getString("client_ref"));
            EMOT emot_ref = this.get_emot_for_id(queryset.getInt("emot_ref"));
            double total_price = queryset.getDouble("total_price");
            OrderStates state = this.get_state_for_id(queryset.getString("state"));
            String tracking_number = queryset.getString("tracking_number");
            orders.add(new Orders(id, client_ref, emot_ref, total_price, state, tracking_number));
        }
        return orders;
    }

    public Orders get_order_for_id(String order_id) throws SQLException {
        PreparedStatement prepared_statement = connection.prepareStatement(
            "SELECT * FROM orders WHERE id='"+order_id+"'"
        );
        ResultSet queryset = prepared_statement.executeQuery();
        List<Customers> customers = get_customers();
        if (queryset.next()) {
            String id = queryset.getString("id");
            Customers client_ref = this.get_customer_for_id(queryset.getString("client_ref"));
            EMOT emot_ref = this.get_emot_for_id(queryset.getInt("emot_ref"));
            double total_price = queryset.getDouble("total_price");
            OrderStates state = this.get_state_for_id(queryset.getString("state"));
            String tracking_number = queryset.getString("tracking_number");
            return new Orders(id, client_ref, emot_ref, total_price, state, tracking_number);
        }
        throw new SQLDataException("Aucune données dans la table 'Orders'");
    }

    public List<Orders_Options> get_orders_options(String _order_id) throws SQLException {
        List<Orders_Options> orders_options = new ArrayList<>();
        PreparedStatement prepared_statement = connection.prepareStatement(
            "SELECT * FROM orders_options WHERE order_id='"+_order_id+"'"
        );
        ResultSet queryset = prepared_statement.executeQuery();
        while (queryset.next()) {
            Orders order_id = this.get_order_for_id(queryset.getString("order_id"));
//            String order_id = queryset.getString("order_id");
//            String option_id = queryset.getString("option_id");
            Options option_id = this.get_option_for_id(queryset.getString("option_id"));
            orders_options.add(new Orders_Options(order_id, option_id));
        }
        return orders_options;
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

    public EMOT get_emot_for_id(int _id) throws SQLException {
        PreparedStatement prepared_statement = connection.prepareStatement("SELECT * FROM emot WHERE id="+_id);
        ResultSet queryset = prepared_statement.executeQuery();
        if (queryset.next()) {
            int id = queryset.getInt("id");
            String name = queryset.getString("name");
            double price = queryset.getDouble("price");
            return new EMOT(id, name, price);
        }
        throw new SQLDataException("Aucune données dans la table 'EMOT'");
    }

    public List<Options> get_emot_for_id_options(int ref, String option_type) throws SQLException {
        List<Options> options = new ArrayList<>();
        PreparedStatement prepared_statement = connection.prepareStatement(
                "SELECT * FROM options WHERE emot_ref="+ref+" AND type='"+option_type+"'"
        );
        ResultSet queryset = prepared_statement.executeQuery();
        while (queryset.next()) {
            String id = queryset.getString("id");
            String name = queryset.getString("name");
            String type = queryset.getString("type");
            double price = queryset.getDouble("price");
            int emot_ref = queryset.getInt("emot_ref");
            options.add(new Options(id, name, type, price, this.get_emot_for_id(emot_ref)));
        }
        return options;
    }

    public Options get_emot_for_id_option(int ref, String option_type, String option_name) throws SQLException {
        PreparedStatement prepared_statement = connection.prepareStatement(
            "SELECT * FROM options WHERE emot_ref="+ref+" AND type='"+option_type+"' AND name='"+option_name+"'"
        );
        ResultSet queryset = prepared_statement.executeQuery();
        if (queryset.next()) {
            String id = queryset.getString("id");
            String name = queryset.getString("name");
            String type = queryset.getString("type");
            double price = queryset.getDouble("price");
            int emot_ref = queryset.getInt("emot_ref");
            return new Options(id, name, type, price, this.get_emot_for_id(emot_ref));
        }
        throw new SQLDataException("Aucune données dans la table 'EMOT'");
    }

    public Options get_option_for_id(String _id) throws SQLException {
        PreparedStatement prepared_statement = connection.prepareStatement(
            "SELECT * FROM options WHERE id='"+_id+"'"
        );
        ResultSet queryset = prepared_statement.executeQuery();
        if (queryset.next()) {
            String id = queryset.getString("id");
            String name = queryset.getString("name");
            String type = queryset.getString("type");
            double price = queryset.getDouble("price");
            EMOT emot_ref = this.get_emot_for_id(queryset.getInt("emot_ref"));
            return new Options(id, name, type, price, emot_ref);
        }
        throw new SQLDataException("Aucune données dans la table 'Options'");
    }
}
