/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.dao;

import com.ztarmobile.notification.common.AbstractJdbc;
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

        Map<String, String> params = new HashMap<>();
        params.put("data", customerBalance.getData());

        this.getZtarJdbc().update(sql, new MapSqlParameterSource(params));
    }
}
