package ru.shiler.likeit.database.dao.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.shiler.likeit.database.dao.GenericDao;
import ru.shiler.likeit.database.dao.Identified;
import ru.shiler.likeit.model.user.User;

import java.util.List;

/**
 * Created by Evgeny Yushkevich on 23.11.2016.
 */

@RunWith(Parameterized.class)
public abstract class GenericDaoTest<Context> {

    protected Class daoClass;
    protected Identified notPersistedDto;

    public abstract GenericDao dao();

    public abstract Context context();

    public GenericDaoTest(Class clazz, Identified<Integer> notPersistedDto) {
        this.daoClass = clazz;
        this.notPersistedDto = getTestUser();
    }

    @Test
    public void testPersist() throws Exception {
        notPersistedDto = dao().persist(getTestUser());

        Assert.assertNotNull("After persist id is null.", notPersistedDto.getId());
    }

    private User getTestUser() {
        User user = new User();
//        user.setUsername("test");
//        user.setEmail("test");
//        user.setPassword("test");
//        user.setRole(1);
//        user.setStatus(1);
//        user.setAge(1);
//        user.setName("test");
        return user;
    }


    @Test
    public void testGetByPK() throws Exception {
        Identified dto = dao().getByPK(1);
        Assert.assertNotNull(dto);
    }

    @Test
    public void testDelete() throws Exception {
        Assert.assertNotNull(notPersistedDto);

        List list = dao().getAll();
        Assert.assertNotNull(list);

        int oldSize = list.size();
        Assert.assertTrue(oldSize > 0);
        Identified dto = dao().getByPK(1);
        dao().delete(dto);

        list = dao().getAll();
        Assert.assertNotNull(list);
        int newSize = list.size();
        Assert.assertEquals(1, oldSize - newSize);
    }

    @Test
    public void testGetAll() throws Exception {
        List list = dao().getAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }
}
