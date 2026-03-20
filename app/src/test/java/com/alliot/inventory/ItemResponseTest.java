package com.alliot.inventory;
import com.alliot.inventory.model.Item;
import com.alliot.inventory.model.ItemResponse;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;
public class ItemResponseTest {
    @Test
    public void hasMorePages_true_whenOnFirstOfManyPages() {
        assertTrue(response(1, 15, 100).hasMorePages());
    }

    @Test
    public void hasMorePages_false_whenOnLastPage() {
        assertFalse(response(100, 15, 100).hasMorePages());
    }

    @Test
    public void hasMorePages_false_whenPastLastPage() {
        assertFalse(response(101, 15, 100).hasMorePages());
    }

    @Test
    public void hasMorePages_true_whenOnPageBeforeLast() {
        assertTrue(response(99, 15, 100).hasMorePages());
    }

    @Test
    public void hasMorePages_false_whenZeroTotalPages() {
        assertFalse(response(1, 15, 0).hasMorePages());
    }

    @Test
    public void hasMorePages_false_whenSinglePage() {
        assertFalse(response(1, 15, 1).hasMorePages());
    }

    @Test
    public void hasMorePages_false_whenCurrentEqualsTotal() {
        assertFalse(response(15, 15, 15).hasMorePages());
    }

    @Test
    public void hasMorePages_true_whenTwoPagesOnFirst() {
        assertTrue(response(1, 15, 2).hasMorePages());
    }

    @Test
    public void getEstimatedTotalItems_returnsPagesByPer() {
        ItemResponse r = response(1, 20, 754);
        assertEquals(15080, r.getEstimatedTotalItems());
    }

    @Test
    public void getEstimatedTotalItems_zeroWhenNoPages() {
        assertEquals(0, response(1, 15, 0).getEstimatedTotalItems());
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
