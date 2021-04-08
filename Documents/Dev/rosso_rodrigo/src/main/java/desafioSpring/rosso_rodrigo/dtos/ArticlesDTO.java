package desafioSpring.rosso_rodrigo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesDTO
{
    private List<ProductoResponseDTO> articles;
}
