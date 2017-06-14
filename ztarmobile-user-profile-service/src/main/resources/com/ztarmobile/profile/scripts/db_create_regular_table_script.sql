-- This script must be executed only once.

CREATE SCHEMA `account`;
USE `account`;

-- The user profiles table is now created.
CREATE TABLE `user_profiles` (
  `row_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- The addresses table is now created.
CREATE TABLE `addresses` (
  `row_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `line1` varchar(100) NOT NULL,
  `line2` varchar(100) DEFAULT NULL,
  `line3` varchar(100) DEFAULT NULL,
  `city` varchar(100) NOT NULL,
  `state` varchar(2) NOT NULL,
  `zip` varchar(15) NOT NULL,
  `country` varchar(100) DEFAULT NULL,
  `is_primary` TINYINT(1) NOT NULL,
  `user_profile_id` bigint(20) NOT NULL,
  PRIMARY KEY (`row_id`),
  KEY `fk_addresses-user_profiles-user_profile_id` (`user_profile_id`),
  CONSTRAINT `fk_addresses-user_profiles-user_profile_id` FOREIGN KEY (`user_profile_id`) REFERENCES `user_profiles` (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- The mdns table is now created.
CREATE TABLE `mdns` (
  `row_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(10) NOT NULL,
  `user_profile_id` bigint(20) NOT NULL,
  PRIMARY KEY (`row_id`),
  KEY `fk_mdns-user_profiles-user_profile_id` (`user_profile_id`),
  CONSTRAINT `fk_mdns-user_profiles-user_profile_id` FOREIGN KEY (`user_profile_id`) REFERENCES `user_profiles` (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- The payment profiles table is now created.
CREATE TABLE `payment_profiles` (
  `row_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alias` varchar(50) NOT NULL,
  `is_primary` TINYINT(1) NOT NULL,
  `exp_date` varchar(4) NOT NULL,
  `profile_key` varchar(50) NOT NULL,
  `user_profile_id` bigint(20) NOT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`row_id`),
  KEY `fk_payment_profiles-user_profiles-user_profile_id` (`user_profile_id`),
  KEY `fk_payment_profiles-addresses-address_id` (`address_id`),
  CONSTRAINT `fk_payment_profiles-user_profiles-user_profile_id` FOREIGN KEY (`user_profile_id`) REFERENCES `user_profiles` (`row_id`),
  CONSTRAINT `fk_payment_profiles-addresses-address_id` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

