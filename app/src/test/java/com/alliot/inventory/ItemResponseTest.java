package com.alliot.inventory;
import com.alliot.inventory.model.Item;
import com.alliot.inventory.model.ItemResponse;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;
public class ItemResponseTest {
    @Test
    public void hasMorePages_trueWhenNotOnLastPage() {
        ItemResponse r = response(1, 15, 100);
        assertTrue(r.hasMorePages());
    }
    @Test
    public void hasMorePages_falseWhenExactFit() {
        assertFalse(response(2, 15, 30).hasMorePages());
    }
    @Test
    public void hasMorePages_falseWhenPastLastPage() {
        assertFalse(response(7, 15, 87).hasMorePages());
    }
    @Test
    public void hasMorePages_trueJustBeforeLastPage() {
        assertTrue(response(5, 15, 87).hasMorePages());
    }
    @Test
    public void hasMorePages_falseWhenTotalIsZero() {
        assertFalse(response(1, 15, 0).hasMorePages());
    }
    @Test
    public void hasMorePages_singleItemSinglePage() {
        assertFalse(response(1, 15, 1).hasMorePages());
    }
    @Test
    public void hasMorePages_totalEqualsPerPage() {
        assertFalse(response(1, 15, 15).hasMorePages());
    }
    @Test
    public void hasMorePages_totalOneMoreThanPerPage() {
        assertTrue(response(1, 15, 16).hasMorePages());
    }
    @Test
    public void count_getterSetter() {
        ItemResponse r = new ItemResponse();
        r.setCount(42);
        assertEquals(42, r.getCount());
    }
    @Test
    public void paginated_getterSetter() {
        ItemResponse r = new ItemResponse();
        assertFalse(r.isPaginated());
        r.setPaginated(true);
        assertTrue(r.isPaginated());
    }
    @Test
    public void data_defaultIsNull() {
        assertNull(new ItemResponse().getData());
    }
    @Test
    public void data_setAndGet() {
        ItemResponse r = new ItemResponse();
        Item a = new Item(); a.setId(1);
        Item b = new Item(); b.setId(2);
        r.setData(Arrays.asList(a, b));
        assertEquals(2, r.getData().size());
        assertEquals(1, r.getData().get(0).getId());
    }
    @Test
    public void data_emptyList() {
        ItemResponse r = new ItemResponse();
        r.setData(Collections.emptyList());
        assertTrue(r.getData().isEmpty());
    }
    private static ItemResponse response(int page, int per, int total) {
        ItemResponse r = new ItemResponse();
        r.setPage(page); r.setPer(per); r.setTotal(total);
        return r;
    }
}
