package web.entities;

import jakarta.persistence.*;

import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "session")
    private String session;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private SortedSet<Request> history;

    public User(String session) {
        this.session = session;
        history = new TreeSet<>();
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public SortedSet<Request> getHistory() {
        return history;
    }

    public void setHistory(SortedSet<Request> history) {
        this.history = history;
    }

    public void addHistory(Request request) {
        request.setOwner(this);
        history.add(request);
    }

    public void removeHistory(Request request) {
        request.setOwner(null);
        history.remove(request);
    }
}
