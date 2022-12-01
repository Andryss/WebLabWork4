package web.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PersonDTO {

    @NotNull(message = "should not be null")
    @NotEmpty(message = "should not be empty")
    private String username;

    @NotNull(message = "should not be null")
    @NotEmpty(message = "should not be empty")
    private String password;

    public PersonDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public PersonDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
