package com.me4502.supermart.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.me4502.supermart.SuperMartApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StoreTest {

    @Before
    public void setupApplication() {
        new SuperMartApplication();
        new StoreImpl("Test Store");
    }

    @After
    public void closeApplication() {
        SuperMartApplication.getInstance().close();
        ((StoreImpl) StoreImpl.getInstance()).close();
    }

    @Test
    public void testGetStore() {
        Store instance = StoreImpl.getInstance();
        assertNotNull(instance);
    }

    @Test(expected = IllegalStateException.class)
    public void testRecreateStoreFails() {
        new StoreImpl("Test Store");
    }

    @Test
    public void testRecreateStoreWorksAfterClose() {
        ((StoreImpl) StoreImpl.getInstance()).close();
        new StoreImpl("Test Store");
    }

    @Test
    public void testGetName() {
        assertEquals("Test Store", StoreImpl.getInstance().getName());
    }

    @Test
    public void testCapital() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(1.0);
        // Check with a delta of 0.01 due to dollars and cents
        assertEquals(1.0, instance.getCapital(), 0.01);
    }

    @Test
    public void testDefaultCapital() {
        Store instance = StoreImpl.getInstance();
        // Check with a delta of 0.01 due to dollars and cents
        assertEquals(100000, instance.getCapital(), 0.01);
    }

    @Test
    public void testDefaultCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        assertEquals("$100,000.00", instance.getFormattedCapital());
    }

    @Test
    public void testSimpleCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(100);
        assertEquals("$100.00", instance.getFormattedCapital());
    }

    @Test
    public void testCentsCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(0.17);
        assertEquals("$0.17", instance.getFormattedCapital());
    }

    @Test
    public void testCentsOverflowCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(0.4502);
        assertEquals("$0.45", instance.getFormattedCapital());
    }

    @Test
    public void testCentsOverflowCapitalRoundFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(0.505);
        assertEquals("$0.51", instance.getFormattedCapital());
    }

    @Test
    public void testNegativeCapitalFormat() {
        Store instance = StoreImpl.getInstance();
        instance.setCapital(-10);
        assertEquals("-$10.00", instance.getFormattedCapital());
    }

    @Test
    public void testDefaultEmptyStock() {
        Store instance = StoreImpl.getInstance();
        assertTrue(instance.getInventory().getStockedItems().isEmpty());
    }

    @Test
    public void testSettingStock() {
        Store instance = StoreImpl.getInstance();
        Stock mockStock = mock(Stock.class);
        instance.setInventory(mockStock);
        // Simple reference equality check - it shouldn't change the object
        assertEquals(mockStock, instance.getInventory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullStockFails() {
        Store instance = StoreImpl.getInstance();
        instance.setInventory(null);
    }

    @Test
    public void testSettingStockIsCorrect() {
        Store instance = StoreImpl.getInstance();
        Stock mockStock = mock(Stock.class);
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test");
        when(mockStock.getStockedItems()).thenReturn(ImmutableSet.of(mockItem));
        instance.setInventory(mockStock);
        assertTrue(instance.getInventory().getStockedItems().stream()
                .anyMatch(item -> item.getName().equals("Test")));
    }

    @Test
    public void testAddingItemsWork() {
        Store instance = StoreImpl.getInstance();
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test Item");
        instance.addItem(mockItem);
        assertEquals("Test Item", instance.getItem("Test Item").map(Item::getName).orElse("Empty"));
    }

    @Test
    public void testAddingDuplicateItemsDontAdd() {
        Store instance = StoreImpl.getInstance();
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test Item");
        instance.addItem(mockItem);
        instance.addItem(mockItem);
        assertEquals(1, instance.getItems().size());
    }

    @Test
    public void testGettingOtherItemIsEmpty() {
        Store instance = StoreImpl.getInstance();
        assertFalse(instance.getItem("Empty").isPresent());
    }

    @Test
    public void testGetItemsSet() {
        Store instance = StoreImpl.getInstance();
        assertEquals(0, instance.getItems().size());
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Test Item");
        instance.addItem(mockItem);
        assertEquals(1, instance.getItems().size());
    }
}
