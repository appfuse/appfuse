package org.appfuse.dao;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.ibatis.GenericDaoiBatis;
import org.appfuse.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class GenericDaoTest extends BaseDaoTestCase {
    Log log = LogFactory.getLog(GenericDaoTest.class);
    GenericDao<User, Long> genericDao;
    @Autowired
    SqlMapClient sqlMapClient;

    @Before
    public void setUp() {
        genericDao = new GenericDaoiBatis<User, Long>(User.class, sqlMapClient);
    }

    @Test
    public void getUser() {
        User user = genericDao.get(-1L);
        assertNotNull(user);
        assertEquals("user", user.getUsername());
    }
}