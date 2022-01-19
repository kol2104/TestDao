
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomersDao implements Dao<Customer> {

    private static final String GET_CUSTOMER_BY_ID = "select * from customers where customer_id=?";
    private static final String DELETE_CUSTOMER_BY_ID = "delete from customers where customer_id=?";
    private static final String UPDATE_CUSTOMER_BY_ID = "update customers set customer_id=cast(? as int),first_name=?,last_name=?,middle_name=?,phone_number=?,email=? where customer_id=?";
    private static final String INSERT_INTO_CUSTOMER = "insert into customers (customer_id, first_name, last_name, middle_name, phone_number, email) values (?, ?, ?, ?, ?, ?)";
    private Connection con;

    public CustomersDao() {
        con = ConnectionManager.getConnection();
    }

    @Override
    public Optional<Customer> get(int id) {
        try (PreparedStatement pstnt = con.prepareStatement(GET_CUSTOMER_BY_ID)) {
            pstnt.setInt(1, id);

            ResultSet res = pstnt.executeQuery();
            while (res.next()) {
                Customer cust = new Customer();
                cust.setCustomerId(res.getInt("customer_id"));
                cust.setFirstName(res.getString("first_name"));
                cust.setLastName(res.getString("last_name"));
                cust.setMiddleName(res.getString("middle_name"));
                cust.setPhoneNumber(res.getString("phone_number"));
                cust.setEmail(res.getString("email"));
                return Optional.of(cust);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();

        try (Statement stnt = con.createStatement()) {
            ResultSet res = stnt.executeQuery("select * from customers");
            while (res.next()) {
                Customer cust = new Customer();
                cust.setCustomerId(res.getInt("customer_id"));
                cust.setFirstName(res.getString("first_name"));
                cust.setLastName(res.getString("last_name"));
                cust.setMiddleName(res.getString("middle_name"));
                cust.setPhoneNumber(res.getString("phone_number"));
                cust.setEmail(res.getString("email"));
                customers.add(cust);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customers;
    }

    @Override
    public void save(Customer customer) {
        try (PreparedStatement pstnt = con.prepareStatement(INSERT_INTO_CUSTOMER)) {
            pstnt.setInt(1, customer.getCustomerId());
            pstnt.setString(2, customer.getFirstName());
            pstnt.setString(3, customer.getLastName());
            pstnt.setString(4, customer.getMiddleName());
            pstnt.setString(5, customer.getPhoneNumber());
            pstnt.setString(6, customer.getEmail());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Customer customer, String[] params) {
        try (PreparedStatement pstnt = con.prepareStatement(UPDATE_CUSTOMER_BY_ID)) {
            int i = 1;
            for (String s : params)
                pstnt.setString(i++, s);
            while (i < 7)
                pstnt.setString(i++, "");
            pstnt.setInt(7, customer.getCustomerId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(Customer customer) {
        try (PreparedStatement pstnt = con.prepareStatement(DELETE_CUSTOMER_BY_ID)) {
            pstnt.setInt(1, customer.getCustomerId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
