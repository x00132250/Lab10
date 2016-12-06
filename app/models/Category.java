package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.validation.*;

@Entity
public class Category extends Model {


    //Properties
    //Annotate id as the primary key

    @Id
    private Long id;

    @Constraints.Required
    private String name;

    @OneToMany
    private List<Product> products;

    public Category() {
    }

    public Category(Long id, String name, List<Product> products) {
        this.setId(id);
        this.setName(name);
        this.setProducts(products);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    //Generic query helper for entity Computer with id Long
    public static Finder<Long, Category> find = new Finder<Long, Category>(Category.class);

    // Find all Products in the database in ascending order by name
    public static List<Category> findAll() {
        return Category.find.where().orderBy("name asc").findList();
    }

    public static Map<String, String> options(){
        LinkedHashMap<String, String> options = new LinkedHashMap<>();

        for(Category c: Category.findAll()){
            options.put(c.getId().toString(), c.getName());
        }
        return options;
    }
}
