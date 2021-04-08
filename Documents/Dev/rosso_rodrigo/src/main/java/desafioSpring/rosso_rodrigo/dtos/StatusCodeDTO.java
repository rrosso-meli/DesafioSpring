package desafioSpring.rosso_rodrigo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class StatusCodeDTO
{
    private int code;
    private String message;
}
