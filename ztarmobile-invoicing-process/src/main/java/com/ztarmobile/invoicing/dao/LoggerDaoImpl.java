/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddDashFormat;
import static com.ztarmobile.invoicing.vo.LoggerStatusVo.COMPLETED;
import static com.ztarmobile.invoicing.vo.LoggerStatusVo.ERROR;
import static com.ztarmobile.invoicing.vo.LoggerStatusVo.PENDING;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ztarmobile.invoicing.common.AbstractJdbc;
import com.ztarmobile.invoicing.vo.LoggerCdrFileVo;
import com.ztarmobile.invoicing.vo.LoggerReportFileVo;
import com.ztarmobile.invoicing.vo.LoggerRequestVo;
import com.ztarmobile.invoicing.vo.LoggerStatusVo;
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
        LoggerStatusVo status = errorDescription == null ? COMPLETED : ERROR;
        String sql = sqlStatements.getProperty("insert.logger_cdr_file");

        Map<String, String> params = new HashMap<>();
        params.put("source_file_name", sourceFileName);
        params.put("target_file_name", targetFileName);
        params.put("file_type", String.valueOf(type));
        params.put("status", String.valueOf(status.getStatusVal()));
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
        LoggerStatusVo status = errorDescription == null ? COMPLETED : ERROR;
        String sql = byMonth ? sqlStatements.getProperty("update.logger_report_file")
                : sqlStatements.getProperty("insert.logger_report_file");

        Map<String, String> params = new HashMap<>();
        params.put("product", product);
        params.put("report_date", fromDateToYYYYmmddDashFormat(reportDate));

        switch (phase) {
        case ALLOCATIONS:
            params.put("status_allocations", String.valueOf(status.getStatusVal()));
            params.put("status_usage", String.valueOf(PENDING));
            break;
        case USAGE:
            params.put("status_allocations", String.valueOf(COMPLETED));
            params.put("status_usage", String.valueOf(status.getStatusVal()));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public long saveOrUpdateInvoiceProcessed(long rowId, String product, Date reportDateFrom, Date reportDateTo,
            long totalTime, LoggerStatusVo status) {
        // call the overloaded version
        return this.saveOrUpdateInvoiceProcessed(rowId, product, reportDateFrom, reportDateTo, totalTime, status, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long saveOrUpdateInvoiceProcessed(long rowId, String product, Date reportDateFrom, Date reportDateTo,
            long totalTime, LoggerStatusVo status, String errorDescription) {
        log.debug("Saving or updating record between: " + reportDateFrom + " - " + reportDateTo);

        boolean isInsert = rowId == 0;
        KeyHolder keyHolder = null;
        String sql = null;

        Map<String, String> params = new HashMap<>();
        params.put("product", product);
        params.put("report_date_from", fromDateToYYYYmmddDashFormat(reportDateFrom));
        params.put("report_date_to", fromDateToYYYYmmddDashFormat(reportDateTo));
        params.put("total_time", String.valueOf(totalTime));
        params.put("status", String.valueOf(status.getStatusVal()));
        params.put("error_description", errorDescription);

        if (isInsert) {
            // we return the generated id after the insertion is done.
            sql = sqlStatements.getProperty("insert.logger_request");
            keyHolder = new GeneratedKeyHolder();
            this.getJdbc().update(sql, new MapSqlParameterSource(params), keyHolder, new String[] { "row_id" });
            return keyHolder.getKey().longValue();
        } else {
            // we return the number of records affected by the update operation.
            params.put("row_id", String.valueOf(rowId));
            sql = sqlStatements.getProperty("update.logger_request");
            return this.getJdbc().update(sql, new MapSqlParameterSource(params));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoggerRequestVo> getInvoiceProcessed(int max) {
        String sql = sqlStatements.getProperty("select.logger_request");

        Map<String, Integer> params = new HashMap<>();
        params.put("max_records", max);

        return this.getJdbc().query(sql, new MapSqlParameterSource(params), new RowMapper<LoggerRequestVo>() {
            @Override
            public LoggerRequestVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                LoggerRequestVo vo = new LoggerRequestVo();
                int rcnt = 0;
                vo.setRowId(rs.getLong(++rcnt));
                vo.setProduct(rs.getString(++rcnt));
                vo.setFrom(rs.getDate(++rcnt));
                vo.setTo(rs.getDate(++rcnt));
                vo.setResponseTime(rs.getLong(++rcnt));
                String status = rs.getString(++rcnt);
                if (!status.isEmpty()) {
                    vo.setStatus(status.charAt(0));
                }
                vo.setAvailableReport(vo.getStatus() == COMPLETED.getStatusVal());
                vo.setErrorDescription(rs.getString(++rcnt));
                Date date = null;
                Timestamp timestamp = rs.getTimestamp(++rcnt);
                if (timestamp != null) {
                    date = new Date(timestamp.getTime());
                    vo.setRequestDate(date);
                }

                return vo;
            }
        });
    }

}
