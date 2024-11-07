package edu.java.kudagoapi.services;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.clients.KudagoClient;
import edu.java.kudagoapi.dtos.CategoryDto;
import edu.java.kudagoapi.exceptions.*;
import edu.java.kudagoapi.model.Category;
import edu.java.kudagoapi.repositories.CategoryRepository;
import edu.java.kudagoapi.services.category.UpdatableCategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class CategoryServiceTest extends IntegrationEnvironment {

    @MockBean
    CategoryRepository repository;
    @MockBean
    KudagoClient client;
    @Autowired
    UpdatableCategoryService service;
    @Autowired
    ModelMapper mapper;

    @Test
    public void testSaveAll() {
        //given
        CategoryDto dto1 = new CategoryDto(
                1L,
                "test1",
                "test1"
        );
        CategoryDto dto2 = new CategoryDto(
                2L,
                "test2",
                "test2"
        );
        Category category1 = mapper.map(dto1, Category.class);
        Category category2 = mapper.map(dto2, Category.class);

        //when
        service.saveAll(List.of(dto1, dto2));

        //then
        List<Category> expected = List.of(category1, category2);
        Mockito.verify(repository, Mockito.times(1)).saveAll(expected);
    }

    @Test
    public void testSaveAllWhenIdNotSpecified() {
        //given
        CategoryDto dto1 = new CategoryDto(
                "test1",
                "test1"
        );
        CategoryDto dto2 = new CategoryDto(
                2L,
                "test2",
                "test2"
        );

        //then
        assertThatExceptionOfType(BadRequestApiException.class)
                .isThrownBy(() -> service.saveAll(List.of(dto1, dto2)));
    }

    @Test
    public void testSave() {
        //given
        long id = 1L;
        CategoryDto dto = new CategoryDto(
                "test",
                "test"
        );
        Category category = mapper.map(dto, Category.class);
        category.setId(id);

        //when
        service.save(dto, id);

        //then
        Mockito.verify(repository, Mockito.times(1)).save(category);
    }

    @Test
    public void testSaveThenAlreadyExists() {
        //given
        long id = 1L;
        Category category = new Category(
                id,
                "test",
                "test"
        );
        CategoryDto dto2 = new CategoryDto(
                "test2",
                "test2"
        );

        //when
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(category));

        //then
        assertThatExceptionOfType(BadRequestApiException.class)
                .isThrownBy(() -> service.save(dto2, id));
    }

    @Test
    public void testGetById() {
        //given
        long id = 1L;
        Category category = new Category(
                id,
                "test",
                "test"
        );
        CategoryDto dto = mapper.map(category, CategoryDto.class);

        //when
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(category));

        //then
        assertThat(service.getById(id).getBody()).isEqualTo(dto);
    }

    @Test
    public void testGetByIdWhenNotFound() {
        //when
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(CategoryNotFoundApiException.class)
                .isThrownBy(() -> service.getById(1L));
    }

    @Test
    public void testFindAll() {
        //given
        Category category1 = new Category(
                1L,
                "test1",
                "test1"
        );
        Category category2 = new Category(
                2L,
                "test2",
                "test2"
        );
        CategoryDto dto1 = mapper.map(category1, CategoryDto.class);
        CategoryDto dto2 = mapper.map(category2, CategoryDto.class);

        //when
        Mockito.when(repository.findAll()).thenReturn(List.of(category1, category2));

        //then
        List<CategoryDto> expected = List.of(dto1, dto2);
        assertThat(service.findAll().getBody()).containsExactlyElementsOf(expected);
    }

    @Test
    public void testFullUpdate() {
        //given
        long id = 1L;
        Category category = new Category(
                id,
                "test",
                "test"
        );
        CategoryDto dto = new CategoryDto(
                "test1",
                "test1"
        );
        Category saved = mapper.map(dto, Category.class);
        saved.setId(id);

        //when
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(category));
        service.fullUpdate(dto, id);

        //then
        Mockito.verify(repository, Mockito.times(1)).save(saved);
    }

    @Test
    public void testFullUpdateWhenNotFound() {
        //given
        CategoryDto dto = new CategoryDto(
                "test",
                "test"
        );

        //when
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(CategoryNotFoundApiException.class)
                .isThrownBy(() -> service.fullUpdate(dto, 1L));
    }

    @Test
    public void testDelete() {
        //given
        long id = 1L;
        Category category = new Category(
                id,
                "test",
                "test"
        );

        //when
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(category));
        service.deleteById(id);

        //then
        Mockito.verify(repository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void testDeleteWhenNotFound() {
        //when
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(CategoryNotFoundApiException.class)
                .isThrownBy(() -> service.deleteById(1L));
    }

    @Test
    public void testUndoUpdate() {
        //given
        long id = 1L;
        CategoryDto dto1 = new CategoryDto(id, "test1", "test1");
        CategoryDto dto2 = new CategoryDto(id, "test2", "test2");
        Category category1 = mapper.map(dto1, Category.class);

        //when
        service.save(dto1, id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(category1));
        service.fullUpdate(dto2, id);
        service.undoUpdate(id);

        //then
        assertThat(service.getById(id).getBody()).isEqualTo(dto1);
    }

    @Test
    public void testUndoUpdateWhenNotFound() {
        //then
        assertThatExceptionOfType(SnapshotNotFoundApiException.class)
                .isThrownBy(() -> service.undoUpdate(1000L));
    }
}
