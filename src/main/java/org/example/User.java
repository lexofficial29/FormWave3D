package org.example;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role = "default";

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserOrder> orders = new ArrayList<>();

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.orders = new ArrayList<>();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<UserOrder> getOrders() { return orders; }
    public void setOrders(List<UserOrder> orders) { this.orders = orders; }
}
