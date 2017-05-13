/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddDashFormat;

import com.ztarmobile.invoicing.common.AbstractJdbc;
import com.ztarmobile.invoicing.model.ReportDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
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

/**
 * Direct DAO Implementation.
 *
 * @author armandorivas
 * @since 03/13/17
 */
@Repository
public class InvoicingDaoImpl extends AbstractJdbc implements InvoicingDao {
    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(InvoicingDaoImpl.class);

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
        LOG.debug("Cleaning up invoicing data from " + start + " - " + end);

        String sql = sqlStatements.getProperty("delete.invoicing_report_details");

        Map<String, String> params = createParameters(start, end, product);
        int rowAffected = this.getJdbc().update(sql, new MapSqlParameterSource(params));
        LOG.debug("Rows deleted: " + rowAffected);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveInvoicing(Date start, Date end, String product) {
        LOG.debug("Saving invoicing details from " + start + " to " + end);

        String sql = sqlStatements.getProperty("select.insert.invoicing_report_details");

        Map<String, String> params = createParameters(start, end, product);
        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReportDetails> generateReport(Date start, Date end, String product) {
        String sql = sqlStatements.getProperty("select.invoicing_report_details");

        Map<String, String> params = createParameters(start, end, product);
        return this.getJdbc().query(sql, new MapSqlParameterSource(params), new RowMapper<ReportDetails>() {
            @Override
            public ReportDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                ReportDetails vo = new ReportDetails();
                int rcnt = 0;
                vo.setRowId(rs.getLong(++rcnt));
                vo.setYear(rs.getInt(++rcnt));
                vo.setMonth(rs.getInt(++rcnt));
                vo.setMdn(rs.getString(++rcnt));
                vo.setRatePlan(rs.getString(++rcnt));
                vo.setDayOnPlans(rs.getInt(++rcnt));
                vo.setMou(rs.getDouble(++rcnt));
                vo.setMbs(rs.getDouble(++rcnt));
                vo.setSms(rs.getDouble(++rcnt));
                vo.setMms(rs.getDouble(++rcnt));

                return vo;
            }
        });
    }

    /**
     * Creates the parameters to be executed in the database.
     * 
     * @return
     */
    private Map<String, String> createParameters(Date start, Date end, String product) {
        Map<String, String> params = new HashMap<>();
        params.put("init_date", fromDateToYYYYmmddDashFormat(start));
        params.put("end_date", fromDateToYYYYmmddDashFormat(end));
        params.put("product", product);

        return params;
    }
}
