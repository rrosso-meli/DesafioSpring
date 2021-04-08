package desafioSpring.rosso_rodrigo.services;

import desafioSpring.rosso_rodrigo.dtos.*;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;
import desafioSpring.rosso_rodrigo.repositories.ProductoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductoServiceImpl implements ProductoService
{
    @Autowired
    private ProductoRepositoryImpl productoRepository;
    private static final List<String> parametersValid = new ArrayList<>(Arrays.asList( "category", "productId", "name", "brand", "price", "quantity", "freeShipping", "prestige", "order"));


    @Override
    public List<ProductoDTO> findProductsByFilters(Map<String, String> filters) throws ApiException
    {
        boolean bandOrder = filters.containsKey("order");
        String order = null;
        List<ProductoDTO> response = new ArrayList<>();

        //Validamos nombre de parametros
        validateParams(filters);

        if(bandOrder)
        {
            order = filters.get("order");
            filters.remove("order");
        }

        //Validamos parámetros y retornamos lista de productos
       response = validateQuantityParamsAndGetList(filters);

        //Aplicamos ordenamiento según corresponda
        if(bandOrder)
            response = productoRepository.orderList(order,response);

        return response;
    }

    @Override
    public ResponseDTO purchaseRequest(List<ProductoResponseDTO> articles) throws ApiException
    {
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO(HttpStatus.OK.value(), "La solicitud de compra se completó con éxito");
        TicketDTO ticketDTO = new TicketDTO();
        try
        {
            ticketDTO = productoRepository.generateTicket(articles);
        }
        catch (Exception ex)
        {
            statusCodeDTO.setCode(400);
            statusCodeDTO.setMessage(ex.getMessage());
        }

        return new ResponseDTO(ticketDTO,statusCodeDTO);
    }

    private List<ProductoDTO> getAll()
    {
        return productoRepository.convertExcelToProducts();
    }

    private List<ProductoDTO> getProductsByFilters(Map<String,String> filters) throws ApiException
    {
        return productoRepository.findProductsByFilters(filters);
    }

    //Busca el artículo y lo devuelve, falta restarte las ventas
    //Valida la cantidad de parámetros
    private List<ProductoDTO> validateQuantityParamsAndGetList(Map<String, String> filters) throws ApiException
    {
        int size = filters.size();

        if(size > 2)
        {
            throw new ApiException(HttpStatus.BAD_REQUEST, "No se pueden ingresar más de 2 filtros");
        }
        else
        {
            if(size == 0)
                return getAll();
            else
                return getProductsByFilters(filters);
        }
    }

    //Valida el nombre de los parámetros ingresados
    private void validateParams(Map<String, String> filters) throws ApiException
    {
        for(Map.Entry<String, String> filter:filters.entrySet()){
            if(!parametersValid.contains(filter.getKey()))
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Se han detectado parámetros NO Válidos!");
            }
        }
    }


}
