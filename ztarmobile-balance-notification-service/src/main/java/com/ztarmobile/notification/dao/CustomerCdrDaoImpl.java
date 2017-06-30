/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.dao;

import com.ztarmobile.notification.common.AbstractJdbc;
import com.ztarmobile.notification.model.NotificationActity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Performs operations over the customer balance table.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Repository
public class CustomerCdrDaoImpl extends AbstractJdbc implements CustomerCdrDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(CustomerCdrDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "customerCdrDao")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationActity> fetchAllMdnActivityAir() {
        log.debug("Getting activity for air");
        String sql = sqlStatements.getProperty("select.activity-air");
        return this.getCdrsJdbc().query(sql, new NotificationActityMapper());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationActity> fetchAllMdnActivityData() {
        log.debug("Getting activity for data");
        String sql = sqlStatements.getProperty("select.activity-data");
        return this.getCdrsJdbc().query(sql, new NotificationActityMapper());
    }

    class NotificationActityMapper implements RowMapper<NotificationActity> {
        @Override
        public NotificationActity mapRow(ResultSet rs, int rowNum) throws SQLException {
            NotificationActity activity = new NotificationActity();
            activity.setMdn(rs.getString("mdn"));
            activity.setFileDate(rs.getDate("file_date"));
            return activity;
        }
    }
}
