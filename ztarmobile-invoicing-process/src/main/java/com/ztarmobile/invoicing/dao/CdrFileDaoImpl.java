/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

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
 * @since 03/14/17
 */
@Repository
public class CdrFileDaoImpl extends AbstractJdbc implements CdrFileDao {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(CdrFileDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "cdrFileDaoSql")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */

    @Override
    public boolean isFileProcessed(String fileName) {
        String sql = sqlStatements.getProperty("select.count.cdr_file");

        Map<String, String> params = new HashMap<>();
        params.put("file_name", fileName);
        int total = this.getJdbc().queryForObject(sql, new MapSqlParameterSource(params), Integer.class);
        log.debug("Cdrs file exists: " + fileName + ", " + (total != 0));
        return total != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFileProcessed(String fileName, char type) {
        String sql = sqlStatements.getProperty("insert.cdr_file");

        Map<String, String> params = new HashMap<>();
        params.put("file_name", fileName);
        params.put("file_type", String.valueOf(type));
        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }
}
