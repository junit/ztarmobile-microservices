package com.ztarmobile.profile.dao;

import com.ztarmobile.profile.common.AbstractJdbc;
import com.ztarmobile.profile.model.Address;
import com.ztarmobile.profile.model.PaymentProfile;
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
public class PaymentProfileDaoImpl extends AbstractJdbc implements PaymentProfileDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentProfileDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "paymentProfileDao")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentProfile save(PaymentProfile paymentProfile) {
        log.debug("Creating a new PaymentProfile");
        String sql = sqlStatements.getProperty("insert.payment-profile");

        Map<String, String> params = new HashMap<>();
        params.put("alias", paymentProfile.getAlias());
        params.put("is_primary", String.valueOf(paymentProfile.isPrimary() ? 1 : 0));
        params.put("exp_date", paymentProfile.getExpirationDate());
        params.put("profile_key", paymentProfile.getProfileKey());
        params.put("user_profile_id", String.valueOf(paymentProfile.getUserProfile().getId()));
        params.put("address_id", String.valueOf(paymentProfile.getAddress().getId()));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbc().update(sql, new MapSqlParameterSource(params), keyHolder, new String[] { "row_id" });
        paymentProfile.setId(keyHolder.getKey().longValue());
        return paymentProfile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PaymentProfile> findByUserProfile(UserProfile userProfile) {
        log.debug("Getting a list of payment profiles for a single profile");
        String sql = sqlStatements.getProperty("select.payment-profile.by.user-profile");

        Map<String, String> params = new HashMap<>();
        params.put("user_profile_id", String.valueOf(userProfile.getId()));

        return this.getJdbc().query(sql, params, new PaymentProfileMapper(false, true));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentProfile findOne(long paymentProfileId) {
        log.debug("Getting an paymentProfile");
        String sql = sqlStatements.getProperty("select.payment-profile");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(paymentProfileId));

        List<PaymentProfile> list = this.getJdbc().query(sql, params, new PaymentProfileMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentProfile findOneWithUserProfile(long paymentProfileId) {
        log.debug("Getting an paymentProfile with user profile");
        String sql = sqlStatements.getProperty("select.payment-profile.with.user-profile");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(paymentProfileId));

        List<PaymentProfile> list = this.getJdbc().query(sql, params, new PaymentProfileMapper(true, false));
        return list.isEmpty() ? null : list.get(0);
    }

    class PaymentProfileMapper implements RowMapper<PaymentProfile> {
        private boolean includeUserProfile;
        private boolean includeAddress;

        public PaymentProfileMapper() {
            this.includeUserProfile = false;
            this.includeAddress = false;
        }

        public PaymentProfileMapper(boolean includeUserProfile, boolean includeAddress) {
            this.includeUserProfile = includeUserProfile;
            this.includeAddress = includeAddress;
        }

        @Override
        public PaymentProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
            PaymentProfile paymentProfile = new PaymentProfile();
            paymentProfile.setId(rs.getLong("row_id"));
            paymentProfile.setAlias(rs.getString("alias"));
            paymentProfile.setPrimary(rs.getBoolean("is_primary"));
            paymentProfile.setExpirationDate(rs.getString("exp_date"));
            paymentProfile.setProfileKey(rs.getString("profile_key"));
            if (this.includeUserProfile) {
                // we add user profile details.
                UserProfile userProfile = new UserProfile();
                userProfile.setId(rs.getLong("user_profile_id"));
                // later, we can add more columns into the user profile object.
                paymentProfile.setUserProfile(userProfile);
            }
            if (this.includeAddress) {
                // we add the address details.
                String addressId = rs.getString("address_id");
                if (addressId != null) {
                    Address address = new Address();
                    address.setId(Long.parseLong(addressId));
                    // later, we can add more columns into the address object.
                    paymentProfile.setAddress(address);
                }
            }
            return paymentProfile;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PaymentProfile paymentProfile) {
        log.debug("Updating existing PaymentProfile");
        String sql = sqlStatements.getProperty("update.payment-profile");

        Map<String, String> params = new HashMap<>();
        params.put("alias", paymentProfile.getAlias());
        params.put("is_primary", String.valueOf(paymentProfile.isPrimary() ? 1 : 0));
        params.put("exp_date", paymentProfile.getExpirationDate());
        params.put("profile_key", paymentProfile.getProfileKey());
        params.put("row_id", String.valueOf(paymentProfile.getId()));

        this.getJdbc().update(sql, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAll(PaymentProfile paymentProfile) {
        log.debug("Updating all existing PaymentProfile");
        String sql = sqlStatements.getProperty("update.all.payment-profile");

        Map<String, String> params = new HashMap<>();
        params.put("alias", paymentProfile.getAlias());
        params.put("is_primary", String.valueOf(paymentProfile.isPrimary() ? 1 : 0));
        params.put("exp_date", paymentProfile.getExpirationDate());
        params.put("profile_key", paymentProfile.getProfileKey());
        params.put("user_profile_id", String.valueOf(paymentProfile.getUserProfile().getId()));
        params.put("address_id", String.valueOf(paymentProfile.getAddress().getId()));
        params.put("row_id", String.valueOf(paymentProfile.getId()));

        this.getJdbc().update(sql, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PaymentProfile paymentProfileSaved) {
        log.debug("Deleting existing PaymentProfile");
        String sql = sqlStatements.getProperty("delete.payment-profile");

        Map<String, String> params = new HashMap<>();
        params.put("row_id", String.valueOf(paymentProfileSaved.getId()));

        this.getJdbc().update(sql, params);
    }

}
