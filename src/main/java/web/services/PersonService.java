package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.entities.Person;
import web.entities.Role;
import web.repositories.PersonRepository;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean hasUser(String username) {
        return personRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public Person addNewUser(String username, String password) {
        return personRepository.save(new Person(username, passwordEncoder.encode(password), Role.USER));
    }
}
