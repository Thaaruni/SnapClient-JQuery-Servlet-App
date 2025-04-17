package SnapClient.backend.data;

import SnapClient.backend.entity.Customer;
import SnapClient.backend.to.CustomerTo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDataAccess {

    public static List<Customer> getAllCustomers(Connection connection) {
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM customer");
            List<Customer> customerList = new ArrayList<>();
            while (rst.next()) {
                int id = rst.getInt("id");
                String name = rst.getString("name");
                String address = rst.getString("address");
                String profilePic = rst.getString("profile_pic");
                customerList.add(new Customer(id, name, address, profilePic));
            }
            return customerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int saveCustomer(Connection connection, Customer customer) {
        try {
            Statement stm = connection.createStatement();
            stm.executeUpdate("INSERT INTO customer (name, address, profile_pic) VALUES ('%s', '%s', '%s')"
                            .formatted(customer.name(), customer.address(), customer.profilePicture()),
                    Statement.RETURN_GENERATED_KEYS);
            ResultSet rst = stm.getGeneratedKeys();
            rst.next();
            return rst.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProfilePic(Connection connection, int id) {
        try(Statement stm = connection.createStatement()){
            ResultSet rst = stm.executeQuery("SELECT profile_pic FROM customer WHERE id = " + id);
            if (rst.next()){
                return rst.getString("profile_pic");
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean deleteCustomer(Connection connection, int id) {
        try {
            Statement stm = connection.createStatement();
            return stm.executeUpdate("DELETE FROM customer WHERE id ='%s'".formatted(id)) == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}