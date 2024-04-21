package QuizApp.controllers;

import javax.validation.Valid;

import QuizApp.exceptions.AccessDeniedException;
import QuizApp.model.user.User;
import QuizApp.model.user.UserInput;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserView;
import QuizApp.repositories.UserRepository;
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
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserInput userInput) {
        User user = QuizObjectMapper.convertUserInputToModel(userInput);
        User registeredUser = userService.registerUser(user);
        ResponseEntity<?> response = getUser(registeredUser.getUserId());
        return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
    }
    @PostMapping("/admin/")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody UserInput userInput) {
        User user = QuizObjectMapper.convertUserInputToModel(userInput);
        User registeredAdmin = userService.registerAdmin(user);
        ResponseEntity<?> response = getUser(registeredAdmin.getUserId());
        return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId) {
        UserView user = userRepository.findUserViewByUserId(userId);
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
    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> editUserDetails(
            @RequestHeader("Authorization") String token,
            @PathVariable int userId,
            @Valid @RequestBody UserUpdate updatedUser) {

        String jwt = token.substring(7);
        Map<String, Object> response = new HashMap<>();

        try {
            User user = QuizObjectMapper.convertUserUpdateToModel(updatedUser);
            User editedUser = userService.updateUserDetails(userId, user, jwt);
            UserView userView = userRepository.findUserViewByUserId(userId);

            if (userView != null) {
                response.put("user", userView);
                response.put("message", "User details updated successfully. Login required.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "User not found with ID: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (AccessDeniedException e) {
            response.put("error", "Access denied: Users can only update their own details.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (IllegalStateException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
