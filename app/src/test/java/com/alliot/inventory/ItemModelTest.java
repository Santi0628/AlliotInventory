package com.alliot.inventory;

import com.alliot.inventory.model.DefaultPicture;
import com.alliot.inventory.model.ImageUrl;
import com.alliot.inventory.model.Inventory;
import com.alliot.inventory.model.Item;
import com.alliot.inventory.model.ItemResponse;
import com.alliot.inventory.model.PictureFile;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Unit tests for the Item model and related POJOs.
 * Validates JSON field mapping logic, fallback behavior, and edge cases.
 */
public class ItemModelTest {

    private Item item;

    @Before
    public void setUp() {
        item = new Item();
        item.setId(1024);
        item.setName("Taladro Percutor Bosch GSB 13RE");
        item.setInternalSku("SKU-00142");
        item.setExternalSku("EXT-9981");
        item.setBarcode("7801234567890");
        item.setActive(true);
        item.setConsumable(false);
        item.setRepairable(true);
        item.setRestricted(false);
        item.setQuantity(5);
    }

    // ========== Basic getters ==========

    @Test
    public void testBasicGetters() {
        assertEquals(1024, item.getId());
        assertEquals("Taladro Percutor Bosch GSB 13RE", item.getName());
        assertEquals("SKU-00142", item.getInternalSku());
        assertEquals("EXT-9981", item.getExternalSku());
        assertEquals("7801234567890", item.getBarcode());
        assertTrue(item.isActive());
        assertFalse(item.isConsumable());
        assertTrue(item.isRepairable());
        assertFalse(item.isRestricted());
        assertEquals(5, item.getQuantity());
    }

    // ========== Barcode fallback ==========

    @Test
    public void testBarcodeReturnsBarcode_whenBarcodeIsNotNull() {
        item.setBarcode("PRIMARY_BARCODE");
        assertEquals("PRIMARY_BARCODE", item.getBarcode());
    }

    @Test
    public void testBarcodeReturnsNull_whenBothNull() {
        Item emptyItem = new Item();
        assertNull(emptyItem.getBarcode());
    }

    // ========== Inventory fallback ==========

    @Test
    public void testGetInventory_returnsInventory_whenNotNull() {
        Inventory inv = new Inventory();
        inv.setAvailableQuantity(3.0);
        item.setInventory(inv);

        assertNotNull(item.getInventory());
        assertEquals(3.0, item.getInventory().getAvailableQuantity(), 0.01);
    }

    @Test
    public void testGetInventory_returnsNull_whenBothNull() {
        Item emptyItem = new Item();
        assertNull(emptyItem.getInventory());
    }

    @Test
    public void testGetAvailableQuantity_returnsZero_whenNoInventory() {
        Item emptyItem = new Item();
        assertEquals(0, emptyItem.getAvailableQuantity(), 0.01);
    }

    @Test
    public void testGetAvailableQuantity_returnsValue_whenInventoryPresent() {
        Inventory inv = new Inventory();
        inv.setAvailableQuantity(7.5);
        item.setInventory(inv);
        assertEquals(7.5, item.getAvailableQuantity(), 0.01);
    }

    // ========== Image URL helpers ==========

    @Test
    public void testGetMediumImageUrl_returnsNull_whenNoPicture() {
        assertNull(item.getMediumImageUrl());
    }

    @Test
    public void testGetMediumImageUrl_returnsNull_whenFileIsNull() {
        DefaultPicture dp = new DefaultPicture();
        dp.setFile(null);
        item.setDefaultPicture(dp);
        assertNull(item.getMediumImageUrl());
    }

    @Test
    public void testGetMediumImageUrl_returnsMediumUrl() {
        ImageUrl medium = new ImageUrl();
        medium.setUrl("https://cdn.example.com/medium.jpg");
        PictureFile file = new PictureFile();
        file.setMedium(medium);
        DefaultPicture dp = new DefaultPicture();
        dp.setFile(file);
        item.setDefaultPicture(dp);

        assertEquals("https://cdn.example.com/medium.jpg", item.getMediumImageUrl());
    }

