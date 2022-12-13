package web.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.entities.Person;
import web.entities.PersonDTO;
import web.services.PersonService;
import web.util.PersonDTORegisterValidator;
import web.util.RequestErrorResponse;
import web.util.RequestFormatException;
import web.util.RequestMessageResponse;

@RestController
@RequestMapping("/auth")
public class SessionController {

    private final PersonService personService;

    private final PersonDTORegisterValidator registerValidator;

    @Autowired
    public SessionController(PersonService personService, PersonDTORegisterValidator registerValidator) {
        this.personService = personService;
        this.registerValidator = registerValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<RequestMessageResponse> registerPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        registerValidator.validate(personDTO, bindingResult);

        if (bindingResult.hasErrors())
            throw new RequestFormatException(bindingResult);

        Person person = personService.addNewUser(personDTO.getUsername(), personDTO.getPassword());
        return new ResponseEntity<>(
                new RequestMessageResponse("user " + person.getUsername() + " successfully registered"),
                HttpStatus.OK
        );
    }

    @GetMapping
    public void printAuthorities() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
    }

    @ExceptionHandler
    public ResponseEntity<RequestErrorResponse> handleRequestError(RequestFormatException e) {
        return new ResponseEntity<>(
                new RequestErrorResponse(e),
                HttpStatus.BAD_REQUEST
        );
    }

}
