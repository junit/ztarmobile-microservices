/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddDashFormat;

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

import com.ztarmobile.invoicing.common.AbstractJdbc;
import com.ztarmobile.invoicing.vo.ReportDetailsVo;

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

        Map<String, String> params = new HashMap<>();
        params.put("init_date", fromDateToYYYYmmddDashFormat(start));
        params.put("end_date", fromDateToYYYYmmddDashFormat(end));
        params.put("product", product);

        int rowAffected = this.getJdbc().update(sql, new MapSqlParameterSource(params));
        LOG.debug("Rows deleted: " + rowAffected);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveInvoicing(Date start, Date end, String product) {
        LOG.debug("Saving invoicing details from " + start + " - " + end);

        String sql = sqlStatements.getProperty("select.insert.invoicing_report_details");

        Map<String, String> params = new HashMap<>();
        params.put("init_date", fromDateToYYYYmmddDashFormat(start));
        params.put("end_date", fromDateToYYYYmmddDashFormat(end));
        params.put("product", product);

        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReportDetailsVo> generateReport(String product, Date start, Date end) {
        String sql = sqlStatements.getProperty("select.invoicing_report_details");

        Map<String, String> params = new HashMap<>();
        params.put("product", product);
        params.put("init_date", fromDateToYYYYmmddDashFormat(start));
        params.put("end_date", fromDateToYYYYmmddDashFormat(end));

        return this.getJdbc().query(sql, new MapSqlParameterSource(params), new RowMapper<ReportDetailsVo>() {
            @Override
            public ReportDetailsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                ReportDetailsVo vo = new ReportDetailsVo();
                int rcnt = 0;
                vo.setRowId(rs.getLong(++rcnt));
                vo.setMdn(rs.getString(++rcnt));
                vo.setRatePlan(rs.getString(++rcnt));
                vo.setMou(rs.getDouble(++rcnt));
                vo.setMbs(rs.getDouble(++rcnt));
                vo.setSms(rs.getDouble(++rcnt));
                vo.setMms(rs.getDouble(++rcnt));

                return vo;
            }
        });
    }
}
