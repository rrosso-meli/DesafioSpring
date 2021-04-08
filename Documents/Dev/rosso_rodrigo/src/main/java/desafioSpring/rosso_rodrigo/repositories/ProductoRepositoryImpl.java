package desafioSpring.rosso_rodrigo.repositories;

import desafioSpring.rosso_rodrigo.dtos.*;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;
import desafioSpring.rosso_rodrigo.utils.CsvApi;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.*;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ProductoRepositoryImpl implements ProductoRepository
{

    private AtomicLong countCompras = new AtomicLong();
    private static final String pathFile = "/src/main/resources/dbProductos.xlsx";

    private List<ProductoResponseDTO> dataBase = new ArrayList<>();

    private CsvApi csvApi;

    public ProductoRepositoryImpl(CsvApi csvApi) {
        this.csvApi = csvApi;
    }

    @Override
    public List<ProductoDTO> convertExcelToProducts() {
        List<ProductoDTO> lProducts = new ArrayList<>();
        File file = new File("");
        file = new File(file.getAbsolutePath() + pathFile);
        try
        {
            XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(file));

            XSSFSheet sheet = work.getSheet("Hoja 1");
            XSSFRow row = null;

            int i = 1;
            while ((row = sheet.getRow(i)) != null)
            {
                lProducts.add(convertRowToProduct(row));
                i++;
            }
        }
        catch (IOException | ApiException ex)
        {
            ex.printStackTrace();
        }

        return lProducts;
    }

    //Convierte una fila del Excel en un Producto
    public ProductoDTO convertRowToProduct(XSSFRow row) throws ApiException
    {
        try
        {
            ProductoDTO productoDTO = new ProductoDTO();
            productoDTO.setProductId((int)row.getCell(0).getNumericCellValue());
            productoDTO.setName(row.getCell(1).getStringCellValue());
            productoDTO.setCategory(row.getCell(2).getStringCellValue());
            productoDTO.setBrand(row.getCell(3).getStringCellValue());
            productoDTO.setPrice(Double.parseDouble(row.getCell(4).getStringCellValue().replace("$","").replace(".","")));
            productoDTO.setQuantity((int)row.getCell(5).getNumericCellValue());
            boolean freeShip = (row.getCell(6).getStringCellValue()) == "SI" ? true : false;
            productoDTO.setFreeShip(freeShip);
            int prestige = countChart(row.getCell(7).getStringCellValue(), '*') ;
            productoDTO.setPrestige(prestige);

            return productoDTO;
        }
        catch (Exception e)
        {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error al intentar mapear el objeto, revise nombre y tipo de dato");
        }

    }

    //Método que retorna cantidad de repeticiones de un caracter en una palabra
    public int countChart(String phrase, char x)
    {
        int count = 0;

        for (int i = 0; i < phrase.length(); i++) {
            if (phrase.charAt(i) == x) {
                count++;
            }
        }
        return count;
    }


    @Override
    public List<ProductoDTO> findProductsByFilters(Map<String, String> filters) throws ApiException
    {
        List<ProductoDTO> lProducts = convertExcelToProducts();
        return lProducts = lProducts.stream()
                .filter(ProductoDTO -> {
                    boolean matches = true;
                    if (filters.get("name") != null) {
                        matches = ProductoDTO.getName().equals(filters.get("name"));
                    }
                    if (filters.get("brand") != null) {
                        matches = matches && ProductoDTO.getBrand().equals(filters.get("brand"));
                    }
                    if (filters.get("category") != null) {
                        matches = matches && ProductoDTO.getCategory().equals(filters.get("category"));
                    }
                    if (filters.get("freeShipping") != null) {
                        matches = matches && ProductoDTO.isFreeShip() == (Boolean.parseBoolean(filters.get("freeShipping")));
                    }
                    if (filters.get("prestige") != null) {
                        matches = matches && ProductoDTO.getPrestige() == (Integer.parseInt(filters.get("prestige")));
                    }

                    return matches;
                }).collect(Collectors.toList());

    }

    @Override
    public TicketDTO generateTicket(List<ProductoResponseDTO> articles) throws ApiException, IOException
    {
        loadDataBase();
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        TicketDTO ticketDTO = new TicketDTO();
        double priceTotal = 0;

        List<ProductoResponseDTO> listCustom = new ArrayList<ProductoResponseDTO>(articles);
        listCustom.addAll(getArticlesByClient(100));

        //Si el artículo ya se encuentra en la lista debería sumar sòlo el quantity
        //Debo agregar los artículos que no se encuentren en la lista

        validateArticlesInDataBase(listCustom);

        priceTotal = calculateTotal(listCustom);

        ticketDTO.setId(countCompras.getAndIncrement());
        ticketDTO.setArticles(listCustom);
        ticketDTO.setTotal(priceTotal);

        dataBase.addAll(articles);
        saveTicketInDataBase(dataBase);

        return  ticketDTO;
    }

    //Calcula el monto total del ticket
    private double calculateTotal(List<ProductoResponseDTO> articles) throws ApiException
    {
        double response = 0;
        List<ProductoDTO> lProducts = convertExcelToProducts();

        try
        {
            for(ProductoResponseDTO products:articles)
            {
                ProductoDTO productDTOFilter = lProducts.stream().filter(ProductoDTO -> ProductoDTO.getProductId() == products.getProductId()).findFirst().get();
                response += (productDTOFilter.getPrice() * products.getQuantity());
            }

            return response;
        }
        catch (Exception ex)
        {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al calcular importe total");
        }

    }


    //Ordena la lista de Objeto según criterio
    public List<ProductoDTO> orderList(String order, List<ProductoDTO> lProducts) throws ApiException
    {

        switch (order)
        {
            //Por nombre de forma Ascendente.
            case "0":
            {
                return lProducts.stream().sorted(Comparator.comparing(ProductoDTO::getName)).collect(Collectors.toList());
            }
            //Por Nombre de forma Descendente
            case "1":
            {
                return lProducts.stream().sorted(Comparator.comparing(ProductoDTO::getName).reversed()).collect(Collectors.toList());
            }
            //Por Precio de Mayor a Menor
            case "2":
            {
                return lProducts.stream().sorted(Comparator.comparing(ProductoDTO::getPrice).reversed()).collect(Collectors.toList());
            }
            //Por Precio de Menor a Mayor
            case "3":
            {
                return lProducts.stream().sorted(Comparator.comparing(ProductoDTO::getPrice)).collect(Collectors.toList());
            }
            default:
                throw new ApiException(HttpStatus.PRECONDITION_FAILED, "Parámetro de ordenamiento incorrecto");
        }


    }

    //Valida que el artículo se encuentre en la base
    private void validateArticlesInDataBase(List<ProductoResponseDTO> products) throws ApiException
    {

        List<ProductoDTO> listOfProducts = convertExcelToProducts();
        ProductoDTO productDataBase = new ProductoDTO();

            for (ProductoResponseDTO product : products)
            {
                try
                {
                    productDataBase = listOfProducts.stream().filter(ProductoDTO ->
                            ProductoDTO.getProductId() == product.getProductId() &&
                                    ProductoDTO.getName().equals(product.getName()) &&
                                    ProductoDTO.getBrand().equals(product.getBrand()))
                            .findFirst().get();
                }
                catch (Exception ex)
                {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Revisar datos de envío!");
                }
                validateQuantityError(product.getQuantity(), productDataBase.getQuantity());
            }


    }

    //Valida el stock
    private void validateQuantityError(int productQuantity, int productDataBaseQuantity) throws ApiException
    {
        if(productQuantity > productDataBaseQuantity)
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Hay faltante de Stock!");
    }

    //Guarda en archivo los artículos de venta
    private void saveTicketInDataBase(List<ProductoResponseDTO> lProducts) throws ApiException
    {
        try
        {
            csvApi.writeCSVFilePurchase(lProducts);
        } catch (Exception e)
        {
           throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Error al intentar escribir el archivo");
        }

    }

    //Tomamos todas las compras realizadas por el cliente
    private List<ProductoResponseDTO> getArticlesByClient(int clientId) throws IOException
    {
        return csvApi.readCSVFilePurchase(clientId);
    }

    private void loadDataBase() throws IOException {
        dataBase = csvApi.readCSVFilePurchase(100);
    }


}
