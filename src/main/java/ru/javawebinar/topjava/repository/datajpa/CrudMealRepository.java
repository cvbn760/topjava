package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query("SELECT ms FROM Meal ms WHERE ms.user.id=:user_id ORDER BY ms.dateTime DESC")
    List<Meal> getAll(@Param("user_id") int userId);

    @Transactional
    @Modifying
    @Query("SELECT ms FROM Meal ms WHERE ms.id=:id AND ms.user.id=:user_id")
    List<Meal> get(@Param("id") int id, @Param("user_id") int userId);


    @Transactional
    @Modifying
    @Query("SELECT ms FROM Meal ms WHERE ms.user.id=:userId AND ms.dateTime>=:after AND ms.dateTime<:before ORDER BY ms.dateTime DESC")
    List<Meal> getBetweenHalfOpen(@Param("after") LocalDateTime startDateTime, @Param("before") LocalDateTime endDateTime, @Param("userId") int userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal ms WHERE ms.id=:id AND ms.user.id=:user_id")
    int delete(@Param("id") int id, @Param("user_id") int userId);


    @Transactional
    @Modifying
    @Query("UPDATE Meal ms SET ms.dateTime=:dateTime, ms.description=:description, ms.calories=:calories WHERE ms.id=:id AND ms.user.id=:user_id")
    int save(@Param("id") int id, @Param("user_id") int userId, @Param("dateTime") LocalDateTime dateTime, @Param("description") String description, @Param("calories") int calories);
}
