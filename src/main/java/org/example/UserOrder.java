package org.example;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;

    private String filePath;
    private String filamentType;
    private String filamentColor;
    private int completion = 0;

    public UserOrder() {}

    public UserOrder(List<Item> items, String filePath, String filamentType, String filamentColor) {
        this.items = items;
        this.filePath = filePath;
        this.filamentType = filamentType;
        this.filamentColor = filamentColor;
        this.completion = 0;
    }

    // Getters and setters
    public int getCompletion() { return completion; }
    public void setCompletion(int completion) { this.completion = completion; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFilamentType() { return filamentType; }
    public void setFilamentType(String filamentType) { this.filamentType = filamentType; }

    public String getFilamentColor() { return filamentColor; }
    public void setFilamentColor(String filamentColor) { this.filamentColor = filamentColor; }
}