package desafioSpring.rosso_rodrigo.controllers;

import desafioSpring.rosso_rodrigo.dtos.ClientDTO;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;
import desafioSpring.rosso_rodrigo.services.ClientService;
import desafioSpring.rosso_rodrigo.services.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v2")
public class ClientController
{

    @Autowired
    private ClientServiceImpl clientService;

    //EndPoint para retornar todos los clientes o filtrado por provincia
    @GetMapping("/client")
    public ResponseEntity<List<ClientDTO>> getClients(@RequestParam(value = "province", defaultValue = "") String province) throws IOException {
        List<ClientDTO> response = clientService.getClients(province);
        if(response.size() > 0)
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //EndPoint para crear un nuevo cliente.
    //dni - name - surname - province
    @PostMapping("/client/create")
    public ResponseEntity<String> createClient(@RequestBody ClientDTO clientDTO) throws ApiException, IOException {
        clientService.createClient(clientDTO);
        return new ResponseEntity<>("El cliente fue creado exitosamente!", HttpStatus.CREATED);
    }
}
