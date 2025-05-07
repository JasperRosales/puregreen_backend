package com.ncnl.quiz_service.repository;

import com.ncnl.quiz_service.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Integer> {

    List<Progress> findProgressByUserIdIn(List<Integer> userId);

    @Query("SELECT p FROM Progress p " +
            "JOIN FETCH p.quiz " +
            "JOIN FETCH p.user " +
            "WHERE p.user.id = :userId")
    List<Progress> findProgressWithQuizAndUserByUserId(@Param("userId") Integer userId);

    Optional<Progress> findByUserIdAndQuizId(Integer userId, Integer quizId);
    Optional<Progress> findProgressByUserSrcodeAndQuizTitle(String srcode, String title);

}
