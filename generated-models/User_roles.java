import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user_roles")
public class User_roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(name = "assigned_at")
    private LocalDateTime assigned_at;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Profiles profiles;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles roles;
}
