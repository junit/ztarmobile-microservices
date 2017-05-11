/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/20/17
 */
public class ReportDetails {
    private long rowId;
    private int year;
    private int month;
    private String mdn;
    private String ratePlan;
    private int dayOnPlans;
    private double mou;
    private double mbs;
    private double sms;
    private double mms;

    /**
     * @return the rowId
     */
    public long getRowId() {
        return rowId;
    }

    /**
     * @param rowId
     *            the rowId to set
     */
    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month
     *            the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

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
     * @return the ratePlan
     */
    public String getRatePlan() {
        return ratePlan;
    }

    /**
     * @param ratePlan
     *            the ratePlan to set
     */
    public void setRatePlan(String ratePlan) {
        this.ratePlan = ratePlan;
    }

    /**
     * @return the dayOnPlans
     */
    public int getDayOnPlans() {
        return dayOnPlans;
    }

    /**
     * @param dayOnPlans
     *            the dayOnPlans to set
     */
    public void setDayOnPlans(int dayOnPlans) {
        this.dayOnPlans = dayOnPlans;
    }

    /**
     * @return the mou
     */
    public double getMou() {
        return mou;
    }

    /**
     * @param mou
     *            the mou to set
     */
    public void setMou(double mou) {
        this.mou = mou;
    }

    /**
     * @return the mbs
     */
    public double getMbs() {
        return mbs;
    }

    /**
     * @param mbs
     *            the mbs to set
     */
    public void setMbs(double mbs) {
        this.mbs = mbs;
    }

    /**
     * @return the sms
     */
    public double getSms() {
        return sms;
    }

    /**
     * @param sms
     *            the sms to set
     */
    public void setSms(double sms) {
        this.sms = sms;
    }

    /**
     * @return the mms
     */
    public double getMms() {
        return mms;
    }

    /**
     * @param mms
     *            the mms to set
     */
    public void setMms(double mms) {
        this.mms = mms;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReportDetails [rowId=" + rowId + ", year=" + year + ", month=" + month + ", mdn=" + mdn + ", ratePlan="
                + ratePlan + ", dayOnPlans=" + dayOnPlans + ", mou=" + mou + ", mbs=" + mbs + ", sms=" + sms + ", mms="
                + mms + "]";
    }

}
