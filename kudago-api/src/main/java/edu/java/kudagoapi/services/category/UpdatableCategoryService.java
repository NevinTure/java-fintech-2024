package edu.java.kudagoapi.services.category;

import edu.java.kudagoapi.dtos.CategoryDto;
import org.springframework.http.ResponseEntity;

public interface UpdatableCategoryService extends CategoryService {

    ResponseEntity<Object> fullUpdate(CategoryDto dto, long id);

    ResponseEntity<Object> undoUpdate(long id);
}
