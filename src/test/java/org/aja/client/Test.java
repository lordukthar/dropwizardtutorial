package org.aja.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {

    public static void main(String...args){
        new Test().init();
    }

    private void init(){
        List<String> list = Arrays.asList("Adam", "Delta", "Bravo", "Charlie");

        Collections.sort(list, String::compareTo);

        System.out.println(list);
    }
}
