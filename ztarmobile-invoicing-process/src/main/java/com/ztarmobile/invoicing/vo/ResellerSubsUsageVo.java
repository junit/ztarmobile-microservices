/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.vo;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddFormat;
import static com.ztarmobile.invoicing.common.DateUtils.fromStringToYYmmddHHmmssFormat;

import java.util.Date;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/09/17
 */
public class ResellerSubsUsageVo {
    private long rowId;
    private Date callDate;
    private String ratePlan;
    private String mdn;
    private double allocMou; // maps to 'alloc_voice' field
    private double allocSms;
    private double allocMms;
    private double allocMbs; // maps to 'alloc_mou_mbs' field
    private double actualMou; // maps to 'actual_voice' field
    private double actualSms;
    private double actualMms;
    private double actualKbs;
    private Date durationStart;
    private Date durationEnd;
    private int newAddInd;
    private int renewalInd;
    private Date lstUpdDate;
    private boolean isUpdated;

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
     * @return the callDate
     */
    public Date getCallDate() {
        return callDate;
    }

    /**
     * @param callDate
     *            the callDate to set
     */
    public void setCallDate(Date callDate) {
        this.callDate = callDate;
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
     * @return the allocMou
     */
    public double getAllocMou() {
        return allocMou;
    }

    /**
     * @param allocMou
     *            the allocMou to set
     */
    public void setAllocMou(double allocMou) {
        this.allocMou = allocMou;
    }

    /**
     * @return the allocSms
     */
    public double getAllocSms() {
        return allocSms;
    }

    /**
     * @param allocSms
     *            the allocSms to set
     */
    public void setAllocSms(double allocSms) {
        this.allocSms = allocSms;
    }

    /**
     * @return the allocMms
     */
    public double getAllocMms() {
        return allocMms;
    }

    /**
     * @param allocMms
     *            the allocMms to set
     */
    public void setAllocMms(double allocMms) {
        this.allocMms = allocMms;
    }

    /**
     * @return the allocMbs
     */
    public double getAllocMbs() {
        return allocMbs;
    }

    /**
     * @param allocMbs
     *            the allocMbs to set
     */
    public void setAllocMbs(double allocMbs) {
        this.allocMbs = allocMbs;
    }

    /**
     * @return the actualMou
     */
    public double getActualMou() {
        return actualMou;
    }

    /**
     * @param actualMou
     *            the actualMou to set
     */
    public void setActualMou(double actualMou) {
        this.actualMou = actualMou;
    }

    /**
     * @return the actualSms
     */
    public double getActualSms() {
        return actualSms;
    }

    /**
     * @param actualSms
     *            the actualSms to set
     */
    public void setActualSms(double actualSms) {
        this.actualSms = actualSms;
    }

    /**
     * @return the actualMms
     */
    public double getActualMms() {
        return actualMms;
    }

    /**
     * @param actualMms
     *            the actualMms to set
     */
    public void setActualMms(double actualMms) {
        this.actualMms = actualMms;
    }

    /**
     * @return the actualKbs
     */
    public double getActualKbs() {
        return actualKbs;
    }

    /**
     * @param actualKbs
     *            the actualKbs to set
     */
    public void setActualKbs(double actualKbs) {
        this.actualKbs = actualKbs;
    }

    /**
     * @return the durationStart
     */
    public Date getDurationStart() {
        return durationStart;
    }

    /**
     * @param durationStart
     *            the durationStart to set
     */
    public void setDurationStart(Date durationStart) {
        this.durationStart = durationStart;
    }

    /**
     * @return the durationEnd
     */
    public Date getDurationEnd() {
        return durationEnd;
    }

    /**
     * @param durationEnd
     *            the durationEnd to set
     */
    public void setDurationEnd(Date durationEnd) {
        this.durationEnd = durationEnd;
    }

    /**
     * @return the newAddInd
     */
    public int getNewAddInd() {
        return newAddInd;
    }

    /**
     * @param newAddInd
     *            the newAddInd to set
     */
    public void setNewAddInd(int newAddInd) {
        this.newAddInd = newAddInd;
    }

    /**
     * @return the renewalInd
     */
    public int getRenewalInd() {
        return renewalInd;
    }

    /**
     * @param renewalInd
     *            the renewalInd to set
     */
    public void setRenewalInd(int renewalInd) {
        this.renewalInd = renewalInd;
    }

    /**
     * @return the lstUpdDate
     */
    public Date getLstUpdDate() {
        return lstUpdDate;
    }

    /**
     * @param lstUpdDate
     *            the lstUpdDate to set
     */
    public void setLstUpdDate(Date lstUpdDate) {
        this.lstUpdDate = lstUpdDate;
    }

    /**
     * @return the isUpdated
     */
    public boolean isUpdated() {
        return isUpdated;
    }

    /**
     * @param isUpdated
     *            the isUpdated to set
     */
    public void setUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    /**
     * Compare if the mdn, and call date matches that of this object.
     * 
     * @param mdn
     *            The mdn.
     * @param callDate
     *            The call date under this format yyyyMMdd
     * @return true if it's the same or false it that does not match.
     */
    public boolean isEqualsByMdnAndCallDate(String mdn, String callDate) {
        return this.mdn.equals(mdn) && fromDateToYYYYmmddFormat(this.callDate).equals(callDate);
    }

    /**
     * The time is in range.
     * 
     * @param callDate
     *            The call date.
     * @return true, it's in range.
     */
    public boolean isTimeInRange(String callDate) {
        boolean isInRange = true;
        Date calldt = fromStringToYYmmddHHmmssFormat(callDate);
        if (calldt.before(durationStart) || calldt.after(durationEnd)) {
            isInRange = false;
        }
        return isInRange;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResellerSubsUsageVo [rowId=" + rowId + ", callDate=" + callDate + ", ratePlan=" + ratePlan + ", mdn="
                + mdn + ", allocMou=" + allocMou + ", allocSms=" + allocSms + ", allocMms=" + allocMms + ", allocMbs="
                + allocMbs + ", actualMou=" + actualMou + ", actualSms=" + actualSms + ", actualMms=" + actualMms
                + ", actualKbs=" + actualKbs + ", durationStart=" + durationStart + ", durationEnd=" + durationEnd
                + ", newAddInd=" + newAddInd + ", renewalInd=" + renewalInd + ", lstUpdDate=" + lstUpdDate
                + ", isUpdated=" + isUpdated + "]";
    }

}
