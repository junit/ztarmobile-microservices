-- This script must be executed only once.

DROP TABLE IF EXISTS `us_customer_balances`;

CREATE TABLE `us_customer_balances` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `mdn` varchar(10) NOT NULL,
  `plan_billing_id` int(11) DEFAULT NULL,
  `data` varchar(25) DEFAULT NULL,
  `low_data` varchar(25) DEFAULT NULL,
  `high_data` varchar(25) DEFAULT NULL,
  `voice` varchar(25) DEFAULT NULL,
  `sms` varchar(25) DEFAULT NULL,
  `mms` varchar(25) DEFAULT NULL,
  `percentage_data` varchar(25) DEFAULT NULL,
  `percentage_voice` varchar(25) DEFAULT NULL,
  `percentage_sms` varchar(25) DEFAULT NULL,
  `notified_data` TINYINT(1) NOT NULL,
  `notified_voice` TINYINT(1) NOT NULL,
  `notified_sms` TINYINT(1) NOT NULL,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` char(1) NOT NULL DEFAULT 'S',
  `status_message` text,
  PRIMARY KEY (`row_id`),
  INDEX `mdn_index` (`mdn`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Adding more columns to support the SMS configuration.

ALTER TABLE `us_rate_plan_billing_params` 
ADD COLUMN `low_balance_threshold` INT(11) NULL COMMENT 'Column used to store the threshold that a service is about to reach. Valid values include from 0 to 100, different values will be ignored' AFTER `alloc_data`,
ADD COLUMN `sms_low_balance_data` INT(11) NOT NULL DEFAULT '0' COMMENT 'Reference from the sms_parameters' AFTER `low_balance_threshold`,
ADD COLUMN `sms_low_balance_voice` INT(11) NOT NULL DEFAULT '0' COMMENT 'Reference from the sms_parameters' AFTER `sms_low_balance_data`,
ADD COLUMN `sms_low_balance_sms` INT(11) NOT NULL DEFAULT '0' COMMENT 'Reference from the sms_parameters' AFTER `sms_low_balance_voice`;

-- Adding SMS parameters
INSERT INTO `sms_parameters` (`row_id`, `product`, `description`, `sms`) VALUES (16, 'GOOD2GOUS', 'Low Balance', 'Your remaining Data is running low. You have ${data} MB remaining. Need More data this month? Add-ons are available at freeupmobile.com');
