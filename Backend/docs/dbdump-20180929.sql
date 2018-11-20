CREATE DATABASE  IF NOT EXISTS `contacto` /*!40100 DEFAULT CHARACTER SET utf16 */;
USE `contacto`;
-- MySQL dump 10.13  Distrib 5.5.50, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: contacto
-- ------------------------------------------------------
-- Server version	5.5.50-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Agency`
--

DROP TABLE IF EXISTS `Agency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Agency` (
  `idAgency` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `addressLine1` varchar(90) NOT NULL,
  `addressLine2` varchar(90) DEFAULT NULL,
  `zipCode` varchar(6) DEFAULT NULL,
  `city` varchar(45) NOT NULL,
  `provinceOrState` varchar(6) NOT NULL,
  `country` varchar(45) NOT NULL,
  `phone1` varchar(15) DEFAULT NULL,
  `phone2` varchar(15) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `web` varchar(65) DEFAULT NULL,
  PRIMARY KEY (`idAgency`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Agency`
--

LOCK TABLES `Agency` WRITE;
/*!40000 ALTER TABLE `Agency` DISABLE KEYS */;
INSERT INTO `Agency` VALUES (1,'wedi Italia','via vimercate 44','','20876','Ornago','MB','IT','0392459420','','info@wedi.it','http://www.wedi.eu/Italy'),(2,'modus srl','via Lago d\'Orta, 12','','','Senigallia','PU','IT','','','info@modusmateria.it',NULL);
/*!40000 ALTER TABLE `Agency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Agent`
--

DROP TABLE IF EXISTS `Agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Agent` (
  `idAgent` int(11) NOT NULL AUTO_INCREMENT,
  `idAgency` int(11) NOT NULL,
  `firstName` varchar(25) NOT NULL,
  `lastName` varchar(25) NOT NULL,
  `phoneMob` varchar(15) DEFAULT NULL,
  `phoneDesk` varchar(15) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idAgent`),
  KEY `agentAgecnyId_idx` (`idAgency`),
  CONSTRAINT `agentAgencyId` FOREIGN KEY (`idAgency`) REFERENCES `Agency` (`idAgency`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Agent`
--

LOCK TABLES `Agent` WRITE;
/*!40000 ALTER TABLE `Agent` DISABLE KEYS */;
INSERT INTO `Agent` VALUES (1,1,'Osvaldo','Lucchini','3482229104','','osvaldo.lucchini@wedi.it'),(2,1,'Marco','Di Terlizzi','3482222222','','marco.diterlizzi@wedi.it'),(3,1,'Stefano','Broccoletti','3471111111','','stefano.broccoletti@wedi.it'),(4,2,'Federico','Messersì','3492222222','','federico@modusmateria.it'),(5,2,'Marco','Mengucci','3360000000','','marco@modusmateria.it');
/*!40000 ALTER TABLE `Agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Company`
--

DROP TABLE IF EXISTS `Company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Company` (
  `idCompany` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) DEFAULT NULL,
  `isGroup` bit(1) DEFAULT NULL,
  `idGroup` int(11) DEFAULT NULL COMMENT 'self reference to company when a company belongs to a group. Null if it does not belong',
  PRIMARY KEY (`idCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Company`
--

LOCK TABLES `Company` WRITE;
/*!40000 ALTER TABLE `Company` DISABLE KEYS */;
INSERT INTO `Company` VALUES (1,'wedi Italia srl',NULL,NULL),(2,'AFIS CLERICI','',NULL),(3,'IDRAS',NULL,1),(4,'ALER - Varese, Como, Monza, Busto',NULL,NULL),(5,'Regina srl',NULL,NULL);
/*!40000 ALTER TABLE `Company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Contact`
--

DROP TABLE IF EXISTS `Contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Contact` (
  `idContact` int(11) NOT NULL AUTO_INCREMENT,
  `idSubsidiary` int(11) DEFAULT NULL,
  `firstName` varchar(25) NOT NULL,
  `lastName` varchar(25) NOT NULL,
  `title` varchar(12) DEFAULT NULL,
  `idRole` smallint(4) NOT NULL,
  `phoneMob` varchar(15) DEFAULT NULL,
  `phoneDesk` varchar(15) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idContact`),
  KEY `contactSubsidiaryId_idx` (`idSubsidiary`),
  CONSTRAINT `contactSubsidiaryId` FOREIGN KEY (`idSubsidiary`) REFERENCES `Subsidiary` (`idSubsidiary`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Contact`
--

LOCK TABLES `Contact` WRITE;
/*!40000 ALTER TABLE `Contact` DISABLE KEYS */;
INSERT INTO `Contact` VALUES (1,2,'Alberto','Montini',NULL,0,'','','alberto.montini@afisclerici.eu'),(2,3,'Carmelo Roberto','Lenzo','Ing',0,'','','roberto.lanzo@alervarese.com'),(3,3,'Emilio','Tagliabue','Arch',0,'','','emilio.tagliabue@alervarese.com'),(4,NULL,'Felice Gian Marco','Volpi','Arch',0,'','+39030717176','arch.marcovolpi@gmail.com');
/*!40000 ALTER TABLE `Contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Event`
--

DROP TABLE IF EXISTS `Event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Event` (
  `idEvent` int(11) NOT NULL AUTO_INCREMENT,
  `idOwner` int(11) NOT NULL,
  `idCompany` int(11) NOT NULL,
  `actionId` smallint(4) NOT NULL COMMENT 'mail, phone call, visit, other',
  `calendarDate` datetime NOT NULL,
  `alertType` smallint(4) NOT NULL,
  PRIMARY KEY (`idEvent`),
  KEY `eventOwnerId_idx` (`idOwner`),
  KEY `eventCompanyId_idx` (`idCompany`),
  CONSTRAINT `eventCompanyId` FOREIGN KEY (`idCompany`) REFERENCES `Company` (`idCompany`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `eventOwnerId` FOREIGN KEY (`idOwner`) REFERENCES `Contact` (`idContact`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Event`
--

LOCK TABLES `Event` WRITE;
/*!40000 ALTER TABLE `Event` DISABLE KEYS */;
INSERT INTO `Event` VALUES (1,2,2,1,'2018-08-31 23:59:59',1);
/*!40000 ALTER TABLE `Event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EventAttachments`
--

DROP TABLE IF EXISTS `EventAttachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EventAttachments` (
  `idEventAttachments` int(11) NOT NULL AUTO_INCREMENT,
  `idEvent` int(11) NOT NULL,
  `documentURL` varchar(4096) NOT NULL,
  PRIMARY KEY (`idEventAttachments`),
  KEY `eventAttachmentsEventId_idx` (`idEvent`),
  CONSTRAINT `eventAttachmentsEventId` FOREIGN KEY (`idEvent`) REFERENCES `Event` (`idEvent`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EventAttachments`
--

LOCK TABLES `EventAttachments` WRITE;
/*!40000 ALTER TABLE `EventAttachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `EventAttachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EventReport`
--

DROP TABLE IF EXISTS `EventReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EventReport` (
  `idEventReport` int(11) NOT NULL AUTO_INCREMENT,
  `idEvent` int(11) DEFAULT NULL,
  `report` blob,
  `text` mediumtext,
  PRIMARY KEY (`idEventReport`),
  KEY `reportEventId_idx` (`idEvent`),
  CONSTRAINT `reportEventId` FOREIGN KEY (`idEvent`) REFERENCES `Event` (`idEvent`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EventReport`
--

LOCK TABLES `EventReport` WRITE;
/*!40000 ALTER TABLE `EventReport` DISABLE KEYS */;
INSERT INTO `EventReport` VALUES (1,1,'\'questo e\'\' il report nel campo BLOB\'','questo il testo nel campo text');
/*!40000 ALTER TABLE `EventReport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Menu`
--

DROP TABLE IF EXISTS `Menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Menu` (
  `idMenu` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `idParent` int(11) NOT NULL,
  `roleConstraintView` int(11) NOT NULL,
  `roleConstraintRead` int(11) NOT NULL,
  `roleConstraintWrite` int(11) NOT NULL,
  `captionRef` varchar(45) NOT NULL,
  `restCall` varchar(45) NOT NULL,
  PRIMARY KEY (`idMenu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Menu`
--

LOCK TABLES `Menu` WRITE;
/*!40000 ALTER TABLE `Menu` DISABLE KEYS */;
INSERT INTO `Menu` VALUES (1,0,1,1000,0,0,'root',''),(2,1,1,1,0,0,'preferences',''),(3,1,1,1,0,0,'referenceData',''),(4,2,3,100,100,100,'clientsAdmin',''),(5,2,3,1,1,100,'clientsView',''),(6,1,1,1,1,1,'reports',''),(7,2,6,1,1,1,'addReport','/rest/reports'),(8,2,2,1,1,1,'update','/rest/preferences'),(9,2,2,1,1,1,'subscribe','/rest/preferences/subscribe'),(10,2,4,100,100,100,'add','/rest/admin/customers'),(11,2,4,100,100,100,'status','/rest/admin/customers/update');
/*!40000 ALTER TABLE `Menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MenuCaption`
--

DROP TABLE IF EXISTS `MenuCaption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MenuCaption` (
  `idMenuCaption` int(11) NOT NULL,
  `idMenu` int(11) NOT NULL DEFAULT '1',
  `idLanguage` char(5) NOT NULL,
  `caption` varchar(45) NOT NULL,
  PRIMARY KEY (`idMenuCaption`),
  KEY `menuCaption_Menu_idx` (`idMenu`),
  CONSTRAINT `menuCaption_Menu` FOREIGN KEY (`idMenu`) REFERENCES `Menu` (`idMenu`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MenuCaption`
--

LOCK TABLES `MenuCaption` WRITE;
/*!40000 ALTER TABLE `MenuCaption` DISABLE KEYS */;
INSERT INTO `MenuCaption` VALUES (1,2,'it-IT','Opzioni'),(2,3,'it-IT','Anagrafiche'),(3,6,'it-IT','Report'),(4,4,'it-IT','Gestisci'),(5,5,'it-IT','Elenco'),(6,8,'it-IT','Modifica'),(7,9,'it-IT','Sottoscrivi'),(8,1,'it-IT','root');
/*!40000 ALTER TABLE `MenuCaption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `idProject` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `financialEstimation` int(11) DEFAULT NULL,
  `city` varchar(45) NOT NULL,
  `areaOfInterest` int(11) NOT NULL COMMENT 'Hotel, sport center, wellness & spa, private construction, public project',
  `idReferral` int(11) NOT NULL,
  `shortDescription` text NOT NULL,
  `idAssignee` int(11) NOT NULL,
  `extReferenceId` varchar(30) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `workStartDate` datetime DEFAULT NULL,
  `scheduledEnd` datetime DEFAULT NULL,
  `customerId` int(11) DEFAULT NULL,
  `supervisorId` int(11) DEFAULT NULL,
  `plannerId` int(11) DEFAULT NULL,
  `keywords` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idProject`),
  KEY `projectReferralId_idx` (`idReferral`),
  KEY `projectAssigneeId_idx` (`idAssignee`),
  KEY `projectPlannerId_idx` (`plannerId`),
  KEY `projectSupervisorId_idx` (`supervisorId`),
  KEY `projectCustmerId_idx` (`customerId`),
  CONSTRAINT `projectAssigneeId` FOREIGN KEY (`idAssignee`) REFERENCES `Contact` (`idContact`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `projectCustmerId` FOREIGN KEY (`customerId`) REFERENCES `Company` (`idCompany`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `projectPlannerId` FOREIGN KEY (`plannerId`) REFERENCES `Contact` (`idContact`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `projectReferralId` FOREIGN KEY (`idReferral`) REFERENCES `Contact` (`idContact`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `projectSupervisorId` FOREIGN KEY (`supervisorId`) REFERENCES `Contact` (`idContact`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
INSERT INTO `Project` VALUES (1,'Alloggi e autorimesse - Como',3749000,'Como',0,1,'Realizzazione di 33 alloggi, unita\' con uso diverso, 32 box. Palazzina residenziale di 6 piano',1,'NII-409332',0,'2018-05-01 00:00:00','2019-12-31 00:00:00',4,NULL,3,NULL),(2,'16 alloggi e posti auto - Brescia ',2500000,'Brescia',0,1,'Costruzione palazzina 2 pft. 16 alloggi con relativi box',1,'NII-438118',0,NULL,NULL,5,NULL,4,NULL),(3,'progetto di osvaldo',1000,'Cremona',1,1,'questa e\' la descrizione del mio progetto',1,'ref id ext',0,'1970-01-01 01:00:00','1970-01-01 01:00:00',1,1,1,'wellness, bagno turco, cazzi e mazzi');
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Subsidiary`
--

DROP TABLE IF EXISTS `Subsidiary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Subsidiary` (
  `idSubsidiary` int(11) NOT NULL AUTO_INCREMENT,
  `idCompany` int(11) NOT NULL,
  `name` varchar(80) DEFAULT NULL,
  `addressLine1` varchar(90) NOT NULL,
  `addressLine2` varchar(90) DEFAULT NULL,
  `zipCode` varchar(6) DEFAULT NULL,
  `city` varchar(45) NOT NULL,
  `provinceOrState` varchar(6) NOT NULL,
  `country` varchar(45) NOT NULL,
  `idExternalERP` varchar(20) DEFAULT NULL,
  `phone1` varchar(15) DEFAULT NULL,
  `phone2` varchar(15) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `web` varchar(65) DEFAULT NULL,
  PRIMARY KEY (`idSubsidiary`),
  KEY `subsidiaryCompanyId` (`idCompany`),
  CONSTRAINT `subsidiaryCompanyId` FOREIGN KEY (`idCompany`) REFERENCES `Company` (`idCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Subsidiary`
--

LOCK TABLES `Subsidiary` WRITE;
/*!40000 ALTER TABLE `Subsidiary` DISABLE KEYS */;
INSERT INTO `Subsidiary` VALUES (1,1,'','via Vimercate 44','','20876','Ornago','MB','IT','I0000300','+390392459420','','info@wedi.it','http://www.wedi.eu/Italy'),(2,2,'headquarter','via del cazzo','','','Brescia','BS','',NULL,NULL,NULL,NULL,NULL),(3,4,'Como','Via Monterosa, 19','','22100','Como','CO','IT','','+390313191','','','http://www.alervarese.com'),(4,4,'Varese','Via Monterosa, 21','','21100','Varese','VA','IT','','+390332806911','','','http://www.alervarese.it'),(5,5,'headquarter','Via Aldo Moro, 10','','25124','Brescia','BS','IT',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `Subsidiary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(45) DEFAULT NULL,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `phoneMob` varchar(15) NOT NULL,
  `phoneLand` varchar(15) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `idRole` smallint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (2,'pippo','osvaldo','lucchini','+393488704431',NULL,'osvaldo.lucchini@gmail.com',1000),(3,'pippo','osvaldo','lucchini','+393488704431',NULL,'osvaldo.lucchini@gmail.com',1000);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserAuth`
--

DROP TABLE IF EXISTS `UserAuth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserAuth` (
  `idUserAuth` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) NOT NULL,
  `createdOn` datetime NOT NULL,
  `lastRefreshedOn` datetime NOT NULL,
  `lastActiveToken` varchar(60) NOT NULL,
  PRIMARY KEY (`idUserAuth`),
  KEY `userAuthUsers_idx` (`idUser`),
  CONSTRAINT `userAuthUsers` FOREIGN KEY (`idUser`) REFERENCES `User` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserAuth`
--

LOCK TABLES `UserAuth` WRITE;
/*!40000 ALTER TABLE `UserAuth` DISABLE KEYS */;
INSERT INTO `UserAuth` VALUES (5,2,'2018-02-26 23:38:48','2018-09-01 00:22:38','7e6677e3-360c-4bff-9d7b-fdf83dc46a53');
/*!40000 ALTER TABLE `UserAuth` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-29 22:19:22
