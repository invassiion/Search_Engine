package searchengine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
public class ErrorResponse {
     private boolean result;
     private String error;

}
