package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.GET_ALL, query = "SELECT ms FROM Meal ms WHERE ms.user_id=:user_id ORDER BY ms.dateTime DESC"),
        @NamedQuery(name = Meal.GET_ONE, query = "SELECT ms FROM Meal ms WHERE ms.user_id=:user_id AND ms.id=:id"),
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal ms WHERE ms.id=:id AND ms.user_id=:user_id"),
        @NamedQuery(name = Meal.GET_BETWEEN, query = "SELECT ms FROM Meal ms WHERE ms.dateTime>=:after AND ms.dateTime<:before AND ms.user_id=:user_id  ORDER BY ms.dateTime DESC"),
})

@javax.persistence.Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"date_time","user_id"}, name = "meals_unique_user_datetime_idx")})
@Access(AccessType.FIELD)
public class Meal extends AbstractBaseEntity {
    public static final String GET_ALL = "Meals.getAll";
    public static final String GET_ONE = "Meals.getOne";
    public static final String DELETE = "Meals.delete";
    public static final String SAVE = "Meals.save";
    public static final String GET_BETWEEN = "Meals.getBetween";

  //  @NotNull
   // @Column(name = "user_id", nullable = false)
    private int user_id;

    @NotNull
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @NotEmpty
    @Size(min = 3)
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "calories", nullable = false)
    private int calories;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
