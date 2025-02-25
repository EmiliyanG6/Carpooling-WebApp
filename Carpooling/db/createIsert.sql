-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.5.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for carpooling
CREATE DATABASE IF NOT EXISTS `carpooling` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `carpooling`;

-- Dumping structure for table carpooling.feedback
CREATE TABLE IF NOT EXISTS `feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `giver_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_users_id_fk` (`giver_id`),
  KEY `feedback_users_id_fk_2` (`receiver_id`),
  CONSTRAINT `feedback_users_id_fk` FOREIGN KEY (`giver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `feedback_users_id_fk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Dumping data for table carpooling.feedback: ~2 rows (approximately)
INSERT INTO `feedback` (`id`, `giver_id`, `receiver_id`, `rating`, `comment`) VALUES
	(12, 80, 82, 3, 'He is good driver and very funny person'),
	(13, 82, 80, 4, 'Good driver');

-- Dumping structure for table carpooling.passengers
CREATE TABLE IF NOT EXISTS `passengers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `travel_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `passengers_travels_id_fk` (`travel_id`),
  KEY `passengers_users_id_fk` (`user_id`),
  CONSTRAINT `passengers_travels_id_fk` FOREIGN KEY (`travel_id`) REFERENCES `travels` (`id`) ON DELETE CASCADE,
  CONSTRAINT `passengers_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Dumping data for table carpooling.passengers: ~2 rows (approximately)
INSERT INTO `passengers` (`id`, `travel_id`, `user_id`, `status`) VALUES
	(30, 39, 81, 'APPROVED'),
	(31, 39, 80, 'PENDING');

-- Dumping structure for table carpooling.travels
CREATE TABLE IF NOT EXISTS `travels` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `driver_id` bigint(20) NOT NULL,
  `start_point` varchar(255) NOT NULL,
  `end_point` varchar(255) NOT NULL,
  `departure_time` timestamp NOT NULL,
  `free_spots` int(11) NOT NULL,
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `completed` tinyint(1) DEFAULT 0,
  `travel_status` enum('ACTIVE','COMPLETED','CANCELED') DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`),
  KEY `travels_users_id_fk` (`driver_id`),
  CONSTRAINT `travels_users_id_fk` FOREIGN KEY (`driver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Dumping data for table carpooling.travels: ~3 rows (approximately)
INSERT INTO `travels` (`id`, `driver_id`, `start_point`, `end_point`, `departure_time`, `free_spots`, `comment`, `completed`, `travel_status`) VALUES
	(39, 82, 'Sofia', 'Varna', '2025-02-22 10:24:00', 3, 'Без кучета', 0, 'ACTIVE'),
	(40, 83, 'Varna', 'Burgas', '2025-02-20 09:30:00', 3, 'Няма мястно за много багаж', 0, 'ACTIVE'),
	(41, 80, 'Stara Zagora', 'Nova Zagora', '2025-02-20 09:10:00', 4, 'Без много багаж', 0, 'ACTIVE');

-- Dumping structure for table carpooling.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `is_admin` tinyint(1) DEFAULT 0,
  `is_blocked` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_pk_2` (`username`),
  UNIQUE KEY `users_pk_3` (`email`),
  UNIQUE KEY `users_pk_4` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Dumping data for table carpooling.users: ~4 rows (approximately)
INSERT INTO `users` (`id`, `username`, `password`, `first_name`, `last_name`, `email`, `phone_number`, `is_admin`, `is_blocked`) VALUES
	(80, 'GeorgiP', 'georgi123', 'Georgi', 'Petrov', 'georgi@gmail.com', '0892157788', 0, 0),
	(81, 'PreslavI', 'preslav123', 'Preslav', 'Ivanov', 'preslav@gmail.com', '0876058899', 0, 0),
	(82, 'MihailG', 'mihail123', 'Mihail', 'Georgiev', 'mihail@gmail.com', '0896541122', 0, 0),
	(83, 'IvanP', 'ivanp123', 'Ivan', 'Petrov', 'ivan@gmail.com', '0896758833', 0, 0),
	(84, 'GeorgiN', 'georgi123', 'Georgi', 'Nedev', 'georgiNedev@gmail.com', '0893025588', 0, 0);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
