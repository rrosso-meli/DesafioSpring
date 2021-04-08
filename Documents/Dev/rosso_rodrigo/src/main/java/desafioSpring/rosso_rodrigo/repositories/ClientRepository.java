package desafioSpring.rosso_rodrigo.repositories;

import desafioSpring.rosso_rodrigo.dtos.ClientDTO;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;

import java.io.IOException;
import java.util.List;

public interface ClientRepository
{
    void loadData() throws IOException;
    List<ClientDTO> getAllClients() throws IOException;
    List<ClientDTO> getClientsByProvince(String province) throws IOException;
    void addClient(ClientDTO client) throws ApiException, IOException;
    void validateClientExist(ClientDTO client) throws ApiException, IOException;

}
