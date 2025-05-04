import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "addresses")
public class Addresses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer address_id;

    @Column(name = "user_id")
    private Integer user_id;
    @Column(name = "city")
    private String city;
    @Column(name = "zip_code")
    private String zip_code;
    @Column(name = "street")
    private String street;

    @OneToMany(mappedBy = "addresses")
    private List<Profiles> profilesList;
}
