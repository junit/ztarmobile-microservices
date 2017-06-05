/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Entity
@Table(name = "catalog_resource_set")
public class ResourceSetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "row_id")
    private Long id;
    private String verb;
    private String resource;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCOPE_ID")
    private ScopeEntity scope;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the verb
     */
    public String getVerb() {
        return verb;
    }

    /**
     * @param verb
     *            the verb to set
     */
    public void setVerb(String verb) {
        this.verb = verb;
    }

    /**
     * @return the resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * @param resource
     *            the resource to set
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the scope
     */
    public ScopeEntity getScope() {
        return scope;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(ScopeEntity scope) {
        this.scope = scope;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResourceSetEntity [id=" + id + ", verb=" + verb + ", resource=" + resource + ", description="
                + description + ", scope=" + scope + "]";
    }

}
