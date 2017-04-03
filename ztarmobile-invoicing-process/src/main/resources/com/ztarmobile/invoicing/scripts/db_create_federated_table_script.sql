# Creates the connection to the remote database;

CREATE SERVER fedlink
FOREIGN DATA WRAPPER mysql
OPTIONS (USER 'root', HOST 'localhost', PASSWORD 'ztar11', PORT 3306, DATABASE 'ztar');

USE `cdrs`;

# New federated table for the ztar schema.

DROP TABLE IF EXISTS `invoicing_ztar_test_accounts`;
DROP TABLE IF EXISTS `invoicing_ztar_account_subscribers`;
DROP TABLE IF EXISTS `invoicing_ztar_us_rate_plan_billing`;
DROP TABLE IF EXISTS `invoicing_ztar_scythe_notes`;

CREATE TABLE `invoicing_ztar_test_accounts` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key for this table',
  `mdn` varchar(45) NOT NULL COMMENT 'The testing mdn',
  `uan` bigint(20) NOT NULL COMMENT 'Primary key of the account table',
  `user` varchar(50) NOT NULL COMMENT 'Record created by...',
  `creation_date` datetime NOT NULL COMMENT 'Date when this mdn was created',
  PRIMARY KEY (`row_id`),
  KEY `mdn_idx` (`mdn`)
) ENGINE=FEDERATED DEFAULT CHARSET=latin1 CONNECTION='fedlink/test_accounts';

CREATE TABLE `invoicing_ztar_account_subscribers` (
  `row_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mdn` char(12) NOT NULL,
  `uan` bigint(20) unsigned NOT NULL,
  `status` char(1) NOT NULL,
  `creation_date` datetime NOT NULL,
  `status_date` datetime NOT NULL,
  PRIMARY KEY (`row_id`),
  KEY `mdn_index` (`mdn`),
  KEY `uan_index` (`uan`),
  KEY `idx_mdn_uan` (`uan`,`mdn`)
) ENGINE=FEDERATED DEFAULT CHARSET=latin1 CONNECTION='fedlink/test_accounts';

CREATE TABLE `invoicing_ztar_us_rate_plan_billing` (
  `row_id` int(11) NOT NULL AUTO_INCREMENT,
  `mdn` varchar(10) NOT NULL,
  `product` varchar(20) NOT NULL,
  `equipment_type` varchar(20) NOT NULL DEFAULT 'GSM',
  `rate_plan` varchar(20) NOT NULL,
  `billing_status` char(1) NOT NULL,
  `billing_start_date` datetime NOT NULL,
  `billing_renewal_date` datetime NOT NULL,
  `billing_end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`row_id`),
  KEY `urpb_mdn` (`mdn`)
) ENGINE=FEDERATED DEFAULT CHARSET=latin1 CONNECTION='fedlink/test_accounts';

CREATE TABLE `invoicing_ztar_scythe_notes` (
  `note_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uan` bigint(20) NOT NULL,
  `market` varchar(20) NOT NULL DEFAULT '',
  `note_timestamp` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `note_created_by` varchar(45) NOT NULL DEFAULT '',
  `note_title` varchar(45) NOT NULL DEFAULT '',
  `note_text` text NOT NULL,
  PRIMARY KEY (`note_id`),
  KEY `scynotes` (`uan`,`market`)
) ENGINE=FEDERATED DEFAULT CHARSET=latin1 CONNECTION='fedlink/test_accounts';
