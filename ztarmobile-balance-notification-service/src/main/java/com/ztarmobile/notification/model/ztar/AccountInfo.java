/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.model.ztar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The account status.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class AccountInfo extends BaseModel {
    /**
     * the serial number.
     */
    private static final long serialVersionUID = -7121648701135089513L;
    private String balance;
    private String accountStatus;
    private Date airtimeExpiryDate;
    private Date creditClearanceDate;
    private String currency;
    private String currentServiceClass;
    private Date serviceFeeDate;
    private Date serviceRemovalDate;
    private boolean refillBarredFlag;
    private boolean temporaryBlockFlag;
    private String subscriberNumber;
    private String sim;
    private String imei;
    private boolean activeInLast7Days = false;
    private Date refillUnbarDateTime;
    private ArrayList<DedicatedAccount> dedicatedAccounts;
    private int accountGroupID;
    private String masterAccountNumber;
    private ArrayList<Integer> communityIds;
    private String promotionCounter1;
    private int bundleRowId;
    private String pricePlan;
    private String pricePlanDesc;
    private double pricePlanMaxBalance;
    private List<String> services;
    private String rateCenter;
    private String accountPassword;
    private String accountEmailAddress;
    private boolean smsOptOut = false;
    private Date activationDate;
    private String product;
    private String esn;
    private String market;
    private String msl;
    private String networkProviderStatus;
    private String puk1;
    private String puk2;
    private String min;
    private String language;
    private String handsetModel;
    private ArrayList<Bundle> bundles;
    private Date pricePlanExpiryDate;
    private String tlgRatePlan;
    private String equipmentType;
    private String bundleRenewalInd;
    private List<USRatePlanInfo> ratePlanInfos;
    private List<USRatePlanInfo> pendingRatePlans;
    private List<USRatePlanInfo> ratePlansAddons;
    private String networkProvider;
    private Date serviceFee911Date;

    private boolean planExpired;
    private String unlmtdClass;

    private String allocVoice;
    private String allocText;
    private String allocData;
    private String allocIdd;

    private Integer threshold;

    private boolean payg;

    /** Creates a new instance of AccountInfo */
    public AccountInfo() {
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getAirtimeExpiryDate() {
        return airtimeExpiryDate;
    }

    public void setAirtimeExpiryDate(Date airtimeExpiryDate) {
        this.airtimeExpiryDate = airtimeExpiryDate;
    }

    public Date getCreditClearanceDate() {
        return creditClearanceDate;
    }

    public void setCreditClearanceDate(Date creditClearanceDate) {
        this.creditClearanceDate = creditClearanceDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrentServiceClass() {
        return currentServiceClass;
    }

    public void setCurrentServiceClass(String currentServiceClass) {
        this.currentServiceClass = currentServiceClass;
    }

    public Date getServiceFeeDate() {
        return serviceFeeDate;
    }

    public void setServiceFeeDate(Date serviceFeeDate) {
        this.serviceFeeDate = serviceFeeDate;
    }

    public Date getServiceRemovalDate() {
        return serviceRemovalDate;
    }

    public void setServiceRemovalDate(Date serviceRemovalDate) {
        this.serviceRemovalDate = serviceRemovalDate;
    }

    public boolean isRefillBarredFlag() {
        return refillBarredFlag;
    }

    public void setRefillBarredFlag(boolean refillBarredFlag) {
        this.refillBarredFlag = refillBarredFlag;
    }

    public boolean isTemporaryBlockFlag() {
        return temporaryBlockFlag;
    }

    public void setTemporaryBlockFlag(boolean temporaryBlockFlag) {
        this.temporaryBlockFlag = temporaryBlockFlag;
    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    public void setSubscriberNumber(String subscriberNumber) {
        this.subscriberNumber = subscriberNumber;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isActiveInLast7Days() {
        return activeInLast7Days;
    }

    public void setActiveInLast7Days(boolean activeInLast7Days) {
        this.activeInLast7Days = activeInLast7Days;
    }

    public Date getRefillUnbarDateTime() {
        return refillUnbarDateTime;
    }

    public void setRefillUnbarDateTime(Date refillUnbarDateTime) {
        this.refillUnbarDateTime = refillUnbarDateTime;
    }

    public ArrayList<DedicatedAccount> getDedicatedAccounts() {
        return dedicatedAccounts;
    }

    public void setDedicatedAccounts(ArrayList<DedicatedAccount> dedicatedAccounts) {
        this.dedicatedAccounts = dedicatedAccounts;
    }

    public int getAccountGroupID() {
        return accountGroupID;
    }

    public void setAccountGroupID(int accountGroupID) {
        this.accountGroupID = accountGroupID;
    }

    public String getMasterAccountNumber() {
        return masterAccountNumber;
    }

    public void setMasterAccountNumber(String masterAccountNumber) {
        this.masterAccountNumber = masterAccountNumber;
    }

    public ArrayList<Integer> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(ArrayList<Integer> communityIds) {
        this.communityIds = communityIds;
    }

    public String getPromotionCounter1() {
        return promotionCounter1;
    }

    public void setPromotionCounter1(String promotionCounter1) {
        this.promotionCounter1 = promotionCounter1;
    }

    public int getBundleRowId() {
        return bundleRowId;
    }

    public void setBundleRowId(int bundleRowId) {
        this.bundleRowId = bundleRowId;
    }

    public String getPricePlan() {
        return pricePlan;
    }

    public void setPricePlan(String pricePlan) {
        this.pricePlan = pricePlan;
    }

    public String getPricePlanDesc() {
        return pricePlanDesc;
    }

    public void setPricePlanDesc(String pricePlanDesc) {
        this.pricePlanDesc = pricePlanDesc;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getRateCenter() {
        return rateCenter;
    }

    public void setRateCenter(String rateCenter) {
        this.rateCenter = rateCenter;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getAccountEmailAddress() {
        return accountEmailAddress;
    }

    public void setAccountEmailAddress(String accountEmailAddress) {
        this.accountEmailAddress = accountEmailAddress;
    }

    /**
     * @return the smsOptOut
     */
    public boolean isSmsOptOut() {
        return smsOptOut;
    }

    /**
     * @param smsOptOut
     *            the smsOptOut to set
     */
    public void setSmsOptOut(boolean smsOptOut) {
        this.smsOptOut = smsOptOut;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct() {
        return product;
    }

    public void setEsn(String esn) {
        this.esn = esn;
    }

    public String getEsn() {
        return esn;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getMarket() {
        return market;
    }

    public void setMsl(String msl) {
        this.msl = msl;
    }

    public String getMsl() {
        return msl;
    }

    public void setNetworkProviderStatus(String networkProviderStatus) {
        this.networkProviderStatus = networkProviderStatus;
    }

    public String getNetworkProviderStatus() {
        return networkProviderStatus;
    }

    public void setPuk1(String puk1) {
        this.puk1 = puk1;
    }

    public String getPuk1() {
        return puk1;
    }

    public void setPuk2(String puk2) {
        this.puk2 = puk2;
    }

    public String getPuk2() {
        return puk2;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMin() {
        return min;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setHandsetModel(String handsetModel) {
        this.handsetModel = handsetModel;
    }

    public String getHandsetModel() {
        return handsetModel;
    }

    public void setBundles(ArrayList<Bundle> bundles) {
        this.bundles = bundles;
    }

    public ArrayList<Bundle> getBundles() {
        return bundles;
    }

    public void setPricePlanExpiryDate(Date pricePlanExpiryDate) {
        this.pricePlanExpiryDate = pricePlanExpiryDate;
    }

    public Date getPricePlanExpiryDate() {
        return pricePlanExpiryDate;
    }

    public String getTlgRatePlan() {
        return tlgRatePlan;
    }

    public void setTlgRatePlan(String tlgRatePlan) {
        this.tlgRatePlan = tlgRatePlan;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getBundleRenewalInd() {
        return bundleRenewalInd;
    }

    public void setBundleRenewalInd(String bundleRenewalInd) {
        this.bundleRenewalInd = bundleRenewalInd;
    }

    public List<USRatePlanInfo> getRatePlanInfos() {
        return ratePlanInfos;
    }

    public void setRatePlanInfos(List<USRatePlanInfo> ratePlanInfos) {
        this.ratePlanInfos = ratePlanInfos;
    }

    public List<USRatePlanInfo> getPendingRatePlans() {
        return pendingRatePlans;
    }

    public void setPendingRatePlans(List<USRatePlanInfo> pendingRatePlans) {
        this.pendingRatePlans = pendingRatePlans;
    }

    public List<USRatePlanInfo> getRatePlansAddons() {
        return ratePlansAddons;
    }

    public void setRatePlansAddons(List<USRatePlanInfo> ratePlansAddons) {
        this.ratePlansAddons = ratePlansAddons;
    }

    public String getNetworkProvider() {
        return networkProvider;
    }

    public void setNetworkProvider(String networkProvider) {
        this.networkProvider = networkProvider;
    }

    public Date getServiceFee911Date() {
        return serviceFee911Date;
    }

    public void setServiceFee911Date(Date serviceFee911Date) {
        this.serviceFee911Date = serviceFee911Date;
    }

    public boolean isPlanExpired() {
        return planExpired;
    }

    public void setPlanExpired(boolean planExpired) {
        this.planExpired = planExpired;
    }

    public String getUnlmtdClass() {
        return unlmtdClass;
    }

    public void setUnlmtdClass(String unlmtdClass) {
        this.unlmtdClass = unlmtdClass;
    }

    public String getAllocVoice() {
        return allocVoice;
    }

    public void setAllocVoice(String allocVoice) {
        this.allocVoice = allocVoice;
    }

    public String getAllocText() {
        return allocText;
    }

    public void setAllocText(String allocText) {
        this.allocText = allocText;
    }

    public String getAllocData() {
        return allocData;
    }

    public void setAllocData(String allocData) {
        this.allocData = allocData;
    }

    public String getAllocIdd() {
        return allocIdd;
    }

    public void setAllocIdd(String allocIdd) {
        this.allocIdd = allocIdd;
    }

    public boolean isPayg() {
        return payg;
    }

    public void setPayg(boolean payg) {
        this.payg = payg;
    }

    public double getPricePlanMaxBalance() {
        return pricePlanMaxBalance;
    }

    public void setPricePlanMaxBalance(double pricePlanMaxBalance) {
        this.pricePlanMaxBalance = pricePlanMaxBalance;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

}
