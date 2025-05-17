package org.example;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String material;
    private String color;
    private float price;
    private String file;

    private LocalDateTime orderTime;

    public Item() {
        this.orderTime = LocalDateTime.now();
    }

    public Item(String material, String color, float price, String file) {
        this.material = material;
        this.color = color;
        this.price = price;
        this.file = file;
        this.orderTime = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public String getFile() { return file; }
    public void setFile(String file) { this.file = file; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
}
