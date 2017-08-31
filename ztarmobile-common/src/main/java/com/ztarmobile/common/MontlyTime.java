/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.common;

import java.util.Calendar;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/14/17
 */
public class MontlyTime {
    /**
     * The start calendar.
     */
    private Calendar start;
    /**
     * The end calendar.
     */
    private Calendar end;

    /**
     * @return the start
     */
    public Calendar getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(Calendar start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public Calendar getEnd() {
        return end;
    }

    /**
     * @param end
     *            the end to set
     */
    public void setEnd(Calendar end) {
        this.end = end;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MontlyTime [start=" + start.getTime() + ", end=" + end.getTime() + "]";
    }

}
