/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.service.impl;

import static com.ztarmobile.notification.common.CommonUtils.validateInput;
import static com.ztarmobile.notification.model.Bucket.DATA;
import static com.ztarmobile.notification.model.Bucket.SMS;
import static com.ztarmobile.notification.model.Bucket.VOICE;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.ztarmobile.notification.dao.CustomerBalanceDao;
import com.ztarmobile.notification.dao.CustomerCdrDao;
import com.ztarmobile.notification.exception.BalanceValueException;
import com.ztarmobile.notification.model.Bucket;
import com.ztarmobile.notification.model.CustomerBalance;
import com.ztarmobile.notification.model.NotificationActity;
import com.ztarmobile.notification.model.Status;
import com.ztarmobile.notification.model.ztar.AccountInfo;
import com.ztarmobile.notification.model.ztar.AccountStatus;
import com.ztarmobile.notification.model.ztar.DedicatedAccount;
import com.ztarmobile.notification.service.BalanceNotificationService;
import com.ztarmobile.utils.RestClientUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Direct service implementation that calculates and perform the low balance
 * notification process.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Service
public class BalanceNotificationServiceImpl implements BalanceNotificationService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(BalanceNotificationServiceImpl.class);

    /**
     * URI to check balances from Ericsson.
     */
    @Value("${notification.ztarmobile.search.provider-uri}")
    private String providerResource;

    /**
     * URI to check account from Ztar.
     */
    @Value("${notification.ztarmobile.search.ztar-uri}")
    private String ztarResource;

    /**
     * URI to check account from Ztar.
     */
    @Value("${notification.ztarmobile.sender.sms-uri}")
    private String smsResource;

    /**
     * Customer Balance DAO.
     */
    @Autowired
    private CustomerBalanceDao customerBalanceDao;

    /**
     * Customer Balance DAO.
     */
    @Autowired
    private CustomerCdrDao customerCdrDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAllAvailableActivity() {
        log.debug("Retrieving latest activity...");

        Set<String> allMdns = new HashSet<>();
        List<NotificationActity> air = customerCdrDao.fetchAllMdnActivityAir();
        log.debug("Records retrieved from air: " + air.size());
        List<NotificationActity> data = customerCdrDao.fetchAllMdnActivityData();
        log.debug("Records retrieved from data: " + data.size());

        for (NotificationActity actity : air) {
            allMdns.add(actity.getMdn());
        }
        log.debug("Total Records with air: " + allMdns.size());

        for (NotificationActity actity : data) {
            allMdns.add(actity.getMdn());
        }
        log.debug("Total Records with data: " + allMdns.size());

        return allMdns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void performNotification(String mdn) {
        log.debug(">> Starting the low balance notification process [" + mdn + "]...");
        AccountStatus accountStatus = null;
        AccountInfo accountInfo = null;

        try {
            // check the Ztar account
            accountInfo = getAccountStatusFromZtar(mdn);
            validateInput(accountInfo, "Account Info is not valid");
            int bundleId = accountInfo.getBundleRowId();
            Integer threshold = accountInfo.getThreshold();

            if (threshold != null && threshold > 0 && threshold <= 100) {
                // check the balances in Ericsson
                accountStatus = getAccountStatusFromProvider(mdn, "US");

                double dataPerc = 0, voicePerc = 0, smsPerc = 0;
                CustomerBalance customerBalance = getCustomerBalance(mdn, accountStatus, accountInfo);
                if (customerBalance != null) {
                    dataPerc = calculatePercentagePerBucket(DATA, customerBalance, accountInfo);
                    voicePerc = calculatePercentagePerBucket(VOICE, customerBalance, accountInfo);
                    smsPerc = calculatePercentagePerBucket(SMS, customerBalance, accountInfo);

                    customerBalance.setPercentageData(dataPerc);
                    customerBalance.setPercentageVoice(voicePerc);
                    customerBalance.setPercentageSms(smsPerc);

                    boolean data = false, voice = false, sms = false;
                    String ratePlan = accountInfo.getPricePlan();
                    String product = accountInfo.getProduct();

                    log.debug("threshold: " + threshold + ", ratePlan: " + ratePlan + ", data: " + dataPerc
                            + "%, voice: " + voicePerc + "%, sms: " + smsPerc + "%");
                    // the following condition determines whether the customer
                    // will be notified by SMS or not
                    if (dataPerc > threshold) {
                        // send SMS notification for data.
                        data = sendSMS(mdn, product, ratePlan, customerBalance, DATA);
                    }
                    if (voicePerc > threshold) {
                        // send SMS notification for voice.
                        voice = sendSMS(mdn, product, ratePlan, customerBalance, VOICE);
                    }
                    if (smsPerc > threshold) {
                        // send SMS notification for SMS.
                        sms = sendSMS(mdn, product, ratePlan, customerBalance, SMS);
                    }

                    customerBalance.setNotifiedData(data);
                    customerBalance.setNotifiedVoice(voice);
                    customerBalance.setNotifiedSms(sms);
                } else {
                    log.debug("No customer balance was found for: " + mdn);
                }
                // Saves the status into our tables.
                insertOrUpdateBalanceNotification(mdn, customerBalance);
            } else {
                log.debug("No notification will be sent, threshold = " + threshold + ", bundleId: " + bundleId);
            }
        } catch (Throwable ex) {
            log.error(ex.toString());
            // we handle the error...
            CustomerBalance customerBalance = new CustomerBalance();
            customerBalance.setMdn(mdn);
            customerBalance.setStatus(Status.FAILURE);
            customerBalance.setStatusMessage(ex.getMessage() == null ? "Unknown Error" : ex.getMessage());
            customerBalanceDao.updateBalances(customerBalance);
        }
        log.debug("<< Ending low balance notification process");
    }

    /**
     * Sends a SMS to the customer.
     * 
     * @param mdn
     *            The MDN.
     * @param product
     *            The product.
     * @param ratePlan
     *            The rate plan.
     * @param myBalance
     *            The customer balance.
     * @param bucket
     *            The bucket.
     * @return If the SMS was sent or not.
     * @throws Exception
     *             When there's an exception while sending the SMS.
     */
    private boolean sendSMS(String mdn, String product, String ratePlan, CustomerBalance myBalance, Bucket bucket)
            throws Exception {

        boolean sent = false;
        int total = customerBalanceDao.countCustomerBalance(mdn, myBalance, bucket);
        if (total == 0) {
            log.debug("Trying to send SMS to MDN: " + mdn + " - " + bucket);

            try {
                switch (bucket) {
                case DATA:
                    int myTotalData = getEffeBucketValue(myBalance.getData())
                            + getEffeBucketValue(myBalance.getHighData()) + getEffeBucketValue(myBalance.getLowData());
                    // converts the usage in MB
                    double db = Double.valueOf(myTotalData) / 1024;
                    BigDecimal result = new BigDecimal(db);
                    result = result.setScale(2, BigDecimal.ROUND_HALF_UP);

                    sent = sendNotificationSms(mdn, product, ratePlan, "data", String.valueOf(result.doubleValue()));
                    break;
                case VOICE:
                    sent = sendNotificationSms(mdn, product, ratePlan, "voice", myBalance.getVoice());
                    break;
                case SMS:
                    sent = sendNotificationSms(mdn, product, ratePlan, "sms", myBalance.getSms());
                    break;
                }
            } catch (BalanceValueException ex) {
                log.debug("No SMS was sent, there was a problem with the calculation: " + mdn);
            }
            if (!sent) {
                log.debug("*** Unable to Send SMS to: " + mdn + "***");
            } else {
                log.debug("SMS sent to: " + mdn + " service: " + bucket);
            }
        } else {
            log.debug("Skipping SMS for MDN: " + mdn + " -> " + bucket);
            log.debug("Message already sent, we marked it as sent: " + mdn + " -> " + bucket);
            sent = true;
        }
        return sent;
    }

    /**
     * Calculates the percentage per bucket.
     * 
     * @param bucket
     *            The bucket.
     * @param myBalance
     *            The customer balance.
     * @return The percentage returned.
     */
    private double calculatePercentagePerBucket(Bucket bucket, CustomerBalance myBalance, AccountInfo accountInfo) {
        try {
            switch (bucket) {
            case DATA:
                // converts from MB to KB... (modified data)
                int modifiedData = myBalance.getModifiedData();
                modifiedData = modifiedData * 1024;

                int myTotalData = getEffeBucketValue(myBalance.getData()) + getEffeBucketValue(myBalance.getHighData())
                        + getEffeBucketValue(myBalance.getLowData());
                // adds the modified data into the total data
                myTotalData = modifiedData + myTotalData;

                int allocData = Integer.parseInt(accountInfo.getAllocData());
                // converts from MB to KB...
                allocData = allocData * 1024;
                return calculatePercentage(myTotalData, allocData);
            case VOICE:
                int myTotalVoice = getEffeBucketValue(myBalance.getVoice());
                int allocVoice = Integer.parseInt(accountInfo.getAllocVoice());
                return calculatePercentage(myTotalVoice, allocVoice);
            case SMS:
                int myTotalSms = getEffeBucketValue(myBalance.getSms());
                int allocSms = Integer.parseInt(accountInfo.getAllocText());
                return calculatePercentage(myTotalSms, allocSms);
            }
        } catch (BalanceValueException ex) {
            log.debug("Imposible to calculate the percentage, defaulting to 0 then - " + bucket);
        }
        return 0;
    }

    /**
     * Gets the customer balance object based on the account status and the
     * account info.
     * 
     * @param mdn
     *            The MDN.
     * @param accountStatus
     *            The account status.
     * @param accountInfo
     *            The account info.
     * @return The customer balance or null when the balances were not found.
     */
    private CustomerBalance getCustomerBalance(String mdn, AccountStatus accountStatus, AccountInfo accountInfo) {
        log.debug("getting customer balance for " + mdn + "...");
        validateInput(accountStatus, "Account Status cannot be null");
        validateInput(accountInfo, "Account Info cannot be null");

        CustomerBalance customerBalance = new CustomerBalance();
        customerBalance.setMdn(mdn);
        customerBalance.setBundleRowId(accountInfo.getBundleRowId());
        customerBalance.setModifiedData(customerBalanceDao.getModifiedData(mdn, accountInfo.getBundleRowId()));

        // sets the balances for each bucket.
        if (accountStatus.getDedicatedAccounts() != null && !accountStatus.getDedicatedAccounts().isEmpty()) {
            for (DedicatedAccount da : accountStatus.getDedicatedAccounts()) {
                switch (da.getAccountId()) {
                case DedicatedAccount.ACCOUNT_ID_VOICE:
                    customerBalance.setVoice(da.getAccountBalance());
                    break;
                case DedicatedAccount.ACCOUNT_ID_SMS:
                    customerBalance.setSms(da.getAccountBalance());
                    break;
                case DedicatedAccount.ACCOUNT_ID_MMS:
                    customerBalance.setMms(da.getAccountBalance());
                    break;
                case DedicatedAccount.ACCOUNT_ID_DATA:
                    customerBalance.setData(da.getAccountBalance());
                    break;
                case DedicatedAccount.ACCOUNT_ID_HIGHDATA:
                    customerBalance.setHighData(da.getAccountBalance());
                    break;
                case DedicatedAccount.ACCOUNT_ID_LOWDATA:
                    customerBalance.setLowData(da.getAccountBalance());
                    break;
                }
            }
        } else {
            customerBalance = null;
            log.warn("*** Dedicated account did not return balances for this MDN [" + mdn + "] ***");
        }
        return customerBalance;
    }

    /**
     * Calculates the percentage for each bucket.
     * 
     * @param customerBalance
     *            The customer balance.
     * @param customerAllocation
     *            The customer allocation.
     * @return The percentage.
     */
    private double calculatePercentage(int customerBalance, int customerAllocation) {
        if (customerAllocation == 0) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(customerBalance).multiply(new BigDecimal(100));
        bd = bd.divide(new BigDecimal(customerAllocation), MathContext.DECIMAL32);
        return new BigDecimal(100).subtract(bd).doubleValue();
    }

    /**
     * Calculates the effective bucket value.
     * 
     * @param value
     *            Representation of data.
     * @return The data in a number.
     */
    private int getEffeBucketValue(String value) throws BalanceValueException {
        String valueString = value == null ? "0" : value;
        int valueInt = 0;
        try {
            valueInt = Integer.parseInt(valueString);
        } catch (NumberFormatException e) {
            log.warn("Bucket value is not a number: " + e.getMessage());
            throw new BalanceValueException(e.getMessage());
        }
        return valueInt;
    }

    /**
     * Updates the balance notification.
     * 
     * @param mdn
     *            The MDN.
     * @param customerBalance
     *            The customer balance.
     */
    private void insertOrUpdateBalanceNotification(final String mdn, final CustomerBalance customerBalance) {
        log.debug("updating customer balance table...");
        if (customerBalance != null) {
            // updates the balances.
            customerBalanceDao.updateBalances(customerBalance);
        } else {
            log.debug("No records were updated...");
        }
    }

    /**
     * Retrieves the network status information.
     *
     * @param mdn
     *            The MDN to look up.
     * @return The AccountStatus with network information
     * @throws Exception
     *             When there's an error during the resource call.
     */
    private AccountStatus getAccountStatusFromProvider(String mdn, String market) throws Exception {
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();

        params.add("mdn", mdn);
        params.add("market", market);
        params.add("simple", "true");
        return RestClientUtils.fetch(providerResource, AccountStatus.class, params);
    }

    /**
     * Gets the account information.
     * 
     * @param mdn
     *            The MDN.
     * @return Account information.
     * @throws Exception
     *             When there's an error during the resource call.
     */
    private AccountInfo getAccountStatusFromZtar(String mdn) throws Exception {
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();

        params.add("searchValue", mdn);
        params.add("searchType", "MDN");
        return RestClientUtils.fetch(ztarResource, AccountInfo.class, params);
    }

    /**
     * Sends the SMS notification.
     * 
     * @param mdn
     *            The MDN.
     * @param product
     *            The product.
     * @param ratePlan
     *            The rate plan.
     * @param category
     *            The category.
     * @param quantity
     *            The quantity.
     * @return The response of the call.
     * @throws Exception
     *             When there's an error during the resource call.
     */
    private Boolean sendNotificationSms(String mdn, String product, String ratePlan, String category, String quantity)
            throws Exception {
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();

        params.add("mdn", mdn);
        params.add("product", product);
        params.add("ratePlan", ratePlan);
        params.add("category", category);
        params.add("quantity", quantity);
        return RestClientUtils.fetch(smsResource, Boolean.class, params);
    }

}
