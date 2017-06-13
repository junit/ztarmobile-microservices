package com.ztarmobile.profile.dao;

import com.ztarmobile.profile.common.AbstractJdbc;
import com.ztarmobile.profile.model.UserProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileDaoImpl extends AbstractJdbc implements UserProfileDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserProfileDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "userProfileDao")
    private Properties sqlStatements;

    @Override
    public UserProfile findOne(long userProfileId) {
        log.debug("Getting a user profile");
        String sql = sqlStatements.getProperty("select.user-profile");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(userProfileId));

        List<UserProfile> list = this.getJdbc().query(sql, params, new UserProfileRowMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserProfile findByEmail(UserProfile userProfile) {
        log.debug("Getting a user profile by email");
        String sql = sqlStatements.getProperty("select.user-profile.by.email");

        Map<String, String> params = new HashMap<>();
        params.put("email", String.valueOf(userProfile.getEmail()));

        List<UserProfile> list = this.getJdbc().query(sql, params, new UserProfileRowMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    class UserProfileRowMapper implements RowMapper<UserProfile> {
        /**
         * {@inheritDoc}
         */
        @Override
        public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserProfile vo = new UserProfile();
            vo.setId(rs.getLong("row_id"));
            vo.setFirstName(rs.getString("first_name"));
            vo.setLastName(rs.getString("last_name"));
            vo.setEmail(rs.getString("email"));
            vo.setPassword(rs.getString("password"));
            return vo;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserProfile save(UserProfile userProfile) {
        log.debug("Creating a new UserProfile");
        String sql = sqlStatements.getProperty("insert.user-profile");

        Map<String, String> params = new HashMap<>();
        params.put("email", userProfile.getEmail());
        params.put("first_name", userProfile.getFirstName());
        params.put("last_name", userProfile.getLastName());
        params.put("password", userProfile.getPassword());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbc().update(sql, new MapSqlParameterSource(params), keyHolder, new String[] { "row_id" });
        userProfile.setId(keyHolder.getKey().longValue());
        return userProfile;
    }

}
