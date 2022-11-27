package web.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.entities.Request;
import web.entities.User;
import web.model.AreaChecker;
import web.repositories.RequestRepository;
import web.repositories.UserRepository;

import java.util.Date;
import java.util.SortedSet;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    // TODO: make a properties
    private static final int historyLimit = 10;

    private final AreaChecker areaChecker;

    @Autowired
    public UserService(UserRepository userRepository, RequestRepository requestRepository, AreaChecker areaChecker) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.areaChecker = areaChecker;
    }

    // TODO: add session management
    @PostConstruct
    public void init() {
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    // TODO: add session management
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // TODO: add session management
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void addUserRequest(String session, Request request) {
        User user = findUserBySession(session);
        Request newRequest = fillRequest(request);
        user.addHistory(newRequest);
        requestRepository.save(newRequest);

        user = findUserBySession(session);
        SortedSet<Request> history = user.getHistory();
        while (history.size() > historyLimit) {
            Request lastRequest = history.last();
            user.removeHistory(lastRequest);
            requestRepository.delete(lastRequest);
        }
    }

    private Request fillRequest(Request request) {
        // TODO: add request DTO
        request.setCreatedTime(new Date());
        request.setResult(areaChecker.check(request));
        return request;
    }

    @Transactional
    public SortedSet<Request> getUserHistory(String session) {
        return findUserBySession(session).getHistory();
    }

    private User findUserBySession(String session) {
        User user = userRepository.findBySession(session).orElse(null);
        if (user == null) {
            user = new User(session);
            userRepository.save(user);
        }
        return user;
    }

}
