# New table that stores all invoicing data.
DROP TABLE IF EXISTS `invocing_details`;

# New table that stores all the products
DROP TABLE IF EXISTS `invoicing_catalog_product`;

CREATE TABLE `cdrs`.`invoicing_catalog_product` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `product` VARCHAR(50) NOT NULL,
  `cdma` TINYINT(1) NULL,
  PRIMARY KEY (`row_id`),
  UNIQUE INDEX `product_UNIQUE` (`product` ASC));

INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('ANELTO_ATT', '0');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('DATAWIND', '0');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('GOOD2GOUS', '0');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('GOOD2GOUS-CDMA', '1');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('GOOD2GOUS-TRAVEL', '0');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('JOLT_ATT', '0');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('PIX-CDMA', '1');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('PIX_ATT', '0');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('REALMOBILE', '0');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('REALMOBILE-CDMA', '1');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('STREAM_ATT', '0');
INSERT INTO `cdrs`.`invoicing_catalog_product` (`product`, `cdma`) VALUES ('TELBILL', '0');


CREATE TABLE `cdrs`.`invocing_details` (
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
  `creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`row_id`),
  KEY `fk_invoicing_product_idx` (`product_id`),
  CONSTRAINT `fk_invoicing_product_idx`
    FOREIGN KEY (`product_id`)
    REFERENCES `cdrs`.`invoicing_catalog_product` (`row_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

