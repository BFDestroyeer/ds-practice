package edu.bfdestroyeer.pizzeria.utils;

public class IdGenerator {
    Long currentId = 0L;

    public Long next() {
        if (currentId < Long.MAX_VALUE) {
            return currentId++;
        } else {
            return currentId = 0L;
        }
    }
}
