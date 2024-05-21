package dio.spring.patterns.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address address;
}
