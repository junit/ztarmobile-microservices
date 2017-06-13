/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 05/04/17
 */
public class ApplicationEmailNotification extends EmailNotification {
    /**
     * The reason of failure.
     */
    private Throwable reason;
    /**
     * The artifact.
     */
    private String artifact;
    /**
     * The name.
     */
    private String name;
    /**
     * The description.
     */
    private String description;
    /**
     * The version.
     */
    private String version;
    /**
     * The URL of the service.
     */
    private String url;
    /**
     * Just a flag to indicate whether the application was success or not.
     */
    private boolean success;

    /**
     * Creates an object that holds the state of the application when it started
     * Successfully.
     * 
     */
    public ApplicationEmailNotification() {
        this(true, null);
    }

    /**
     * Creates an object that holds the state of the application.
     * 
     * @param success
     *            Was success or not.
     * @param reason
     *            The reason of failure in case success is false, if success is
     *            true, this is ignored.
     */
    public ApplicationEmailNotification(boolean success, Throwable reason) {
        this.reason = reason;
        this.success = success;
        if (success) {
            this.setSubject("Microservice User Profile has started successfully!!!");
            this.reason = null;
        } else {
            this.setSubject("Ohh no!!, Microservice User Profile could not start");
        }
    }

    /**
     * @return the reason
     */
    public Throwable getReason() {
        return reason;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return the artifact
     */
    public String getArtifact() {
        return artifact;
    }

    /**
     * @param artifact
     *            the artifact to set
     */
    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ApplicationEmailNotification [reason=" + reason + ", artifact=" + artifact + ", name=" + name
                + ", description=" + description + ", version=" + version + ", success=" + success + "]";
    }
}
