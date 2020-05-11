/*
 Navicat Premium Data Transfer

 Source Server         : 个人数据库
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 114.55.34.44:3306
 Source Schema         : goods

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 11/05/2020 12:57:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attribute_key
-- ----------------------------
DROP TABLE IF EXISTS `attribute_key`;
CREATE TABLE `attribute_key`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `CATEGORY_ID` int(11) NULL DEFAULT NULL COMMENT '分类ID',
  `ATTRIBUTE_NAME` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名称',
  `NAME_SORT` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称排序',
  `REVISION` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品规格Key表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attribute_key
-- ----------------------------
INSERT INTO `attribute_key` VALUES (1, 11, '内存', '0', 1, NULL, '2019-03-02 15:34:35', '2019-03-02 15:34:35', '2019-03-02 15:34:35');
INSERT INTO `attribute_key` VALUES (2, 11, '颜色', '0', 1, NULL, '2019-03-02 15:34:35', '2019-03-02 15:34:35', '2019-03-02 15:34:35');
INSERT INTO `attribute_key` VALUES (3, 11, '年份', '0', 1, NULL, '2019-03-02 15:34:35', '2019-03-02 15:34:35', '2019-03-02 15:34:35');
INSERT INTO `attribute_key` VALUES (4, 11, '尺寸', '0', 1, NULL, '2019-03-02 15:34:35', '2019-03-02 15:34:35', '2019-03-02 15:34:35');

-- ----------------------------
-- Table structure for attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `attribute_value`;
CREATE TABLE `attribute_value`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `ATTRIBUTE_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性ID',
  `ATTRIBUTE_VALUE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `VALUE_SORT` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值排序',
  `REVISION` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品规格值表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attribute_value
-- ----------------------------
INSERT INTO `attribute_value` VALUES (1, '1', '4G', '0', 1, NULL, '2019-03-02 15:36:27', '2019-03-02 15:36:27', '2019-03-02 15:36:27');
INSERT INTO `attribute_value` VALUES (2, '1', '8G', '0', 1, NULL, '2019-03-02 15:36:42', '2019-03-02 15:36:42', '2019-03-02 15:36:42');
INSERT INTO `attribute_value` VALUES (3, '1', '16G', '0', 1, NULL, '2019-03-02 15:36:43', '2019-03-02 15:36:43', '2019-03-02 15:36:43');
INSERT INTO `attribute_value` VALUES (4, '1', '32G', '0', 1, NULL, '2019-03-02 15:36:43', '2019-03-02 15:36:43', '2019-03-02 15:36:43');
INSERT INTO `attribute_value` VALUES (5, '2', '白色', '0', 1, NULL, '2019-03-02 15:38:55', '2019-03-02 15:38:55', '2019-03-02 15:38:55');
INSERT INTO `attribute_value` VALUES (6, '2', '红色', '0', 1, NULL, '2019-03-02 15:38:55', '2019-03-02 15:38:55', '2019-03-02 15:38:55');
INSERT INTO `attribute_value` VALUES (7, '2', '紫色', '0', 1, NULL, '2019-03-02 15:38:55', '2019-03-02 15:38:55', '2019-03-02 15:38:55');
INSERT INTO `attribute_value` VALUES (8, '3', '2017', '0', 1, NULL, '2019-03-02 15:38:57', '2019-03-02 15:38:57', '2019-03-02 15:38:57');
INSERT INTO `attribute_value` VALUES (9, '3', '2018', '0', 1, NULL, '2019-03-02 15:38:57', '2019-03-02 15:38:57', '2019-03-02 15:38:57');
INSERT INTO `attribute_value` VALUES (10, '3', '2019', '0', 1, NULL, '2019-03-02 15:38:57', '2019-03-02 15:38:57', '2019-03-02 15:38:57');
INSERT INTO `attribute_value` VALUES (11, '3', '16寸', '0', 1, NULL, '2019-03-02 15:38:59', '2019-03-02 15:38:59', '2019-03-02 15:38:59');
INSERT INTO `attribute_value` VALUES (12, '3', '20寸', '0', 1, NULL, '2019-03-02 15:38:59', '2019-03-02 15:38:59', '2019-03-02 15:38:59');
INSERT INTO `attribute_value` VALUES (13, '3', '24寸', '0', 1, NULL, '2019-03-02 15:38:59', '2019-03-02 15:38:59', '2019-03-02 15:38:59');
INSERT INTO `attribute_value` VALUES (14, '3', '32寸', '0', 1, NULL, '2019-03-02 15:38:59', '2019-03-02 15:38:59', '2019-03-02 15:38:59');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `PARENT_ID` int(11) NULL DEFAULT NULL COMMENT '父ID',
  `NAME` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `STATUS` int(11) NULL DEFAULT NULL COMMENT '状态',
  `SORT_ORDER` int(11) NULL DEFAULT NULL COMMENT '分类顺序',
  `REVISION` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品分类 商品分类信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 0, '家用电器', 0, 0, NULL, NULL, '2019-03-02 15:00:57', '2019-03-02 15:00:57', '2019-03-02 15:00:57');
INSERT INTO `category` VALUES (2, 1, '空调', 0, 0, NULL, NULL, '2019-03-02 15:02:08', '2019-03-02 15:02:08', '2019-03-02 15:02:08');
INSERT INTO `category` VALUES (3, 1, '冰箱', 0, 0, NULL, NULL, '2019-03-02 15:02:08', '2019-03-02 15:02:08', '2019-03-02 15:02:08');
INSERT INTO `category` VALUES (4, 1, '洗衣机', 0, 0, NULL, NULL, '2019-03-02 15:02:08', '2019-03-02 15:02:08', '2019-03-02 15:02:08');
INSERT INTO `category` VALUES (5, 1, '生活电器', 0, 0, NULL, NULL, '2019-03-02 15:02:08', '2019-03-02 15:02:08', '2019-03-02 15:02:08');
INSERT INTO `category` VALUES (6, 2, '中央空调', 0, 0, NULL, NULL, '2019-03-02 15:03:19', '2019-03-02 15:03:19', '2019-03-02 15:03:19');
INSERT INTO `category` VALUES (7, 2, '柜式空调', 0, 0, NULL, NULL, '2019-03-02 15:03:19', '2019-03-02 15:03:19', '2019-03-02 15:03:19');
INSERT INTO `category` VALUES (8, 0, '电脑办公', 0, 0, NULL, NULL, '2019-03-02 15:28:39', '2019-03-02 15:28:39', '2019-03-02 15:28:39');
INSERT INTO `category` VALUES (9, 8, '电脑整机', 0, 0, NULL, NULL, '2019-03-02 15:28:49', '2019-03-02 15:28:49', '2019-03-02 15:28:49');
INSERT INTO `category` VALUES (10, 8, '电脑配件', 0, 0, NULL, NULL, '2019-03-02 15:28:49', '2019-03-02 15:28:49', '2019-03-02 15:28:49');
INSERT INTO `category` VALUES (11, 9, '平板电脑', 0, 0, NULL, NULL, '2019-03-02 15:30:10', '2019-03-02 15:30:10', '2019-03-02 15:30:10');
INSERT INTO `category` VALUES (12, 9, '笔记本', 0, 0, NULL, NULL, '2019-03-02 15:30:10', '2019-03-02 15:30:10', '2019-03-02 15:30:10');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `CATEGORY_ID` int(11) NULL DEFAULT NULL COMMENT '类型ID',
  `NAME` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `SUBTITLE` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小标题',
  `MAIN_IMAGE` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主图像',
  `SUB_IMAGES` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '小标题图像',
  `DETAIL` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `ATTRIBUTE_LIST` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品规格',
  `PRICE` decimal(32, 8) NULL DEFAULT NULL COMMENT '价格',
  `STOCK` int(11) NULL DEFAULT NULL COMMENT '库存',
  `STATUS` int(11) NULL DEFAULT NULL COMMENT '状态',
  `REVISION` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 11, 'Pad平板电脑无线局域网', 'Pad平板电脑', 'http://img.iblimg.com/photo-42/3020/1135120490_200x200.jpg', '{\"imgages\":[{\"http://img.iblimg.com/photo-42/3020/1135120490_200x200.jpg\"},{\"http://img.iblimg.com/photo-42/3020/1135120490_200x200.jpg\"}]}', '官方授权Pad苹果电脑', '1,2,3', NULL, NULL, 0, 1, NULL, '2019-03-02 16:02:40', NULL, '2019-03-02 16:02:40');

-- ----------------------------
-- Table structure for product_specs
-- ----------------------------
DROP TABLE IF EXISTS `product_specs`;
CREATE TABLE `product_specs`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `PRODUCT_ID` int(11) NULL DEFAULT NULL COMMENT '商品ID',
  `PRODUCT_SPECS` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品规格',
  `SPECS_SEQ` int(11) NULL DEFAULT NULL COMMENT '规格顺序',
  `PRODUCT_STOCK` int(11) NULL DEFAULT NULL COMMENT '商品库存',
  `PRODUCT_PRICE` decimal(32, 8) NULL DEFAULT NULL COMMENT '商品价格',
  `REVISION` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品规格表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_specs
-- ----------------------------
INSERT INTO `product_specs` VALUES (1, 1, '{\"内存\":\"4G\",\"颜色\":\"红色\",\"年份\":\"2019\",\"尺寸\":\"16寸\"}', 0, 30, 3699.00000000, 1, NULL, '2019-03-02 15:50:04', '2019-03-02 15:50:04', '2019-03-02 15:50:04');
INSERT INTO `product_specs` VALUES (2, 1, '{\"内存\":\"8G\",\"颜色\":\"白色\",\"年份\":\"2019\",\"尺寸\":\"16寸\"}', 0, 30, 3899.00000000, 1, NULL, '2019-03-02 15:50:04', '2019-03-02 15:50:04', '2019-03-02 15:50:04');
INSERT INTO `product_specs` VALUES (3, 1, '{\"内存\":\"16G\",\"颜色\":\"白色\",\"年份\":\"2019\",\"尺寸\":\"16寸\"}', 0, 30, 4199.00000000, 1, NULL, '2019-03-02 15:50:04', '2019-03-02 15:50:04', '2019-03-02 15:50:04');

SET FOREIGN_KEY_CHECKS = 1;
