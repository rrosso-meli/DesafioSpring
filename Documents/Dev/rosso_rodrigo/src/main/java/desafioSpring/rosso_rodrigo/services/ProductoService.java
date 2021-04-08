package desafioSpring.rosso_rodrigo.services;

import desafioSpring.rosso_rodrigo.dtos.ProductoDTO;
import desafioSpring.rosso_rodrigo.dtos.ProductoResponseDTO;
import desafioSpring.rosso_rodrigo.dtos.ResponseDTO;
import desafioSpring.rosso_rodrigo.dtos.TicketDTO;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;

import java.util.List;
import java.util.Map;

public interface ProductoService
{

    List<ProductoDTO> findProductsByFilters(Map<String, String> filters) throws ApiException;

    ResponseDTO purchaseRequest(List<ProductoResponseDTO> articles) throws ApiException;
}
