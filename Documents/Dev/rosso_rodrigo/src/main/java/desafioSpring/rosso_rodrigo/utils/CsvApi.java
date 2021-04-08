package desafioSpring.rosso_rodrigo.utils;

import desafioSpring.rosso_rodrigo.dtos.ClientDTO;
import desafioSpring.rosso_rodrigo.dtos.ProductoResponseDTO;
import desafioSpring.rosso_rodrigo.dtos.TicketDTO;
import org.apache.xmlbeans.UserType;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Service
public class CsvApi
{
    private final String rootFileClients = "/src/main/resources/dbClients.csv";
    private final String rootFilePurchase = "/src/main/resources/dbVentas.csv";

    //NO TUVE TIEMPO DE REFACTORIZAR!

    //Mapea el archivo a Lista de Clientes
    public List<ClientDTO> readCSVFile(List<ClientDTO> dataBase) throws IOException
    {
        dataBase = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            File file = new File("");
            file = new File(file.getAbsolutePath() + rootFileClients);
            br = new BufferedReader(new FileReader(file));
            line = br.readLine();
            while ((line = br.readLine()) != null)
            {
                String[] data = line.split(cvsSplitBy);
                dataBase.add(new ClientDTO(Double.parseDouble(data[0]), data[1], data[2], data[3]));
            }
        } catch (FileNotFoundException e)
        {
            throw new FileNotFoundException("No se encuentra el archivo dbClients.csv");
        }
        catch (IOException e)
        {
            throw new IOException("No se puede leer el archivo dbClients.csv");
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    throw new IOException("Error al cerrar el archivo dbClients.csv");
                }
            }
            return dataBase;
        }
    }

    //Mapea el archivo a Lista de Clientes
    public List<ProductoResponseDTO> readCSVFilePurchase(int clientId) throws IOException
    {
        List<ProductoResponseDTO> dataBase = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            File file = new File("");
            file = new File(file.getAbsolutePath() + rootFilePurchase);
            br = new BufferedReader(new FileReader(file));
            line = br.readLine();
            while ((line = br.readLine()) != null)
            {
                String[] data = line.split(cvsSplitBy);
                ProductoResponseDTO prod = new ProductoResponseDTO();
                prod.setProductId(Integer.parseInt(data[1]));
                prod.setName(data[2]);
                prod.setBrand(data[3]);
                prod.setQuantity(Integer.parseInt(data[4]));
                dataBase.add(prod);
            }
        } catch (FileNotFoundException e)
        {
            throw new FileNotFoundException("No se encuentra el archivo dvVentas.csv");
        }
        catch (IOException e)
        {
            throw new IOException("No se puede leer el archivo dbVentas.csv");
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    throw new IOException("Error al cerrar el archivo dbVentas.csv");
                }
            }
            return dataBase;
        }
    }

    //Escribe en el archivo dbClients
    public void writeCSVFile(List<ClientDTO> dataBase) throws IOException
    {
        File file = new File("");
        file = new File(file.getAbsolutePath() + rootFileClients);
        if(file.isFile())
        {
            PrintWriter writer = new PrintWriter(new File(file.getPath()));

            String separator = ",";
            String collect = "DNI,Name,Surname,Province\n";
            for(ClientDTO clientDTO: dataBase) {
                collect += clientDTO.getDni() + separator + clientDTO.getName() + separator + clientDTO.getSurname() + separator + clientDTO.getProvince() + "\n";
            }

            writer.write(collect);

            writer.close();

        }

    }

    //Escribe en el archivo dbClients
    public void writeCSVFilePurchase(List<ProductoResponseDTO> dataBase ) throws IOException
    {
        File file = new File("");
        file = new File(file.getAbsolutePath() + rootFilePurchase);
        if(file.isFile())
        {
            PrintWriter writer = new PrintWriter(new File(file.getPath()));

            String separator = ",";
            String collect = "ClientId,ProductId,Name,Brand,Quantity\n";

            for(ProductoResponseDTO product: dataBase)
            {

                collect += 100 + separator + product.getProductId() + separator + product.getName() + separator + product.getBrand() + separator + product.getQuantity() + "\n";

            }

            writer.write(collect);

            writer.close();

        }

    }
}
