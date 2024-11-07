package edu.java.kudagoapi.services.category;

import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.utils.CategoryRequestOperation;

public interface CategoryEventListener {

    void update(CategoryRequestOperation op, Category category);
}
