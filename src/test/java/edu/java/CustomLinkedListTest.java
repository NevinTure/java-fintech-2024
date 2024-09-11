package edu.java;

import edu.java.utils.CustomLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import java.util.LinkedList;
import java.util.List;

public class CustomLinkedListTest {

    private LinkedList<Integer> list;

    @BeforeEach
    public void init() {
        list = new CustomLinkedList<>();
    }

    @Test
    public void testAdd() {
        //when
        list.add(1);

        //then
        assertThat(list).size().isEqualTo(1);
    }

    @Test
    public void testGet() {
        //when
        list.add(1);
        list.add(2);
        list.add(3);

        //then
        assertThat(list.get(2)).isEqualTo(3);
    }

    @Test
    public void testGetThenException() {
        //then
        assertThatException().isThrownBy(() -> list.get(2));
    }

    @Test
    public void testRemoveByIndex() {
        //given
        list.add(1);
        list.add(2);
        list.add(3);

        //when
        list.remove(1);

        //then
        assertThat(list).size().isEqualTo(2);
        assertThat(list.get(1)).isEqualTo(3);
    }

    @Test
    public void testRemoveThenException() {
        assertThatException().isThrownBy(() -> list.remove(1));
    }

    @Test
    public void testRemoveObject() {
        //given
        list.add(5);
        list.add(3);
        list.add(8);

        //when
        list.remove(Integer.valueOf(5));

        //then
        assertThat(list).size().isEqualTo(2);
        assertThat(list.get(0)).isEqualTo(3);
    }

    @Test
    public void testContainsThenTrue() {
        //given
        list.add(1);
        list.add(2);
        list.add(3);

        //when
        boolean result = list.contains(3);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void testContainsThenFalse() {
        //given
        list.add(1);
        list.add(2);
        list.add(3);

        //when
        boolean result = list.contains(10);

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void testAddAll() {
        //given
        list.add(1);
        list.add(2);
        List<Integer> elems = List.of(5,3,1);

        //when
        boolean result = list.addAll(elems);

        //then
        assertThat(result).isTrue();
        assertThat(list).size().isEqualTo(5);
    }
}
