package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser == null || !passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        String token = jwtUtil.generateToken(foundUser);

        return ResponseEntity.ok(new AuthResponse(token, foundUser.getEmail(), foundUser.getRole()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole() != null ? user.getRole() : "USER");

        User savedUser = userRepository.save(newUser);

        String token = jwtUtil.generateToken(savedUser);

        return ResponseEntity.ok(new AuthResponse(token, savedUser.getEmail(), savedUser.getRole()));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("filamentType") String filamentType,
            @RequestParam("filamentColor") String filamentColor,
            @RequestParam("userId") Long userId) {
        try {
            // Validate inputs
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is required");
            }
            if (filamentType == null || filamentType.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Filament type is required");
            }
            if (filamentColor == null || filamentColor.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Filament color is required");
            }

            // Find user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Store file
            String filePath = fileStorageService.storeFile(file);

            // Create item (optional, depending on your requirements)
            Item item = new Item(file.getOriginalFilename(), "3D print item");

            // Create order
            UserOrder order = new UserOrder();
            order.setFilePath(filePath);
            order.setFilamentType(filamentType);
            order.setFilamentColor(filamentColor);
            order.setItems(List.of(item));

            // Add order to user's orders
            user.getOrders().add(order);
            userRepository.save(user);

            return ResponseEntity.ok("File uploaded and order saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    public static class AuthResponse {
        private String token;
        private String email;
        private String role;

        public AuthResponse(String token, String email, String role) {
            this.token = token;
            this.email = email;
            this.role = role;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}