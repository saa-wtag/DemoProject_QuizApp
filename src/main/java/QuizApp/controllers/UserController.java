package QuizApp.controllers;

import javax.validation.Valid;

import QuizApp.exceptions.AccessDeniedException;
import QuizApp.model.quiz.QuizzesAndScoresView;
import QuizApp.model.user.User;
import QuizApp.model.user.UserInput;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserView;
import QuizApp.repositories.UserRepository;
import QuizApp.services.quiz.QuizService;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import QuizApp.services.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final QuizService quizService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, QuizService quizService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.quizService = quizService;
    }

    @PostMapping("/")
    public ResponseEntity<UserView> createUser(@Valid @RequestBody UserInput userInput) {
        User user = QuizObjectMapper.convertUserInputToModel(userInput);
        UserView registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
    @GetMapping("/{userId}/quizzes")
    public ResponseEntity<List<QuizzesAndScoresView>> listQuizzesForUser(@PathVariable("userId") int userId) {
        List<QuizzesAndScoresView> quizzes = quizService.listQuizzesForUser(userId);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId) {
        UserView user = userService.getUser(userId);//userRepository.findUserViewByUserId(userId);
        if(user != null) {
            MappingJacksonValue mapping = new MappingJacksonValue(user);
            SimpleFilterProvider filters = new SimpleFilterProvider();
            if (user.getRole() == User.UserRole.ADMIN) {
                filters.addFilter("userViewFilter", SimpleBeanPropertyFilter.serializeAllExcept("quizzes"));
            } else {
                filters.addFilter("userViewFilter", SimpleBeanPropertyFilter.serializeAll());
            }

            mapping.setFilters(filters);
            return ResponseEntity.ok(mapping);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    @PutMapping("/")
    public ResponseEntity<Map<String, Object>> editUserDetails(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserUpdate updatedUser) {

        String jwt = token.substring(7); // Extract the JWT
        Map<String, Object> response = new HashMap<>();

        UserView editedUser = userService.updateUserDetails(jwt, updatedUser);
        response.put("user", editedUser);
        response.put("message", "User details updated successfully. Re-authentication required.");
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
