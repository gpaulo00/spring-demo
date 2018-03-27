package org.gpaulo.springdemo.repos;

import org.gpaulo.springdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM users u WHERE u.last_name ~* :query OR u.first_name ~* :query", nativeQuery = true)
    List<User> search(@Param("query") String query);
}
