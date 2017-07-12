/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.dao;

import com.ztarmobile.notification.common.AbstractJdbc;
import com.ztarmobile.notification.model.SubscriberUsage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * Performs operations over the subscriber usage data.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Repository
public class CustomerUsageDaoImpl extends AbstractJdbc implements SubscriberUsageDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(CustomerUsageDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "subscriberUsageDao")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SubscriberUsage> findUsageByBundle(String bundleId) {
        log.debug("Retrieving subscribers...");
        String sql = sqlStatements.getProperty("select.subscriber_usage");

        Map<String, String> params = new HashMap<>();
        params.put("bundle_id", bundleId);
        return this.getJdbc().query(sql, new MapSqlParameterSource(params), new SubscriberUsageRowMapper());
    }

    class SubscriberUsageRowMapper implements RowMapper<SubscriberUsage> {
        /**
         * {@inheritDoc}
         */
        @Override
        public SubscriberUsage mapRow(ResultSet rs, int rowNum) throws SQLException {
            SubscriberUsage vo = new SubscriberUsage();
            vo.setMdn(rs.getString("mdn"));
            vo.setServiceClass(rs.getString("service_class"));
            return vo;
        }
    }
}
