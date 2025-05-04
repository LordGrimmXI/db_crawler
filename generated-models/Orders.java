import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;

    @Column(name = "user_id")
    private Integer user_id;
    @Column(name = "product_code")
    private String product_code;
    @Column(name = "version")
    private Integer version;
    @Column(name = "quantity")
    private Integer quantity;

    @OneToMany(mappedBy = "orders")
    private List<Products> productsList;

    @OneToMany(mappedBy = "orders")
    private List<Products> productsList;

    @OneToMany(mappedBy = "orders")
    private List<Profiles> profilesList;
}
