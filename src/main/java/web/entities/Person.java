package web.entities;

import jakarta.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private SortedSet<Request> history = new TreeSet<>();

    public Person(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Person() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password.substring(0, 10) + "..." + '\'' +
                ", role=" + role +
                '}';
    }
}
