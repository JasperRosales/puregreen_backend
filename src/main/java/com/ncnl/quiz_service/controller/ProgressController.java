package com.ncnl.quiz_service.controller;

import com.ncnl.quiz_service.model.Progress;
import com.ncnl.quiz_service.model.ProgressInput;
import com.ncnl.quiz_service.model.Quiz;
import com.ncnl.quiz_service.model.User;
import com.ncnl.quiz_service.repository.ProgressRepository;
import com.ncnl.quiz_service.repository.QuizRepository;
import com.ncnl.quiz_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ProgressController {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;


    @QueryMapping
    public List<Progress> getAllProgressByUser(@Argument String srcode){
        User user = userRepository.findBySrcode(srcode);
        List<Progress> progress = progressRepository.findProgressWithQuizAndUserByUserId(user.getId());

        if (progress.isEmpty()) {
            throw new RuntimeException("No progress for user");
        }

        return progress;
    }


    @MutationMapping
    public Progress addProgress(@Argument ProgressInput progressInput, @Argument String srcode, @Argument String title) {

        var user = userRepository.findBySrcode(srcode);

        var quiz = quizRepository.findByTitle(title);

        var existingProgress = progressRepository.findProgressByUserSrcodeAndQuizTitle(srcode, title);

        if (existingProgress.isPresent()) {
            log.warn("Progress already exists for userId={} and quizId={}", srcode, title);
            return existingProgress.get();
        }

        Progress progress = new Progress();
        progress.setUser(user);
        progress.setQuiz(quiz);
        progress.setStatus("DONE");

        return progressRepository.save(progress);
    }

    @MutationMapping
    public Boolean deleteProgress(@Argument Integer id){

        progressRepository.deleteById(id);

        return true;

    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Progress updateProgress( Integer id, ProgressInput input){

        var found = progressRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("No progress of that id"));

        found.setUser(input.user());
        found.setQuiz(input.quiz());
        found.setStatus(input.status());

        return progressRepository.save(found);
    }

    @BatchMapping(field = "quiz", typeName = "Progress")
    public Map<Progress, Quiz> batchQuizzes(List<Progress> progresses) {
        List<Integer> quizIds = progresses.stream()
                .map(p -> p.getQuiz().getId())
                .distinct()
                .toList();

        Map<Integer, Quiz> quizMap = quizRepository.findAllById(quizIds).stream()
                .collect(Collectors.toMap(Quiz::getId, Function.identity()));

        return progresses.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        p -> quizMap.get(p.getQuiz().getId())
                ));
    }

    @BatchMapping(field = "user", typeName = "Progress")
    public Map<Progress, User> batchUsers(List<Progress> progresses) {
        List<Integer> userIds = progresses.stream()
                .map(p -> p.getUser().getId())
                .distinct()
                .toList();

        Map<Integer, User> userMap = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return progresses.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        p -> userMap.get(p.getUser().getId())
                ));
    }




}
