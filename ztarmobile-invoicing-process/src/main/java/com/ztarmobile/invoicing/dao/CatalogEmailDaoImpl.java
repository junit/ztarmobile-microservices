/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ztarmobile.invoicing.common.AbstractJdbc;
import com.ztarmobile.invoicing.model.CatalogEmail;

/**
 * Direct DAO Implementation.
 *
 * @author armandorivas
 * @since 04/09/17
 */
@Repository
public class CatalogEmailDaoImpl extends AbstractJdbc implements CatalogEmailDao {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(CatalogEmailDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "catalogEmailDaoSql")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CatalogEmail> getCatalogEmail() {
        LOG.debug("Getting all the email catalogs");
        String sql = sqlStatements.getProperty("select.catalog_emails");

        return this.getJdbc().query(sql, new CatalogEmailRowMapper());
    }

    class CatalogEmailRowMapper implements RowMapper<CatalogEmail> {
        /**
         * {@inheritDoc}
         */
        @Override
        public CatalogEmail mapRow(ResultSet rs, int rowNum) throws SQLException {
            CatalogEmail vo = new CatalogEmail();
            int rcnt = 0;
            vo.setRowId(rs.getLong(++rcnt));
            vo.setEmail(rs.getString(++rcnt));
            vo.setFirstName(rs.getString(++rcnt));
            vo.setLastName(rs.getString(++rcnt));
            return vo;
        }
    }
}
