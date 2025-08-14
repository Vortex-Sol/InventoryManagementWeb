package vortex.imwp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    public Category() {}
    public Category(String name) {
        this.name = name;
    }


    public Long getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Category:\n" + "ID: " + id + "\nName: " + name;
    }
}
