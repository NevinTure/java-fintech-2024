package edu.java.kudagoapi.services.category;

import edu.java.kudagoapi.model.Category;

public interface CategoryHistory {

    void push(Long id, Category category);

    Category poll(Long id);
}
