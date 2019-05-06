/*
Navicat MySQL Data Transfer

Source Server         : hmh
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-05-06 23:27:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `aid` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('2019', '123456789');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `course` varchar(255) NOT NULL,
  `tid` int(11) NOT NULL,
  PRIMARY KEY (`cid`),
  KEY `tid` (`tid`),
  CONSTRAINT `ct` FOREIGN KEY (`tid`) REFERENCES `teacher` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', 'JavaEE', '1');
INSERT INTO `course` VALUES ('2', '工程实践4', '2');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sname` varchar(255) NOT NULL,
  `spsw` varchar(255) NOT NULL,
  `sclass` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2016051103 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('2016051100', '阿豪', '123456', '3');
INSERT INTO `student` VALUES ('2016051101', '阿浩', '12356', '4');
INSERT INTO `student` VALUES ('2016051102', '阿祥', '123', '3');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `tid` int(20) NOT NULL AUTO_INCREMENT,
  `tname` varchar(255) NOT NULL,
  `tpsw` varchar(255) NOT NULL,
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1', '老王', '123456');
INSERT INTO `teacher` VALUES ('2', '老孙', '654321');
INSERT INTO `teacher` VALUES ('3', 'Aiden', '666');
