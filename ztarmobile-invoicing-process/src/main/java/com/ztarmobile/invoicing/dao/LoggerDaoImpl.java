/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddDashFormat;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
import com.ztarmobile.invoicing.vo.LoggerCdrFileVo;
import com.ztarmobile.invoicing.vo.LoggerReportFileVo;
import com.ztarmobile.invoicing.vo.PhaseVo;

/**
 * Direct DAO Implementation.
 *
 * @author armandorivas
 * @since 03/14/17
 */
@Repository
public class LoggerDaoImpl extends AbstractJdbc implements LoggerDao {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(LoggerDaoImpl.class);
    /**
     * Status to indicate that the record was loaded successfully.
     */
    private static final char STATUS_PENDING = 'P';
    /**
     * Status to indicate that the record was loaded successfully.
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
    @Qualifier(value = "loggerDaoSql")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggerCdrFileVo getCdrFileProcessed(String fileName) {
        String sql = sqlStatements.getProperty("select.logger_cdr_file");

        Map<String, String> params = new HashMap<>();
        params.put("file_name", fileName);

        List<LoggerCdrFileVo> list;
        list = this.getJdbc().query(sql, new MapSqlParameterSource(params), new RowMapper<LoggerCdrFileVo>() {
            @Override
            public LoggerCdrFileVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                LoggerCdrFileVo vo = new LoggerCdrFileVo();
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
    public void saveOrUpdateCdrFileProcessed(String sourceFileName, String targetFileName, char type) {
        // call the overloaded version
        this.saveOrUpdateCdrFileProcessed(sourceFileName, targetFileName, type, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdateCdrFileProcessed(String sourceFileName, String targetFileName, char type,
            String errorDescription) {
        log.debug("Saving record for this file: " + sourceFileName);
        char status = errorDescription == null ? STATUS_COMPLETED : STATUS_ERROR;
        String sql = sqlStatements.getProperty("insert.logger_cdr_file");

        Map<String, String> params = new HashMap<>();
        params.put("source_file_name", sourceFileName);
        params.put("target_file_name", targetFileName);
        params.put("file_type", String.valueOf(type));
        params.put("status", String.valueOf(status));
        params.put("error_description", errorDescription);

        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggerReportFileVo getReportFileProcessed(String product, Date reportDate) {
        String sql = sqlStatements.getProperty("select.logger_report_file");

        Map<String, String> params = new HashMap<>();
        params.put("product", product);
        params.put("report_date", fromDateToYYYYmmddDashFormat(reportDate));

        List<LoggerReportFileVo> list;
        list = this.getJdbc().query(sql, new MapSqlParameterSource(params), new RowMapper<LoggerReportFileVo>() {
            @Override
            public LoggerReportFileVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                LoggerReportFileVo vo = new LoggerReportFileVo();
                int rcnt = 0;
                vo.setRowId(rs.getLong(++rcnt));
                String status = rs.getString(++rcnt);
                if (!status.isEmpty()) {
                    vo.setStatusAllocations(status.charAt(0));
                }
                status = rs.getString(++rcnt);
                if (!status.isEmpty()) {
                    vo.setStatusUsage(status.charAt(0));
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
    public void saveOrUpdateReportFileProcessed(String product, Date reportDate, PhaseVo phase, boolean byMonth) {
        // call the overloaded version
        this.saveOrUpdateReportFileProcessed(product, reportDate, phase, byMonth, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdateReportFileProcessed(String product, Date reportDate, PhaseVo phase, boolean byMonth,
            String errorDescription) {
        log.debug("Saving record for this date: " + reportDate);
        char status = errorDescription == null ? STATUS_COMPLETED : STATUS_ERROR;
        String sql = byMonth ? sqlStatements.getProperty("update.logger_report_file")
                : sqlStatements.getProperty("insert.logger_report_file");

        Map<String, String> params = new HashMap<>();
        params.put("product", product);
        params.put("report_date", fromDateToYYYYmmddDashFormat(reportDate));

        switch (phase) {
        case ALLOCATIONS:
            params.put("status_allocations", String.valueOf(status));
            params.put("status_usage", String.valueOf(STATUS_PENDING));
            break;
        case USAGE:
            params.put("status_allocations", String.valueOf(STATUS_COMPLETED));
            params.put("status_usage", String.valueOf(status));
            break;
        }
        params.put("error_description", errorDescription);

        if (byMonth && phase == PhaseVo.USAGE) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reportDate);

            params.put("month_report_date", String.valueOf(calendar.get(MONTH) + 1));
            params.put("year_report_date", String.valueOf(calendar.get(YEAR)));
        }

        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }

}
