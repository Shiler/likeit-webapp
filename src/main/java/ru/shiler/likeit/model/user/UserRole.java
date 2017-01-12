package ru.shiler.likeit.model.user;

import ru.shiler.likeit.database.dao.Identified;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public class UserRole implements Identified<Integer> {
    private int id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
