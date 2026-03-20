package com.alliot.inventory;

import com.alliot.inventory.model.Inventory;

import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    @Test
    public void defaultConstructor_availableQuantityIsZero() {
        Inventory inv = new Inventory();
        assertEquals(0.0, inv.getAvailableQuantity(), 0.001);
    }

    @Test
    public void setAvailableQuantity_returnsCorrectValue() {
        Inventory inv = new Inventory();
        inv.setAvailableQuantity(42.5);
        assertEquals(42.5, inv.getAvailableQuantity(), 0.001);
    }

    @Test
    public void setAvailableQuantity_negativeValue() {
        Inventory inv = new Inventory();
        inv.setAvailableQuantity(-3.0);
        assertEquals(-3.0, inv.getAvailableQuantity(), 0.001);
    }

    @Test
    public void setAvailableQuantity_zero() {
        Inventory inv = new Inventory();
        inv.setAvailableQuantity(10.0);
        inv.setAvailableQuantity(0.0);
        assertEquals(0.0, inv.getAvailableQuantity(), 0.001);
    }

    @Test
    public void setAvailableQuantity_largeNumber() {
        Inventory inv = new Inventory();
        inv.setAvailableQuantity(999999.99);
        assertEquals(999999.99, inv.getAvailableQuantity(), 0.001);
    }

    @Test
    public void setAvailableQuantity_fractionalValue() {
        Inventory inv = new Inventory();
        inv.setAvailableQuantity(0.333);
        assertEquals(0.333, inv.getAvailableQuantity(), 0.0001);
    }
}

