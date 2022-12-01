package web.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

public class RequestDTO {

    @Null(message = "must be null")
    private Date createdTime;

    @NotNull
    @Range(min = -5, max = 3, message = "must be in range (-5..3)")
    private Integer x;

    @NotNull
    @Range(min = -5, max = 3, message = "must be in range (-5..3)")
    private Double y;

    @NotNull(message = "must be not null")
    @Positive(message = "must be positive")
    @Max(value = 3, message = "must be less than 3")
    private Integer r;
    
    @Null(message = "must be null")
    private Boolean result;

    public RequestDTO(Date createdTime, Integer x, Double y, Integer r, Boolean result) {
        this.createdTime = createdTime;
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
    }

    public RequestDTO(Integer x, Double y, Integer r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public RequestDTO() {
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

    public Boolean isResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
