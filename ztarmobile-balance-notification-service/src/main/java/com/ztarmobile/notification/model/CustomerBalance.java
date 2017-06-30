/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class CustomerBalance {
    private String mdn;
    private Integer bundleRowId;
    private String data;
    private String lowData;
    private String highData;
    private String voice;
    private String sms;
    private String mms;
    private Double percentageData;
    private Double percentageVoice;
    private Double percentageSms;
    private boolean notifiedData;
    private boolean notifiedVoice;
    private boolean notifiedSms;

    private Status status = Status.SUCCESS;
    private String statusMessage;

    /**
     * @return the mdn
     */
    public String getMdn() {
        return mdn;
    }

    /**
     * @param mdn
     *            the mdn to set
     */
    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    /**
     * @return the bundleRowId
     */
    public Integer getBundleRowId() {
        return bundleRowId;
    }

    /**
     * @param bundleRowId
     *            the bundleRowId to set
     */
    public void setBundleRowId(Integer bundleRowId) {
        this.bundleRowId = bundleRowId;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the lowData
     */
    public String getLowData() {
        return lowData;
    }

    /**
     * @param lowData
     *            the lowData to set
     */
    public void setLowData(String lowData) {
        this.lowData = lowData;
    }

    /**
     * @return the highData
     */
    public String getHighData() {
        return highData;
    }

    /**
     * @param highData
     *            the highData to set
     */
    public void setHighData(String highData) {
        this.highData = highData;
    }

    /**
     * @return the voice
     */
    public String getVoice() {
        return voice;
    }

    /**
     * @param voice
     *            the voice to set
     */
    public void setVoice(String voice) {
        this.voice = voice;
    }

    /**
     * @return the sms
     */
    public String getSms() {
        return sms;
    }

    /**
     * @param sms
     *            the sms to set
     */
    public void setSms(String sms) {
        this.sms = sms;
    }

    /**
     * @return the mms
     */
    public String getMms() {
        return mms;
    }

    /**
     * @param mms
     *            the mms to set
     */
    public void setMms(String mms) {
        this.mms = mms;
    }

    /**
     * @return the percentageData
     */
    public Double getPercentageData() {
        return percentageData;
    }

    /**
     * @param percentageData
     *            the percentageData to set
     */
    public void setPercentageData(Double percentageData) {
        this.percentageData = percentageData;
    }

    /**
     * @return the percentageVoice
     */
    public Double getPercentageVoice() {
        return percentageVoice;
    }

    /**
     * @param percentageVoice
     *            the percentageVoice to set
     */
    public void setPercentageVoice(Double percentageVoice) {
        this.percentageVoice = percentageVoice;
    }

    /**
     * @return the percentageSms
     */
    public Double getPercentageSms() {
        return percentageSms;
    }

    /**
     * @param percentageSms
     *            the percentageSms to set
     */
    public void setPercentageSms(Double percentageSms) {
        this.percentageSms = percentageSms;
    }

    /**
     * @return the notifiedData
     */
    public boolean isNotifiedData() {
        return notifiedData;
    }

    /**
     * @param notifiedData
     *            the notifiedData to set
     */
    public void setNotifiedData(boolean notifiedData) {
        this.notifiedData = notifiedData;
    }

    /**
     * @return the notifiedVoice
     */
    public boolean isNotifiedVoice() {
        return notifiedVoice;
    }

    /**
     * @param notifiedVoice
     *            the notifiedVoice to set
     */
    public void setNotifiedVoice(boolean notifiedVoice) {
        this.notifiedVoice = notifiedVoice;
    }

    /**
     * @return the notifiedSms
     */
    public boolean isNotifiedSms() {
        return notifiedSms;
    }

    /**
     * @param notifiedSms
     *            the notifiedSms to set
     */
    public void setNotifiedSms(boolean notifiedSms) {
        this.notifiedSms = notifiedSms;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @param statusMessage
     *            the statusMessage to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CustomerBalance [mdn=" + mdn + ", bundleRowId=" + bundleRowId + ", data=" + data + ", lowData="
                + lowData + ", highData=" + highData + ", voice=" + voice + ", sms=" + sms + ", mms=" + mms
                + ", percentageData=" + percentageData + ", percentageVoice=" + percentageVoice + ", percentageSms="
                + percentageSms + ", notifiedData=" + notifiedData + ", notifiedVoice=" + notifiedVoice
                + ", notifiedSms=" + notifiedSms + ", status=" + status + ", statusMessage=" + statusMessage + "]";
    }
}
