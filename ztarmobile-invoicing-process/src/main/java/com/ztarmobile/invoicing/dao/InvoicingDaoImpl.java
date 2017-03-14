/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddDashFormat;

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
 * @since 03/13/17
 */
@Repository
public class InvoicingDaoImpl extends AbstractJdbc implements InvoicingDao {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(InvoicingDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "invoicingDaoSql")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanUpInvoicing(Date start, Date end, String product) {
        log.debug("Cleaning up invoicing data...");

        String sql = sqlStatements.getProperty("delete.invocing_details");

        Map<String, String> params = new HashMap<>();
        params.put("init_date", fromDateToYYYYmmddDashFormat(start));
        params.put("end_date", fromDateToYYYYmmddDashFormat(end));
        params.put("product", product);

        int rowAffected = this.getJdbc().update(sql, new MapSqlParameterSource(params));
        log.debug("Rows deleted: " + rowAffected);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveInvoicing(Date start, Date end, String product) {
        log.debug("Saving invoicing details...");

        String sql = sqlStatements.getProperty("select.insert.invocing_details");

        Map<String, String> params = new HashMap<>();
        params.put("init_date", fromDateToYYYYmmddDashFormat(start));
        params.put("end_date", fromDateToYYYYmmddDashFormat(end));
        params.put("product", product);

        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }

}