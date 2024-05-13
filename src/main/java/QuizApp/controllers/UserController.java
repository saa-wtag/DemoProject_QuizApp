package QuizApp.controllers;

import javax.validation.Valid;

import QuizApp.model.quiz.UserQuizDTO;
import QuizApp.model.user.*;
import QuizApp.services.quiz.QuizService;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import QuizApp.services.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final QuizService quizService;


    public UserController(UserService userService, QuizService quizService) {
        this.userService = userService;
        this.quizService = quizService;
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserInput userInput) {
        User user = QuizObjectMapper.convertUserInputToModel(userInput);
        UserDTO registeredUser = QuizObjectMapper.convertToUserViewDTO(userService.registerUser(user));
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/quizzes")
    public ResponseEntity<List<UserQuizDTO>> listQuizzesForUser(@PathVariable("userId") int userId) {
        List<UserQuizDTO> quizzes = quizService.listQuizzesForUser(userId);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId) {
        UserDTO user = QuizObjectMapper.convertToUserViewDTO(userService.getUser(userId));
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        SimpleFilterProvider filters = new SimpleFilterProvider();
        if (user.getRole() == User.UserRole.ADMIN) {
            filters.addFilter("userViewFilter", SimpleBeanPropertyFilter.serializeAllExcept("quizzes"));
        } else {
            filters.addFilter("userViewFilter", SimpleBeanPropertyFilter.serializeAll());
        }

        mapping.setFilters(filters);

        return ResponseEntity.ok(mapping);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> editUserDetails(
            @RequestHeader("Authorization") String token,@PathVariable int userId,
            @Valid @RequestBody UserUpdate updatedUser) {

        String jwt = token.substring(7);
        Map<String, Object> response = new HashMap<>();

        UserDTO editedUser = QuizObjectMapper.convertToUserViewDTO(userService.updateUserDetails(jwt,userId, updatedUser));
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
