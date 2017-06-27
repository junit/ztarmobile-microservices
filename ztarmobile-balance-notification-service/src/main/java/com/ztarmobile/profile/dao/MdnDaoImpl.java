package com.ztarmobile.profile.dao;

import com.ztarmobile.profile.common.AbstractJdbc;
import com.ztarmobile.profile.model.Mdn;
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
public class MdnDaoImpl extends AbstractJdbc implements MdnDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(MdnDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "mdnDao")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mdn save(Mdn mdn) {
        log.debug("Creating a new Mdn");
        String sql = sqlStatements.getProperty("insert.mdn");

        Map<String, String> params = new HashMap<>();
        params.put("phone_number", mdn.getPhoneNumber());
        params.put("user_profile_id", String.valueOf(mdn.getUserProfile().getId()));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbc().update(sql, new MapSqlParameterSource(params), keyHolder, new String[] { "row_id" });
        mdn.setId(keyHolder.getKey().longValue());
        return mdn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mdn> findByUserProfile(UserProfile userProfile) {
        log.debug("Getting a list of mdns for a single profile");
        String sql = sqlStatements.getProperty("select.mdns.by.balance-notification");

        Map<String, String> params = new HashMap<>();
        params.put("user_profile_id", String.valueOf(userProfile.getId()));

        return this.getJdbc().query(sql, params, new RowMapper<Mdn>() {
            @Override
            public Mdn mapRow(ResultSet rs, int rowNum) throws SQLException {
                Mdn mdn = new Mdn();
                mdn.setId(rs.getLong("row_id"));
                mdn.setPhoneNumber(rs.getString("phone_number"));
                return mdn;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mdn findOne(long mdnId) {
        log.debug("Getting a mdn");
        String sql = sqlStatements.getProperty("select.mdn");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(mdnId));

        List<Mdn> list = this.getJdbc().query(sql, params, new MdnMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    class MdnMapper implements RowMapper<Mdn> {
        @Override
        public Mdn mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mdn mdn = new Mdn();
            mdn.setId(rs.getLong("row_id"));
            mdn.setPhoneNumber(rs.getString("phone_number"));
            return mdn;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Mdn mdn) {
        log.debug("Updating existing Mdn");
        String sql = sqlStatements.getProperty("update.mdn");

        Map<String, String> params = new HashMap<>();
        params.put("phone_number", mdn.getPhoneNumber());
        params.put("row_id", String.valueOf(mdn.getId()));

        this.getJdbc().update(sql, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Mdn mdnSaved) {
        log.debug("Deleting existing Mdns");
        String sql = sqlStatements.getProperty("delete.mdn");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(mdnSaved.getId()));

        this.getJdbc().update(sql, params);
    }
}
