/*
 Navicat Premium Data Transfer
 by 车江毅

 Date: 21/10/2022 13:35:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_log`;
CREATE TABLE `tb_log`  (
                           `id` int(0) NOT NULL AUTO_INCREMENT,
                           `task_id` int(0) NULL DEFAULT NULL,
                           `node` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                           `type` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                           `create_time` datetime(0) NULL DEFAULT NULL,
                           `message` text CHARACTER SET utf8mb4  NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_node
-- ----------------------------
DROP TABLE IF EXISTS `tb_node`;
CREATE TABLE `tb_node`  (
                            `id` int(0) NOT NULL AUTO_INCREMENT,
                            `node` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                            `cpu` decimal(10, 2) NULL DEFAULT NULL,
                            `memory` decimal(10, 2) NULL DEFAULT NULL,
                            `threads` int(0) NULL DEFAULT NULL,
                            `ip` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                            `heatbeat_time` datetime(0) NULL DEFAULT NULL,
                            `port` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                            `local_cpu` int(0) NULL DEFAULT NULL,
                            `local_memory` int(0) NULL DEFAULT NULL,
                            `used` bit(1) NULL DEFAULT b'0' COMMENT '节点状态 1:使用中 0:未使用',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_report
-- ----------------------------
DROP TABLE IF EXISTS `tb_report`;
CREATE TABLE `tb_report`  (
                              `id` int(0) NOT NULL AUTO_INCREMENT,
                              `report_name` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL,
                              `tran_id` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '事务id',
                              `task_id` int(0) NULL DEFAULT NULL,
                              `nodes` varchar(2048) CHARACTER SET utf8mb4 NULL DEFAULT NULL,
                              `nodes_info` text CHARACTER SET utf8mb4 NULL COMMENT '节点配置信息',
                              `filter_table` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL,
                              `filter_store` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL,
                              `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                              `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                              `create_time` datetime(0) NULL DEFAULT NULL,
                              `report_node_table` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'report_node 表',
                              `report_url_table` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'report_url表',
                              `task_name` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL,
                              `filter_table_lines` int(0) NULL DEFAULT NULL,
                              `filter_table_error_lines` int(0) NULL DEFAULT NULL,
                              `create_user` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '用户',
                              `create_user_id` int(0) NULL DEFAULT NULL COMMENT '用户id',
                              `remark` text CHARACTER SET utf8mb4 NULL COMMENT '备注信息',
                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE INDEX `事务id`(`tran_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 292 CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_report_node_example
-- ----------------------------
DROP TABLE IF EXISTS `tb_report_node_example`;
CREATE TABLE `tb_report_node_example`  (
                                           `id` int(0) NOT NULL AUTO_INCREMENT,
                                           `node` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                                           `cpu` decimal(10, 2) NULL DEFAULT NULL,
                                           `memory` decimal(10, 2) NULL DEFAULT NULL,
                                           `network_read` decimal(10, 2) NULL DEFAULT NULL,
                                           `network_write` decimal(10, 2) NULL DEFAULT NULL,
                                           `active_threads` int(0) NULL DEFAULT NULL,
                                           `throughput` decimal(10, 2) NULL DEFAULT NULL,
                                           `error` decimal(10, 2) NULL DEFAULT NULL,
                                           `create_time` datetime(0) NULL DEFAULT NULL,
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1759 CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_report_url_example
-- ----------------------------
DROP TABLE IF EXISTS `tb_report_url_example`;
CREATE TABLE `tb_report_url_example`  (
                                          `id` int(0) NOT NULL AUTO_INCREMENT,
                                          `url` varchar(1024) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                                          `node` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                                          `visit_num` decimal(10, 2) NULL DEFAULT NULL COMMENT '访问次数',
                                          `throughput` decimal(10, 2) NULL DEFAULT NULL COMMENT '吞吐量/s',
                                          `error` decimal(10, 2) NULL DEFAULT NULL COMMENT '错误量/s',
                                          `visit_time` decimal(10, 2) NULL DEFAULT NULL COMMENT 'avg 访问耗时/s ',
                                          `network_write` decimal(10, 2) NULL DEFAULT NULL COMMENT '网络写/s',
                                          `network_read` decimal(10, 2) NULL DEFAULT NULL COMMENT '网络读/s',
                                          `create_time` datetime(0) NULL DEFAULT NULL,
                                          `attribute` json NULL COMMENT '属性',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 792 CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_sample_example
-- ----------------------------
DROP TABLE IF EXISTS `tb_sample_example`;
CREATE TABLE `tb_sample_example`  (
                                      `id` bigint(0) NOT NULL AUTO_INCREMENT,
                                      `url` varchar(1024) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                                      `app_name` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                                      `header` json NULL,
                                      `body` text CHARACTER SET utf8mb4  NULL,
                                      `create_time` datetime(0) NULL DEFAULT NULL,
                                      `fromip` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                                      `traceid` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                                      `trace_top` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '是否是顶部trace',
                                      `method` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                                      `operator_type` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '枚举:未知,操作,仅查询',
                                      `attribute` json NULL COMMENT '属性',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 427 CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_task
-- ----------------------------
DROP TABLE IF EXISTS `tb_task`;
CREATE TABLE `tb_task`  (
                            `id` int(0) NOT NULL AUTO_INCREMENT,
                            `task` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '任务',
                            `filter_store` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '存储引擎',
                            `run_heart_time` datetime(0) NULL DEFAULT NULL COMMENT '运行时间',
                            `create_user` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                            `update_user` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '更新人',
                            `exec_result` text CHARACTER SET utf8mb4  NULL COMMENT '执行结果',
                            `filter_script` text CHARACTER SET utf8mb4  NULL COMMENT '过滤筛选样本脚本',
                            `first_filter_error_script` text CHARACTER SET utf8mb4  NULL COMMENT '第一次执行过滤错误脚本',
                            `run_threads_count` int(0) NULL DEFAULT NULL COMMENT '运行线程数',
                            `http_begin_script` text CHARACTER SET utf8mb4  NULL COMMENT '运行前脚本',
                            `http_end_script` text CHARACTER SET utf8mb4  NULL COMMENT '运行后脚本',
                            `check_stop_script` text CHARACTER SET utf8mb4  NULL COMMENT '检测终止脚本',
                            `sleep_time_every_thread` int(0) NULL DEFAULT 0 COMMENT '每个线程启动时的间隔时间',
                            `node_count` int(0) NULL DEFAULT NULL COMMENT '节点数量',
                            `run_nodes` text CHARACTER SET utf8mb4  NULL COMMENT '运行节点',
                            `use_http_keepalive` bit(1) NULL DEFAULT b'0' COMMENT '是否使用http keepalive',
                            `create_user_id` int(0) NULL DEFAULT NULL COMMENT '创建用户id',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_job
-- ----------------------------
DROP TABLE IF EXISTS `tb_job`;
CREATE TABLE `tb_job`  (
                           `id` int(0) NOT NULL AUTO_INCREMENT,
                           `title` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                           `remark` text CHARACTER SET utf8mb4  NULL COMMENT '描述',
                           `corn` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT 'corn表达式',
                           `state` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '运行状态:运行,停止',
                           `jscript` text CHARACTER SET utf8mb4  NULL COMMENT '脚本',
                           `create_time` datetime(0) NULL DEFAULT NULL,
                           `create_user` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                           `create_user_id` int(0) NULL DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
                            `id` int(0) NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                            `pwd` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                            `create_time` datetime(0) NULL DEFAULT NULL,
                            `role` tinyint(0) NULL DEFAULT 0 COMMENT '0:普通用户 1:管理员',
                            `limit_node_count` int(0) NULL DEFAULT 0 COMMENT '限制节点数',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_config`;
CREATE TABLE `tb_config`  (
                              `dic_key` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL,
                              `dic_value` text CHARACTER SET utf8mb4  NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;

-- ----------------------------
-- 添加配置
-- ----------------------------
INSERT INTO `tb_config` VALUES ('public_code', 'function demo(){}');

-- 添加管理员账户
INSERT INTO `tb_user`( `name`, `pwd`, `create_time`,`role`,`limit_node_count`) VALUES ('admin', 'admin', now(),1,200);

-- 添加任务简单模板
INSERT INTO `tb_task`(`task`, `filter_store`, `run_heart_time`, `create_user`, `create_time`, `update_time`, `update_user`, `exec_result`, `filter_script`, `first_filter_error_script`, `run_threads_count`, `http_begin_script`, `http_end_script`, `check_stop_script`, `sleep_time_every_thread`, `node_count`, `run_nodes`, `use_http_keepalive`, `create_user_id`)
VALUES ('简单模板', 'mysql', '1900-01-01 00:00:00', 'admin', '2022-11-03 18:10:50', '2022-11-03 19:21:14', 'admin', '\r\nLAPTOP-7V5D9SQE:用户执行关闭', '/*通过直接写url和压测参数，然后写入到本地样本(sample)文件中*/\nvar dataMaps = [\n{\"app_name\":\"test\",\"url\":\"http://www.baidu.com\",\"method\":\"GET\",\"header\":JSON.stringify({\'aaa\':\'bbb\'}),\"body\":JSON.stringify({})},\n{\"app_name\":\"test\",\"url\":\"http://www.baidu.com\",\"method\":\"GET\",\"header\":\"{}\",\"body\":\"{}\"}\n];\n/*var dataMaps=api.httpGet(\"http://远程数据源/\");*/\nfor(var i=0;i<dataMaps.length;i++){\n    api.writeSample(dataMaps[i]);\n}', '   /*在样本筛选的基础上,对本地样本文件中的部分请求进行过滤，比如特定业务请求或者错误请求或者无法访问的请求等*/\n   var r = api.ps.response;\n   if(r==null||r.code!=200)\n   {\n        /*api.error([\"过滤错误请求\",r]);*/\n       return false;\n   }', 1, '', '', '   /*压测时,定期心跳检测当前任务是否符合退出条件，进行任务退出关闭动作，一般会根据压测报告结果或者运行时间进行判断*/\n   /*此处举例吞吐量超过5000,运行时间超过10分钟则终止任务,特别注意nodeReport在任务刚启动的时候可能为null*/\n   if(api.ps.nodeReport!=null&&(api.ps.nodeReport.throughput>5000||api.ps.runtime>10*60)){\n      api.log([\"结束\",api.ps.nodeReport,api.ps.runtime]);\n      return false;\n   }', 100, 1, 'LAPTOP-7V5D9SQE', b'0', 3);

SET FOREIGN_KEY_CHECKS = 1;
