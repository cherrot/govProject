SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `govproject` ;
CREATE SCHEMA IF NOT EXISTS `govproject` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `govproject` ;

-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users` ;

CREATE  TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `login` VARCHAR(32) NOT NULL ,
  `pass` VARCHAR(64) NOT NULL ,
  `userLevel` INT NOT NULL COMMENT '1024表示未审核' ,
  `registerDate` DATETIME NOT NULL ,
  `displayName` VARCHAR(45) NOT NULL ,
  `email` VARCHAR(100) NOT NULL ,
  `url` VARCHAR(100) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `login` (`login` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `posts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `posts` ;

CREATE  TABLE IF NOT EXISTS `posts` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '	' ,
  `user_id` INT NOT NULL ,
  `createDate` DATETIME NOT NULL ,
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
  INDEX `post_users` (`user_id` ASC) ,
  INDEX `slug` (`slug` ASC) ,
  INDEX `parent` (`post_parent` ASC) ,
  INDEX `type_status_date` (`type` ASC, `status` ASC, `createDate` ASC, `id` ASC) ,
  INDEX `post_parents` (`post_parent` ASC) ,
  CONSTRAINT `post_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `users` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `post_parent`
    FOREIGN KEY (`post_parent` )
    REFERENCES `posts` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `comments` ;

CREATE  TABLE IF NOT EXISTS `comments` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `post_id` INT NOT NULL ,
  `commentDate` DATETIME NOT NULL ,
  `approved` TINYINT(1) NOT NULL ,
  `user_id` INT NULL ,
  `comment_parent` INT NULL ,
  `author` VARCHAR(45) NOT NULL ,
  `authorEmail` VARCHAR(45) NOT NULL ,
  `authorUrl` VARCHAR(200) NOT NULL ,
  `authorIp` VARCHAR(39) NOT NULL ,
  `content` TEXT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `comment_posts` (`post_id` ASC) ,
  INDEX `approved` (`approved` ASC) ,
  INDEX `date` (`commentDate` ASC) ,
  INDEX `parent` (`comment_parent` ASC) ,
  INDEX `comment_parents` (`comment_parent` ASC) ,
  CONSTRAINT `comment_post`
    FOREIGN KEY (`post_id` )
    REFERENCES `posts` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `comment_parent`
    FOREIGN KEY (`comment_parent` )
    REFERENCES `comments` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = big5
COLLATE = big5_chinese_ci
COMMENT = '文章评论';


-- -----------------------------------------------------
-- Table `commentmeta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `commentmeta` ;

CREATE  TABLE IF NOT EXISTS `commentmeta` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `comment_id` INT NOT NULL ,
  `metaKey` VARCHAR(45) NOT NULL ,
  `metaValue` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `commentmeta_comment` (`comment_id` ASC) ,
  INDEX `key` (`metaKey` ASC) ,
  CONSTRAINT `commentmeta_comment`
    FOREIGN KEY (`comment_id` )
    REFERENCES `comments` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `postmeta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `postmeta` ;

CREATE  TABLE IF NOT EXISTS `postmeta` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `post_id` INT NOT NULL ,
  `metaKey` VARCHAR(45) NOT NULL ,
  `metaValue` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `postmeta_post` (`post_id` ASC) ,
  INDEX `key` (`metaKey` ASC) ,
  CONSTRAINT `postmeta_post`
    FOREIGN KEY (`post_id` )
    REFERENCES `posts` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usermeta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `usermeta` ;

CREATE  TABLE IF NOT EXISTS `usermeta` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `user_id` INT NOT NULL ,
  `metaKey` VARCHAR(45) NOT NULL ,
  `metaValue` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `usermeta_user` (`user_id` ASC) ,
  INDEX `key` (`metaKey` ASC) ,
  CONSTRAINT `usermeta_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `users` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `link_categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `link_categories` ;

CREATE  TABLE IF NOT EXISTS `link_categories` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `count` INT NOT NULL ,
  `name` VARCHAR(100) NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `links`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `links` ;

CREATE  TABLE IF NOT EXISTS `links` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `category_id` INT NOT NULL ,
  `url` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `target` VARCHAR(20) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `link_category_id` (`category_id` ASC) ,
  CONSTRAINT `link_category_id`
    FOREIGN KEY (`category_id` )
    REFERENCES `link_categories` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `terms`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `terms` ;

CREATE  TABLE IF NOT EXISTS `terms` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `count` INT NOT NULL ,
  `term_parent` INT NULL ,
  `type` VARCHAR(8) NULL ,
  `name` VARCHAR(100) NULL ,
  `slug` VARCHAR(200) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `term_parents` (`term_parent` ASC) ,
  UNIQUE INDEX `slug_UNIQUE` (`slug` ASC) ,
  CONSTRAINT `term_parent`
    FOREIGN KEY (`term_parent` )
    REFERENCES `terms` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'terms 和 term_taxonomy分离解决 分类名、链接分类、tag不能重名的问题\n\nThis table de' /* comment truncated */;


-- -----------------------------------------------------
-- Table `term_relationships`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `term_relationships` ;

CREATE  TABLE IF NOT EXISTS `term_relationships` (
  `post_id` INT NOT NULL AUTO_INCREMENT ,
  `term_id` INT NOT NULL ,
  `termOrder` INT NULL ,
  PRIMARY KEY (`post_id`, `term_id`) ,
  INDEX `relationship_post` (`post_id` ASC) ,
  INDEX `relationship_term` (`term_id` ASC) ,
  CONSTRAINT `relationship_post`
    FOREIGN KEY (`post_id` )
    REFERENCES `posts` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `relationship_term`
    FOREIGN KEY (`term_id` )
    REFERENCES `terms` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'http://codex.wordpress.org/Database_Description 和 http://cod' /* comment truncated */;


-- -----------------------------------------------------
-- Table `options`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `options` ;

CREATE  TABLE IF NOT EXISTS `options` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `optionKey` VARCHAR(64) NOT NULL ,
  `optionValue` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `key_UNIQUE` (`optionKey` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `site_logs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `site_logs` ;

CREATE  TABLE IF NOT EXISTS `site_logs` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `logDate` DATETIME NOT NULL ,
  `user_id` INT NOT NULL ,
  `logOperation` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `user_site_log` (`user_id` ASC) ,
  CONSTRAINT `user_site_log`
    FOREIGN KEY (`user_id` )
    REFERENCES `users` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
