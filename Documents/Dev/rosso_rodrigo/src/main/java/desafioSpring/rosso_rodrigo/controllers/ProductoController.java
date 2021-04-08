package desafioSpring.rosso_rodrigo.controllers;

import desafioSpring.rosso_rodrigo.config.GlobalHandlerException;
import desafioSpring.rosso_rodrigo.dtos.*;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;
import desafioSpring.rosso_rodrigo.services.ProductoServiceImpl;
import org.bouncycastle.asn1.cms.RsaKemParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class ProductoController
{
    @Autowired
    private ProductoServiceImpl productoService;

    //EndPoint que retorna los artículos registrados en el archivo
    @GetMapping("/articles")
    public ResponseEntity<List<ProductoDTO>> findProductsByFilter(@RequestParam Map<String, String> filters) throws ApiException
    {
        return new ResponseEntity<>(productoService.findProductsByFilters(filters), HttpStatus.OK);
    }

    //EndPoint que retorna Ticket de compra
    @PostMapping("/purchase-request")
    public ResponseEntity<ResponseDTO> purchaseRequest(@RequestBody ArticlesDTO articles) throws ApiException
    {
        return new ResponseEntity<>(productoService.purchaseRequest(articles.getArticles()), HttpStatus.OK);
    }
}
