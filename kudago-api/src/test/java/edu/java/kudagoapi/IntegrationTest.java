//package edu.java.kudagoapi;
//
//import edu.java.kudagoapi.dtos.CategoryDto;
//import edu.java.kudagoapi.dtos.LocationDto;
//import edu.java.kudagoapi.model.Category;
//import edu.java.kudagoapi.model.Location;
//import edu.java.kudagoapi.repositories.CategoryRepository;
//import edu.java.kudagoapi.repositories.JpaLocationRepository;
//import edu.java.kudagoapi.services.category.CategoryHistory;
//import edu.java.kudagoapi.services.category.CategoryService;
//import edu.java.kudagoapi.services.location.JpaLocationHistory;
//import edu.java.kudagoapi.services.location.JpaLocationService;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class IntegrationTest extends IntegrationEnvironment {
//
//    private final JpaLocationRepository repo;
//    private final JpaLocationHistory history;
//    private final JpaLocationService locationService;
//    private final CategoryService categoryService;
//    private final CategoryHistory categoryHistory;
//    private final CategoryRepository categoryRepository;
//
//    @Autowired
//    public IntegrationTest(JpaLocationRepository repo, JpaLocationHistory history, JpaLocationService locationService, CategoryService categoryService, CategoryHistory categoryHistory, CategoryRepository categoryRepository) {
//        this.repo = repo;
//        this.history = history;
//        this.locationService = locationService;
//        this.categoryService = categoryService;
//        this.categoryHistory = categoryHistory;
//        this.categoryRepository = categoryRepository;
//    }
//
//    @Test
//    public void test() {
//        LocationDto dto = new LocationDto(
//                "test",
//                "test",
//                "test"
//        );
//        locationService.save(dto);
//        LocationDto dto2 = new LocationDto(
//                "test1",
//                "test1",
//                "test1"
//        );
//        locationService.fullUpdate(1L, dto2);
//        Location first = repo.findAll().getFirst();
//        System.out.println(first);
//        Location poll = history.poll(first.getId());
//        System.out.println(poll);
//        first = repo.findAll().getFirst();
//        System.out.println(first);
//
//    }
//
//    @Test
//    public void test2() {
//        CategoryDto dto1 = new CategoryDto(
//                "test1",
//                "test1"
//        );
//        categoryService.save(dto1, 1L);
//        CategoryDto dto2 = new CategoryDto(
//                "test2",
//                "test2"
//        );
//        categoryService.fullUpdate(dto2, 1L);
//        Category category = categoryRepository.findById(1L).get();
//        System.out.println(category);
//        category = categoryHistory.poll(1L);
//        System.out.println(category);
//        category = categoryRepository.findById(1L).get();
//        System.out.println(category);
//    }
//}
