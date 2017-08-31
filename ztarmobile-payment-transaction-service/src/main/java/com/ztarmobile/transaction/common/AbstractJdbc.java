/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.common;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Parent class to handle jdbcTemplate operations, all the DAO classes must
 * extend this class.
 *
 * @author armandorivas
 * @since 03/01/17
 */
public abstract class AbstractJdbc {
    /**
     * The jdbcTemplate.
     */
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Injects the dataSource.
     * 
     * @param dataSource
     *            The dataSource.
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Gets the jdbcTemplate object.
     * 
     * @return The JDBC template object.
     */
    protected NamedParameterJdbcTemplate getJdbc() {
        return jdbcTemplate;
    }

    /**
     * Check the number of records affected.
     * 
     * @param affected
     *            The affected records.
     */
    protected void checkAffected(int affected) {
        if (affected != 1) {
            throw new IllegalStateException("More than one record was affected");
        }
    }
}
