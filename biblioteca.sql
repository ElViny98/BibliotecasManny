/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.5-10.1.33-MariaDB : Database - Biblioteca_Manny
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`Biblioteca_Manny` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `Biblioteca_Manny`;

/*Table structure for table `cliente` */

DROP TABLE IF EXISTS `cliente`;

CREATE TABLE `cliente` (
  `IdCliente` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(20) DEFAULT NULL,
  `Direccion` varchar(45) DEFAULT NULL,
  `Telefono` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`IdCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `cliente` */

insert  into `cliente`(`IdCliente`,`Nombre`,`Direccion`,`Telefono`) values (1,'Jesus Espino Cardena','calle. Mar de java 221','669-564-3465'),(2,'Charly Piña Sanchez','calle. Miguel Hidalgo 123','669-455-4534'),(3,'Jaramillo Lopez','calle. La Perla 231','669-456-7467'),(4,'Dania Melissa Perez','calle. Juan Pablo 423','669-223-6362'),(5,'Alfonso Perez Quinon','calle. Ostiones 331','669-523-5473'),(6,'Jesus Alejandro Osun','calle. pradera 532','669-765-4533');

/*Table structure for table `libro` */

DROP TABLE IF EXISTS `libro`;

CREATE TABLE `libro` (
  `IdLibro` int(11) NOT NULL AUTO_INCREMENT,
  `Titulo` varchar(40) DEFAULT NULL,
  `Autor` varchar(30) DEFAULT NULL,
  `Anio` int(4) DEFAULT NULL,
  `Genero` varchar(15) DEFAULT NULL,
  `CopiasPrestadas` int(2) DEFAULT NULL,
  `TotalCopias` int(2) DEFAULT NULL,
  `Sucursal_IdSucursal` int(11) NOT NULL,
  `Editorial` varchar(20) DEFAULT NULL,
  `NumPaginas` int(4) DEFAULT NULL,
  `CostoPerdida` float(5,2) DEFAULT NULL,
  PRIMARY KEY (`IdLibro`,`Sucursal_IdSucursal`),
  KEY `fk_Libro_Sucursal1_idx` (`Sucursal_IdSucursal`),
  CONSTRAINT `fk_Libro_Sucursal1` FOREIGN KEY (`Sucursal_IdSucursal`) REFERENCES `sucursal` (`IdSucursal`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;

/*Data for the table `libro` */

insert  into `libro`(`IdLibro`,`Titulo`,`Autor`,`Anio`,`Genero`,`CopiasPrestadas`,`TotalCopias`,`Sucursal_IdSucursal`,`Editorial`,`NumPaginas`,`CostoPerdida`) values (1,'Sinsajo','Suzanne Collins',2010,'Ciencia Ficcion',0,3,1,'RBA Libros - 2012',422,600.00),(2,'Corazon del hambre','Xavier Aldekoa',2011,'Periodistico',0,2,1,'Ebooks de Van - 2011',84,500.00),(3,'Fantasmas','Joe Hill',2010,'Terror',0,4,1,'ABC books - 2012',323,479.00),(4,'Hombres de negro','Sam Ismael',2013,'Ciencia Ficcion',0,2,1,'ADC Mag - 2014',443,350.00),(5,'Dragones Chill','Adair Perez',2014,'Ficcion',0,4,1,'RockOut - 2014',500,654.00),(6,'The official smash','Eric Medina',2011,'Informativo',0,4,1,'OOF - 2013',344,203.00),(7,'Hamlett','Shakespear',2000,'Tragedia',0,5,1,'Sugar - 2000',432,300.00),(8,'Paco el chato','Juan Pantuflas',2013,'Comedia',0,4,2,'CDNA - 2015',100,400.00),(9,'Elijah of Boston','Ben Oswell',2012,'Drama',0,5,2,'DFG - 2012',412,792.00),(10,'Mocking Bird','Shakespear',2001,'Drama',0,3,2,'Funmom - 2002',321,454.00),(11,'El retrato de Dorian Grey','Oscar Wild',2004,'Drama',0,4,2,'NEWs - 2005',325,389.00),(12,'El club de las luchas','Chuck Palownski',2000,'Suspenso',0,4,2,'Rich - 2002',347,505.00),(13,'Elanor & Park','J.K. Rowelling',2010,'Drama',0,3,2,'Fash - 2010',298,460.00),(14,'Ciudades de Papel','Jhon Green',2012,'Romance',0,3,2,'CMS - 2012',323,590.00),(15,'Bajo la misma Estrella','Jhon Green',2013,'Tragedia',0,3,2,'MAD - 2014',432,600.00),(16,'Buscando Alaska','Jhon Green',2015,'Romance',0,3,2,'ALK - 2015',298,356.00),(17,'Elijah of Boston','Ben Oswell',2012,'Drama',0,3,3,'DFG - 2012',412,792.00),(18,'El retrato de Dorian Grey','Oscar Wild',2004,'Drama',0,4,1,'NEWs - 2005',325,389.00),(19,'El club de las luchas','Chuck Palownski',2000,'Suspenso',0,4,1,'Rich - 2002',347,505.00),(20,'Elanor & Park','J.K. Rowelling',2010,'Drama',0,3,1,'Fash - 2010',298,460.00),(21,'El retrato de Dorian Grey','Oscar Wild',2004,'Drama',0,4,3,'NEWs - 2005',325,389.00),(22,'El club de las luchas','Chuck Palownski',2000,'Suspenso',0,4,3,'Rich - 2002',347,505.00),(23,'Elanor & Park','J.K. Rowelling',2010,'Drama',0,3,3,'Fash - 2010',298,460.00),(24,'Crepusculo','Stephani Mayer',2010,'romance',0,3,3,'ADF - 2011',340,400.00),(25,'nueva luna','Stephani Mayer',2011,'romance',0,3,3,'ADF - 2012',456,500.00),(26,'Eclipse','Stephani Mayer',2013,'romance',0,3,3,'ADF - 2013',440,500.00),(27,'Amanecer','Stephani Mayer',2014,'romance',0,3,3,'ADF - 2014',480,700.00),(29,'Maravilloso Desastre','David Marsh',2012,'romance',0,3,3,'JUY - 2013',369,630.00),(31,'Scarletts web','Stephan Jhonson',2012,'aventura',0,3,3,'FOK - 2012',340,350.00),(32,'El diario de Greg','Max Robinson',2013,'Comedia',0,3,3,'KFC - 2013',400,300.00),(43,'Malevolo','Picazo',1230,'Drama',0,2,1,'Pepin',500,400.00),(59,'a','a',12,'Lírica',0,12,1,'a',12,12.00);

/*Table structure for table `reserva` */

DROP TABLE IF EXISTS `reserva`;

CREATE TABLE `reserva` (
  `Cliente_IdCliente` int(11) NOT NULL,
  `Libro_IdLibro` int(11) NOT NULL,
  `FechaPrestamo` datetime DEFAULT NULL,
  `FechaEntrega` datetime DEFAULT NULL,
  `FechaLimite` datetime DEFAULT NULL,
  `Multa` float(5,2) DEFAULT NULL,
  PRIMARY KEY (`Cliente_IdCliente`,`Libro_IdLibro`),
  KEY `fk_Cliente_has_Libro_Libro1_idx` (`Libro_IdLibro`),
  KEY `fk_Cliente_has_Libro_Cliente_idx` (`Cliente_IdCliente`),
  CONSTRAINT `fk_Cliente_has_Libro_Cliente` FOREIGN KEY (`Cliente_IdCliente`) REFERENCES `cliente` (`IdCliente`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cliente_has_Libro_Libro1` FOREIGN KEY (`Libro_IdLibro`) REFERENCES `libro` (`IdLibro`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `reserva` */

insert  into `reserva`(`Cliente_IdCliente`,`Libro_IdLibro`,`FechaPrestamo`,`FechaEntrega`,`FechaLimite`,`Multa`) values (1,5,'2018-06-28 22:00:20','2018-06-30 22:00:27','2018-06-30 22:00:31',NULL),(2,2,'2018-06-06 22:00:38','0000-00-00 00:00:00','2018-07-01 22:00:46',NULL),(3,32,'2018-06-06 22:00:49','0000-00-00 00:00:00','2018-06-29 22:00:57',NULL),(4,23,'2018-06-05 22:01:06','2018-06-22 22:01:10','2018-06-22 22:01:12',NULL),(6,15,'2018-06-13 22:01:16','2018-07-06 22:01:19','2018-06-22 22:01:22',NULL);

/*Table structure for table `sucursal` */

DROP TABLE IF EXISTS `sucursal`;

CREATE TABLE `sucursal` (
  `IdSucursal` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) DEFAULT NULL,
  `Direccion` varchar(45) DEFAULT NULL,
  `IP` varchar(15) DEFAULT NULL,
  `Password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`IdSucursal`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `sucursal` */

insert  into `sucursal`(`IdSucursal`,`Nombre`,`Direccion`,`IP`,`Password`) values (1,'BiblioVini','Culiacan Av. Juarez','192.168.4.73','1234567'),(2,'BiblioBryan','Mazatlan, Av. Localhost','192.168.1.74','1234567'),(3,'Biblioteca el Aldas','Monterey Av. Lazaña Cardenas','192.168.1.73','1234567');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
