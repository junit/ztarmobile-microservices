/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToDbFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ztarmobile.invoicing.common.AbstractJdbc;

/**
 * Direct DAO Implementation.
 *
 * @author armandorivas
 * @since 03/01/17
 */
@Repository
public class ResellerAllocationsDaoImpl extends AbstractJdbc implements ResellerAllocationsDao {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(ResellerAllocationsDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "resellerAllocationsDaoSql")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAllocations(Date callDate, Date durationStart, Date durationEnd, String product) {
        String sql = sqlStatements.getProperty("select.insert.reseller_subs_usage");

        Map<String, String> params = new HashMap<>();
        params.put("call_dt", fromDateToDbFormat(callDate));
        params.put("duration_start", fromDateToDbFormat(durationStart));
        params.put("duration_end", fromDateToDbFormat(durationEnd));
        params.put("reseller", product);

        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAllocationIndicators() {
        log.debug("Updating status...");
        String sql = sqlStatements.getProperty("update.reseller_subs_usage");

        Map<String, String> params = new HashMap<>();
        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }

}
