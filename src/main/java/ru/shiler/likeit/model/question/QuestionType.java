package ru.shiler.likeit.model.question;

import ru.shiler.likeit.database.dao.Identified;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class QuestionType implements Identified<Integer> {

    private int id;
    private String name;
    private String nameRu;

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    @Override
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
        return "QuestionType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
