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
    private float mou;
    private float kbs;
    private float sms;
    private float mms;

    /**
     * @return the mou
     */
    public float getMou() {
        return mou;
    }

    /**
     * @param mou
     *            the mou to set
     */
    public void setMou(float mou) {
        this.mou = mou;
    }

    /**
     * @return the kbs
     */
    public float getKbs() {
        return kbs;
    }

    /**
     * @param kbs
     *            the kbs to set
     */
    public void setKbs(float kbs) {
        this.kbs = kbs;
    }

    /**
     * @return the sms
     */
    public float getSms() {
        return sms;
    }

    /**
     * @param sms
     *            the sms to set
     */
    public void setSms(float sms) {
        this.sms = sms;
    }

    /**
     * @return the mms
     */
    public float getMms() {
        return mms;
    }

    /**
     * @param mms
     *            the mms to set
     */
    public void setMms(float mms) {
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
