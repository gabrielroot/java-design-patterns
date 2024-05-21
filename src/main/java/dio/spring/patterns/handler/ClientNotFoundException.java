package dio.spring.patterns.handler;

public class ClientNotFoundException extends BusinessException {
    public ClientNotFoundException(Long id) {
        super("Cliente [%d] não encontrado.", id);
    }
}
