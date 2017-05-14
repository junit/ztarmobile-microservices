USE `cdrs`;

# New table that stores all invoicing data.
DROP TABLE IF EXISTS `invoicing_report_details`;

# New table to store the relation between email and products
DROP TABLE IF EXISTS `invoicing_email2product`;

# New table that stores all the products.
DROP TABLE IF EXISTS `invoicing_catalog_product`;

# New table that stores the status of the CDR files.
DROP TABLE IF EXISTS `invoicing_logger_cdr_file`;

# New table that stores the status of the reports.
DROP TABLE IF EXISTS `invoicing_logger_report_file`;

# New table that stores the log of the requests of invoicing.
DROP TABLE IF EXISTS `invoicing_logger_requests`;

# New table that stores all the e-mails.
DROP TABLE IF EXISTS `invoicing_catalog_email`;

CREATE TABLE `invoicing_catalog_product` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `product` VARCHAR(50) NOT NULL,
  `cdma` TINYINT(1) NULL COMMENT 'Is this a cdma version? (sprint)',
  `invoicing_enabled` TINYINT(1) NULL COMMENT 'Enable or disable invoicing for this product',
  PRIMARY KEY (`row_id`),
  UNIQUE INDEX `product_UNIQUE` (`product` ASC));

INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('ANELTO_ATT', '0', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('DATAWIND', '0', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('GOOD2GOUS', '0', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('GOOD2GOUS-CDMA', '1', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('GOOD2GOUS-TRAVEL', '0', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('JOLT-CDMA', '1', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('JOLT_ATT', '0', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('PIX-CDMA', '1', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('PIX_ATT', '0', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('REALMOBILE', '0', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('REALMOBILE-CDMA', '1', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('STREAM_ATT', '0', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`, `invoicing_enabled`) VALUES ('TELBILL', '0', '1');


CREATE TABLE `invoicing_report_details` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `year` INT NOT NULL,
  `month` INT NOT NULL,
  `day` INT NOT NULL,
  `mdn` VARCHAR(10) NOT NULL,
  `rate_plan` VARCHAR(50) NOT NULL,
  `days_on_plan` INT NOT NULL,
  `mou` INT NOT NULL,
  `mbs` INT NOT NULL,
  `sms` INT NOT NULL,
  `mms` INT NOT NULL,
  `creation_date` DATETIME NOT NULL DEFAULT NOW(),
  `update_date` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`row_id`),
  UNIQUE INDEX `unique_idx` (`product_id` ASC, `year` ASC, `month` ASC, `day` ASC, `mdn` ASC, `rate_plan` ASC),
  KEY `fk_invoicing_product_idx` (`product_id`),
  CONSTRAINT `fk_invoicing_product_idx`
    FOREIGN KEY (`product_id`)
    REFERENCES `invoicing_catalog_product` (`row_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

    
CREATE TABLE `invoicing_logger_cdr_file` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `source_file_name` VARCHAR(100) NOT NULL,
  `target_file_name` VARCHAR(100) NOT NULL,
  `file_type` ENUM ('E', 'S') NOT NULL COMMENT 'E = EricssonDumpFiles, S = SprintDumpFiles',
  `status` ENUM ('C', 'R', 'E') NOT NULL COMMENT 'C = Completed, R = Reload, E = Error',
  `error_description` LONGTEXT COMMENT 'This column is populated when the status is E',
  `load_date` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`row_id`),
  UNIQUE INDEX `unique_idx` (`source_file_name` ASC));
  

CREATE TABLE `invoicing_logger_report_file` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `report_date` DATE NOT NULL,
  `status_allocations` ENUM ('P', 'C', 'R', 'E') NOT NULL COMMENT 'P = Pending, C = Completed, R = Reload, E = Error',
  `status_usage` ENUM ('P', 'C', 'R', 'E') NOT NULL COMMENT 'P = Pending, C = Completed, R = Reload, E = Error',
  `error_description` LONGTEXT COMMENT 'This column is populated when the status is E',
  `processed_date` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`row_id`),
  UNIQUE INDEX `unique_idx` (`product_id` ASC, `report_date` ASC));


CREATE TABLE `invoicing_logger_requests` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL DEFAULT 0,
  `report_date_from` DATE NOT NULL,
  `report_date_to` DATE NOT NULL,
  `total_time` BIGINT NOT NULL,
  `status` ENUM ('P', 'C', 'E') NOT NULL COMMENT 'P = Progress, C = Completed, E = Error',
  `error_description` LONGTEXT COMMENT 'This column is populated when the status is E',
  `friendly_error_description` VARCHAR(200) COMMENT 'This column is populated when the status is E',
  `request_date` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`row_id`)
  );
  
CREATE TABLE `invoicing_catalog_email` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `first_name` VARCHAR(100) NOT NULL,
  `last_name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`row_id`)
  );

INSERT INTO `invoicing_catalog_email` (`email`, `first_name`, `last_name`) VALUES ('rivasarmando271084@icloud.com', 'Armando', 'Rivas');
INSERT INTO `invoicing_catalog_email` (`email`, `first_name`, `last_name`) VALUES ('arivas@ztarmobile.com', 'Armando', 'Rivas');
INSERT INTO `invoicing_catalog_email` (`email`, `first_name`, `last_name`) VALUES ('yjoyas@ztarmobile.com', 'Yael', 'Joyas');
INSERT INTO `invoicing_catalog_email` (`email`, `first_name`, `last_name`) VALUES ('khaddad@ztarmobile.com', 'Kevin', 'Haddad');
INSERT INTO `invoicing_catalog_email` (`email`, `first_name`, `last_name`) VALUES ('lkorte@ztarmobile.com', 'Lisa', 'Korte');
INSERT INTO `invoicing_catalog_email` (`email`, `first_name`, `last_name`) VALUES ('csepmoree@ztarmobile.com', 'Cheryl', 'Sepmoree');
INSERT INTO `invoicing_catalog_email` (`email`, `first_name`, `last_name`) VALUES ('driewski@ztarmobile.com', 'Douglas', 'Riewski');
INSERT INTO `invoicing_catalog_email` (`email`, `first_name`, `last_name`) VALUES ('matt_callahan@ztarmobile.com', 'Matt', 'Callahan');

CREATE TABLE `invoicing_email2product` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `email_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `notification_enabled` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`row_id`),
  INDEX `row_id_idx` (`email_id` ASC),
  INDEX `product_id_idx` (`product_id` ASC),
  CONSTRAINT `fk_email_id`
    FOREIGN KEY (`email_id`)
    REFERENCES `invoicing_catalog_email` (`row_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `invoicing_catalog_product` (`row_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '1', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '2', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '3', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '4', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '5', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '6', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '7', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '8', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '9', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '10', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '11', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '12', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('1', '13', '1');

INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '1', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '2', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '3', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '4', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '5', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '6', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '7', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '8', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '9', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '10', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '11', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '12', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('2', '13', '1');

INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '1', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '2', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '3', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '4', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '5', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '6', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '7', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '8', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '9', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '10', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '11', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '12', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('3', '13', '1');

INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('4', '1', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('4', '2', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('4', '8', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('4', '9', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('4', '10', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('4', '11', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('4', '12', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('4', '13', '1');

INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '1', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '2', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '6', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '7', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '8', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '9', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '10', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '11', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '12', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('5', '13', '1');

INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('6', '10', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('6', '11', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('6', '12', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('6', '13', '1');

INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '1', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '2', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '3', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '4', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '5', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '6', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '7', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '8', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '9', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '10', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '11', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '12', '1');
INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('7', '13', '1');

INSERT INTO `invoicing_email2product` (`email_id`, `product_id`, `notification_enabled`) VALUES ('8', '12', '1');
