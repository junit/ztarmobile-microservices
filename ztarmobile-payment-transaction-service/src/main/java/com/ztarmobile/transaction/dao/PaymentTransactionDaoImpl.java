/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.dao;

import static com.ztarmobile.common.DateUtils.fromDateToDbFormat;

import com.ztarmobile.transaction.common.AbstractJdbc;
import com.ztarmobile.transaction.model.SubscriberTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
 * Performs operations over the CC transactions for a given product.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Repository
public class PaymentTransactionDaoImpl extends AbstractJdbc implements PaymentTransactionDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentTransactionDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "paymentTransactionDao")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SubscriberTransaction> findCCTransactionsByProduct(Date start, Date end, String product) {
        log.debug("Retrieving CC transactions by product...");
        String sql = sqlStatements.getProperty("select.payment_details");

        Map<String, String> params = new HashMap<>();
        params.put("init_date", fromDateToDbFormat(start));
        params.put("end_date", fromDateToDbFormat(end));
        params.put("product", product);

        return this.getJdbc().query(sql, new MapSqlParameterSource(params), new SubscriberTransactionsRowMapper());
    }

    class SubscriberTransactionsRowMapper implements RowMapper<SubscriberTransaction> {
        /**
         * {@inheritDoc}
         */
        @Override
        public SubscriberTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            SubscriberTransaction vo = new SubscriberTransaction();
            vo.setMdn(rs.getString("mdn"));
            vo.setTotal(rs.getString("amount"));
            vo.setPaymentDate(rs.getString("payment_timestamp"));
            return vo;
        }
    }
}
