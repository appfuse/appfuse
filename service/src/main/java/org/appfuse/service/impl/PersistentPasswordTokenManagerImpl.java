/**
 * 
 */
package org.appfuse.service.impl;

import javax.sql.DataSource;

import org.appfuse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Provides {@link PasswordTokenManager} functionality persisting tokens to the db as an extra security check.
 * 
 * You will need to create a db table with the following structure:
 * 
 * <pre>
 * <code>
 * create table password_reset_token (
 *     username varchar(50) NOT NULL,
 *     token varchar(255) NOT NULL,
 *     PRIMARY KEY (username, token)
 * )
 * </code>
 * </pre>
 * 
 * and configure this alternative PasswordTokenManager in the spring BeanFactory.
 * 
 * @author ivangsa
 */
public class PersistentPasswordTokenManagerImpl extends PasswordTokenManagerImpl implements PasswordTokenManager {

    private JdbcTemplate jdbcTemplate;

    private String deleteTokenSql = "delete from password_reset_token where username=?";
    private String insertTokenSql = "insert into password_reset_token (username, token) values (?, ?)";
    private String selectTokenSql = "select count(token) from password_reset_token where username=? and token=?";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setDeleteTokenSql(String deleteTokenSql) {
        this.deleteTokenSql = deleteTokenSql;
    }

    public void setInsertTokenSql(String insertTokenSql) {
        this.insertTokenSql = insertTokenSql;
    }

    public void setSelectTokenSql(String selectTokenSql) {
        this.selectTokenSql = selectTokenSql;
    }

    /**
     * @see org.appfuse.service.impl.PasswordTokenManager#generateRecoveryToken(org.appfuse.model.User)
     */
    @Override
    public String generateRecoveryToken(final User user) {
        String token = super.generateRecoveryToken(user);
        persistToken(user, token);
        return token;
    }

    /**
     * @see org.appfuse.service.impl.PasswordTokenManager#isRecoveryTokenValid(org.appfuse.model.User, java.lang.String)
     */
    @Override
    public boolean isRecoveryTokenValid(final User user, final String token) {
        boolean isValid = super.isRecoveryTokenValid(user, token);
        return isValid && isRecoveryTokenPersisted(user, token);
    }

    /**
     * 
     * @see org.appfuse.service.impl.PasswordTokenManager#invalidateRecoveryToken(User, String)
     */
    @Override
    public void invalidateRecoveryToken(User user, String token) {
        jdbcTemplate.update(deleteTokenSql, user.getUsername());
    }

    protected void persistToken(User user, String token) {
        jdbcTemplate.update(deleteTokenSql, user.getUsername());
        jdbcTemplate.update(insertTokenSql, user.getUsername(), token);
    }

    protected boolean isRecoveryTokenPersisted(final User user, final String token) {
        Number count = jdbcTemplate.queryForObject(
                selectTokenSql,
                new Object[] { user.getUsername(), token }, Integer.class);
        return count != null && count.intValue() == 1;
    }

}
