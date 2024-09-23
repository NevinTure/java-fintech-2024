package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    List<Category> saveAll(List<Category> categories);

    Optional<Category> findById(long id);

    void deleteById(long id);
}