    @Test
    public void testGetLargeImageUrl_fallsBackToMedium() {
        ImageUrl medium = new ImageUrl();
        medium.setUrl("https://cdn.example.com/medium.jpg");
        PictureFile file = new PictureFile();
        file.setMedium(medium);
        DefaultPicture dp = new DefaultPicture();
        dp.setFile(file);
        item.setDefaultPicture(dp);

        // large is null, should fallback to medium
        assertEquals("https://cdn.example.com/medium.jpg", item.getLargeImageUrl());
    }

    @Test
    public void testGetLargeImageUrl_returnsLargeUrl() {
        ImageUrl large = new ImageUrl();
        large.setUrl("https://cdn.example.com/large.jpg");
        PictureFile file = new PictureFile();
        file.setLarge(large);
        DefaultPicture dp = new DefaultPicture();
        dp.setFile(file);
        item.setDefaultPicture(dp);

        assertEquals("https://cdn.example.com/large.jpg", item.getLargeImageUrl());
    }

    // ========== PictureFile URL fallback chain ==========

    @Test
    public void testPictureFileBestUrl_prefersLarge() {
        PictureFile file = new PictureFile();
        ImageUrl large = new ImageUrl();
        large.setUrl("large.jpg");
        ImageUrl medium = new ImageUrl();
        medium.setUrl("medium.jpg");
        file.setLarge(large);
        file.setMedium(medium);

        assertEquals("large.jpg", file.getBestUrl());
    }

    @Test
    public void testPictureFileBestUrl_fallsToBaseUrl() {
        PictureFile file = new PictureFile();
        file.setUrl("base.jpg");
        assertEquals("base.jpg", file.getBestUrl());
    }

    @Test
    public void testPictureFileMediumUrl_fallsToLarge_whenNoMedium() {
        PictureFile file = new PictureFile();
        ImageUrl large = new ImageUrl();
        large.setUrl("large.jpg");
        file.setLarge(large);

        assertEquals("large.jpg", file.getMediumUrl());
    }

    // ========== ItemResponse ==========

    @Test
    public void testItemResponse_hasMorePages() {
        ItemResponse response = new ItemResponse();
        response.setPage(1);
        response.setPer(15);
        response.setTotal(87);

        assertTrue(response.hasMorePages());
    }

    @Test
    public void testItemResponse_noMorePages_whenOnLastPage() {
        ItemResponse response = new ItemResponse();
        response.setPage(6);
        response.setPer(15);
        response.setTotal(87);

        assertFalse(response.hasMorePages());
    }

    @Test
    public void testItemResponse_noMorePages_whenExact() {
        ItemResponse response = new ItemResponse();
        response.setPage(2);
        response.setPer(15);
        response.setTotal(30);

        assertFalse(response.hasMorePages());
    }

    @Test
    public void testItemResponse_dataList() {
        ItemResponse response = new ItemResponse();
        response.setData(Arrays.asList(item));
        response.setCount(1);
        response.setTotal(1);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals("Taladro Percutor Bosch GSB 13RE", response.getData().get(0).getName());
    }

    // ========== Edge cases ==========

    @Test
    public void testEmptyItem_defaults() {
        Item empty = new Item();
        assertEquals(0, empty.getId());
        assertNull(empty.getName());
        assertNull(empty.getInternalSku());
        assertNull(empty.getExternalSku());
        assertNull(empty.getBarcode());
        assertFalse(empty.isActive());
        assertFalse(empty.isConsumable());
        assertFalse(empty.isRepairable());
        assertFalse(empty.isRestricted());
        assertEquals(0, empty.getQuantity());
        assertNull(empty.getDefaultPicture());
        assertNull(empty.getInventory());
        assertNull(empty.getModel());
        assertNull(empty.getBrand());
        assertEquals(0, empty.getAvailableQuantity(), 0.01);
        assertNull(empty.getMediumImageUrl());
        assertNull(empty.getLargeImageUrl());
    }

    @Test
    public void testModel_andBrand() {
        item.setModel("GSB 13RE");
        item.setBrand("Bosch");
        assertEquals("GSB 13RE", item.getModel());
        assertEquals("Bosch", item.getBrand());
    }
}

