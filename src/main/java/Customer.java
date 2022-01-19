import lombok.Data;

@Data
public class Customer {
    Integer customerId;
    String firstName;
    String lastName;
    String middleName;
    String phoneNumber;
    String email;
}
