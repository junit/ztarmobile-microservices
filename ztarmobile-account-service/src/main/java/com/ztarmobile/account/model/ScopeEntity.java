/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Entity
@Table(name = "scope_resource_set")
public class ScopeEntity {
    @Id
    @Column(name = "SCOPE_ID")
    private String scope;
    private String description;
    @OneToMany(mappedBy = "scope")
    private List<ResourceSetEntity> resources;

    /**
     * Adds a new resource entity.
     * 
     * @param resource
     *            A resource entity.
     */
    public void addResource(ResourceSetEntity resource) {
        this.resources.add(resource);
        if (resource.getScope() != this) {
            resource.setScope(this);
        }
    }

    /**
     * @return the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(String scope) {
        this.scope = scope;
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
     * @return the resources
     */
    public List<ResourceSetEntity> getResources() {
        return resources;
    }

    /**
     * @param resources
     *            the resources to set
     */
    public void setResources(List<ResourceSetEntity> resources) {
        this.resources = resources;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ScopeEntity [scope=" + scope + ", resources=" + resources + "]";
    }

}
