-- This script must be executed only once.

DROP TABLE IF EXISTS `us_customer_balances`;

CREATE TABLE `us_customer_balances` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `mdn` varchar(10) NOT NULL,
  `plan_billing_id` int(11) DEFAULT NULL,
  `data` varchar(25) DEFAULT NULL COMMENT 'Expressed in KB',
  `low_data` varchar(25) DEFAULT NULL COMMENT 'Expressed in KB',
  `high_data` varchar(25) DEFAULT NULL COMMENT 'Expressed in KB',
  `voice` varchar(25) DEFAULT NULL,
  `sms` varchar(25) DEFAULT NULL,
  `mms` varchar(25) DEFAULT NULL,
  `percentage_data` varchar(25) DEFAULT NULL,
  `percentage_voice` varchar(25) DEFAULT NULL,
  `percentage_sms` varchar(25) DEFAULT NULL,
  `notified_data` TINYINT(1) NOT NULL DEFAULT 0,
  `notified_voice` TINYINT(1) NOT NULL DEFAULT 0,
  `notified_sms` TINYINT(1) NOT NULL DEFAULT 0,
  `modified_data` varchar(25) NOT NULL DEFAULT 0 COMMENT 'Expressed in MB',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `change_date` timestamp NULL,
  `status` char(1) NOT NULL DEFAULT 'S',
  `status_message` text,
  PRIMARY KEY (`row_id`),
  UNIQUE INDEX `mdn_index` (`mdn` ASC),
  KEY `fk_us_c_b-us_rate_plan_billing_params-plan_billing_id` (`plan_billing_id`),
  CONSTRAINT `fk_us_c_b-us_rate_plan_billing_params-plan_billing_id` FOREIGN KEY (`plan_billing_id`) REFERENCES `us_rate_plan_billing_params` (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Adding more columns to support the SMS configuration.

ALTER TABLE `us_rate_plan_billing_params` 
ADD COLUMN `low_balance_threshold` INT(11) NULL COMMENT 'Column used to store the threshold that a service is about to reach. Valid values include from 0 to 100, different values will be ignored' AFTER `alloc_data`,
ADD COLUMN `sms_low_balance_data` INT(11) NOT NULL DEFAULT '0' COMMENT 'Reference from the sms_parameters' AFTER `low_balance_threshold`,
ADD COLUMN `sms_low_balance_voice` INT(11) NOT NULL DEFAULT '0' COMMENT 'Reference from the sms_parameters' AFTER `sms_low_balance_data`,
ADD COLUMN `sms_low_balance_sms` INT(11) NOT NULL DEFAULT '0' COMMENT 'Reference from the sms_parameters' AFTER `sms_low_balance_voice`;

-- Adding SMS parameters
INSERT INTO `sms_parameters` (`row_id`, `product`, `description`, `sms`) VALUES (16, 'FREEUP_ATT', 'Low Balance', 'Your remaining Data is running low. You have ${data} MB remaining. Need More data this month? Add-ons are available at freeupmobile.com');
INSERT INTO `sms_parameters` (`row_id`, `product`, `description`, `sms`) VALUES (17, 'FREEUP_ATT', 'Low Balance', 'Your Minutes are running low. You have ${minutes} minutes remaining. Need More Minutes this month? Add-ons are available at freeupmobile.com');
INSERT INTO `sms_parameters` (`row_id`, `product`, `description`, `sms`) VALUES (18, 'FREEUP_ATT', 'Low Balance', 'Your Text messages are running low. You have ${text} Texts remaining. Need More Texts this month? Add-ons are available at freeupmobile.com');
