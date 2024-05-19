package dio.spring.patterns.service.impl;

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
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public void update(Long id, Client client) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isEmpty()) {
            return;
        }

        this.saveClientAndAddress(client);
    }

    @Override
    public Client save(Client client) {
        return this.saveClientAndAddress(client);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    private Client saveClientAndAddress(Client client) {
        String cep = client.getAddress().getCep();
        Address locatedAddress = addressRepository.findById(cep).orElseGet(() -> {
            Address address = viaCepService.locateCep(cep);
            return address;
        });

        addressRepository.save(locatedAddress);
        return clientRepository.save(client);
    }
}
