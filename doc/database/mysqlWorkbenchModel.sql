SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `govproject` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `govproject` ;

-- -----------------------------------------------------
-- Table `govproject`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`users` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `login` VARCHAR(32) NOT NULL ,
  `pass` VARCHAR(64) NOT NULL ,
  `level` INT NOT NULL COMMENT '1024表示未审核' ,
  `registerDate` DATETIME NOT NULL ,
  `displayName` VARCHAR(45) NOT NULL ,
  `email` VARCHAR(100) NOT NULL ,
  `url` VARCHAR(100) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `login` (`login` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `govproject`.`posts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`posts` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`posts` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '	' ,
  `user_id` INT NOT NULL ,
  `date` DATETIME NOT NULL ,
  `modifyDate` DATETIME NOT NULL ,
  `commentStatus` TINYINT(1) NOT NULL ,
  `commentCount` INT NOT NULL ,
  `post_parent` INT NULL COMMENT '比如图片等附件所属的文章，或者是自动保存文章的所属文章。' ,
  `status` VARCHAR(20) NOT NULL COMMENT 'publish 已发布\ndraft 草稿\nprivate 私人\nprotect 密码保护\n等等' ,
  `type` VARCHAR(20) NOT NULL COMMENT 'post-普通文章\nalbum-相册\nattachment-附件\nrevision-自动保存文章\n\n等等\n' ,
  `slug` VARCHAR(200) NOT NULL COMMENT '文章的永久链接，必须使用URL编码方式' ,
  `tittle` VARCHAR(255) NOT NULL ,
  `password` VARCHAR(20) NULL ,
  `mime` VARCHAR(45) NULL ,
  `content` TEXT NOT NULL ,
  `excerpt` TEXT NULL COMMENT '文章摘要' ,
  PRIMARY KEY (`id`) ,
  INDEX `userID` (`user_id` ASC) ,
  INDEX `slug` (`slug` ASC) ,
  INDEX `parent` (`post_parent` ASC) ,
  INDEX `type_status_date` (`type` ASC, `status` ASC, `date` ASC, `id` ASC) ,
  CONSTRAINT `userID`
    FOREIGN KEY (`user_id` )
    REFERENCES `govproject`.`users` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `govproject`.`comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`comments` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`comments` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `post_id` INT NOT NULL ,
  `date` DATETIME NOT NULL ,
  `approved` TINYINT(1) NOT NULL ,
  `user_id` INT NULL ,
  `comment_parent` INT NULL ,
  `author` VARCHAR(45) NOT NULL ,
  `authorEmail` VARCHAR(45) NOT NULL ,
  `authorUrl` VARCHAR(200) NOT NULL ,
  `authorIp` VARCHAR(39) NOT NULL ,
  `content` TEXT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `postID` (`post_id` ASC) ,
  INDEX `approved` (`approved` ASC) ,
  INDEX `date` (`date` ASC) ,
  INDEX `parent` (`comment_parent` ASC) ,
  CONSTRAINT `postID`
    FOREIGN KEY (`post_id` )
    REFERENCES `govproject`.`posts` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = big5
COLLATE = big5_chinese_ci
COMMENT = '文章评论';


-- -----------------------------------------------------
-- Table `govproject`.`commentmeta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`commentmeta` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`commentmeta` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `comment_id` INT NOT NULL ,
  `key` VARCHAR(45) NOT NULL ,
  `value` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `commentID` (`comment_id` ASC) ,
  INDEX `key` (`key` ASC) ,
  CONSTRAINT `commentID`
    FOREIGN KEY (`comment_id` )
    REFERENCES `govproject`.`comments` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `govproject`.`postmeta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`postmeta` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`postmeta` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `post_id` INT NOT NULL ,
  `key` VARCHAR(45) NOT NULL ,
  `value` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `postID` (`post_id` ASC) ,
  INDEX `key` (`key` ASC) ,
  CONSTRAINT `postID`
    FOREIGN KEY (`post_id` )
    REFERENCES `govproject`.`posts` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `govproject`.`usermeta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`usermeta` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`usermeta` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `user_id` INT NOT NULL ,
  `key` VARCHAR(45) NOT NULL ,
  `value` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `userID` (`user_id` ASC) ,
  INDEX `key` (`key` ASC) ,
  CONSTRAINT `userID`
    FOREIGN KEY (`user_id` )
    REFERENCES `govproject`.`users` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `govproject`.`links`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`links` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`links` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `url` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `target` VARCHAR(20) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `govproject`.`terms`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`terms` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`terms` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(100) NOT NULL ,
  `slug` VARCHAR(200) NOT NULL ,
  `group` INT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `slug_UNIQUE` (`slug` ASC) ,
  INDEX `name` (`name` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `govproject`.`term_taxonomy`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`term_taxonomy` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`term_taxonomy` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `term_id` INT NOT NULL ,
  `count` INT NOT NULL ,
  `taxonomy_parent` INT NULL ,
  `name` VARCHAR(45) NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `termID` (`term_id` ASC) ,
  INDEX `termID_taxonomy` (`term_id` ASC, `name` ASC) ,
  CONSTRAINT `term`
    FOREIGN KEY (`term_id` )
    REFERENCES `govproject`.`terms` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `govproject`.`term_relationships`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`term_relationships` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`term_relationships` (
  `object_id` INT NOT NULL AUTO_INCREMENT ,
  `taxonomy_id` INT NOT NULL ,
  `termOrder` INT NULL ,
  PRIMARY KEY (`object_id`, `taxonomy_id`) ,
  INDEX `postRelationship` (`object_id` ASC) ,
  INDEX `linkRelationship` (`object_id` ASC) ,
  INDEX `taxonomy` (`taxonomy_id` ASC) ,
  CONSTRAINT `postRelationship`
    FOREIGN KEY (`object_id` )
    REFERENCES `govproject`.`posts` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `linkRelationship`
    FOREIGN KEY (`object_id` )
    REFERENCES `govproject`.`links` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `taxonomy`
    FOREIGN KEY (`taxonomy_id` )
    REFERENCES `govproject`.`term_taxonomy` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'http://codex.wordpress.org/Database_Description 和 http://cod' /* comment truncated */;


-- -----------------------------------------------------
-- Table `govproject`.`options`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `govproject`.`options` ;

CREATE  TABLE IF NOT EXISTS `govproject`.`options` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `key` VARCHAR(64) NOT NULL ,
  `value` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `key_UNIQUE` (`key` ASC) )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
