package ru.shiler.likeit.database.dao;

import java.io.Serializable;

/**
 * Used to identify models from database by PK
 *
 * @param <PK> PrimaryKey of the database's table
 */
public interface Identified<PK extends Serializable> {
    PK getId();
}
