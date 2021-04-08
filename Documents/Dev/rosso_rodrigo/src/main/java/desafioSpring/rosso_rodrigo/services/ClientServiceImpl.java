package desafioSpring.rosso_rodrigo.services;

import desafioSpring.rosso_rodrigo.dtos.ClientDTO;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;
import desafioSpring.rosso_rodrigo.repositories.ClientRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService
{
    @Autowired
    private ClientRepositoryImpl clientRepository;

    @Override
    public List<ClientDTO> getClients(String providence) throws IOException
    {
        if (providence != null && !providence.isEmpty())
        {
            return clientRepository.getClientsByProvince(providence);
        } else {
            return clientRepository.getAllClients();
        }
    }

    @Override
    public void createClient(ClientDTO client) throws ApiException, IOException {
        validateDataClient(client);

        clientRepository.addClient(client);
    }

    public void validateDataClient(ClientDTO clientDTO) throws ApiException
    {
        if(clientDTO.getProvince().equals("") ||
                clientDTO.getDni() == 0 ||
                clientDTO.getName().equals("") ||
                clientDTO.getSurname().equals(""))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Revisar información enviada(name, surname, dni, province)");
    }

}
