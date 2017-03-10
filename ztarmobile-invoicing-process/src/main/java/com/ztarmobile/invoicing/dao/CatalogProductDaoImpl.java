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
import com.ztarmobile.invoicing.vo.CatalogProductVo;

/**
 * Direct DAO Implementation.
 *
 * @author armandorivas
 * @since 03/10/17
 */
@Repository
public class CatalogProductDaoImpl extends AbstractJdbc implements CatalogProductDao {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(CatalogProductDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "catalogProductDaoSql")
    private Properties sqlStatements;

    /**
     * {@inheritDoc}
     */
    @Override
    public CatalogProductVo getCatalogProduct(String product) {
        log.debug("Getting product by [" + product + "]");
        String sql = sqlStatements.getProperty("select.catalog_product");

        Map<String, String> params = new HashMap<>();
        params.put("product", product);

        List<CatalogProductVo> list;
        list = this.getJdbc().query(sql, new MapSqlParameterSource(params), new RowMapper<CatalogProductVo>() {
            @Override
            public CatalogProductVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                CatalogProductVo vo = new CatalogProductVo();
                int rcnt = 0;
                vo.setRowId(rs.getLong(++rcnt));
                vo.setProduct(rs.getString(++rcnt));
                vo.setCdma(rs.getBoolean(++rcnt));

                return vo;
            }
        });
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
