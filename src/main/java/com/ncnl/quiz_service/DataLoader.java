//package com.ncnl.quiz_service;
//
//import com.ncnl.quiz_service.model.Question;
//import com.ncnl.quiz_service.model.Quiz;
//import com.ncnl.quiz_service.repository.QuestionRepository;
//import com.ncnl.quiz_service.repository.QuizRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//
////Run Only Once if runned delete this or comment
//@Component
//public class DataLoader implements CommandLineRunner {
//
//    private final QuizRepository quizRepository;
//    private final QuestionRepository questionRepository;
//
//    public DataLoader(QuizRepository quizRepository, QuestionRepository questionRepository) {
//        this.quizRepository = quizRepository;
//        this.questionRepository = questionRepository;
//    }
//
//    private record QuizAndQuestions(
//            Map<String, Quiz> quizzes,
//            Map<String, Question> questions
//    ){}
//
//    @Override
//    public void run(String... args) throws Exception {
//        var QuizAndQuestions = loadQuizAndQuestions();
//    }
//
//
//
//    private QuizAndQuestions loadQuizAndQuestions(){
//        Quiz intro = new Quiz();
//        intro.setTitle("Introduction to Environmental Science");
//        intro.setCategory("prelim");
//        quizRepository.save(intro);
//
//        Quiz energy = new Quiz();
//        energy.setTitle("Energy, Matter and Cycles");
//        energy.setCategory("prelim");
//        quizRepository.save(energy);
//
//        Quiz ecosystems = new Quiz();
//        ecosystems.setTitle("Ecosystems, Biomes and Species");
//        ecosystems.setCategory("prelim");
//        quizRepository.save(ecosystems);
//
//        Quiz biodiversity = new Quiz();
//        biodiversity.setTitle("Humans and the Biodiversity Crisis");
//        biodiversity.setCategory("prelim");
//        quizRepository.save(biodiversity);
//
//        Quiz resources = new Quiz();
//        resources.setTitle("Land Use, Natural Resources and Energy");
//        resources.setCategory("prelim");
//        quizRepository.save(resources);
//
//        Quiz pollution = new Quiz();
//        pollution.setTitle("Air Pollution and Climate Change");
//        pollution.setCategory("prelim");
//        quizRepository.save(pollution);
//
//        Quiz water = new Quiz();
//        water.setTitle("Water Use and Pollution");
//        water.setCategory("prelim");
//        quizRepository.save(water);
//
//        Quiz economics = new Quiz();
//        economics.setTitle("Economics");
//        economics.setCategory("prelim");
//        quizRepository.save(economics);
//
//        Quiz policies = new Quiz();
//        policies.setTitle("Policies");
//        policies.setCategory("prelim");
//        quizRepository.save(policies);
//
//        Quiz sustainable = new Quiz();
//        sustainable.setTitle("Sustainable Management System");
//        sustainable.setCategory("prelim");
//        quizRepository.save(sustainable);
//
//        return new QuizAndQuestions(
//                Map.of(
//                    "introduction", intro,
//                    "energy", energy,
//                    "ecosystems", ecosystems ,
//                    "human", biodiversity,
//                    "resources", resources,
//                    "air", pollution,
//                    "water", water,
//                    "economics", economics,
//                    "policices", policies,
//                    "sustainable", sustainable
//                ),
//                Map.of(
//
//                )
//        );
//    }
//
//}
