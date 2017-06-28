-- This script must be executed only once.

USE `ztar`;
DROP TABLE IF EXISTS `us_customer_balances`;

CREATE TABLE `us_customer_balances` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `mdn` varchar(10) NOT NULL,
  `data` varchar(25) NOT NULL,
  `voice` varchar(25) NOT NULL,
  `sms` varchar(25) NOT NULL,
  `mms` varchar(25) NOT NULL,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` char(1) NOT NULL DEFAULT 'S',
  `status_message` text,
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `mdn_index` (`mdn`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1