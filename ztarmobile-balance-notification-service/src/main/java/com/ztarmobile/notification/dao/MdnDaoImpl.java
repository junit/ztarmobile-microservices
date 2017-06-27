package com.ztarmobile.notification.dao;

import com.ztarmobile.notification.common.AbstractJdbc;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class MdnDaoImpl extends AbstractJdbc implements MdnDao {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(MdnDaoImpl.class);

    /**
     * The SQL statements.
     */
    @Autowired
    @Qualifier(value = "mdnDao")
    private Properties sqlStatements;

}
