package dio.spring.patterns.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private Address address;
}
