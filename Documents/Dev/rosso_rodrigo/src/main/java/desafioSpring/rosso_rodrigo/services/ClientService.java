package desafioSpring.rosso_rodrigo.services;

import desafioSpring.rosso_rodrigo.dtos.ClientDTO;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;

import java.io.IOException;
import java.util.List;

public interface ClientService
{
    List<ClientDTO> getClients(String providence) throws IOException;
    public void createClient(ClientDTO client) throws ApiException, IOException;
}
