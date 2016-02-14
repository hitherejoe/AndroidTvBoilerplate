package com.hitherejoe.androidtvboilerplate.test.common;

import com.hitherejoe.androidtvboilerplate.data.model.Cat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestDataFactory {

    public static String generateRandomString() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    public static Cat makeCat(String unique) {
        Cat cat = new Cat();
        cat.name = "Name " + unique;
        cat.description = "Description " + unique;
        cat.imageUrl = generateRandomString();
        return cat;
    }

    public static List<Cat> makeCats(int count) {
        List<Cat> cats = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cats.add(makeCat(String.valueOf(i)));
        }
        return cats;
    }

}
