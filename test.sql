/*
Navicat MySQL Data Transfer

Source Server         : hmh
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-05-26 22:29:31
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
INSERT INTO `admin` VALUES ('2019', '123456');

-- ----------------------------
-- Table structure for classname
-- ----------------------------
DROP TABLE IF EXISTS `classname`;
CREATE TABLE `classname` (
  `classid` int(11) NOT NULL AUTO_INCREMENT,
  `classname` varchar(255) NOT NULL,
  PRIMARY KEY (`classid`),
  KEY `classname` (`classname`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of classname
-- ----------------------------
INSERT INTO `classname` VALUES ('9', '工程');
INSERT INTO `classname` VALUES ('1', '应用1');
INSERT INTO `classname` VALUES ('2', '应用2');
INSERT INTO `classname` VALUES ('3', '应用3');
INSERT INTO `classname` VALUES ('4', '应用4');
INSERT INTO `classname` VALUES ('5', '应用5');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `course` varchar(255) NOT NULL,
  `tid` int(11) NOT NULL,
  `chapters` varchar(255) NOT NULL DEFAULT '',
  `ifqiandao` varchar(255) DEFAULT '',
  `open` int(1) DEFAULT '1',
  PRIMARY KEY (`cid`),
  KEY `tid` (`tid`),
  CONSTRAINT `ct` FOREIGN KEY (`tid`) REFERENCES `teacher` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', 'JavaEE', '1', '第二章||第三章', '0,0', '0');
INSERT INTO `course` VALUES ('2', '工程实践4', '2', '', '', '0');
INSERT INTO `course` VALUES ('3', 'C语言', '1', '第一章||第二章', '', '0');
INSERT INTO `course` VALUES ('4', '大学英语', '3', '', '', '0');
INSERT INTO `course` VALUES ('5', 'Java高级编程', '2', '', '', '0');
INSERT INTO `course` VALUES ('6', '计算机网络', '1', '', '', '0');

-- ----------------------------
-- Table structure for course_students
-- ----------------------------
DROP TABLE IF EXISTS `course_students`;
CREATE TABLE `course_students` (
  `cid` int(11) NOT NULL,
  `id` bigint(100) NOT NULL,
  `ifjoin` int(11) NOT NULL,
  `scores` varchar(255) DEFAULT '',
  `arrived` varchar(255) DEFAULT '',
  KEY `course_courses` (`cid`),
  KEY `c_students` (`id`),
  CONSTRAINT `c_students` FOREIGN KEY (`id`) REFERENCES `student` (`id`),
  CONSTRAINT `course_courses` FOREIGN KEY (`cid`) REFERENCES `course` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_students
-- ----------------------------
INSERT INTO `course_students` VALUES ('1', '2016051101', '2', '40||50', '2,0');
INSERT INTO `course_students` VALUES ('1', '2016051100', '2', '20||50', '1,2');
INSERT INTO `course_students` VALUES ('2', '2016051100', '1', '', '');
INSERT INTO `course_students` VALUES ('3', '2016051100', '2', '20||100', '');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT,
  `sname` varchar(255) NOT NULL,
  `spsw` varchar(255) NOT NULL,
  `classid` int(11) NOT NULL,
  `ssex` int(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sclass` (`classid`),
  CONSTRAINT `classid_classname` FOREIGN KEY (`classid`) REFERENCES `classname` (`classid`)
) ENGINE=InnoDB AUTO_INCREMENT=2016051107 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('2016051100', '阿豪', '123456', '2', '1');
INSERT INTO `student` VALUES ('2016051101', '阿浩', '123456', '4', '1');
INSERT INTO `student` VALUES ('2016051102', '阿祥', '123456', '3', '0');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `tid` int(20) NOT NULL AUTO_INCREMENT,
  `tname` varchar(255) NOT NULL,
  `tpsw` varchar(255) NOT NULL,
  `tsex` int(1) NOT NULL,
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1', '老王', '123456', '1');
INSERT INTO `teacher` VALUES ('2', '老孙', '123456', '0');
INSERT INTO `teacher` VALUES ('3', 'Aiden', '123456', '1');
