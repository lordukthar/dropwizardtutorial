package org.aja.client;

import java.util.Arrays;
import java.util.List;

public class MapReduce {

    public static void main(String...args) {

    }
    private void init() {

        List<User> peoples = Arrays.asList(new User("Adam", 11, true),
                new User("Eve", 22, false),
                new User("Zorro", 33, true),
                new User("Toby", 44, true),
                new User("Susan", 54, false),
                new User("Nelly", 23, false),
                new User("Lula", 65, false),
                new User("Anna", 70, false));
    }

    public double oldFashionedAverageAge(List<? extends User> users) {
        int age = 0;

        for (User u : users) {
            age =+ u.getAge();
        }



        return 0;
    }

}


class User {
    private final String name;
    private final int age;
    private final boolean male;

    public User(String name, int age, boolean male) {
        this.name = name;
        this.age = age;
        this.male = male;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isMale() {
        return male;
    }
}