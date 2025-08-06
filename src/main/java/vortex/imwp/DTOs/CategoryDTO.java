package vortex.imwp.DTOs;

import vortex.imwp.Models.Item;
import java.util.HashSet;
import java.util.Set;

public class CategoryDTO {
    private Long id;
    private String name;
    private Set<ItemDTO> items = new HashSet<>();

    public CategoryDTO() {}
    public CategoryDTO(Long id, String name, Set<ItemDTO> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Set<ItemDTO> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setItems(Set<ItemDTO> items) { this.items = items; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Category:\n" + "ID: " + id + "\nName: " + name + "\nItems: " + items;
    }
}
