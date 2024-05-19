package dio.spring.patterns.service;

import dio.spring.patterns.model.Client;

public interface ClientService {
    Iterable<Client> findAll();

    Client findById(Long id);

    Client save(Client client);

    void update(Long id, Client client);

    void delete(Long id);
}
