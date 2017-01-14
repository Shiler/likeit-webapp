package ru.shiler.likeit.database.dao.impl;

import org.junit.Test;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.answer.MySqlAnswerDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.answer.Answer;

/**
 * Created by Evgeny Yushkevich on 14.01.2017.
 */
public class MySqlAnswerDaoTest {

    @Test
    public void testGetAnswerRating() throws PersistException {
        DaoFactory daoFactory = new MySqlDaoFactory();
        MySqlAnswerDao answerDao = (MySqlAnswerDao) daoFactory.getDao(daoFactory.getContext(), Answer.class);
        System.out.println(answerDao.getAnswerRating(2));
    }

}
