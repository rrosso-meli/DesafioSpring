package desafioSpring.rosso_rodrigo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO
{
    TicketDTO ticket;
    StatusCodeDTO statusCode;
}
