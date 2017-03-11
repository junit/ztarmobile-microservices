/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToDbFormat;

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
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.ztarmobile.invoicing.common.AbstractJdbc;
import com.ztarmobile.invoicing.vo.ResellerSubsUsageVo;

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
        log.debug("Creating allocations...");
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
        String sql = sqlStatements.getProperty("update.reseller_subs_usage.indicators");

        Map<String, String> params = new HashMap<>();
        this.getJdbc().update(sql, new MapSqlParameterSource(params));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResellerSubsUsageVo> getResellerSubsUsage(Date startDate, Date endDate, String product) {
        log.debug("Getting reseller subsUsage...");
        String sql = sqlStatements.getProperty("select.reseller_subs_usage");

        Map<String, String> params = new HashMap<>();
        params.put("reseller", product);
        params.put("start_dt", fromDateToDbFormat(startDate));
        params.put("end_dt", fromDateToDbFormat(endDate));

        return this.getJdbc().query(sql, new MapSqlParameterSource(params), new RowMapper<ResellerSubsUsageVo>() {
            @Override
            public ResellerSubsUsageVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                ResellerSubsUsageVo vo = new ResellerSubsUsageVo();
                int rcnt = 0;
                vo.setRowId(rs.getLong(++rcnt));
                vo.setCallDate(new Date(rs.getDate(++rcnt).getTime()));
                vo.setRatePlan(rs.getString(++rcnt));
                vo.setMdn(rs.getString(++rcnt));
                vo.setAllocMou(rs.getFloat(++rcnt));
                vo.setAllocSms(rs.getFloat(++rcnt));
                vo.setAllocMms(rs.getFloat(++rcnt));
                vo.setAllocMbs(rs.getFloat(++rcnt));
                vo.setActualMou(rs.getFloat(++rcnt));
                vo.setActualSms(rs.getFloat(++rcnt));
                vo.setActualMms(rs.getFloat(++rcnt));
                vo.setActualKbs(rs.getFloat(++rcnt));
                vo.setDurationStart(new Date(rs.getTimestamp(++rcnt).getTime()));
                vo.setDurationEnd(new Date(rs.getTimestamp(++rcnt).getTime()));
                vo.setNewAddInd(rs.getInt(++rcnt));
                vo.setRenewalInd(rs.getInt(++rcnt));
                vo.setLstUpdDate(new Date(rs.getTimestamp(++rcnt).getTime()));
                return vo;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] updateResellerSubsUsage(List<ResellerSubsUsageVo> subscribers) {
        log.debug("Updating " + subscribers.size() + " subscriber usage...");
        String sql = sqlStatements.getProperty("update.reseller_subs_usage");

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(subscribers.toArray());
        return this.getJdbc().batchUpdate(sql, batch);
    }
}
