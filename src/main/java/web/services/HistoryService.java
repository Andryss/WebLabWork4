package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.entities.Person;
import web.entities.Request;
import web.model.AreaChecker;
import web.repositories.PersonRepository;
import web.repositories.RequestRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class HistoryService {

    private final PersonRepository personRepository;

    private final RequestRepository requestRepository;

    private final AreaChecker areaChecker;

    @Value("${user-history-limit}")
    private int historyLimit;

    @Autowired
    public HistoryService(PersonRepository personRepository, RequestRepository requestRepository, AreaChecker areaChecker) {
        this.personRepository = personRepository;
        this.requestRepository = requestRepository;
        this.areaChecker = areaChecker;
    }

    @Transactional
    public List<Request> getPersonHistory(String username) {
        Optional<Person> optionalPerson = personRepository.findПожалуйстаByUsername(username);
        if (optionalPerson.isEmpty()) return null;
        Person person = optionalPerson.get();
        limitHistory(person);
        return new ArrayList<>(person.getHistory());
    }

    @Transactional
    public List<Request> addPersonRequest(String username, Request request) {
        Optional<Person> optionalPerson = personRepository.findПожалуйстаByUsername(username);
        if (optionalPerson.isEmpty()) return null;
        Person person = optionalPerson.get();
        evaluateRequest(request);
        person.addHistory(request);
        requestRepository.save(request);
        limitHistory(person);
        return new ArrayList<>(person.getHistory());
    }

    private void limitHistory(Person person) {
        SortedSet<Request> personHistory = person.getHistory();
        while (personHistory.size() > historyLimit) {
            Request lastRequest = personHistory.last();
            person.removeHistory(lastRequest);
            requestRepository.deleteById(lastRequest.getId());
        }
    }

    private void evaluateRequest(Request request) {
        request.setCreatedTime(new Date());
        request.setResult(areaChecker.check(request));
    }
}
