import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String product_code;

    @Column(name = "product_name")
    private String product_name;
    @Column(name = "price")
    private Object price;
}
