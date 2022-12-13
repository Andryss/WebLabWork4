package web.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    @Null(message = "Id must be null")
    private Long id;

    @Null(message = "CreatedTime must be null")
    private String createdTime;

    @NotNull
    @Range(min = -5, max = 3, message = "X must be in range (-5..3)")
    private Double x;

    @NotNull
    @Range(min = -5, max = 3, message = "Y must be in range (-5..3)")
    private Double y;

    @NotNull(message = "R must be not null")
    @Positive(message = "R must be positive")
    @Max(value = 3, message = "R must be less than 3")
    private Integer r;
    
    @Null(message = "Result must be null")
    private Boolean result;
}
