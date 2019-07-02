/*
Navicat MySQL Data Transfer

Source Server         : STU
Source Server Version : 50549
Source Host           : localhost:3306
Source Database       : mll

Target Server Type    : MYSQL
Target Server Version : 50549
File Encoding         : 65001

Date: 2019-07-02 20:36:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mll_message
-- ----------------------------
DROP TABLE IF EXISTS `mll_message`;
CREATE TABLE `mll_message` (
  `message_id` bigint(20) NOT NULL COMMENT '全局唯一id',
  `message_body` text COMMENT '消息体',
  `expire` date DEFAULT NULL COMMENT '过期时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '消息状态',
  `sendCount` tinyint(4) DEFAULT NULL COMMENT '发送次数',
  `consumeCount` tinyint(4) DEFAULT NULL COMMENT '消费次数',
  `consumeStatus` tinyint(4) DEFAULT NULL COMMENT '消费状态',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息表';

-- ----------------------------
-- Records of mll_message
-- ----------------------------

-- ----------------------------
-- Table structure for mll_miaosha_order
-- ----------------------------
DROP TABLE IF EXISTS `mll_miaosha_order`;
CREATE TABLE `mll_miaosha_order` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `payment` decimal(20,2) DEFAULT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `product_id` bigint(100) NOT NULL COMMENT '商品ID',
  `user_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
  `post_fee` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '订单更新时间',
  `payment_time` datetime DEFAULT NULL COMMENT '付款时间',
  `consign_time` datetime DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `shipping_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流名称',
  `shipping_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流单号',
  `buyer_message` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '买家留言',
  `buyer_nick` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '买家昵称',
  `buyer_rate` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '买家是否已经评价',
  `receiver_area_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人地区名称(省，市，县)街道',
  `receiver_mobile` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机',
  `receiver_zip_code` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人邮编',
  `receiver` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
  `expire` datetime DEFAULT NULL COMMENT '过期时间，定期清理',
  `invoice_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '发票类型(普通发票，电子发票，增值税发票)',
  `source_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端',
  `seller_id` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '商家ID',
  PRIMARY KEY (`order_id`),
  KEY `create_time` (`create_time`),
  KEY `buyer_nick` (`buyer_nick`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='秒杀订单表';

-- ----------------------------
-- Records of mll_miaosha_order
-- ----------------------------
INSERT INTO `mll_miaosha_order` VALUES ('332256352272302080', '200.00', '536563', '06f1092ec765480c98b73e63197cdc43', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352339410944', '200.00', '536563', 'f82f0e498c8f45bca2a594593a759557', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352347799552', '200.00', '536563', '42fc18ab64e54be9b44701821232aac0', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352351993856', '200.00', '536563', 'c29c0e35f74d4cbd94b3948bc079d30b', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352360382464', '200.00', '536563', 'e2554e08af8a436babbfd4772e2e10a5', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352364576768', '200.00', '536563', '61753c1b193448d9bd1a5c3d0bedc85e', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352368771072', '200.00', '536563', 'bbcd36184b4d4dee8a10a59035d454bd', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352372965376', '200.00', '536563', 'fd5d5089514f438aaab21e3005deef69', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352381353984', '200.00', '536563', '6b745a2887af4fd997a45cab608fb5fa', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332256352385548288', '200.00', '536563', '6e0cbf2beb8b44a1b65582c9548d31a1', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `mll_miaosha_order` VALUES ('332944393789947904', '200.00', '1', 'e2c77d32052b4bcba2ce4c8a96c35fd9', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for mll_miaosha_product
-- ----------------------------
DROP TABLE IF EXISTS `mll_miaosha_product`;
CREATE TABLE `mll_miaosha_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` bigint(100) NOT NULL COMMENT '商品ID',
  `title` varchar(50) DEFAULT NULL COMMENT '商品标题',
  `price` decimal(20,2) NOT NULL COMMENT '商品价格，单位为：元',
  `miaosha_price` decimal(20,2) NOT NULL COMMENT '秒杀价格，单位为：元',
  `stock_count` int(10) DEFAULT NULL COMMENT '库存数量',
  `image` varchar(2000) DEFAULT NULL COMMENT '商品图片',
  `start_date` datetime NOT NULL COMMENT '秒杀开始时间',
  `end_date` datetime NOT NULL COMMENT '秒杀结束时间',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `spec` varchar(200) DEFAULT NULL COMMENT '规格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='商品秒杀表';

-- ----------------------------
-- Records of mll_miaosha_product
-- ----------------------------
INSERT INTO `mll_miaosha_product` VALUES ('1', '1', '麻布款沙发套装', '10000.00', '200.00', '9', 'https://image.meilele.com//images/201711/1511318851523581796.jpg', '2019-04-11 20:38:38', '2019-04-11 20:38:38', '美乐乐', '长10000');

-- ----------------------------
-- Table structure for mll_order
-- ----------------------------
DROP TABLE IF EXISTS `mll_order`;
CREATE TABLE `mll_order` (
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `payment` decimal(20,2) DEFAULT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `payment_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '支付类型，1、在线支付，2、货到付款',
  `post_fee` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `status` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '订单更新时间',
  `payment_time` datetime DEFAULT NULL COMMENT '付款时间',
  `consign_time` datetime DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `shipping_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流名称',
  `shipping_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流单号',
  `user_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
  `buyer_message` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '买家留言',
  `buyer_nick` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '买家昵称',
  `buyer_rate` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '买家是否已经评价',
  `receiver_area_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人地区名称(省，市，县)街道',
  `receiver_mobile` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机',
  `receiver_zip_code` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人邮编',
  `receiver` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
  `expire` datetime DEFAULT NULL COMMENT '过期时间，定期清理',
  `invoice_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '发票类型(普通发票，电子发票，增值税发票)',
  `source_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端',
  `seller_id` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '商家ID',
  PRIMARY KEY (`order_id`),
  KEY `create_time` (`create_time`),
  KEY `buyer_nick` (`buyer_nick`),
  KEY `status` (`status`),
  KEY `payment_type` (`payment_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单表';

-- ----------------------------
-- Records of mll_order
-- ----------------------------

-- ----------------------------
-- Table structure for mll_order_item
-- ----------------------------
DROP TABLE IF EXISTS `mll_order_item`;
CREATE TABLE `mll_order_item` (
  `id` bigint(20) NOT NULL,
  `item_id` bigint(20) NOT NULL COMMENT '商品id',
  `goods_id` bigint(20) DEFAULT NULL COMMENT 'SPU_ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `title` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品标题',
  `price` decimal(20,2) DEFAULT NULL COMMENT '商品单价',
  `num` int(10) DEFAULT NULL COMMENT '商品购买数量',
  `total_fee` decimal(20,2) DEFAULT NULL COMMENT '商品总金额',
  `pic_path` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片地址',
  `seller_id` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `item_id` (`item_id`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单项表\r\n';

-- ----------------------------
-- Records of mll_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for mll_product
-- ----------------------------
DROP TABLE IF EXISTS `mll_product`;
CREATE TABLE `mll_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id，同时也是商品编号',
  `title` varchar(100) NOT NULL COMMENT '商品标题',
  `sell_point` varchar(500) DEFAULT NULL COMMENT '商品卖点',
  `price` decimal(20,2) NOT NULL COMMENT '商品价格，单位为：元',
  `stock_count` int(10) DEFAULT NULL COMMENT '库存数量',
  `barcode` varchar(30) DEFAULT NULL COMMENT '商品条形码',
  `image` varchar(2000) DEFAULT NULL COMMENT '商品图片',
  `small_pic` varchar(150) DEFAULT NULL COMMENT '小图',
  `categoryId` bigint(10) NOT NULL COMMENT '所属类目，叶子类目',
  `status` varchar(1) NOT NULL COMMENT '商品状态，1-正常，2-下架，3-删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `category` varchar(200) DEFAULT NULL,
  `brand` varchar(100) DEFAULT NULL,
  `spec` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cid` (`categoryId`),
  KEY `status` (`status`),
  KEY `updated` (`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=536564 DEFAULT CHARSET=utf8 COMMENT='商品表';

-- ----------------------------
-- Records of mll_product
-- ----------------------------
INSERT INTO `mll_product` VALUES ('536563', '麻布款沙发套装', '云水谣系列 中式风格 进口金丝檀木 能工巧匠E7系列 简约时尚 全实木', '1000.00', '10000', '', 'https://image.meilele.com//images/201711/1511318851523581796.jpg', 'https://image.meilele.com//images/201711/1511318851523581796.jpg', '560', '1', '2019-03-08 21:33:18', '2019-04-11 20:38:38', '沙发', '云水谣', '{\"坐高\":\"400mm\",\"面料\":\"麻布\"}');

-- ----------------------------
-- Table structure for userconnection
-- ----------------------------
DROP TABLE IF EXISTS `userconnection`;
CREATE TABLE `userconnection` (
  `userId` varchar(255) NOT NULL,
  `providerId` varchar(255) NOT NULL,
  `providerUserId` varchar(255) NOT NULL DEFAULT '',
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(512) DEFAULT NULL,
  `imageUrl` varchar(512) DEFAULT NULL,
  `accessToken` varchar(512) NOT NULL,
  `secret` varchar(512) DEFAULT NULL,
  `refreshToken` varchar(512) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`userId`,`providerId`,`providerUserId`),
  UNIQUE KEY `UserConnectionRank` (`userId`,`providerId`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userconnection
-- ----------------------------
