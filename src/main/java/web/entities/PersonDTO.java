package web.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    @NotNull(message = "Username should not be null")
    @NotEmpty(message = "Username should not be empty")
    @Length(min = 3, max = 20, message = "Username must contain from 3 to 20 characters")
    private String username;

    @NotNull(message = "Password should not be null")
    @NotEmpty(message = "Password should not be empty")
    @Length(min = 3, max = 20, message = "Password must contain from 3 to 20 characters")
    private String password;
}
