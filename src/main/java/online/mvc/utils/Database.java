package online.mvc.utils;

import online.mvc.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Database {
    private Connection connection;

    public Database() {
        try {
            connection = this.connectToDatabase();
        } catch (Exception err) {
            System.err.println(err.getMessage());
        }
    }
    public Connection connectToDatabase() {
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

    public List<Customers> getCustomers() throws SQLException {
        List<Customers> customers = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customers");
        ResultSet queryset = preparedStatement.executeQuery();
        while (queryset.next()) {
            String id = queryset.getString("id");
            String firstname = queryset.getString("firstname");
            String lastname = queryset.getString("lastname");
            String email = queryset.getString("email");
            String password = queryset.getString("password");
            String delivery_address = queryset.getString("delivery_address");
            customers.add(new Customers(id, firstname, lastname, email, password, delivery_address));
        }
        if (customers.isEmpty()) {
            throw new SQLDataException("Aucune données dans la table 'Customers'");
        }
        return customers;
    }

    public Customers getCustomerForId(String customer_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT * FROM customers WHERE id='"+customer_id+"'"
        );
        ResultSet queryset = preparedStatement.executeQuery();
        if (queryset.next()) {
            String id = queryset.getString("id");
            String firstname = queryset.getString("firstname");
            String lastname = queryset.getString("lastname");
            String email = queryset.getString("email");
            String password = queryset.getString("password");
            String delivery_address = queryset.getString("delivery_address");
            return new Customers(id, firstname, lastname, email, password, delivery_address);
        }
        throw new SQLDataException("Aucune données dans la table 'Customers'");
    }

    public void addCustomer(Customers data) throws SQLException {
        String sql = "INSERT INTO customers (id, firstname, lastname, email, password, delivery_address) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        try {
            preparedStatement.setString(1, data.getId());
            preparedStatement.setString(2, data.getFirstname());
            preparedStatement.setString(3, data.getLastname());
            preparedStatement.setString(4, data.getEmail());
            preparedStatement.setString(5, data.getPassword());
            preparedStatement.setString(6, data.getDelivery_address());
            preparedStatement.executeUpdate();
        } catch (Exception err) {
            System.err.println(err.getMessage());
            throw new SQLException("La création de l'utilisateur à échoué, veuillez réessayer");
        }
    }

    public List<Orders> getOrders(Customers loggedUser) throws SQLException {
        List<Orders> orders = new ArrayList<>();
        PreparedStatement preparedStatement;
        if (Objects.equals(loggedUser, null)) {
            preparedStatement = connection.prepareStatement("SELECT * FROM orders");
        } else {
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE client_ref='"+loggedUser.getId()+"'");
        }
        ResultSet queryset = preparedStatement.executeQuery();
        while (queryset.next()) {
            String id = queryset.getString("id");
            Customers client_ref = this.getCustomerForId(queryset.getString("client_ref"));
            EMOT emot_ref = this.getEmotForId(queryset.getInt("emot_ref"));
            double total_price = queryset.getDouble("total_price");
            OrderStates state = this.getStateForId(queryset.getInt("state"));
            String tracking_number = queryset.getString("tracking_number");
            orders.add(new Orders(id, client_ref, emot_ref, total_price, state, tracking_number));
        }
        if (orders.isEmpty()) {
            throw new SQLDataException("Aucune données dans la table 'Orders'");
        }
        return orders;
    }

    public List<Orders> getOrdersToExport() throws SQLException {
        List<Orders> orders = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE state=1");
        ResultSet queryset = preparedStatement.executeQuery();
        while (queryset.next()) {
            String id = queryset.getString("id");
            Customers client_ref = this.getCustomerForId(queryset.getString("client_ref"));
            EMOT emot_ref = this.getEmotForId(queryset.getInt("emot_ref"));
            double total_price = queryset.getDouble("total_price");
            OrderStates state = this.getStateForId(queryset.getInt("state"));
            String tracking_number = queryset.getString("tracking_number");
            orders.add(new Orders(id, client_ref, emot_ref, total_price, state, tracking_number));
        }
        if (orders.isEmpty()) {
            throw new SQLDataException("Aucune nouvelle commande à exporter");
        }
        return orders;
    }

    public Orders getOrderForId(String order_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT * FROM orders WHERE id='"+order_id+"'"
        );
        ResultSet queryset = preparedStatement.executeQuery();
        if (queryset.next()) {
            String id = queryset.getString("id");
            Customers client_ref = this.getCustomerForId(queryset.getString("client_ref"));
            EMOT emot_ref = this.getEmotForId(queryset.getInt("emot_ref"));
            double total_price = queryset.getDouble("total_price");
            OrderStates state = this.getStateForId(queryset.getInt("state"));
            String tracking_number = queryset.getString("tracking_number");
            return new Orders(id, client_ref, emot_ref, total_price, state, tracking_number);
        }
        throw new SQLDataException("Aucune données dans la table 'Orders'");
    }

    public void addOrder(Orders order) throws SQLException {
        String sql = "INSERT INTO orders (id, client_ref, emot_ref, total_price, state, tracking_number) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        try {
            preparedStatement.setString(1, order.getId());
            preparedStatement.setString(2, order.getCustomers_ref().getId());
            preparedStatement.setInt(3, order.getEmot_ref().getId());
            preparedStatement.setDouble(4, order.getTotalPrice());
            preparedStatement.setInt(5, order.getStateId().getId());
            preparedStatement.setString(6, order.getTrackingNumber());
            preparedStatement.executeUpdate();
        } catch (Exception err) {
            System.err.println(err.getMessage());
            throw new SQLException("La création de la commande à échoué, veuillez réessayer");
        }
    }

    public void updateOrder(String id, int state) throws SQLException {
        String sql = "UPDATE orders SET state="+state+" WHERE id='"+id+"'";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        try {
            preparedStatement.executeUpdate();
        } catch (Exception err) {
            System.err.println(err.getMessage());
            throw new SQLException("La mise à jour de la commande '"+id+"' à échoué");
        }
    }

    public List<Orders_Options> getOrdersOptions(String _order_id) throws SQLException {
        List<Orders_Options> orders_options = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM orders_options WHERE order_id='"+_order_id+"'"
        );
        ResultSet queryset = preparedStatement.executeQuery();
        while (queryset.next()) {
            Orders order_id = this.getOrderForId(queryset.getString("order_id"));
            Options option_id = this.getOptionForParameters(queryset.getInt("option_id"), null, null, null);
            orders_options.add(new Orders_Options(order_id, option_id));
        }
        if (orders_options.isEmpty()) {
            throw new SQLDataException("Aucune données dans la table 'Orders_Options'");
        }
        return orders_options;
    }

    public void addOrdersOptions(String order_id, HashMap<String, Options> options) throws SQLException {
        for (Options option : options.values()) {
            String sql = "INSERT INTO orders_options (order_id, option_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            try {
                preparedStatement.setString(1, order_id);
                preparedStatement.setInt(2, option.getId());
                preparedStatement.executeUpdate();
            } catch (Exception err) {
                System.err.println(err.getMessage());
            }
        }
    }

    public EMOT getEmotForId(int _id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM emot WHERE id="+_id);
        ResultSet queryset = preparedStatement.executeQuery();
        if (queryset.next()) {
            int id = queryset.getInt("id");
            String name = queryset.getString("name");
            double price = queryset.getDouble("price");
            return new EMOT(id, name, price);
        }
        throw new SQLDataException("Aucune données dans la table 'EMOT'");
    }

    public List<Options> getEmotOptionsForType(int ref, String option_type) throws SQLException {
        List<Options> options = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM options WHERE emot_ref="+ref+" AND type='"+option_type+"'"
        );
        ResultSet queryset = preparedStatement.executeQuery();
        while (queryset.next()) {
            int id = queryset.getInt("id");
            String name = queryset.getString("name");
            String type = queryset.getString("type");
            double price = queryset.getDouble("price");
            int emot_ref = queryset.getInt("emot_ref");
            options.add(new Options(id, name, type, price, this.getEmotForId(emot_ref)));
        }
        if (options.isEmpty()) {
            throw new SQLDataException("Aucune données dans la table 'Options'");
        }
        return options;
    }

    public Options getOptionForParameters(Integer _id, Integer ref, String option_type, String option_name) throws SQLException {
        PreparedStatement preparedStatement;
        if (!Objects.equals(_id, null)) {
            preparedStatement = connection.prepareStatement(
                "SELECT * FROM options WHERE id="+_id
            );
        } else {
            preparedStatement = connection.prepareStatement(
                "SELECT * FROM options WHERE emot_ref="+ref+" AND type='"+option_type+"' AND name='"+option_name+"'"
            );
        }
        ResultSet queryset = preparedStatement.executeQuery();
        if (queryset.next()) {
            int id = queryset.getInt("id");
            String name = queryset.getString("name");
            String type = queryset.getString("type");
            double price = queryset.getDouble("price");
            EMOT emot_ref = this.getEmotForId(queryset.getInt("emot_ref"));
            return new Options(id, name, type, price, emot_ref);
        }
        throw new SQLDataException("Aucune données dans la table 'Options'");
    }

    public OrderStates getStateForId(int state_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM ordersstates WHERE id="+state_id
        );
        ResultSet queryset = preparedStatement.executeQuery();
        if (queryset.next()) {
            int id = queryset.getInt("id");
            String name = queryset.getString("name");
            return new OrderStates(id, name);
        }
        throw new SQLDataException("Aucune données dans la table 'OrdersStates'");
    }
}
