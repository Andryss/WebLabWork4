package web.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Entity
@Table(name = "requests")
public class Request implements Comparable<Request> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "created_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdTime;

    // TODO: add type converter
    @Column(name = "x")
    @NotNull(message = "X should not be empty")
    @Range(min = -5, max = 3, message = "X must be in range (-5..3)")
    private Integer x;

    // TODO: add type converter
    @Column(name = "y")
    @NotNull(message = "Y should not be empty")
    @Range(min = -5, max = 3, message = "Y must be in range (-5..3)")
    private Double y;

    // TODO: add type converter
    @Column(name = "r")
    @NotNull(message = "R should not be empty")
    @Positive(message = "R must be positive")
    @Max(value = 3, message = "R must be less than 3")
    private Integer r;

    @Column(name = "result")
    private boolean result;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    public Request(Integer x, Double y, Integer r, boolean result) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
    }

    public Request() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public int compareTo(Request o) {
        return - createdTime.compareTo(o.createdTime);
    }
}
