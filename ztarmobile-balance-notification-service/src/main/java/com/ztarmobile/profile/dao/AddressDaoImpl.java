package com.ztarmobile.profile.dao;

import com.ztarmobile.profile.common.AbstractJdbc;
import com.ztarmobile.profile.model.Address;
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
public class AddressDaoImpl extends AbstractJdbc implements AddressDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AddressDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "addressDao")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public Address save(Address address) {
        log.debug("Creating a new Address");
        String sql = sqlStatements.getProperty("insert.address");

        Map<String, String> params = new HashMap<>();
        params.put("name", address.getName());
        params.put("line1", address.getLine1());
        params.put("line2", address.getLine2());
        params.put("line3", address.getLine3());
        params.put("city", address.getCity());
        params.put("state", address.getState());
        params.put("zip", address.getZip());
        params.put("country", address.getCountry());

        boolean isPrimary = address.getPrimary() == null ? false : address.getPrimary();
        params.put("is_primary", String.valueOf(isPrimary ? 1 : 0));
        params.put("user_profile_id", String.valueOf(address.getUserProfile().getId()));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbc().update(sql, new MapSqlParameterSource(params), keyHolder, new String[] { "row_id" });
        address.setId(keyHolder.getKey().longValue());
        return address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Address> findByUserProfile(UserProfile userProfile) {
        log.debug("Getting a list of addresses for a single profile");
        String sql = sqlStatements.getProperty("select.addresses.by.balance-notification");

        Map<String, String> params = new HashMap<>();
        params.put("user_profile_id", String.valueOf(userProfile.getId()));

        return this.getJdbc().query(sql, params, new AddressMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address findOne(long addressId) {
        log.debug("Getting an address");
        String sql = sqlStatements.getProperty("select.address");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(addressId));

        List<Address> list = this.getJdbc().query(sql, params, new AddressMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address findOneWithUserProfile(long addressId) {
        log.debug("Getting an addresses with user profile");
        String sql = sqlStatements.getProperty("select.address.with.balance-notification");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(addressId));

        List<Address> list = this.getJdbc().query(sql, params, new AddressMapper(true));
        return list.isEmpty() ? null : list.get(0);
    }

    class AddressMapper implements RowMapper<Address> {
        private boolean includeUserProfile;

        public AddressMapper() {
            this.includeUserProfile = false;
        }

        public AddressMapper(boolean includeUserProfile) {
            this.includeUserProfile = includeUserProfile;
        }

        @Override
        public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
            Address address = new Address();
            address.setId(rs.getLong("row_id"));
            address.setName(rs.getString("name"));
            address.setLine1(rs.getString("line1"));
            address.setLine2(rs.getString("line2"));
            address.setLine3(rs.getString("line3"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setZip(rs.getString("zip"));
            address.setCountry(rs.getString("country"));
            address.setPrimary(rs.getBoolean("is_primary"));
            if (this.includeUserProfile) {
                // we add user profile details.
                UserProfile userProfile = new UserProfile();
                userProfile.setId(rs.getLong("user_profile_id"));
                // later, we can add more columns into the user profile object.
                address.setUserProfile(userProfile);
            }
            return address;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Address address) {
        log.debug("Updating existing Address");
        String sql = sqlStatements.getProperty("update.address");

        Map<String, String> params = new HashMap<>();
        params.put("name", address.getName());
        params.put("line1", address.getLine1());
        params.put("line2", address.getLine2());
        params.put("line3", address.getLine3());
        params.put("city", address.getCity());
        params.put("state", address.getState());
        params.put("zip", address.getZip());
        params.put("country", address.getCountry());

        boolean isPrimary = address.getPrimary() == null ? false : address.getPrimary();
        params.put("is_primary", String.valueOf(isPrimary ? 1 : 0));
        params.put("row_id", String.valueOf(address.getId()));

        this.getJdbc().update(sql, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Address addressSaved) {
        log.debug("Deleting existing Address");
        String sql = sqlStatements.getProperty("delete.address");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(addressSaved.getId()));

        this.getJdbc().update(sql, params);
    }

}
