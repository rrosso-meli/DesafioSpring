package desafioSpring.rosso_rodrigo.repositories;

import desafioSpring.rosso_rodrigo.dtos.ClientDTO;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;
import desafioSpring.rosso_rodrigo.utils.CsvApi;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ClientRepositoryImpl implements ClientRepository
{

    private List<ClientDTO> dataBase = new ArrayList<>();

    private CsvApi csvApi;

    public ClientRepositoryImpl(CsvApi csvApi) {
        this.csvApi = csvApi;
    }

    @Override
    public void loadData() throws IOException {
        dataBase = csvApi.readCSVFile(dataBase);
    }

    @Override
    public List<ClientDTO> getAllClients() throws IOException
    {
        loadData();
        return dataBase;
    }

    @Override
    public List<ClientDTO> getClientsByProvince(String province) throws IOException
    {
        loadData();
        return dataBase.stream().filter(clientDTO -> clientDTO.getProvince().equals(province)).collect(Collectors.toList());
    }


    @Override
    public void validateClientExist(ClientDTO client) throws ApiException, IOException {
        loadData();
        if(dataBase.contains(client))
            throw new ApiException(HttpStatus.ALREADY_REPORTED, "El cliente ya existe");
    }

    public void addClient(ClientDTO client) throws ApiException, IOException {
        loadData();

        validateClientExist(client);

        dataBase.add(client);
        csvApi.writeCSVFile(dataBase);
    }

}
