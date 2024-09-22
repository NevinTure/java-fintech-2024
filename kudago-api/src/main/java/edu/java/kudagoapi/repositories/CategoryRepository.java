package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.Category;
import java.util.*;

public interface CategoryRepository {

    Category save(Category category);

    List<Category> saveAll(List<Category> categories);

    Optional<Category> findById(long id);

    void deleteById(long id);
}
