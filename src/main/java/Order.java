import lombok.Data;

@Data
public class Order {
    Integer orderId;
    String dateOrder;
    Customer customer;
    Float price;
}
