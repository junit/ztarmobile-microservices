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
    private String data;
    private String voice;
    private String sms;
    private String mms;

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CustomerBalance [mdn=" + mdn + ", data=" + data + ", voice=" + voice + ", sms=" + sms + ", mms=" + mms
                + "]";
    }
}
