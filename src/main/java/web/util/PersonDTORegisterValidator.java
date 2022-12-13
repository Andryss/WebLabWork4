package web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import web.entities.PersonDTO;
import web.services.PersonService;

@SuppressWarnings("NullableProblems")
@Component
public class PersonDTORegisterValidator implements Validator {

    @Value("${forbidden-username}")
    private String forbiddenUsername;

    private final PersonService personService;

    @Autowired
    public PersonDTORegisterValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO personDTO = (PersonDTO) target;
        if (personDTO.getUsername().equals(forbiddenUsername))
            errors.rejectValue("username", "", "This username is forbidden");
        if (personService.hasUser(personDTO.getUsername()))
            errors.rejectValue("username", "", "User with given username already exist");
    }
}
