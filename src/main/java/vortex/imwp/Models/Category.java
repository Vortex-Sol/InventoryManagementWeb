package vortex.imwp.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    public Category() {}
    public Category(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Set<Item> getItems() { return items; }

    public void setName(String name) { this.name = name; }
    public void setCategories(Set<Item> items) { this.items = items; }

    @Override
    public String toString() {
        return "Category:\n" + "ID: " + id + "\nName: " + name + "\nItems: " + items;
    }
}
