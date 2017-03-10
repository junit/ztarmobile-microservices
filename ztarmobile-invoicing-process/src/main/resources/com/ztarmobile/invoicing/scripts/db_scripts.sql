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
