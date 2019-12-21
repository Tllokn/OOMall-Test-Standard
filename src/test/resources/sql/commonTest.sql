-- oomall_share_rule

INSERT INTO `oomall_share_rule` VALUES
(
1001000,
'{"strategy": [{"lowerbound":"0", "upperbound":"1", "rate":"0.5"},{"lowerbound":"2", "upperbound":"10", "rate":"0.7"},{"lowerbound":"11", "upperbound":"30", "rate":"1"},{"lowerbound":"31", "upperbound":"100", "rate":"1.5"}],"type":"1"}',
400,
'2018-02-01 00:00:00',
0,
'2018-02-01 00:00:00'
),

(
1001002,
'{"strategy": [{"lowerbound":"0", "upperbound":"1", "rate":"0.5"},{"lowerbound":"2", "upperbound":"10", "rate":"0.7"},{"lowerbound":"11", "upperbound":"30", "rate":"1"},{"lowerbound":"31", "upperbound":"100", "rate":"1.5"}],"type":"0"}',
401,
'2018-02-01 00:00:00',
0,
'2018-02-01 00:00:00'
),

(
1001003,
'{"strategy": [{"lowerbound":"0", "upperbound":"1", "rate":"0.5"},{"lowerbound":"2", "upperbound":"10", "rate":"0.7"},{"lowerbound":"11", "upperbound":"30", "rate":"1"},{"lowerbound":"31", "upperbound":"100", "rate":"1.5"}],"type":"0"}',
402,
'2018-02-01 00:00:00',
0,
'2018-02-01 00:00:00'
),

(
1001004,
'{"strategy": [{"lowerbound":"0", "upperbound":"1", "rate":"0.5"},{"lowerbound":"2", "upperbound":"10", "rate":"0.7"},{"lowerbound":"11", "upperbound":"30", "rate":"1"},{"lowerbound":"31", "upperbound":"100", "rate":"1.5"}],"type":"1"}',
403,
'2018-02-01 00:00:00',
0,
'2018-02-01 00:00:00'
)
;

-- ----------------------------
-- Records of oomall_default_piece_freight
-- ----------------------------
INSERT INTO `oomall_default_piece_freight` VALUES ('1', '{\"dest\":[1]}', '1.00', null, null, '0', '次日');
INSERT INTO `oomall_default_piece_freight` VALUES ('2', '{\"dest\":[1,2,3]}', '9.00', null, '2019-12-21 02:07:59.08', '1', '次日');
INSERT INTO `oomall_default_piece_freight` VALUES ('3', '{\"dest\":[2]}', '1.50', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('4', '{\"dest\":[9]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('5', '{\"dest\":[11]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('6', '{\"dest\":[10]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('7', '{\"dest\":[12]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('8', '{\"dest\":[19]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('9', '{\"dest\":[146,147,150]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('10', '{\"dest\":[148,149,151,152,153,154]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('11', '{\"dest\":[215]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('12', '{\"dest\":[216,217,218,219,220,221,222,223,224,225,226,227,228]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('13', '{\"dest\":[201]}', '2.00', null, null, '0', '市内1天');
INSERT INTO `oomall_default_piece_freight` VALUES ('14', '{\"dest\":[202,203,204,205,206,207,208,209,210,211,212,213,214]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('15', '{\"dest\":[183]}', '2.00', null, null, '0', '市内1天');
INSERT INTO `oomall_default_piece_freight` VALUES ('16', '{\"dest\":[184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('17', '{\"dest\":[3]}', '1.50', null, null, '0', '次日');
INSERT INTO `oomall_default_piece_freight` VALUES ('18', '{\"dest\":[14]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('19', '{\"dest\":[23]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('20', '{\"dest\":[6]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('21', '{\"dest\":[25]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('22', '{\"dest\":[7]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('23', '{\"dest\":[8]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('24', '{\"dest\":[27]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('25', '{\"dest\":[4]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('26', '{\"dest\":[28]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('27', '{\"dest\":[29]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('28', '{\"dest\":[30]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('29', '{\"dest\":[5]}', '2.00', null, null, '0', '次日');
INSERT INTO `oomall_default_piece_freight` VALUES ('30', '{\"dest\":[20]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('31', '{\"dest\":[24]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('32', '{\"dest\":[31]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('33', '{\"dest\":[22]}', '2.00', null, null, '0', '1-2天');
INSERT INTO `oomall_default_piece_freight` VALUES ('34', '{\"dest\":[21]}', '2.00', null, null, '0', '2-3天');
INSERT INTO `oomall_default_piece_freight` VALUES ('35', '{\"dest\":[166,167,168,171,172,175]}', '1.50', '2019-12-21 04:05:59.00', '2019-12-21 04:05:48.00', '0', '次日');


