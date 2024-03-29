/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.dao;

import com.ztarmobile.notification.common.AbstractJdbc;
import com.ztarmobile.notification.model.Bucket;
import com.ztarmobile.notification.model.CustomerBalance;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * Performs operations over the customer balance table.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Repository
public class CustomerBalanceDaoImpl extends AbstractJdbc implements CustomerBalanceDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(CustomerBalanceDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "customerBalanceDao")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBalances(CustomerBalance customerBalance) {
        log.debug("Updating customer balances...");
        String sql = sqlStatements.getProperty("insert-update.customer-balances");

        String bundleId = customerBalance.getBundleRowId() == null ? null
                : String.valueOf(customerBalance.getBundleRowId());

        Map<String, String> params = new HashMap<>();
        params.put("mdn", customerBalance.getMdn());
        params.put("plan_billing_id", bundleId);
        params.put("data", customerBalance.getData());
        params.put("low_data", customerBalance.getLowData());
        params.put("high_data", customerBalance.getHighData());
        params.put("voice", customerBalance.getVoice());
        params.put("sms", customerBalance.getSms());
        params.put("mms", customerBalance.getMms());
        params.put("percentage_data", String.valueOf(customerBalance.getPercentageData()));
        params.put("percentage_voice", String.valueOf(customerBalance.getPercentageVoice()));
        params.put("percentage_sms", String.valueOf(customerBalance.getPercentageSms()));
        params.put("notified_data", customerBalance.isNotifiedData() ? "1" : "0");
        params.put("notified_voice", customerBalance.isNotifiedVoice() ? "1" : "0");
        params.put("notified_sms", customerBalance.isNotifiedSms() ? "1" : "0");

        params.put("status", customerBalance.getStatus().getValue());
        params.put("status_message", customerBalance.getStatusMessage());

        this.getZtarJdbc().update(sql, new MapSqlParameterSource(params));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countCustomerBalance(String mdn, CustomerBalance customerBalance, Bucket bucket) {
        String sql = sqlStatements.getProperty("count.customer-balances");

        String bundleId = customerBalance.getBundleRowId() == null ? null
                : String.valueOf(customerBalance.getBundleRowId());

        Map<String, String> params = new HashMap<>();
        params.put("mdn", customerBalance.getMdn());
        params.put("plan_billing_id", bundleId);
        String column = "";

        // we found those ones that was already sent
        switch (bucket) {
        case DATA:
            column = "notified_data = 1";
            break;
        case SMS:
            column = "notified_sms = 1";
            break;
        case VOICE:
            column = "notified_voice = 1";
            break;
        default:
            break;
        }
        sql += " AND " + column;

        int totalFound = this.getZtarJdbc().queryForObject(sql, new MapSqlParameterSource(params), Integer.class);
        return totalFound;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getModifiedData(String mdn, int bundleRowId) {
        int modifiedData = 0;
        String sql = sqlStatements.getProperty("modified-data.customer-balances");

        if (bundleRowId != 0) {
            Map<String, String> params = new HashMap<>();
            params.put("mdn", mdn);
            return this.getZtarJdbc().queryForObject(sql, new MapSqlParameterSource(params), Integer.class);
        }
        return modifiedData;
    }
}
