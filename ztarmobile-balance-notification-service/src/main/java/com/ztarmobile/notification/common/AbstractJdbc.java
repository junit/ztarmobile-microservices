/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.notification.common;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private NamedParameterJdbcTemplate jdbcTemplateCdrs;
    private NamedParameterJdbcTemplate jdbcTemplateZtar;

    /**
     * Injects the dataSource.
     *
     * @param dataSource The dataSource.
     */
    @Autowired
    @Qualifier(value = "cdrsDataSource")
    public void setCdrsDataSource(DataSource dataSource) {
        this.jdbcTemplateCdrs = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Injects the dataSource.
     *
     * @param dataSource The dataSource.
     */
    @Autowired
    @Qualifier(value = "ztarDataSource")
    public void setZtarDataSource(DataSource dataSource) {
        this.jdbcTemplateZtar = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Gets the jdbcTemplate object.
     *
     * @return The JDBC template object.
     */
    protected NamedParameterJdbcTemplate getCdrsJdbc() {
        return jdbcTemplateCdrs;
    }

    /**
     * Gets the jdbcTemplate object.
     *
     * @return The JDBC template object.
     */
    protected NamedParameterJdbcTemplate getZtarJdbc() {
        return jdbcTemplateZtar;
    }
}
