package dio.spring.patterns.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Address {
    @Id
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String ibge;
    private String gia;
    private String ddd;
    private String siafi;

    public void setCep(String cep) {
        this.cep = cep.replaceAll("-", "");
    }

    public String getCep() {
        return cep.replaceAll("-", "");
    }
}
