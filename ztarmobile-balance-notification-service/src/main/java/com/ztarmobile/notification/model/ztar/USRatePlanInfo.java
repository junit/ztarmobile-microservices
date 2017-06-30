/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.model.ztar;

import java.util.Date;

/**
 * The account status.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class USRatePlanInfo {

    public enum PlanStatus {
        ACTIVE, CANCELED, EXPIRED, PENDING
    }

    private int rowId;
    private String mdn;
    private String ratePlan;
    private String ratePlanDesc;
    private Date planStartDate;
    private Date planRenewalDate;
    private Date planEndDate;
    private PlanStatus PlanStatus;
    private String product;
    private String equipmentType;
    private Date creationDate;

    private boolean addon;
    private int bundleRowId;
    private int addonParentId;

    private boolean expired;
    private boolean refillDateAdjust;

    private int unlmtdClass;

    private boolean renewable;
    private double price;
    private double minBalance;
    private double maxBalance;

    private int days;

    private int allocVoice;
    private int allocText;
    private int allocData;
    private int allocIdd;

    private Integer threshold;

    private String tierCards;

    private boolean payg;

    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    public String getMdn() {
        return mdn;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRatePlan(String ratePlan) {
        this.ratePlan = ratePlan;
    }

    public String getRatePlan() {
        return ratePlan;
    }

    public String getRatePlanDesc() {
        return ratePlanDesc;
    }

    public void setRatePlanDesc(String ratePlanDesc) {
        this.ratePlanDesc = ratePlanDesc;
    }

    public void setPlanRenewalDate(Date planRenewalDate) {
        this.planRenewalDate = planRenewalDate;
    }

    public Date getPlanRenewalDate() {
        return planRenewalDate;
    }

    public void setPlanStatus(PlanStatus planStatus) {
        PlanStatus = planStatus;
    }

    public PlanStatus getPlanStatus() {
        return PlanStatus;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct() {
        return product;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isAddon() {
        return addon;
    }

    public void setAddon(boolean addon) {
        this.addon = addon;
    }

    public int getBundleRowId() {
        return bundleRowId;
    }

    public void setBundleRowId(int bundleRowId) {
        this.bundleRowId = bundleRowId;
    }

    public int getAddonParentId() {
        return addonParentId;
    }

    public void setAddonParentId(int addonParentId) {
        this.addonParentId = addonParentId;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isRefillDateAdjust() {
        return refillDateAdjust;
    }

    public void setRefillDateAdjust(boolean refillDateAdjust) {
        this.refillDateAdjust = refillDateAdjust;
    }

    public int getUnlmtdClass() {
        return unlmtdClass;
    }

    public void setUnlmtdClass(int unlmtdClass) {
        this.unlmtdClass = unlmtdClass;
    }

    public boolean isRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public double getMaxBalance() {
        return maxBalance;
    }

    public void setMaxBalance(double maxBalance) {
        this.maxBalance = maxBalance;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getAllocVoice() {
        return allocVoice;
    }

    public void setAllocVoice(int allocVoice) {
        this.allocVoice = allocVoice;
    }

    public int getAllocText() {
        return allocText;
    }

    public void setAllocText(int allocText) {
        this.allocText = allocText;
    }

    public int getAllocData() {
        return allocData;
    }

    public void setAllocData(int allocData) {
        this.allocData = allocData;
    }

    public int getAllocIdd() {
        return allocIdd;
    }

    public void setAllocIdd(int allocIdd) {
        this.allocIdd = allocIdd;
    }

    public String getTierCards() {
        return tierCards;
    }

    public void setTierCards(String tierCards) {
        this.tierCards = tierCards;
    }

    public boolean isPayg() {
        return payg;
    }

    public void setPayg(boolean payg) {
        this.payg = payg;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "USRatePlanInfo{" + "rowId=" + rowId + ", mdn='" + mdn + '\'' + ", ratePlan='" + ratePlan + '\''
                + ", planRenewalDate=" + planRenewalDate + ", PlanStatus=" + PlanStatus + ", product='" + product + '\''
                + ", equipmentType='" + equipmentType + '\'' + '}';
    }
}
