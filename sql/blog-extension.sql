/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : localhost:3306
Source Database       : blog-extension

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-05-30 21:50:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for crawled_blog
-- ----------------------------
DROP TABLE IF EXISTS `crawled_blog`;
CREATE TABLE `crawled_blog` (
  `blog_id` bigint(20) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `content` longtext,
  `create_time` datetime DEFAULT NULL,
  `first_picture` varchar(255) DEFAULT NULL,
  `thumbs` int(10) DEFAULT NULL,
  `views` int(10) DEFAULT NULL,
  `description` varchar(255) DEFAULT '这是一篇从CSDN上爬取下来的博客，仅供学习使用，如侵权请发邮件到1626680964@qq.com，联系本人立马删除，谢谢！',
  `avatar` varchar(255) DEFAULT NULL,
  `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
  `link_id` bigint(20) NOT NULL COMMENT '友链id',
  `link_name` varchar(50) NOT NULL COMMENT '友链名称',
  `avatar_link` varchar(255) NOT NULL COMMENT '友链头像地址',
  `blog_link` varchar(255) NOT NULL COMMENT '友链地址',
  `description` varchar(255) NOT NULL COMMENT '友链博客描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '友链展示状态',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `mid` bigint(20) NOT NULL COMMENT '留言id',
  `time` varchar(255) NOT NULL COMMENT '弹幕过屏时间',
  `message_content` varchar(255) NOT NULL COMMENT '留言内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `avatar` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
