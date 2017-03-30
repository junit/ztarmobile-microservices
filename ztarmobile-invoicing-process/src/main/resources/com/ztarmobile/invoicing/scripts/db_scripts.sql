USE `cdrs`;

# New table that stores all invoicing data.
DROP TABLE IF EXISTS `invoicing_report_details`;

# New table that stores all the products.
DROP TABLE IF EXISTS `invoicing_catalog_product`;

# New table that stores the status of the cdr files.
DROP TABLE IF EXISTS `invoicing_logger_cdr_file`;

# New table that stores the status of the reports.
DROP TABLE IF EXISTS `invoicing_logger_report_file`;

# New table that stores the log of the requests of invoicing.
DROP TABLE IF EXISTS `invoicing_logger_requests`;

CREATE TABLE `invoicing_catalog_product` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `product` VARCHAR(50) NOT NULL,
  `cdma` TINYINT(1) NULL,
  PRIMARY KEY (`row_id`),
  UNIQUE INDEX `product_UNIQUE` (`product` ASC));

INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('ANELTO_ATT', '0');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('DATAWIND', '0');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('GOOD2GOUS', '0');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('GOOD2GOUS-CDMA', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('GOOD2GOUS-TRAVEL', '0');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('JOLT_ATT', '0');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('PIX-CDMA', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('PIX_ATT', '0');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('REALMOBILE', '0');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('REALMOBILE-CDMA', '1');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('STREAM_ATT', '0');
INSERT INTO `invoicing_catalog_product` (`product`, `cdma`) VALUES ('TELBILL', '0');


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
  `request_date` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`row_id`)
  );