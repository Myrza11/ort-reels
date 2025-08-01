package org.example.aktanoopproject.repository;

import org.example.aktanoopproject.model.Interest;
import org.example.aktanoopproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByNameLikeIgnoreCase(String name);

    // ✅ Фильтрация по интересам (enum)
    @Query("SELECT DISTINCT u FROM User u JOIN u.interest i WHERE i IN :interests")
    List<User> filterByInterest(@Param("interests") Set<Interest> interests);
}
