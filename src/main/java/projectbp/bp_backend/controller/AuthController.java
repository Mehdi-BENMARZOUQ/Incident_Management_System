package projectbp.bp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import projectbp.bp_backend.bean.Agence;
import projectbp.bp_backend.bean.User;
import projectbp.bp_backend.dto.auth.RegisterRequest;
import projectbp.bp_backend.service.AuthenticationUserService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {



    private final AuthenticationUserService authservice;

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authservice.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<RegisterRequest> login(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authservice.login(request));
    }
    @PostMapping("/refresh")
    public ResponseEntity<RegisterRequest> refreshToken(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authservice.refreshToken(request));
    }
    @GetMapping("/current-user")
    public ResponseEntity<Object> getCurrentUser() {
        try {
            UserDetails user = authservice.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving current user: " + e.getMessage());
        }
    }

    @GetMapping("/supervisor/getAll")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(authservice.findAll());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/supervisor/updateUser/{id}")
    public ResponseEntity<RegisterRequest> updateUser(@PathVariable Long id, @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authservice.updateUser(request));
    }

    @DeleteMapping("/supervisor/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authservice.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return authservice.findByEmail(email);
    }


}
