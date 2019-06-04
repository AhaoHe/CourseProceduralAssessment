/*
Navicat MySQL Data Transfer

Source Server         : hmh
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-06-04 19:16:08
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
  `open` int(1) DEFAULT '0',
  `scores` varchar(255) DEFAULT '',
  `end` int(1) DEFAULT '0',
  `information` varchar(255) DEFAULT '',
  `type` varchar(255) DEFAULT '',
  `hardness` varchar(255) DEFAULT '',
  PRIMARY KEY (`cid`),
  KEY `tid` (`tid`),
  CONSTRAINT `ct` FOREIGN KEY (`tid`) REFERENCES `teacher` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', 'JavaEE', '1', 'servlet||jsp||eclipse||作用域||实战', '0,0,0', '1', '', '0', 'JavaEE是一套全然不同于传统应用开发的技术架构，包含许多组件，主要可简化且规范应用系统的开发与部署，进而提高可移植性、安全与再用价值。', '1,1,1', '4,3,3,4,5');
INSERT INTO `course` VALUES ('2', '工程实践4', '2', '', '', '0', '', '0', '提升实践能力', '0,0,0', '');
INSERT INTO `course` VALUES ('3', 'C语言', '1', '第一章||第二章', '', '1', '', '0', '入门级课程', '0,0,0', '3,3');
INSERT INTO `course` VALUES ('4', '大学英语', '3', '', '', '0', '', '0', '英语很重要！', '0,0,0', '');
INSERT INTO `course` VALUES ('5', 'Java高级编程', '2', '', '', '0', '', '0', '基础是关键！', '0,0,0', '');
INSERT INTO `course` VALUES ('6', '计算机网络', '1', '', '', '1', '', '0', '计算机网络是指将地理位置不同的具有独立功能的多台计算机及其外部设备，通过通信线路连接起来，在网络操作系统，网络管理软件及网络通信协议的管理和协调下，实现资源共享和信息传递的计算机系统。 ', '0,0,0', '');
INSERT INTO `course` VALUES ('11', '百家讲坛——老子', '6', '', '', '0', '', '0', '无为而治!', '0,0,0', '');
INSERT INTO `course` VALUES ('13', '亚圣讲坛', '9', '', '', '0', '', '0', '天将降大任于斯人也!', '0,1,0', '');

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
INSERT INTO `course_students` VALUES ('1', '2016051102', '2', '75||68||92||80||empty', '2,1,1');
INSERT INTO `course_students` VALUES ('3', '2016051100', '2', '40||50', '');
INSERT INTO `course_students` VALUES ('1', '2016051100', '2', '55||75||100||80||empty', '1,1,1');
INSERT INTO `course_students` VALUES ('3', '2016051101', '2', 'empty||empty', '');
INSERT INTO `course_students` VALUES ('6', '2016051101', '2', '', '');
INSERT INTO `course_students` VALUES ('1', '2016051107', '2', '65||99||99||80||empty', '3,3,1');
INSERT INTO `course_students` VALUES ('1', '2016051108', '2', '62||100||78||80||empty', '1,2,1');
INSERT INTO `course_students` VALUES ('1', '2016051109', '2', '44||89||40||80||empty', '1,2,0');
INSERT INTO `course_students` VALUES ('1', '2016051110', '2', '90||82||70||80||empty', '1,3,1');
INSERT INTO `course_students` VALUES ('1', '2016051111', '2', '88||77||88||80||empty', '1,3,1');
INSERT INTO `course_students` VALUES ('1', '2016051112', '2', '72||30||60||80||empty', '1,0,0');
INSERT INTO `course_students` VALUES ('1', '2016051101', '2', '95||60||90||80||empty', '1,1,1');
INSERT INTO `course_students` VALUES ('1', '2016051113', '2', '100||98||100||80||empty', '1,0,1');
INSERT INTO `course_students` VALUES ('1', '2016051114', '2', '10||35||64||80||empty', '0,0,0');
INSERT INTO `course_students` VALUES ('2', '2016051100', '1', '', '');
INSERT INTO `course_students` VALUES ('6', '2016051100', '1', '', '');

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
) ENGINE=InnoDB AUTO_INCREMENT=2016051117 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('2016051100', '阿豪', '123456', '2', '1');
INSERT INTO `student` VALUES ('2016051101', '阿浩', '123456', '4', '1');
INSERT INTO `student` VALUES ('2016051102', '阿祥', '123456', '3', '0');
INSERT INTO `student` VALUES ('2016051107', '阿晖', '123456', '1', '1');
INSERT INTO `student` VALUES ('2016051108', '阿杨', '123456', '5', '0');
INSERT INTO `student` VALUES ('2016051109', '阿汤', '123456', '9', '1');
INSERT INTO `student` VALUES ('2016051110', '阿朱', '123456', '4', '0');
INSERT INTO `student` VALUES ('2016051111', '阿彪', '123456', '3', '1');
INSERT INTO `student` VALUES ('2016051112', '阿弥陀佛', '123456', '2', '1');
INSERT INTO `student` VALUES ('2016051113', '敖某', '123456', '9', '1');
INSERT INTO `student` VALUES ('2016051114', '爱新觉罗', '123456', '1', '0');
INSERT INTO `student` VALUES ('2016051115', '测试2', '123456', '1', '0');
INSERT INTO `student` VALUES ('2016051116', '阿姨', '123456', '4', '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1', '老王', '123456', '1');
INSERT INTO `teacher` VALUES ('2', '老孙', '123456', '0');
INSERT INTO `teacher` VALUES ('3', 'Aiden', '123456', '1');
INSERT INTO `teacher` VALUES ('5', '孔子', '123456', '1');
INSERT INTO `teacher` VALUES ('6', '老子', '123456', '1');
INSERT INTO `teacher` VALUES ('7', '墨子', '123456', '1');
INSERT INTO `teacher` VALUES ('8', '庄子', '123456', '1');
INSERT INTO `teacher` VALUES ('9', '孟子', '123456', '1');
