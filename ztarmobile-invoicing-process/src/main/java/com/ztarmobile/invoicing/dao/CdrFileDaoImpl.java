/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ztarmobile.invoicing.common.AbstractJdbc;
import com.ztarmobile.invoicing.vo.CdrFileVo;

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
     * Status to indicate that the file was loaded sucessfully.
     */
    private static final char STATUS_COMPLETED = 'C';
    /**
     * Status to indicate that there was an error.
     */
    private static final char STATUS_ERROR = 'E';
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
    public CdrFileVo getFileProcessed(String fileName) {
        String sql = sqlStatements.getProperty("select.cdr_file");

        Map<String, String> params = new HashMap<>();
        params.put("file_name", fileName);

        List<CdrFileVo> list;
        list = this.getJdbc().query(sql, new MapSqlParameterSource(params), new RowMapper<CdrFileVo>() {
            @Override
            public CdrFileVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                CdrFileVo vo = new CdrFileVo();
                int rcnt = 0;
                vo.setRowId(rs.getLong(++rcnt));
                vo.setSourceFileName(rs.getString(++rcnt));
                vo.setTargetFileName(rs.getString(++rcnt));
                String status = rs.getString(++rcnt);
                if (!status.isEmpty()) {
                    vo.setStatus(status.charAt(0));
                }
                return vo;
            }
        });
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdateFileProcessed(String sourceFileName, String targetFileName, char type) {
        // call the overloaded version
        this.saveOrUpdateFileProcessed(sourceFileName, targetFileName, type, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdateFileProcessed(String sourceFileName, String targetFileName, char type,
            String errorDescription) {
        log.debug("Saving record for this file: " + sourceFileName);
        char status = errorDescription == null ? STATUS_COMPLETED : STATUS_ERROR;
        String sql = sqlStatements.getProperty("insert.cdr_file");

        Map<String, String> params = new HashMap<>();
        params.put("source_file_name", sourceFileName);
        params.put("target_file_name", targetFileName);
        params.put("file_type", String.valueOf(type));
        params.put("status", String.valueOf(status));
        params.put("error_description", errorDescription);
        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }
}
