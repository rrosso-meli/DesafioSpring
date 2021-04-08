package desafioSpring.rosso_rodrigo.repositories;

import desafioSpring.rosso_rodrigo.dtos.ProductoDTO;
import desafioSpring.rosso_rodrigo.dtos.ProductoResponseDTO;
import desafioSpring.rosso_rodrigo.dtos.TicketDTO;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductoRepository
{
    //Lee el archivo Excel y retorna una lista de Productos
    public List<ProductoDTO> convertExcelToProducts();

    //Retorna los productos filtrando según corresponda
    List<ProductoDTO> findProductsByFilters(Map<String, String> filters) throws ApiException;

    //Retorna un ticket de compra
    TicketDTO generateTicket(List<ProductoResponseDTO> articles) throws ApiException, IOException;
}
