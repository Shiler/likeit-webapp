package ru.shiler.likeit.database.dao;

import java.io.Serializable;

/**
 * Created by Evgeny Yushkevich on 23.11.2016.
 */
public interface Identified<PK extends Serializable> {
    PK getId();
}
