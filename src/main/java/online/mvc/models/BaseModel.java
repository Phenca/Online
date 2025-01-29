package online.mvc.models;

import online.mvc.utils.Database;
import online.mvc.utils.InstanceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class BaseModel {
    private final Database database;
    public int emotId;
    public HashMap<String, Options> optionsMap = new HashMap<>();
    public String resourcesPath;

    public BaseModel() {
        this.database = InstanceManager.get_instance().get_database();
        this.emotId = 1;
        this.resourcesPath = "C:/Users/Soran/IdeaProjects/Online/src/main/resources/online/mvc/";
    }

    public Database getDatabase() {
        return database;
    }

    public String getCustomerId(String email) throws SQLException {
        List<Customers> customers = this.database.getCustomers();
        for (Customers customer : customers) {
            if (Objects.equals(customer.getEmail(), email)) {
                throw new IllegalArgumentException("Un compte pour l'email '"+email+"' est déjà renseigné.");
            }
            if (Objects.equals(customers.getLast(), customer)) {
                return getNewID("CUS", customer.getId());
            }
        }
        throw new SQLDataException("Aucune données dans la table 'Customers'");
    }

    public String[] getOrderIds() throws SQLException {
        List<Orders> orders = this.database.getOrders(InstanceManager.get_instance().get_logged_user());
        if (!orders.isEmpty()) {
            return new String[]{
                    getNewID("CMD", orders.getLast().getId()),
                    getNewID("TKR", orders.getLast().getTrackingNumber())
            };
        }
        throw new SQLDataException("Aucune données dans la table 'Orders'");
    }

    public String getNewID(String label, String id) {
        int _id = Integer.parseInt(id.replace(label, ""));
        String cmdId;
        if (_id+1 < 10) {
            cmdId = label+"0"+_id+1;
        } else {
            cmdId = label+_id+1;
        }
        return cmdId;
    }

    public void writeFile(String filename) throws SQLException, IOException {
        List<Orders> orders = this.getDatabase().getOrdersToExport();
        List<Orders_Options> ordersOptions;
        File exportOrders = new File(this.resourcesPath +"/"+filename);
        FileWriter writer = new FileWriter(exportOrders, false);
        for (Orders order : orders) {
            ordersOptions = (this.getDatabase().getOrdersOptions(order.getId()));
            StringBuilder line = new StringBuilder();
            for (Orders_Options orderOption : ordersOptions) {
                if (Objects.equals(orderOption, ordersOptions.getFirst())) {
                    line.append(orderOption.getOrder_id());
                }
                line.append(orderOption.getOption_id());
            }
            if (exportOrders.exists()) {
                writer.write(line+"\n");
                this.getDatabase().updateOrder(order.getId(), 2);
            }
        }
        writer.close();
    }

    public void readFile(String filename) throws FileNotFoundException, SQLException {
        File importOrders = new File(this.resourcesPath +"/"+filename);
        Scanner scanner = new Scanner(importOrders);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(";");
            this.getDatabase().updateOrder(line[0], Integer.parseInt(line[4]));
        }
    }
}
