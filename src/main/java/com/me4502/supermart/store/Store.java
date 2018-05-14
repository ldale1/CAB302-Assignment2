package com.me4502.supermart.store;

/**
 * Represents a store. This class is a singleton.
 *
 * @author Matthew Miller
 */
public interface Store {

    /**
     * Gets the name of the store.
     *
     * @return The name of the store
     */
    String getName();

    /**
     * Gets the current capital of the store.
     *
     * @return The capital of the store
     */
    double getCapital();

    /**
     * Sets the capital of the store.
     *
     * @param capital The new capital
     */
    void setCapital(double capital);

    /**
     * Gets the current inventory of the store.
     *
     * @return The inventory of the store
     */
    Stock getInventory();
}