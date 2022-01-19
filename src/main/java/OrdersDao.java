import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdersDao implements Dao<Order> {

    private static final String GET_ORDER_BY_ID = "select * from orders where order_id=?";
    private static final String DELETE_ORDER_BY_ID = "delete from orders where order_id=?";
    private static final String UPDATE_ORDER_BY_ID = "update orders set order_id=?,date_order=to_date(?, 'yyyy-mm-dd'),customer_id=?,price=? where order_id=?";
    private static final String INSERT_INTO_ORDER = "insert into orders (order_id, date_order, customer_id, price) values (?, to_date(?, 'yyyy-mm-dd'), ?, ?)";
    private Connection con;
    private CustomersDao customersDao;

    public OrdersDao() {
        con = ConnectionManager.getConnection();
        customersDao = new CustomersDao();
    }

    @Override
    public Optional<Order> get(int id) {

        try (PreparedStatement pstnt = con.prepareStatement(GET_ORDER_BY_ID)) {
            pstnt.setInt(1, id);

            ResultSet res = pstnt.executeQuery();
            while (res.next()) {
                Order order = new Order();
                order.setOrderId(res.getInt("order_id"));
                order.setDateOrder(res.getString("date_order"));
                order.setCustomer(customersDao.get(res.getInt("customer_id")).orElse(null));
                order.setPrice(res.getFloat("price"));
                return Optional.of(order);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();

        try (Statement stnt = con.createStatement()) {
            ResultSet res = stnt.executeQuery("select * from orders");
            while (res.next()) {
                Order order = new Order();
                order.setOrderId(res.getInt("order_id"));
                order.setDateOrder(res.getString("date_order"));
                order.setCustomer(customersDao.get(res.getInt("customer_id")).orElse(null)
                );
                order.setPrice(res.getFloat("price"));
                orders.add(order);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orders;
    }

    @Override
    public void save(Order order) {
        try (PreparedStatement pstnt = con.prepareStatement(INSERT_INTO_ORDER)) {
            pstnt.setInt(1, order.getOrderId());
            pstnt.setString(2, order.getDateOrder());
            pstnt.setInt(3, order.getCustomer().getCustomerId());
            pstnt.setFloat(4, order.getPrice());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Order order, String[] params) {
        try (PreparedStatement pstnt = con.prepareStatement(UPDATE_ORDER_BY_ID)) {
            pstnt.setInt(1, Integer.parseInt(params[0]));
            pstnt.setString(2, params[1]);
            pstnt.setInt(3, Integer.parseInt(params[2]));
            pstnt.setFloat(4, Float.parseFloat(params[3]));
            pstnt.setInt(5, order.getOrderId());

            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(Order order) {
        try (PreparedStatement pstnt = con.prepareStatement(DELETE_ORDER_BY_ID)) {
            pstnt.setInt(1, order.getOrderId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
