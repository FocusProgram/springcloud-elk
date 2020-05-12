/*
 Navicat Premium Data Transfer

 Source Server         : 个人数据库
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 114.55.34.44:3306
 Source Schema         : elk

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 12/05/2020 09:21:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Mr.Kong', '2020-05-08 15:30:41');
INSERT INTO `user` VALUES (2, 'Ms.Shao', '2020-05-08 15:36:19');

SET FOREIGN_KEY_CHECKS = 1;
