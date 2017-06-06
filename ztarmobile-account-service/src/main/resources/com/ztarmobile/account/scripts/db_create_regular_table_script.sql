DROP TABLE IF EXISTS `catalog_resource_set`;
DROP TABLE IF EXISTS `scope_resource_set`;

CREATE TABLE `scope_resource_set` (
  `scope_id` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `catalog_resource_set` (
  `row_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `verb` varchar(20) DEFAULT NULL,
  `resource` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `scope_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`row_id`),
  KEY `scope_id_key` (`scope_id`),
  CONSTRAINT `scope_id_key` FOREIGN KEY (`scope_id`) REFERENCES `scope_resource_set` (`scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
