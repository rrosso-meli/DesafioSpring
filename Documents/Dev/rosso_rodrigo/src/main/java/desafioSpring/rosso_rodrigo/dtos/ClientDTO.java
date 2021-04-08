package desafioSpring.rosso_rodrigo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientDTO
{
    private double dni;
    private String name;
    private String surname;
    private String province;
}
