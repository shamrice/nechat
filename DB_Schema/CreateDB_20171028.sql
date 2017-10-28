-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema nechat
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema nechat
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `nechat` DEFAULT CHARACTER SET utf8 ;
USE `nechat` ;

-- -----------------------------------------------------
-- Table `nechat`.`auth_tokens`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nechat`.`auth_tokens` (
  `idauth_tokens` INT(11) NOT NULL AUTO_INCREMENT,
  `idusers` INT(11) NOT NULL,
  `auth_token` VARCHAR(45) NOT NULL,
  `create_dt` DATETIME NOT NULL,
  `expire_dt` DATETIME NOT NULL,
  PRIMARY KEY (`idauth_tokens`),
  INDEX `idusers_idx` (`idusers` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `nechat`.`buddies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nechat`.`buddies` (
  `idbuddies` INT(11) NOT NULL AUTO_INCREMENT,
  `idusers` INT(11) NOT NULL,
  `idusers_buddy` INT(11) NOT NULL,
  `accepted` BIT(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`idbuddies`),
  UNIQUE INDEX `idusers_buddy_UNIQUE` (`idusers_buddy` ASC),
  UNIQUE INDEX `idbuddies_UNIQUE` (`idbuddies` ASC),
  INDEX `idusers` (`idbuddies` ASC, `idusers` ASC, `idusers_buddy` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `nechat`.`messages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nechat`.`messages` (
  `idmessages` INT(11) NOT NULL AUTO_INCREMENT,
  `idusers` INT(11) NOT NULL,
  `from_idusers` INT(11) NOT NULL,
  `message` VARCHAR(100) NULL DEFAULT NULL,
  `is_read` BIT(1) NOT NULL DEFAULT b'0',
  `create_dt` DATETIME NOT NULL,
  PRIMARY KEY (`idmessages`),
  INDEX `ix_idusers` (`idusers` ASC),
  INDEX `ix_from_idusers` (`from_idusers` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `nechat`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nechat`.`users` (
  `idusers` INT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `create_dt` DATETIME NULL DEFAULT NULL,
  `modified_dt` DATETIME NULL DEFAULT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `deleted_dt` DATETIME NULL DEFAULT NULL,
  `deleted` BIT(1) NOT NULL DEFAULT b'0',
  `locked` BIT(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`idusers`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;

USE `nechat` ;

-- -----------------------------------------------------
-- procedure delete_expired_tokens
-- -----------------------------------------------------

DELIMITER $$
USE `nechat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_expired_tokens`()
BEGIN
DELETE FROM auth_tokens WHERE expire_dt < CURRENT_TIMESTAMP();
END$$

DELIMITER ;
USE `nechat`;

DELIMITER $$
USE `nechat`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `nechat`.`auth_tokens_BEFORE_INSERT`
BEFORE INSERT ON `nechat`.`auth_tokens`
FOR EACH ROW
SET NEW.create_dt = NOW()$$

USE `nechat`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `nechat`.`messages_BEFORE_INSERT`
BEFORE INSERT ON `nechat`.`messages`
FOR EACH ROW
SET NEW.create_dt = NOW()$$

USE `nechat`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `nechat`.`users_BEFORE_INSERT`
BEFORE INSERT ON `nechat`.`users`
FOR EACH ROW
SET NEW.create_dt = NOW(),
NEW.modified_dt = NOW()$$

USE `nechat`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `nechat`.`users_BEFORE_UPDATE`
BEFORE UPDATE ON `nechat`.`users`
FOR EACH ROW
SET NEW.modified_dt = NOW()$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
