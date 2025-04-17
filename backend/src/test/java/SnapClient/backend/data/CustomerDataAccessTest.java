package SnapClient.backend.data;

import SnapClient.backend.data.CustomerDataAccess;
import SnapClient.backend.to.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

// Test Suite
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CustomerDataAccessTest {

    Connection connection;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
                "", "");
        executeScripts();
    }

    private void executeScripts(){
        try(BufferedReader schemeBr = new BufferedReader(new InputStreamReader
                (Objects.requireNonNull(getClass().getResourceAsStream("/schema.sql"))))){
            StringBuilder schemaScript = new StringBuilder();
            schemeBr.lines().forEach(schemaScript::append);
            try(var stm = connection.createStatement()){
                stm.execute(schemaScript.toString());
            }

            Path path = Path.of(Objects.requireNonNull(getClass().getResource("/data.sql")).toURI());
            String dataScript = Files.readString(path);
            try(var stm = connection.createStatement()){
                stm.execute(dataScript);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    // Test Case
    @Test
    void getAllCustomers() {
        // 2. Exercise (SUT = System Under Test)
        List<Customer> customerList = CustomerDataAccess.getAllCustomers(connection);

        // 3. Verify (State)
        assertFalse(customerList.isEmpty());
        assertEquals(5, customerList.size());
        customerList.forEach(System.out::println);
    }

    // Test Case
    @CsvSource(textBlock = """
            Buddhi,     Panadura,    buddhi.jpeg
            Duliya,     Moratuwa,    duliya.jpeg
            Milinda,    Gamapaha,    milinda.jpeg
            """, delimiter = ',')
    @ParameterizedTest
    void saveCustomer(String name, String address, String profilePic) {
        int newId = CustomerDataAccess
                .saveCustomer(connection, new Customer(null, name, address, profilePic));

        assertEquals(6, newId);
        assertDoesNotThrow(()->{
            try(Statement stm = connection.createStatement()){
                ResultSet rst = stm
                        .executeQuery("SELECT name, address, profile_pic FROM customer WHERE id = " + newId);
                assertTrue(rst.next());
                assertEquals(name, rst.getString("name"));
                assertEquals(address, rst.getString("address"));
                assertEquals(profilePic, rst.getString("profile_pic"));
            }
        });
    }

    // Test Case
        @ValueSource(ints = {1, 2, 3})
        @ParameterizedTest
        void deleteCustomer(int id) {
            assertDoesNotThrow(() -> {
                boolean result = CustomerDataAccess.deleteCustomer(connection, id);
                assertTrue(result);
            });
    }
}