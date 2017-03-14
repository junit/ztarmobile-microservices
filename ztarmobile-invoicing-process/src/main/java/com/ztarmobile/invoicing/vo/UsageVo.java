/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.vo;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/10/17
 */
public class UsageVo {
    private double mou;
    private double kbs;
    private double sms;
    private double mms;

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
     * @return the kbs
     */
    public double getKbs() {
        return kbs;
    }

    /**
     * @param kbs
     *            the kbs to set
     */
    public void setKbs(double kbs) {
        this.kbs = kbs;
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
        return "UsageVo [mou=" + mou + ", kbs=" + kbs + ", sms=" + sms + ", mms=" + mms + "]";
    }

}
