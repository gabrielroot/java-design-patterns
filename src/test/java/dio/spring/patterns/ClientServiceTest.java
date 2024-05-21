package dio.spring.patterns;

import dio.spring.patterns.model.Address;
import dio.spring.patterns.model.Client;
import dio.spring.patterns.service.ViaCepService;
import dio.spring.patterns.service.impl.ClientServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@ExtendWith(MockitoExtension.class) //TODO: MOCKAR A CHAMADA AO viaCepService.locateCep(cep);
@SpringBootTest
public class ClientServiceTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ViaCepService viaCepService;

    @InjectMocks
    @Autowired
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        Mockito.when(viaCepService.locateCep(Mockito.anyString())).thenAnswer((Answer<Address>) invocation -> {
            int randomCep = (new Random()).nextInt(9000) + 1000;

            Address mockedAddress = new Address();
            mockedAddress.setCep(String.valueOf(randomCep).concat(invocation.getArgument(0)));
            mockedAddress.setLocalidade("Minas Gerais");
            mockedAddress.setLogradouro("Praça da Sé");
            mockedAddress.setDdd("34");
            mockedAddress.setUf("MG");

            return mockedAddress;
        });
    }

    @Test
    void shouldCreateClients() {
        this.createClients();
        Iterable<Client> clients = clientService.findAll();
        Assertions.assertTrue( Lists.newArrayList(clients).size() > 1);
    }

    @Test
    void shouldMockAPI() {
        this.createClients();
        Iterable<Client> clients = clientService.findAll();
        Assertions.assertTrue(
                Lists.newArrayList(clients).stream()
                        .allMatch(client -> client.getAddress().getUf().equals("MG"))
        );
    }

    @Test
    void shouldUpdateOneClient() {
        Client client = new Client();
        Address address = new Address();
        address.setCep("329442");
        client.setName("Gabrel");
        client.setAddress(address);

        clientService.save(client);
        Assertions.assertTrue(Objects.nonNull(clientService.findById(client.getId())));

        client.setName("Gabriel");
        clientService.update(client.getId(), client);

        Assertions.assertEquals(client.getName(), clientService.findById(client.getId()).getName());
    }

    @Test
    void shouldFindClientById() {
        List<Client> clients = this.createClients();
        Client clientToFind = clients.get(0);

        Client clientFound = clientService.findById(clientToFind.getId());

        if(Objects.isNull(clientFound)){
            Assertions.fail("Client not found");
        }

        Assertions.assertEquals(clientToFind, clientFound);
    }

    @Test
    void shouldDeleteClientById() {
        List<Client> clients = this.createClients();
        Client clientToDelete = clients.get(0);

        clientService.delete(clientToDelete.getId());

        Client clientFound = clientService.findById(clientToDelete.getId());

        if(Objects.nonNull(clientFound)){
            Assertions.fail("Client was not deleted");
        }
    }

    private ArrayList<Client> createClients() {
        Assertions.assertNotNull(clientService);

        ArrayList<Client> clients = new ArrayList<>();

        Client client = new Client();
        Address address = new Address();
        address.setCep("1111-111");
        client.setName("John Doe");
        client.setAddress(address);
        clients.add(clientService.save(client));

        Client client2 = new Client();
        Address address2 = new Address();
        address2.setCep("0000-000");
        client2.setName("Maria Doe");
        client2.setAddress(address2);
        clients.add(clientService.save(client2));

        return clients;
    }
}
