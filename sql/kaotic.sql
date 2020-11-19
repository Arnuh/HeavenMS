SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

DROP DATABASE IF EXISTS `kaotic`;
CREATE DATABASE `kaotic` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `kaotic`;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) NOT NULL DEFAULT '',
  `password` varchar(128) NOT NULL DEFAULT '',
  `pin` varchar(10) NOT NULL DEFAULT '',
  `pic` varchar(26) NOT NULL DEFAULT '',
  `loggedin` tinyint(4) NOT NULL DEFAULT '0',
  `lastlogin` timestamp NULL DEFAULT NULL,
  `createdat` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `birthday` date NOT NULL DEFAULT '0000-00-00',
  `banned` tinyint(1) NOT NULL DEFAULT '0',
  `banreason` text,
  `macs` tinytext,
  `nxCredit` int(11) DEFAULT NULL,
  `maplePoint` int(11) DEFAULT NULL,
  `nxPrepaid` int(11) DEFAULT NULL,
  `characterslots` tinyint(2) NOT NULL DEFAULT '6',
  `gender` tinyint(2) NOT NULL DEFAULT '10',
  `tempban` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `greason` tinyint(4) NOT NULL DEFAULT '0',
  `tos` tinyint(1) NOT NULL DEFAULT '0',
  `sitelogged` text,
  `webadmin` int(1) DEFAULT '0',
  `nick` varchar(20) DEFAULT NULL,
  `mute` int(1) DEFAULT '0',
  `email` varchar(45) DEFAULT NULL,
  `ip` text,
  `rewardpoints` int(11) NOT NULL DEFAULT '0',
  `votepoints` int(11) NOT NULL DEFAULT '0',
  `hwid` varchar(12) NOT NULL DEFAULT '',
  `language` int(1) NOT NULL DEFAULT '2',
  `gm` tinyint(1) DEFAULT NULL,
  `ipcheck` varchar(39) DEFAULT NULL,
  `mesos` bigint(255) NOT NULL DEFAULT '0',
  `lastvote` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `voted` int(11) NOT NULL DEFAULT '0',
  `vip` bigint(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `ranking1` (`id`,`banned`),
  KEY `id` (`id`,`name`),
  KEY `id_2` (`id`,`nxCredit`,`maplePoint`,`nxPrepaid`)
) ENGINE=MyISAM AUTO_INCREMENT=12282 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for alliance
-- ----------------------------
DROP TABLE IF EXISTS `alliance`;
CREATE TABLE `alliance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(13) NOT NULL,
  `capacity` int(10) unsigned NOT NULL DEFAULT '2',
  `notice` varchar(20) NOT NULL DEFAULT '',
  `rank1` varchar(11) NOT NULL DEFAULT 'Master',
  `rank2` varchar(11) NOT NULL DEFAULT 'Jr. Master',
  `rank3` varchar(11) NOT NULL DEFAULT 'Member',
  `rank4` varchar(11) NOT NULL DEFAULT 'Member',
  `rank5` varchar(11) NOT NULL DEFAULT 'Member',
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for allianceguilds
-- ----------------------------
DROP TABLE IF EXISTS `allianceguilds`;
CREATE TABLE `allianceguilds` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `allianceid` int(10) NOT NULL DEFAULT '-1',
  `guildid` int(10) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=703 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for area_info
-- ----------------------------
DROP TABLE IF EXISTS `area_info`;
CREATE TABLE `area_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charid` int(11) NOT NULL,
  `area` int(11) NOT NULL,
  `info` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for bbs_replies
-- ----------------------------
DROP TABLE IF EXISTS `bbs_replies`;
CREATE TABLE `bbs_replies` (
  `replyid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `threadid` int(10) unsigned NOT NULL,
  `postercid` int(10) unsigned NOT NULL,
  `timestamp` bigint(20) unsigned NOT NULL,
  `content` varchar(26) NOT NULL DEFAULT '',
  PRIMARY KEY (`replyid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for bbs_threads
-- ----------------------------
DROP TABLE IF EXISTS `bbs_threads`;
CREATE TABLE `bbs_threads` (
  `threadid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postercid` int(10) unsigned NOT NULL,
  `name` varchar(26) NOT NULL DEFAULT '',
  `timestamp` bigint(20) unsigned NOT NULL,
  `icon` smallint(5) unsigned NOT NULL,
  `replycount` smallint(5) unsigned NOT NULL DEFAULT '0',
  `startpost` text NOT NULL,
  `guildid` int(10) unsigned NOT NULL,
  `localthreadid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`threadid`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for bosslog_daily
-- ----------------------------
DROP TABLE IF EXISTS `bosslog_daily`;
CREATE TABLE `bosslog_daily` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL,
  `bosstype` enum('ZAKUM','HORNTAIL','PINKBEAN','SCARGA','PAPULATUS') NOT NULL,
  `attempttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for bosslog_weekly
-- ----------------------------
DROP TABLE IF EXISTS `bosslog_weekly`;
CREATE TABLE `bosslog_weekly` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL,
  `bosstype` enum('ZAKUM','HORNTAIL','PINKBEAN','SCARGA','PAPULATUS') NOT NULL,
  `attempttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for buddies
-- ----------------------------
DROP TABLE IF EXISTS `buddies`;
CREATE TABLE `buddies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL,
  `buddyid` int(11) NOT NULL,
  `pending` tinyint(4) NOT NULL DEFAULT '0',
  `group` varchar(17) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=108349 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for cashitems
-- ----------------------------
DROP TABLE IF EXISTS `cashitems`;
CREATE TABLE `cashitems` (
  `id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for characterbuff
-- ----------------------------
DROP TABLE IF EXISTS `characterbuff`;
CREATE TABLE `characterbuff` (
  `chrid` int(11) NOT NULL,
  `skillid` int(11) NOT NULL,
  `skilllevel` int(11) NOT NULL,
  PRIMARY KEY (`chrid`,`skillid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for characters
-- ----------------------------
DROP TABLE IF EXISTS `characters`;
CREATE TABLE `characters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) NOT NULL DEFAULT '0',
  `world` int(11) NOT NULL DEFAULT '0',
  `name` varchar(13) NOT NULL DEFAULT '',
  `level` int(11) NOT NULL DEFAULT '1',
  `overlevel` int(11) NOT NULL DEFAULT '0',
  `totallevel` int(11) NOT NULL DEFAULT '1',
  `exp` int(11) NOT NULL DEFAULT '0',
  `gachaexp` int(11) NOT NULL DEFAULT '0',
  `str` int(11) NOT NULL DEFAULT '12',
  `dex` int(11) NOT NULL DEFAULT '5',
  `luk` int(11) NOT NULL DEFAULT '4',
  `int` int(11) NOT NULL DEFAULT '4',
  `hp` int(11) NOT NULL DEFAULT '50',
  `mp` int(11) NOT NULL DEFAULT '5',
  `maxhp` int(11) NOT NULL DEFAULT '50',
  `maxmp` int(11) NOT NULL DEFAULT '5',
  `meso` int(11) NOT NULL DEFAULT '0',
  `hpMpUsed` int(11) unsigned NOT NULL DEFAULT '0',
  `job` int(11) NOT NULL DEFAULT '0',
  `skincolor` int(11) NOT NULL DEFAULT '0',
  `gender` int(11) NOT NULL DEFAULT '0',
  `fame` int(11) NOT NULL DEFAULT '0',
  `fquest` int(11) NOT NULL DEFAULT '0',
  `hair` int(11) NOT NULL DEFAULT '0',
  `face` int(11) NOT NULL DEFAULT '0',
  `ap` int(11) NOT NULL DEFAULT '0',
  `sp` varchar(128) NOT NULL DEFAULT '0,0,0,0,0,0,0,0,0,0',
  `map` int(11) NOT NULL DEFAULT '0',
  `spawnpoint` int(11) NOT NULL DEFAULT '0',
  `gm` tinyint(1) NOT NULL DEFAULT '0',
  `party` int(11) NOT NULL DEFAULT '0',
  `buddyCapacity` int(11) NOT NULL DEFAULT '25',
  `createdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rank` int(10) unsigned NOT NULL DEFAULT '1',
  `rankMove` int(11) NOT NULL DEFAULT '0',
  `jobRank` int(10) unsigned NOT NULL DEFAULT '1',
  `jobRankMove` int(11) NOT NULL DEFAULT '0',
  `guildid` int(10) unsigned NOT NULL DEFAULT '0',
  `guildrank` int(10) unsigned NOT NULL DEFAULT '5',
  `messengerid` int(10) unsigned NOT NULL DEFAULT '0',
  `messengerposition` int(10) unsigned NOT NULL DEFAULT '4',
  `mountlevel` int(9) NOT NULL DEFAULT '1',
  `mountexp` int(9) NOT NULL DEFAULT '0',
  `mounttiredness` int(9) NOT NULL DEFAULT '0',
  `omokwins` int(11) NOT NULL DEFAULT '0',
  `omoklosses` int(11) NOT NULL DEFAULT '0',
  `omokties` int(11) NOT NULL DEFAULT '0',
  `matchcardwins` int(11) NOT NULL DEFAULT '0',
  `matchcardlosses` int(11) NOT NULL DEFAULT '0',
  `matchcardties` int(11) NOT NULL DEFAULT '0',
  `MerchantMesos` int(11) DEFAULT '0',
  `HasMerchant` tinyint(1) DEFAULT '0',
  `equipslots` int(11) NOT NULL DEFAULT '24',
  `useslots` int(11) NOT NULL DEFAULT '24',
  `setupslots` int(11) NOT NULL DEFAULT '24',
  `etcslots` int(11) NOT NULL DEFAULT '24',
  `familyId` int(11) NOT NULL DEFAULT '-1',
  `monsterbookcover` int(11) NOT NULL DEFAULT '0',
  `allianceRank` int(10) NOT NULL DEFAULT '5',
  `vanquisherStage` int(11) unsigned NOT NULL DEFAULT '0',
  `ariantPoints` int(11) unsigned NOT NULL DEFAULT '0',
  `dojoPoints` bigint(20) unsigned NOT NULL DEFAULT '0',
  `lastDojoStage` int(10) unsigned NOT NULL DEFAULT '0',
  `finishedDojoTutorial` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `vanquisherKills` int(11) unsigned NOT NULL DEFAULT '0',
  `summonValue` int(11) unsigned NOT NULL DEFAULT '0',
  `partnerId` int(11) NOT NULL DEFAULT '0',
  `marriageItemId` int(11) NOT NULL DEFAULT '0',
  `reborns` int(5) NOT NULL DEFAULT '0',
  `PQPoints` int(11) NOT NULL DEFAULT '0',
  `dataString` varchar(64) NOT NULL DEFAULT '',
  `lastLogoutTime` timestamp NOT NULL DEFAULT '2014-12-31 21:00:00',
  `lastExpGainTime` timestamp NOT NULL DEFAULT '2014-12-31 21:00:00',
  `partySearch` tinyint(1) NOT NULL DEFAULT '0',
  `jailexpire` bigint(20) NOT NULL DEFAULT '0',
  `stamina` int(11) NOT NULL DEFAULT '0',
  `Overexp` bigint(20) NOT NULL DEFAULT '0',
  `maxlevel` int(11) NOT NULL DEFAULT '250',
  PRIMARY KEY (`id`),
  KEY `accountid` (`accountid`),
  KEY `party` (`party`),
  KEY `ranking1` (`level`,`exp`),
  KEY `ranking2` (`gm`,`job`),
  KEY `id` (`id`,`accountid`,`world`),
  KEY `id_2` (`id`,`accountid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4932 DEFAULT CHARSET=latin1 PACK_KEYS=0;

-- ----------------------------
-- Table structure for cooldowns
-- ----------------------------
DROP TABLE IF EXISTS `cooldowns`;
CREATE TABLE `cooldowns` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charid` int(11) NOT NULL,
  `SkillID` int(11) NOT NULL,
  `length` bigint(20) unsigned NOT NULL,
  `StartTime` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=869 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for drop_data
-- ----------------------------
DROP TABLE IF EXISTS `drop_data`;
CREATE TABLE `drop_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dropperid` int(11) NOT NULL,
  `itemid` int(11) NOT NULL DEFAULT '0',
  `minimum_quantity` int(11) NOT NULL DEFAULT '1',
  `maximum_quantity` int(11) NOT NULL DEFAULT '1',
  `questid` int(11) NOT NULL DEFAULT '0',
  `chance` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dropperid` (`dropperid`,`itemid`),
  KEY `mobid` (`dropperid`),
  KEY `dropperid_2` (`dropperid`,`itemid`)
) ENGINE=MyISAM AUTO_INCREMENT=29757 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for drop_data_global
-- ----------------------------
DROP TABLE IF EXISTS `drop_data_global`;
CREATE TABLE `drop_data_global` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `continent` tinyint(1) NOT NULL DEFAULT '-1',
  `itemid` int(11) NOT NULL DEFAULT '0',
  `minimum_quantity` int(11) NOT NULL DEFAULT '1',
  `maximum_quantity` int(11) NOT NULL DEFAULT '1',
  `questid` int(11) NOT NULL DEFAULT '0',
  `chance` int(11) NOT NULL DEFAULT '0',
  `rank` int(11) NOT NULL DEFAULT '0',
  `minlevel` int(11) NOT NULL DEFAULT '0',
  `maxlevel` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mobid` (`continent`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=344 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for dueyitems
-- ----------------------------
DROP TABLE IF EXISTS `dueyitems`;
CREATE TABLE `dueyitems` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PackageId` int(10) unsigned NOT NULL DEFAULT '0',
  `inventoryitemid` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `INVENTORYITEMID` (`inventoryitemid`),
  KEY `PackageId` (`PackageId`),
  CONSTRAINT `dueyitems_ibfk_1` FOREIGN KEY (`PackageId`) REFERENCES `dueypackages` (`PackageId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for dueypackages
-- ----------------------------
DROP TABLE IF EXISTS `dueypackages`;
CREATE TABLE `dueypackages` (
  `PackageId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ReceiverId` int(10) unsigned NOT NULL,
  `SenderName` varchar(13) NOT NULL,
  `Mesos` int(10) unsigned DEFAULT '0',
  `TimeStamp` varchar(10) NOT NULL,
  `Message` varchar(200) NOT NULL DEFAULT '',
  `Checked` tinyint(1) unsigned DEFAULT '1',
  `Type` tinyint(1) unsigned DEFAULT '0',
  PRIMARY KEY (`PackageId`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for eventstats
-- ----------------------------
DROP TABLE IF EXISTS `eventstats`;
CREATE TABLE `eventstats` (
  `characterid` int(11) unsigned NOT NULL,
  `name` varchar(11) NOT NULL DEFAULT '0' COMMENT '0',
  `info` int(11) NOT NULL,
  PRIMARY KEY (`characterid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for famelog
-- ----------------------------
DROP TABLE IF EXISTS `famelog`;
CREATE TABLE `famelog` (
  `famelogid` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL DEFAULT '0',
  `characterid_to` int(11) NOT NULL DEFAULT '0',
  `when` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`famelogid`),
  KEY `characterid` (`characterid`),
  CONSTRAINT `famelog_ibfk_1` FOREIGN KEY (`characterid`) REFERENCES `characters` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for family_character
-- ----------------------------
DROP TABLE IF EXISTS `family_character`;
CREATE TABLE `family_character` (
  `cid` int(11) NOT NULL,
  `familyid` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  `reputation` int(11) NOT NULL,
  `todaysrep` int(11) NOT NULL,
  `totaljuniors` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `juniorsadded` int(11) NOT NULL,
  `totalreputation` int(11) NOT NULL,
  PRIMARY KEY (`cid`),
  KEY `cid` (`cid`,`familyid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for fredstorage
-- ----------------------------
DROP TABLE IF EXISTS `fredstorage`;
CREATE TABLE `fredstorage` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cid` int(10) unsigned NOT NULL,
  `daynotes` int(4) unsigned NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cid_2` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for gach_data
-- ----------------------------
DROP TABLE IF EXISTS `gach_data`;
CREATE TABLE `gach_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gachid` int(11) NOT NULL,
  `itemid` int(11) NOT NULL DEFAULT '0',
  `minimum_quantity` int(11) NOT NULL DEFAULT '1',
  `maximum_quantity` int(11) NOT NULL DEFAULT '1',
  `chance` int(11) NOT NULL DEFAULT '0',
  `itemname` varchar(45) DEFAULT NULL,
  `npcname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3514 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for gifts
-- ----------------------------
DROP TABLE IF EXISTS `gifts`;
CREATE TABLE `gifts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `to` int(11) NOT NULL,
  `from` varchar(13) NOT NULL,
  `message` tinytext NOT NULL,
  `sn` int(10) unsigned NOT NULL,
  `ringid` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for guilds
-- ----------------------------
DROP TABLE IF EXISTS `guilds`;
CREATE TABLE `guilds` (
  `guildid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `leader` int(10) unsigned NOT NULL DEFAULT '0',
  `GP` int(10) unsigned NOT NULL DEFAULT '0',
  `logo` int(10) unsigned DEFAULT NULL,
  `logoColor` smallint(5) unsigned NOT NULL DEFAULT '0',
  `name` varchar(45) NOT NULL,
  `rank1title` varchar(45) NOT NULL DEFAULT 'Master',
  `rank2title` varchar(45) NOT NULL DEFAULT 'Jr. Master',
  `rank3title` varchar(45) NOT NULL DEFAULT 'Member',
  `rank4title` varchar(45) NOT NULL DEFAULT 'Member',
  `rank5title` varchar(45) NOT NULL DEFAULT 'Member',
  `capacity` int(10) unsigned NOT NULL DEFAULT '250',
  `logoBG` int(10) unsigned DEFAULT NULL,
  `logoBGColor` smallint(5) unsigned NOT NULL DEFAULT '0',
  `notice` varchar(101) DEFAULT NULL,
  `signature` int(11) NOT NULL DEFAULT '0',
  `allianceId` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`guildid`),
  KEY `guildid` (`guildid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for hwidaccounts
-- ----------------------------
DROP TABLE IF EXISTS `hwidaccounts`;
CREATE TABLE `hwidaccounts` (
  `accountid` int(11) NOT NULL DEFAULT '0',
  `hwid` varchar(40) NOT NULL DEFAULT '',
  `relevance` tinyint(2) NOT NULL DEFAULT '0',
  `expiresat` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`accountid`,`hwid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for hwidbans
-- ----------------------------
DROP TABLE IF EXISTS `hwidbans`;
CREATE TABLE `hwidbans` (
  `hwidbanid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `hwid` varchar(30) NOT NULL,
  PRIMARY KEY (`hwidbanid`),
  UNIQUE KEY `hwid_2` (`hwid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for inventoryequipment
-- ----------------------------
DROP TABLE IF EXISTS `inventoryequipment`;
CREATE TABLE `inventoryequipment` (
  `inventoryequipmentid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `inventoryitemid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `item` int(11) NOT NULL,
  `player` int(11) NOT NULL,
  `upgradeslots` int(11) NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL DEFAULT '0',
  `str` int(11) NOT NULL DEFAULT '0',
  `dex` int(11) NOT NULL DEFAULT '0',
  `int` int(11) NOT NULL DEFAULT '0',
  `luk` int(11) NOT NULL DEFAULT '0',
  `hp` int(11) NOT NULL DEFAULT '0',
  `mp` int(11) NOT NULL DEFAULT '0',
  `watk` int(11) NOT NULL DEFAULT '0',
  `matk` int(11) NOT NULL DEFAULT '0',
  `wdef` int(11) NOT NULL DEFAULT '0',
  `mdef` int(11) NOT NULL DEFAULT '0',
  `acc` int(11) NOT NULL DEFAULT '0',
  `avoid` int(11) NOT NULL DEFAULT '0',
  `hands` int(11) NOT NULL DEFAULT '0',
  `speed` int(11) NOT NULL DEFAULT '0',
  `jump` int(11) NOT NULL DEFAULT '0',
  `locked` int(11) NOT NULL DEFAULT '0',
  `vicious` int(11) unsigned NOT NULL DEFAULT '0',
  `itemlevel` int(11) NOT NULL DEFAULT '1',
  `itemexp` int(11) unsigned NOT NULL DEFAULT '0',
  `ringid` int(11) NOT NULL DEFAULT '-1',
  `powerlevel` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`inventoryequipmentid`,`inventoryitemid`),
  UNIQUE KEY `pk` (`inventoryequipmentid`,`inventoryitemid`) USING BTREE,
  KEY `INVENTORYITEMID` (`inventoryitemid`)
) ENGINE=MyISAM AUTO_INCREMENT=12606918 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for inventoryitems
-- ----------------------------
DROP TABLE IF EXISTS `inventoryitems`;
CREATE TABLE `inventoryitems` (
  `inventoryitemid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(3) unsigned NOT NULL,
  `characterid` int(11) DEFAULT NULL,
  `accountid` int(11) DEFAULT NULL,
  `itemid` int(11) NOT NULL DEFAULT '0',
  `inventorytype` int(11) NOT NULL DEFAULT '0',
  `position` int(11) NOT NULL DEFAULT '0',
  `quantity` int(11) NOT NULL DEFAULT '0',
  `owner` varchar(26) NOT NULL,
  `petid` int(11) NOT NULL DEFAULT '-1',
  `flag` int(11) NOT NULL,
  `expiration` bigint(20) NOT NULL DEFAULT '-1',
  `giftFrom` varchar(26) NOT NULL,
  PRIMARY KEY (`inventoryitemid`),
  UNIQUE KEY `pk` (`inventoryitemid`,`itemid`) USING BTREE,
  KEY `CHARID` (`characterid`)
) ENGINE=MyISAM AUTO_INCREMENT=31952585 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for inventorymerchant
-- ----------------------------
DROP TABLE IF EXISTS `inventorymerchant`;
CREATE TABLE `inventorymerchant` (
  `inventorymerchantid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `inventoryitemid` int(10) unsigned NOT NULL DEFAULT '0',
  `characterid` int(11) DEFAULT NULL,
  `bundles` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`inventorymerchantid`),
  KEY `INVENTORYITEMID` (`inventoryitemid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for ipbans
-- ----------------------------
DROP TABLE IF EXISTS `ipbans`;
CREATE TABLE `ipbans` (
  `ipbanid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ip` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`ipbanid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for keymap
-- ----------------------------
DROP TABLE IF EXISTS `keymap`;
CREATE TABLE `keymap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL DEFAULT '0',
  `key` int(11) NOT NULL DEFAULT '0',
  `type` int(11) NOT NULL DEFAULT '0',
  `action` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pk` (`key`,`characterid`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=19194203 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for macbans
-- ----------------------------
DROP TABLE IF EXISTS `macbans`;
CREATE TABLE `macbans` (
  `macbanid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mac` varchar(30) NOT NULL,
  `aid` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`macbanid`),
  UNIQUE KEY `mac_2` (`mac`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for macfilters
-- ----------------------------
DROP TABLE IF EXISTS `macfilters`;
CREATE TABLE `macfilters` (
  `macfilterid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `filter` varchar(30) NOT NULL,
  PRIMARY KEY (`macfilterid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for makercreatedata
-- ----------------------------
DROP TABLE IF EXISTS `makercreatedata`;
CREATE TABLE `makercreatedata` (
  `id` tinyint(3) unsigned NOT NULL,
  `itemid` int(11) NOT NULL,
  `req_level` tinyint(3) unsigned NOT NULL,
  `req_maker_level` tinyint(3) unsigned NOT NULL,
  `req_meso` int(11) NOT NULL,
  `req_item` int(11) NOT NULL,
  `req_equip` int(11) NOT NULL,
  `catalyst` int(11) NOT NULL,
  `quantity` smallint(6) NOT NULL,
  `tuc` tinyint(3) NOT NULL,
  PRIMARY KEY (`id`,`itemid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for makerreagentdata
-- ----------------------------
DROP TABLE IF EXISTS `makerreagentdata`;
CREATE TABLE `makerreagentdata` (
  `itemid` int(11) NOT NULL,
  `stat` varchar(20) NOT NULL,
  `value` smallint(6) NOT NULL,
  PRIMARY KEY (`itemid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for makerrecipedata
-- ----------------------------
DROP TABLE IF EXISTS `makerrecipedata`;
CREATE TABLE `makerrecipedata` (
  `itemid` int(11) NOT NULL,
  `req_item` int(11) NOT NULL,
  `count` smallint(6) NOT NULL,
  PRIMARY KEY (`itemid`,`req_item`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for makerrewarddata
-- ----------------------------
DROP TABLE IF EXISTS `makerrewarddata`;
CREATE TABLE `makerrewarddata` (
  `itemid` int(11) NOT NULL,
  `rewardid` int(11) NOT NULL,
  `quantity` smallint(6) NOT NULL,
  `prob` tinyint(3) unsigned NOT NULL DEFAULT '100',
  PRIMARY KEY (`itemid`,`rewardid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for marriages
-- ----------------------------
DROP TABLE IF EXISTS `marriages`;
CREATE TABLE `marriages` (
  `marriageid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `husbandid` int(10) unsigned NOT NULL DEFAULT '0',
  `wifeid` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`marriageid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for medalmaps
-- ----------------------------
DROP TABLE IF EXISTS `medalmaps`;
CREATE TABLE `medalmaps` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL,
  `queststatusid` int(11) unsigned NOT NULL,
  `mapid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `queststatusid` (`queststatusid`)
) ENGINE=MyISAM AUTO_INCREMENT=3429703 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mobdata
-- ----------------------------
DROP TABLE IF EXISTS `mobdata`;
CREATE TABLE `mobdata` (
  `mobid` int(11) NOT NULL DEFAULT '1',
  `level` int(11) NOT NULL DEFAULT '1',
  `rank` int(11) NOT NULL DEFAULT '1',
  `info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`mobid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for monsterbook
-- ----------------------------
DROP TABLE IF EXISTS `monsterbook`;
CREATE TABLE `monsterbook` (
  `accid` int(11) unsigned NOT NULL,
  `cardid` int(11) NOT NULL,
  `level` int(1) DEFAULT '1',
  PRIMARY KEY (`accid`,`cardid`),
  UNIQUE KEY `pk` (`accid`,`cardid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for monstercarddata
-- ----------------------------
DROP TABLE IF EXISTS `monstercarddata`;
CREATE TABLE `monstercarddata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cardid` int(11) NOT NULL DEFAULT '0',
  `mobid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=344 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mts_cart
-- ----------------------------
DROP TABLE IF EXISTS `mts_cart`;
CREATE TABLE `mts_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) NOT NULL,
  `itemid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mts_items
-- ----------------------------
DROP TABLE IF EXISTS `mts_items`;
CREATE TABLE `mts_items` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tab` int(11) NOT NULL DEFAULT '0',
  `type` int(11) NOT NULL DEFAULT '0',
  `itemid` int(10) unsigned NOT NULL DEFAULT '0',
  `quantity` int(11) NOT NULL DEFAULT '1',
  `seller` int(11) NOT NULL DEFAULT '0',
  `price` int(11) NOT NULL DEFAULT '0',
  `bid_incre` int(11) DEFAULT '0',
  `buy_now` int(11) DEFAULT '0',
  `position` int(11) DEFAULT '0',
  `upgradeslots` int(11) DEFAULT '0',
  `level` int(11) DEFAULT '0',
  `str` int(11) DEFAULT '0',
  `dex` int(11) DEFAULT '0',
  `int` int(11) DEFAULT '0',
  `luk` int(11) DEFAULT '0',
  `hp` int(11) DEFAULT '0',
  `mp` int(11) DEFAULT '0',
  `watk` int(11) DEFAULT '0',
  `matk` int(11) DEFAULT '0',
  `wdef` int(11) DEFAULT '0',
  `mdef` int(11) DEFAULT '0',
  `acc` int(11) DEFAULT '0',
  `avoid` int(11) DEFAULT '0',
  `hands` int(11) DEFAULT '0',
  `speed` int(11) DEFAULT '0',
  `jump` int(11) DEFAULT '0',
  `locked` int(11) DEFAULT '0',
  `isequip` int(1) DEFAULT '0',
  `owner` varchar(16) DEFAULT '',
  `sellername` varchar(16) NOT NULL,
  `sell_ends` varchar(16) NOT NULL,
  `transfer` int(2) DEFAULT '0',
  `vicious` int(2) unsigned NOT NULL DEFAULT '0',
  `flag` int(2) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for newyear
-- ----------------------------
DROP TABLE IF EXISTS `newyear`;
CREATE TABLE `newyear` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `senderid` int(10) NOT NULL DEFAULT '-1',
  `sendername` varchar(13) DEFAULT '',
  `receiverid` int(10) NOT NULL DEFAULT '-1',
  `receivername` varchar(13) DEFAULT '',
  `message` varchar(120) DEFAULT '',
  `senderdiscard` tinyint(1) NOT NULL DEFAULT '0',
  `receiverdiscard` tinyint(1) NOT NULL DEFAULT '0',
  `received` tinyint(1) NOT NULL DEFAULT '0',
  `timesent` bigint(20) unsigned NOT NULL,
  `timereceived` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for notes
-- ----------------------------
DROP TABLE IF EXISTS `notes`;
CREATE TABLE `notes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `to` varchar(13) NOT NULL DEFAULT '',
  `from` varchar(13) NOT NULL DEFAULT '',
  `message` text NOT NULL,
  `timestamp` bigint(20) unsigned NOT NULL,
  `fame` int(11) NOT NULL DEFAULT '0',
  `deleted` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for nxcode
-- ----------------------------
DROP TABLE IF EXISTS `nxcode`;
CREATE TABLE `nxcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(17) NOT NULL,
  `retriever` varchar(13) DEFAULT NULL,
  `expiration` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for nxcode_items
-- ----------------------------
DROP TABLE IF EXISTS `nxcode_items`;
CREATE TABLE `nxcode_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codeid` int(11) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '5',
  `item` int(11) NOT NULL DEFAULT '4000000',
  `quantity` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for nxcoupons
-- ----------------------------
DROP TABLE IF EXISTS `nxcoupons`;
CREATE TABLE `nxcoupons` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `couponid` int(11) NOT NULL DEFAULT '0',
  `rate` int(11) NOT NULL DEFAULT '0',
  `activeday` int(11) NOT NULL DEFAULT '0',
  `starthour` int(11) NOT NULL DEFAULT '0',
  `endhour` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for petignores
-- ----------------------------
DROP TABLE IF EXISTS `petignores`;
CREATE TABLE `petignores` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `petid` int(10) unsigned NOT NULL,
  `itemid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for pets
-- ----------------------------
DROP TABLE IF EXISTS `pets`;
CREATE TABLE `pets` (
  `petid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(13) DEFAULT NULL,
  `level` int(10) unsigned NOT NULL,
  `closeness` int(10) unsigned NOT NULL,
  `fullness` int(10) unsigned NOT NULL,
  `summoned` tinyint(1) NOT NULL DEFAULT '0',
  `flag` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`petid`)
) ENGINE=InnoDB AUTO_INCREMENT=382 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for playerdiseases
-- ----------------------------
DROP TABLE IF EXISTS `playerdiseases`;
CREATE TABLE `playerdiseases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charid` int(11) NOT NULL,
  `disease` int(11) NOT NULL,
  `mobskillid` int(11) NOT NULL,
  `mobskilllv` int(11) NOT NULL,
  `length` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for playernpcs
-- ----------------------------
DROP TABLE IF EXISTS `playernpcs`;
CREATE TABLE `playernpcs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) NOT NULL,
  `hair` int(11) NOT NULL,
  `face` int(11) NOT NULL,
  `skin` int(11) NOT NULL,
  `gender` int(11) NOT NULL DEFAULT '0',
  `x` int(11) NOT NULL,
  `cy` int(11) NOT NULL DEFAULT '0',
  `world` int(11) NOT NULL DEFAULT '0',
  `map` int(11) NOT NULL DEFAULT '0',
  `dir` int(11) NOT NULL DEFAULT '0',
  `scriptid` int(10) unsigned NOT NULL DEFAULT '0',
  `fh` int(11) NOT NULL DEFAULT '0',
  `rx0` int(11) NOT NULL DEFAULT '0',
  `rx1` int(11) NOT NULL DEFAULT '0',
  `worldrank` int(11) NOT NULL DEFAULT '0',
  `overallrank` int(11) NOT NULL DEFAULT '0',
  `worldjobrank` int(11) NOT NULL DEFAULT '0',
  `job` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for playernpcs_equip
-- ----------------------------
DROP TABLE IF EXISTS `playernpcs_equip`;
CREATE TABLE `playernpcs_equip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `npcid` int(11) NOT NULL DEFAULT '0',
  `equipid` int(11) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `equippos` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for playernpcs_field
-- ----------------------------
DROP TABLE IF EXISTS `playernpcs_field`;
CREATE TABLE `playernpcs_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `world` int(11) NOT NULL,
  `map` int(11) NOT NULL,
  `step` tinyint(1) NOT NULL DEFAULT '0',
  `podium` smallint(8) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for plife
-- ----------------------------
DROP TABLE IF EXISTS `plife`;
CREATE TABLE `plife` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `world` int(11) NOT NULL DEFAULT '-1',
  `map` int(11) NOT NULL DEFAULT '0',
  `life` int(11) NOT NULL DEFAULT '0',
  `type` varchar(1) NOT NULL DEFAULT 'n',
  `cy` int(11) NOT NULL DEFAULT '0',
  `f` int(11) NOT NULL DEFAULT '0',
  `fh` int(11) NOT NULL DEFAULT '0',
  `rx0` int(11) NOT NULL DEFAULT '0',
  `rx1` int(11) NOT NULL DEFAULT '0',
  `x` int(11) NOT NULL DEFAULT '0',
  `y` int(11) NOT NULL DEFAULT '0',
  `hide` int(11) NOT NULL DEFAULT '0',
  `mobtime` int(11) NOT NULL DEFAULT '0',
  `team` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for questactions
-- ----------------------------
DROP TABLE IF EXISTS `questactions`;
CREATE TABLE `questactions` (
  `questactionid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `questid` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `data` blob NOT NULL,
  PRIMARY KEY (`questactionid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for questprogress
-- ----------------------------
DROP TABLE IF EXISTS `questprogress`;
CREATE TABLE `questprogress` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL,
  `queststatusid` int(10) unsigned NOT NULL DEFAULT '0',
  `progressid` int(11) NOT NULL DEFAULT '0',
  `progress` varchar(15) CHARACTER SET latin1 COLLATE latin1_german1_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=674117 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for questrequirements
-- ----------------------------
DROP TABLE IF EXISTS `questrequirements`;
CREATE TABLE `questrequirements` (
  `questrequirementid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `questid` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `data` blob NOT NULL,
  PRIMARY KEY (`questrequirementid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for queststatus
-- ----------------------------
DROP TABLE IF EXISTS `queststatus`;
CREATE TABLE `queststatus` (
  `queststatusid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL DEFAULT '0',
  `quest` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `time` int(11) NOT NULL DEFAULT '0',
  `expires` bigint(20) NOT NULL DEFAULT '0',
  `forfeited` int(11) NOT NULL DEFAULT '0',
  `completed` int(11) NOT NULL DEFAULT '0',
  `info` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`queststatusid`)
) ENGINE=MyISAM AUTO_INCREMENT=15612072 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for reactordrops
-- ----------------------------
DROP TABLE IF EXISTS `reactordrops`;
CREATE TABLE `reactordrops` (
  `reactordropid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `reactorid` int(11) NOT NULL,
  `itemid` int(11) NOT NULL,
  `chance` int(11) NOT NULL,
  `questid` int(5) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`reactordropid`),
  KEY `reactorid` (`reactorid`)
) ENGINE=MyISAM AUTO_INCREMENT=1535 DEFAULT CHARSET=latin1 PACK_KEYS=1;

-- ----------------------------
-- Table structure for reports
-- ----------------------------
DROP TABLE IF EXISTS `reports`;
CREATE TABLE `reports` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `reporttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `reporterid` int(11) NOT NULL,
  `victimid` int(11) NOT NULL,
  `reason` tinyint(4) NOT NULL,
  `chatlog` text NOT NULL,
  `status` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for responses
-- ----------------------------
DROP TABLE IF EXISTS `responses`;
CREATE TABLE `responses` (
  `chat` text,
  `response` text,
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for rings
-- ----------------------------
DROP TABLE IF EXISTS `rings`;
CREATE TABLE `rings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partnerRingId` int(11) NOT NULL DEFAULT '0',
  `partnerChrId` int(11) NOT NULL DEFAULT '0',
  `itemid` int(11) NOT NULL DEFAULT '0',
  `partnername` varchar(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for savedlocations
-- ----------------------------
DROP TABLE IF EXISTS `savedlocations`;
CREATE TABLE `savedlocations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL,
  `locationtype` enum('FREE_MARKET','WORLDTOUR','FLORINA','INTRO','SUNDAY_MARKET','MIRROR','EVENT','BOSSPQ','HAPPYVILLE','DEVELOPER','MONSTER_CARNIVAL','MONSTERPARK') NOT NULL,
  `map` int(11) NOT NULL,
  `portal` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=96765 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for server_queue
-- ----------------------------
DROP TABLE IF EXISTS `server_queue`;
CREATE TABLE `server_queue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) NOT NULL DEFAULT '0',
  `characterid` int(11) NOT NULL DEFAULT '0',
  `type` tinyint(2) NOT NULL DEFAULT '0',
  `value` int(10) NOT NULL DEFAULT '0',
  `message` varchar(128) NOT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shopitems
-- ----------------------------
DROP TABLE IF EXISTS `shopitems`;
CREATE TABLE `shopitems` (
  `shopitemid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shopid` int(10) unsigned NOT NULL DEFAULT '0',
  `itemid` int(11) NOT NULL DEFAULT '0',
  `price` int(11) NOT NULL DEFAULT '1',
  `pitch` int(11) NOT NULL DEFAULT '0',
  `position` int(11) NOT NULL DEFAULT '0' COMMENT 'sort is an arbitrary field designed to give leeway when modifying shops. The lowest number is 104 and it increments by 4 for each item to allow decent space for swapping/inserting/removing items.',
  PRIMARY KEY (`shopitemid`)
) ENGINE=MyISAM AUTO_INCREMENT=7811 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for shops
-- ----------------------------
DROP TABLE IF EXISTS `shops`;
CREATE TABLE `shops` (
  `shopid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `npcid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`shopid`)
) ENGINE=MyISAM AUTO_INCREMENT=10000000 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for skillmacros
-- ----------------------------
DROP TABLE IF EXISTS `skillmacros`;
CREATE TABLE `skillmacros` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL DEFAULT '0',
  `position` tinyint(1) NOT NULL DEFAULT '0',
  `skill1` int(11) NOT NULL DEFAULT '0',
  `skill2` int(11) NOT NULL DEFAULT '0',
  `skill3` int(11) NOT NULL DEFAULT '0',
  `name` varchar(13) DEFAULT NULL,
  `shout` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=100 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for skills
-- ----------------------------
DROP TABLE IF EXISTS `skills`;
CREATE TABLE `skills` (
  `skillid` int(11) NOT NULL DEFAULT '0',
  `characterid` int(11) NOT NULL DEFAULT '0',
  `skilllevel` int(11) NOT NULL DEFAULT '0',
  `masterlevel` int(11) NOT NULL DEFAULT '0',
  `expiration` bigint(20) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`characterid`,`skillid`),
  UNIQUE KEY `skillpair` (`skillid`,`characterid`),
  KEY `skills_chrid_fk` (`characterid`),
  CONSTRAINT `skills_ibfk_1` FOREIGN KEY (`characterid`) REFERENCES `characters` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for specialcashitems
-- ----------------------------
DROP TABLE IF EXISTS `specialcashitems`;
CREATE TABLE `specialcashitems` (
  `id` int(11) NOT NULL,
  `sn` int(11) NOT NULL,
  `modifier` int(11) NOT NULL COMMENT '1024 is add/remove',
  `info` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for storages
-- ----------------------------
DROP TABLE IF EXISTS `storages`;
CREATE TABLE `storages` (
  `storageid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `accountid` int(11) NOT NULL DEFAULT '0',
  `world` int(2) NOT NULL,
  `slots` int(11) NOT NULL DEFAULT '0',
  `meso` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`storageid`)
) ENGINE=MyISAM AUTO_INCREMENT=583 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for trocklocations
-- ----------------------------
DROP TABLE IF EXISTS `trocklocations`;
CREATE TABLE `trocklocations` (
  `trockid` int(11) NOT NULL AUTO_INCREMENT,
  `characterid` int(11) NOT NULL,
  `mapid` int(11) NOT NULL,
  `vip` int(2) NOT NULL,
  PRIMARY KEY (`trockid`)
) ENGINE=InnoDB AUTO_INCREMENT=1561 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for wishlists
-- ----------------------------
DROP TABLE IF EXISTS `wishlists`;
CREATE TABLE `wishlists` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charid` int(11) NOT NULL,
  `sn` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET FOREIGN_KEY_CHECKS=1;
