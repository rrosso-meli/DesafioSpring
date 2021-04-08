package desafioSpring.rosso_rodrigo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

//Clase que representa la Entidad Producto
public class ProductoDTO
{
    private int productId;
    private String name;
    private String category;
    private String brand;
    private double price;
    private int quantity;
    private boolean freeShip;
    private int prestige;
}
