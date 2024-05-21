package dio.spring.patterns.service.impl;

import dio.spring.patterns.handler.ClientNotFoundException;
import dio.spring.patterns.model.Address;
import dio.spring.patterns.model.Client;
import dio.spring.patterns.repository.AddressRepository;
import dio.spring.patterns.repository.ClientRepository;
import dio.spring.patterns.service.ClientService;
import dio.spring.patterns.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    ViaCepService viaCepService;

    @Override
    public Iterable<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return this.validateById(id);
    }

    @Override
    public void update(Long id, Client client) throws ClientNotFoundException {
        this.validateById(id);
        this.saveClientAndAddress(client);
    }

    @Override
    public Client save(Client client) {
        return this.saveClientAndAddress(client);
    }

    @Override
    public void delete(Long id) {
        this.validateById(id);
        clientRepository.deleteById(id);
    }

    private Client saveClientAndAddress(Client client) {
        String cep = client.getAddress().getCep();
        Address locatedAddress = addressRepository.findById(cep).orElseGet(() -> viaCepService.locateCep(cep));

        client.setAddress(locatedAddress);
        return clientRepository.save(client);
    }

    private Client validateById(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException(id);
        }
        return clientOptional.get();
    }
}
