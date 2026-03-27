-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 27, 2026 at 09:51 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lms_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `academic_calendar_periods`
--

CREATE TABLE `academic_calendar_periods` (
  `id` bigint(20) NOT NULL,
  `academic_year` varchar(255) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_date` date NOT NULL,
  `name` varchar(255) NOT NULL,
  `period_type` enum('EXAM_WEEK','HOLIDAY','ORIENTATION','OTHER','SEMESTER') NOT NULL,
  `start_date` date NOT NULL,
  `establishment_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `academic_calendar_periods`
--

INSERT INTO `academic_calendar_periods` (`id`, `academic_year`, `color`, `description`, `end_date`, `name`, `period_type`, `start_date`, `establishment_id`) VALUES
(1, '2025-2026', '#3b82f6', NULL, '2026-06-07', 'Semestre 2', 'SEMESTER', '2026-01-19', 2),
(2, '2025-2026', '#f1044b', NULL, '2026-06-07', 'Examen', 'EXAM_WEEK', '2026-05-25', 2);

-- --------------------------------------------------------

--
-- Table structure for table `classes`
--

CREATE TABLE `classes` (
  `current_students` int(11) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `max_students` int(11) DEFAULT NULL,
  `department_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `academic_year` varchar(255) NOT NULL,
  `level` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `grade_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `classes`
--

INSERT INTO `classes` (`current_students`, `is_active`, `max_students`, `department_id`, `id`, `academic_year`, `level`, `name`, `grade_id`) VALUES
(1, b'1', 25, 1, 1, '2025-2026', '1 Gestion', '1 Gestion 1', NULL),
(0, b'1', 25, 1, 2, '2025-2026', '1 Gestion ', '1 Gestion 2', NULL),
(0, b'1', 25, 2, 4, '2025-2026', '1 info', '1 info 1', NULL),
(3, b'1', 25, 3, 5, '2025-2026', '4 Arctic', '4 Arctic 3', NULL),
(1, b'1', 25, 5, 6, '2025-2026', '1 info', '1 Info A', NULL),
(0, b'1', 25, 5, 7, '2025-2026', '1 info', '1 Info B', NULL),
(0, b'1', 25, 5, 8, '2025-2026', '2 Info', '2 Info A', NULL),
(0, b'1', 25, 5, 9, '2025-2026', '3 Info', '3 Info A', NULL),
(2, b'1', 25, 3, 11, '2025-2026', '4 Arctic', '4 Arctic 2', NULL),
(2, b'1', 25, 2, 12, '2025-2026', '1 info', '1 info 2', 4),
(0, b'1', 25, 6, 13, '2025-2026', '1 RT', '1 RT 1', 7),
(0, b'1', 25, 1, 14, '2025-2026', '2 Gestion ', '2 Gestion 1', NULL),
(0, b'1', 25, 3, 17, '2025-2026', '4 Arctic', '4 Arctic 1', NULL),
(1, b'1', 25, 4, 19, '2025-2026', '4 Twin', '4 Twin 1', NULL),
(1, b'1', 25, 6, 20, '2025-2026', '2 RT', '2 RT 1', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `departments`
--

CREATE TABLE `departments` (
  `establishment_id` bigint(20) NOT NULL,
  `head_user_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `departments`
--

INSERT INTO `departments` (`establishment_id`, `head_user_id`, `id`, `code`, `description`, `name`) VALUES
(1, NULL, 1, 'Ges', NULL, 'Gestion'),
(1, NULL, 2, 'INFO', NULL, 'Informatique'),
(2, NULL, 3, 'ARCTIC', NULL, 'Arctic'),
(2, NULL, 4, 'TWIN', NULL, 'Twin'),
(3, NULL, 5, 'INFO', 'Département des sciences informatiques et logicielles', 'Info'),
(3, NULL, 6, 'RT', 'Département réseaux, sécurité et télécoms', 'RT'),
(3, NULL, 7, 'GL', 'Département génie logiciel et systèmes d\'information', 'GL');

-- --------------------------------------------------------

--
-- Table structure for table `establishments`
--

CREATE TABLE `establishments` (
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','SUSPENDED') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `establishments`
--

INSERT INTO `establishments` (`created_at`, `id`, `address`, `city`, `country`, `email`, `logo_url`, `name`, `phone`, `website`, `status`) VALUES
(NULL, 1, NULL, 'Tunis', 'Tunisia', 'contact@isi.tn', NULL, 'Institut Supérieur d\'Informatique', NULL, NULL, 'ACTIVE'),
(NULL, 2, '', 'ariana', 'tunis', 'esprit.dep@esprit.tn', NULL, 'esprit', '51473724', NULL, 'ACTIVE'),
(NULL, 3, '2 Rue Abou Raihan Al Biruni', 'Tunis', 'Tunisia', 'contact@isit.tn', NULL, 'Institut Supérieur d\'Informatique de Tunis', '71 234 567', 'www.isit.tn', 'ACTIVE');

-- --------------------------------------------------------

--
-- Table structure for table `establishment_grades`
--

CREATE TABLE `establishment_grades` (
  `id` bigint(20) NOT NULL,
  `grade_order` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `establishment_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `establishment_grades`
--

INSERT INTO `establishment_grades` (`id`, `grade_order`, `name`, `establishment_id`) VALUES
(6, 1, '1ère année', 3),
(7, 2, '2ème année', 3),
(8, 3, '3ème année', 3),
(9, 1, '1ère année', 2),
(10, 2, '2ème année', 2),
(11, 3, '3ème année', 2),
(12, 4, '4ème année', 2),
(13, 5, '5ème année', 2),
(14, 1, '1ère année', 1),
(15, 2, '2ème année', 1),
(16, 3, '3ème année', 1);

-- --------------------------------------------------------

--
-- Table structure for table `establishment_time_slots`
--

CREATE TABLE `establishment_time_slots` (
  `id` bigint(20) NOT NULL,
  `end_time` time NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  `slot_order` int(11) NOT NULL,
  `start_time` time NOT NULL,
  `establishment_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `establishment_time_slots`
--

INSERT INTO `establishment_time_slots` (`id`, `end_time`, `label`, `slot_order`, `start_time`, `establishment_id`) VALUES
(7, '12:00:00', 'Morning', 1, '09:00:00', 2),
(8, '16:45:00', 'Afternoon', 2, '13:30:00', 2),
(9, '12:00:00', 'Morning', 1, '09:00:00', 3),
(10, '16:45:00', 'Afternoon', 2, '13:30:00', 3),
(11, '10:00:00', 'Morning 1', 1, '08:00:00', 1),
(12, '12:00:00', 'Morning 2', 2, '10:00:00', 1),
(13, '16:00:00', 'Afternoon', 3, '14:00:00', 1);

-- --------------------------------------------------------

--
-- Table structure for table `mock_students`
--

CREATE TABLE `mock_students` (
  `id` bigint(20) NOT NULL,
  `class_id` bigint(20) DEFAULT NULL,
  `department_id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `establishment_id` bigint(20) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `grade_id` bigint(20) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `level` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `mock_students`
--

INSERT INTO `mock_students` (`id`, `class_id`, `department_id`, `email`, `establishment_id`, `first_name`, `grade_id`, `last_name`, `level`) VALUES
(8, 5, 3, 'fjf,', 2, 'jlzfqs', 12, 'kbejf', '4 Arctic'),
(9, 5, 3, ';,zeqf', 2, 'lzkqrsgd', 12, 'kjgrsvn', '4 Arctic'),
(10, 5, 3, 'lk,ef', 2, 'kz,qesld,', 12, 'lqzedslz', '4 Arctic'),
(11, 11, 3, 'zoghlamimohamed444@gmail.com', 2, 'Zoghlami', 12, 'Mohamed', '4 Arctic'),
(12, 11, 3, 'i,aed', 2, 'jjaej', 12, 'la,eq', '4 Arctic'),
(13, 1, 1, 'm,ef', 1, ',kaez', 14, 'l,afc', '1 Gestion'),
(14, 19, 4, 'jnqfn', 2, 'nrfns', 12, 'lnr', '4 Twin'),
(15, 6, 5, 'lknaf', 3, 'mahmoud', 6, 'nfgra', '1 Info'),
(16, 20, 6, 'm,r', 3, 'zfdn', 7, 'lkg', '2 RT');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `capacity` int(11) NOT NULL,
  `floor` int(11) DEFAULT NULL,
  `has_computers` bit(1) DEFAULT NULL,
  `has_projector` bit(1) DEFAULT NULL,
  `is_available` bit(1) DEFAULT NULL,
  `establishment_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `building` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `type` enum('AMPHITHEATER','CLASSROOM','CONFERENCE','LAB') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`capacity`, `floor`, `has_computers`, `has_projector`, `is_available`, `establishment_id`, `id`, `building`, `name`, `type`) VALUES
(30, 1, b'0', b'1', b'1', 1, 1, 'Bloc A', 'Salle A1', 'CLASSROOM'),
(30, 1, b'0', b'1', b'1', 1, 2, 'Bloc A', 'Salle A2', 'CLASSROOM'),
(30, 1, b'1', b'1', b'1', 2, 3, 'Bloc D', 'D 5', 'CLASSROOM'),
(30, 1, b'0', b'1', b'1', 3, 4, 'Bloc A', 'Salle A1', 'CLASSROOM'),
(30, 1, b'1', b'1', b'1', 3, 5, 'Bloc A', 'alle A2', 'CLASSROOM'),
(30, 0, b'1', b'1', b'1', 3, 6, 'Bloc B', 'Labo Informatique', 'LAB'),
(120, 0, b'0', b'1', b'1', 3, 7, 'Bloc D', 'Amphi Ibn Khaldoun', 'AMPHITHEATER'),
(30, 0, b'0', b'1', b'1', 2, 8, 'D', 'D 1', 'CLASSROOM'),
(30, 0, b'1', b'1', b'1', 1, 9, 'A', 'A1', 'AMPHITHEATER'),
(30, 0, b'1', b'0', b'1', 1, 10, 'C', 'c1', 'CONFERENCE');

-- --------------------------------------------------------

--
-- Table structure for table `teacher_courses`
--

CREATE TABLE `teacher_courses` (
  `course_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `teacher_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `teacher_courses`
--

INSERT INTO `teacher_courses` (`course_id`, `id`, `teacher_id`) VALUES
(1, 10, 1),
(2, 11, 1),
(2, 12, 2),
(3, 13, 2),
(3, 14, 3),
(4, 15, 3),
(5, 16, 1),
(5, 17, 2),
(1, 18, 4),
(1, 19, 5),
(2, 20, 6),
(2, 21, 7),
(2, 22, 3),
(3, 23, 10),
(4, 24, 11),
(5, 25, 12),
(1, 26, 22),
(6, 27, 22),
(2, 28, 23),
(7, 29, 23),
(3, 30, 24),
(8, 31, 24),
(4, 32, 25),
(9, 33, 26),
(5, 34, 27),
(10, 35, 27),
(6, 36, 1);

-- --------------------------------------------------------

--
-- Table structure for table `timetables`
--

CREATE TABLE `timetables` (
  `class_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `academic_year` varchar(255) NOT NULL,
  `semester` enum('S1','S2') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `timetables`
--

INSERT INTO `timetables` (`class_id`, `id`, `academic_year`, `semester`) VALUES
(5, 18, '2025-2026', 'S1'),
(11, 19, '2025-2026', 'S1'),
(6, 20, '2025-2026', 'S1'),
(7, 21, '2025-2026', 'S1'),
(13, 22, '2025-2026', 'S1');

-- --------------------------------------------------------

--
-- Table structure for table `timetable_slots`
--

CREATE TABLE `timetable_slots` (
  `end_time` time NOT NULL,
  `start_time` time NOT NULL,
  `course_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `room_id` bigint(20) NOT NULL,
  `teacher_id` bigint(20) NOT NULL,
  `timetable_id` bigint(20) NOT NULL,
  `day_of_week` enum('FRIDAY','MONDAY','THURSDAY','TUESDAY','WEDNESDAY') NOT NULL,
  `session_type` enum('EXAM','LECTURE','OTHER','TD','TP') NOT NULL,
  `slot_date` date DEFAULT NULL,
  `session_category` enum('EXAM','HOLIDAY','NORMAL','RATTRAPAGE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `timetable_slots`
--

INSERT INTO `timetable_slots` (`end_time`, `start_time`, `course_id`, `id`, `room_id`, `teacher_id`, `timetable_id`, `day_of_week`, `session_type`, `slot_date`, `session_category`) VALUES
('16:45:00', '13:30:00', 1, 102, 3, 1, 18, 'MONDAY', 'LECTURE', '2026-04-27', NULL),
('12:00:00', '09:00:00', 3, 103, 3, 2, 18, 'TUESDAY', 'LECTURE', '2026-04-28', NULL),
('16:45:00', '13:30:00', 5, 104, 3, 1, 18, 'TUESDAY', 'LECTURE', '2026-04-28', NULL),
('12:00:00', '09:00:00', 2, 105, 3, 1, 18, 'WEDNESDAY', 'LECTURE', '2026-04-29', NULL),
('12:00:00', '09:00:00', 4, 106, 8, 11, 19, 'MONDAY', 'LECTURE', '2026-04-27', NULL),
('16:45:00', '13:30:00', 1, 107, 8, 4, 19, 'MONDAY', 'LECTURE', '2026-04-27', NULL),
('12:00:00', '09:00:00', 3, 108, 8, 3, 19, 'TUESDAY', 'LECTURE', '2026-04-28', NULL),
('16:45:00', '13:30:00', 5, 109, 8, 2, 19, 'TUESDAY', 'LECTURE', '2026-04-28', NULL),
('12:00:00', '09:00:00', 2, 110, 8, 2, 19, 'WEDNESDAY', 'LECTURE', '2026-04-29', NULL),
('16:45:00', '13:30:00', 4, 116, 8, 11, 19, 'WEDNESDAY', 'LECTURE', '2026-03-25', NULL),
('12:00:00', '09:00:00', 1, 117, 8, 4, 19, 'THURSDAY', 'LECTURE', '2026-03-26', NULL),
('16:45:00', '13:30:00', 3, 118, 8, 3, 19, 'THURSDAY', 'LECTURE', '2026-03-26', NULL),
('12:00:00', '09:00:00', 5, 119, 8, 12, 19, 'FRIDAY', 'LECTURE', '2026-03-27', NULL),
('16:45:00', '13:30:00', 2, 120, 8, 6, 19, 'FRIDAY', 'LECTURE', '2026-03-27', NULL),
('16:45:00', '13:30:00', 6, 121, 3, 22, 18, 'WEDNESDAY', 'LECTURE', '2026-04-01', NULL),
('12:00:00', '09:00:00', 4, 122, 3, 3, 18, 'THURSDAY', 'LECTURE', '2026-04-02', NULL),
('16:45:00', '13:30:00', 1, 123, 3, 1, 18, 'THURSDAY', 'LECTURE', '2026-04-02', NULL),
('12:00:00', '09:00:00', 6, 124, 3, 22, 19, 'FRIDAY', 'LECTURE', '2026-04-03', NULL),
('16:45:00', '13:30:00', 4, 125, 3, 3, 19, 'FRIDAY', 'LECTURE', '2026-04-03', NULL),
('12:00:00', '09:00:00', 4, 127, 3, 3, 18, 'MONDAY', 'LECTURE', '2026-04-06', NULL),
('16:45:00', '13:30:00', 1, 128, 3, 5, 18, 'MONDAY', 'LECTURE', '2026-04-06', NULL),
('12:00:00', '09:00:00', 3, 129, 3, 10, 18, 'TUESDAY', 'LECTURE', '2026-04-07', NULL),
('16:45:00', '13:30:00', 5, 130, 3, 12, 18, 'TUESDAY', 'LECTURE', '2026-04-07', NULL),
('12:00:00', '09:00:00', 2, 131, 3, 6, 18, 'WEDNESDAY', 'LECTURE', '2026-04-08', NULL),
('12:00:00', '09:00:00', 4, 132, 8, 25, 19, 'MONDAY', 'LECTURE', '2026-04-06', NULL),
('16:45:00', '13:30:00', 1, 133, 8, 22, 19, 'MONDAY', 'LECTURE', '2026-04-06', NULL),
('12:00:00', '09:00:00', 3, 134, 8, 24, 19, 'TUESDAY', 'LECTURE', '2026-04-07', NULL),
('16:45:00', '13:30:00', 5, 135, 8, 27, 19, 'TUESDAY', 'LECTURE', '2026-04-07', NULL),
('12:00:00', '09:00:00', 2, 136, 8, 7, 19, 'WEDNESDAY', 'LECTURE', '2026-04-08', NULL),
('16:45:00', '13:30:00', 4, 137, 3, 3, 18, 'MONDAY', 'LECTURE', '2026-05-11', NULL),
('12:00:00', '09:00:00', 1, 138, 3, 1, 18, 'TUESDAY', 'LECTURE', '2026-05-12', NULL),
('16:45:00', '13:30:00', 3, 139, 3, 3, 18, 'TUESDAY', 'LECTURE', '2026-05-12', NULL),
('12:00:00', '09:00:00', 5, 140, 3, 12, 18, 'WEDNESDAY', 'LECTURE', '2026-05-13', NULL),
('16:45:00', '13:30:00', 2, 141, 3, 1, 18, 'WEDNESDAY', 'LECTURE', '2026-05-13', NULL),
('16:45:00', '13:30:00', 4, 142, 8, 11, 19, 'MONDAY', 'LECTURE', '2026-05-11', NULL),
('12:00:00', '09:00:00', 1, 143, 8, 4, 19, 'TUESDAY', 'LECTURE', '2026-05-12', NULL),
('16:45:00', '13:30:00', 3, 144, 8, 10, 19, 'TUESDAY', 'LECTURE', '2026-05-12', NULL),
('12:00:00', '09:00:00', 5, 145, 8, 27, 19, 'WEDNESDAY', 'LECTURE', '2026-05-13', NULL),
('16:45:00', '13:30:00', 2, 146, 8, 2, 19, 'WEDNESDAY', 'LECTURE', '2026-05-13', NULL),
('16:45:00', '13:30:00', 4, 147, 3, 25, 18, 'MONDAY', 'LECTURE', '2026-05-04', NULL),
('12:00:00', '09:00:00', 1, 148, 3, 5, 18, 'TUESDAY', 'LECTURE', '2026-05-05', NULL),
('16:45:00', '13:30:00', 3, 149, 3, 24, 18, 'TUESDAY', 'LECTURE', '2026-05-05', NULL),
('16:45:00', '13:30:00', 5, 150, 3, 12, 18, 'WEDNESDAY', 'LECTURE', '2026-05-06', NULL),
('12:00:00', '09:00:00', 2, 151, 3, 1, 18, 'THURSDAY', 'LECTURE', '2026-05-07', NULL),
('12:00:00', '09:00:00', 4, 152, 8, 11, 19, 'TUESDAY', 'LECTURE', '2026-05-05', NULL),
('16:45:00', '13:30:00', 1, 153, 8, 4, 19, 'TUESDAY', 'LECTURE', '2026-05-05', NULL),
('12:00:00', '09:00:00', 3, 154, 3, 3, 19, 'WEDNESDAY', 'LECTURE', '2026-05-06', NULL),
('16:45:00', '13:30:00', 5, 155, 8, 27, 19, 'WEDNESDAY', 'LECTURE', '2026-05-06', NULL),
('12:00:00', '09:00:00', 2, 156, 8, 2, 19, 'THURSDAY', 'LECTURE', '2026-05-07', NULL),
('12:00:00', '09:00:00', 4, 157, 3, 25, 18, 'TUESDAY', 'LECTURE', '2026-05-19', NULL),
('16:45:00', '13:30:00', 1, 158, 3, 5, 18, 'TUESDAY', 'LECTURE', '2026-05-19', NULL),
('12:00:00', '09:00:00', 3, 159, 3, 10, 18, 'WEDNESDAY', 'LECTURE', '2026-05-20', NULL),
('12:00:00', '09:00:00', 5, 160, 3, 12, 18, 'THURSDAY', 'LECTURE', '2026-05-21', NULL),
('16:45:00', '13:30:00', 2, 161, 3, 2, 18, 'THURSDAY', 'LECTURE', '2026-05-21', NULL),
('16:45:00', '13:30:00', 4, 162, 8, 11, 19, 'TUESDAY', 'LECTURE', '2026-05-19', NULL),
('12:00:00', '09:00:00', 1, 163, 8, 4, 19, 'WEDNESDAY', 'LECTURE', '2026-05-20', NULL),
('16:45:00', '13:30:00', 3, 164, 3, 3, 19, 'WEDNESDAY', 'LECTURE', '2026-05-20', NULL),
('12:00:00', '09:00:00', 5, 165, 8, 27, 19, 'THURSDAY', 'LECTURE', '2026-05-21', NULL),
('16:45:00', '13:30:00', 2, 166, 8, 6, 19, 'THURSDAY', 'LECTURE', '2026-05-21', NULL),
('16:45:00', '13:30:00', 4, 167, 3, 25, 18, 'TUESDAY', 'LECTURE', '2026-05-26', NULL),
('12:00:00', '09:00:00', 1, 168, 3, 5, 18, 'WEDNESDAY', 'LECTURE', '2026-05-27', NULL),
('16:45:00', '13:30:00', 3, 169, 3, 10, 18, 'WEDNESDAY', 'LECTURE', '2026-05-27', NULL),
('16:45:00', '13:30:00', 5, 170, 3, 12, 18, 'THURSDAY', 'LECTURE', '2026-05-28', NULL),
('12:00:00', '09:00:00', 2, 171, 3, 1, 18, 'FRIDAY', 'LECTURE', '2026-05-29', NULL),
('12:00:00', '09:00:00', 4, 172, 8, 11, 19, 'WEDNESDAY', 'LECTURE', '2026-05-27', NULL),
('16:45:00', '13:30:00', 1, 173, 8, 4, 19, 'WEDNESDAY', 'LECTURE', '2026-05-27', NULL),
('12:00:00', '09:00:00', 3, 174, 3, 10, 19, 'THURSDAY', 'LECTURE', '2026-05-28', NULL),
('16:45:00', '13:30:00', 5, 175, 8, 27, 19, 'THURSDAY', 'LECTURE', '2026-05-28', NULL),
('12:00:00', '09:00:00', 2, 176, 8, 2, 19, 'FRIDAY', 'LECTURE', '2026-05-29', NULL),
('16:45:00', '13:30:00', 4, 177, 3, 25, 18, 'WEDNESDAY', 'LECTURE', '2026-04-08', NULL),
('12:00:00', '09:00:00', 1, 178, 3, 4, 18, 'FRIDAY', 'LECTURE', '2026-04-10', NULL),
('16:45:00', '13:30:00', 3, 179, 3, 2, 18, 'FRIDAY', 'LECTURE', '2026-04-10', NULL),
('12:00:00', '09:00:00', 4, 180, 8, 3, 19, 'FRIDAY', 'LECTURE', '2026-04-10', NULL),
('16:45:00', '13:30:00', 1, 181, 8, 1, 19, 'FRIDAY', 'LECTURE', '2026-04-10', NULL),
('12:00:00', '09:00:00', 4, 191, 3, 25, 18, 'THURSDAY', 'LECTURE', '2026-04-23', NULL),
('16:45:00', '13:30:00', 1, 192, 3, 5, 18, 'THURSDAY', 'LECTURE', '2026-04-23', NULL),
('12:00:00', '09:00:00', 3, 193, 3, 24, 18, 'FRIDAY', 'LECTURE', '2026-04-24', NULL),
('16:45:00', '13:30:00', 5, 194, 3, 27, 18, 'FRIDAY', 'LECTURE', '2026-04-24', NULL),
('16:45:00', '13:30:00', 4, 195, 8, 11, 19, 'THURSDAY', 'LECTURE', '2026-04-23', NULL),
('12:00:00', '09:00:00', 1, 196, 8, 5, 19, 'FRIDAY', 'LECTURE', '2026-04-24', NULL),
('16:45:00', '13:30:00', 3, 197, 8, 10, 19, 'FRIDAY', 'LECTURE', '2026-04-24', NULL),
('16:45:00', '13:30:00', 4, 198, 3, 25, 18, 'THURSDAY', 'LECTURE', '2026-05-14', NULL),
('16:45:00', '13:30:00', 1, 199, 3, 4, 18, 'FRIDAY', 'LECTURE', '2026-05-15', NULL),
('12:00:00', '09:00:00', 4, 200, 3, 11, 19, 'FRIDAY', 'LECTURE', '2026-05-15', NULL),
('16:45:00', '13:30:00', 1, 201, 8, 5, 19, 'FRIDAY', 'LECTURE', '2026-05-15', NULL),
('12:00:00', '09:00:00', 6, 202, 4, 22, 20, 'MONDAY', 'LECTURE', '2026-03-30', 'NORMAL'),
('16:45:00', '13:30:00', 7, 203, 4, 23, 20, 'MONDAY', 'LECTURE', '2026-03-30', 'NORMAL'),
('12:00:00', '09:00:00', 8, 204, 4, 24, 20, 'WEDNESDAY', 'LECTURE', '2026-04-01', 'NORMAL'),
('16:45:00', '13:30:00', 9, 205, 4, 26, 20, 'WEDNESDAY', 'LECTURE', '2026-04-01', 'NORMAL'),
('12:00:00', '09:00:00', 6, 206, 4, 22, 21, 'TUESDAY', 'LECTURE', '2026-03-31', 'NORMAL'),
('16:45:00', '13:30:00', 7, 207, 4, 23, 21, 'TUESDAY', 'LECTURE', '2026-03-31', 'NORMAL'),
('16:45:00', '13:30:00', 8, 208, 5, 24, 21, 'WEDNESDAY', 'LECTURE', '2026-04-01', 'NORMAL'),
('12:00:00', '09:00:00', 9, 209, 4, 26, 21, 'THURSDAY', 'LECTURE', '2026-04-02', 'NORMAL'),
('12:00:00', '09:00:00', 6, 210, 4, 22, 20, 'MONDAY', 'LECTURE', '2026-04-06', 'NORMAL'),
('16:45:00', '13:30:00', 7, 211, 4, 23, 20, 'MONDAY', 'LECTURE', '2026-04-06', 'NORMAL'),
('12:00:00', '09:00:00', 8, 212, 4, 24, 20, 'WEDNESDAY', 'LECTURE', '2026-04-08', 'NORMAL'),
('16:45:00', '13:30:00', 9, 213, 4, 26, 20, 'WEDNESDAY', 'LECTURE', '2026-04-08', 'NORMAL'),
('12:00:00', '09:00:00', 6, 214, 4, 22, 21, 'TUESDAY', 'LECTURE', '2026-04-07', 'NORMAL'),
('16:45:00', '13:30:00', 7, 215, 4, 23, 21, 'TUESDAY', 'LECTURE', '2026-04-07', 'NORMAL'),
('16:45:00', '13:30:00', 8, 216, 5, 24, 21, 'WEDNESDAY', 'LECTURE', '2026-04-08', 'NORMAL'),
('12:15:00', '09:00:00', 3, 217, 4, 2, 20, 'TUESDAY', 'LECTURE', '2026-03-31', 'RATTRAPAGE'),
('12:00:00', '09:00:00', 8, 218, 5, 24, 22, 'MONDAY', 'LECTURE', '2026-03-30', 'NORMAL'),
('16:45:00', '13:30:00', 6, 219, 5, 22, 22, 'TUESDAY', 'LECTURE', '2026-03-31', 'NORMAL'),
('12:00:00', '09:00:00', 4, 220, 5, 25, 22, 'WEDNESDAY', 'LECTURE', '2026-04-01', 'NORMAL');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `academic_calendar_periods`
--
ALTER TABLE `academic_calendar_periods`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhnxvbrmmawo3plqb6412ukhw4` (`establishment_id`);

--
-- Indexes for table `classes`
--
ALTER TABLE `classes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKeerjjltjmtwpjo3jlr7037vxt` (`department_id`);

--
-- Indexes for table `departments`
--
ALTER TABLE `departments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKst1h8ircnlw9q1r0bw04rq6aw` (`establishment_id`);

--
-- Indexes for table `establishments`
--
ALTER TABLE `establishments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKdr6k8vj9mhkf3k3a5q1j5h9fk` (`email`);

--
-- Indexes for table `establishment_grades`
--
ALTER TABLE `establishment_grades`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKflldkd48xt8owwelhsf9ipw4n` (`establishment_id`);

--
-- Indexes for table `establishment_time_slots`
--
ALTER TABLE `establishment_time_slots`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3vp1avh6f1cpdwvdb9255cwxp` (`establishment_id`);

--
-- Indexes for table `mock_students`
--
ALTER TABLE `mock_students`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKdvwoxcs3diway2t81gooo4846` (`establishment_id`);

--
-- Indexes for table `teacher_courses`
--
ALTER TABLE `teacher_courses`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `timetables`
--
ALTER TABLE `timetables`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkxq2wdv8anfrbu9wasnl4h1h2` (`class_id`);

--
-- Indexes for table `timetable_slots`
--
ALTER TABLE `timetable_slots`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6ot8sov3bx1yxhgimv7h707lx` (`room_id`),
  ADD KEY `FK9obefb0x2okio4i9xugjtpnqp` (`timetable_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `academic_calendar_periods`
--
ALTER TABLE `academic_calendar_periods`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `classes`
--
ALTER TABLE `classes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `departments`
--
ALTER TABLE `departments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `establishments`
--
ALTER TABLE `establishments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `establishment_grades`
--
ALTER TABLE `establishment_grades`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `establishment_time_slots`
--
ALTER TABLE `establishment_time_slots`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `mock_students`
--
ALTER TABLE `mock_students`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `teacher_courses`
--
ALTER TABLE `teacher_courses`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `timetables`
--
ALTER TABLE `timetables`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `timetable_slots`
--
ALTER TABLE `timetable_slots`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=221;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `academic_calendar_periods`
--
ALTER TABLE `academic_calendar_periods`
  ADD CONSTRAINT `FKhnxvbrmmawo3plqb6412ukhw4` FOREIGN KEY (`establishment_id`) REFERENCES `establishments` (`id`);

--
-- Constraints for table `classes`
--
ALTER TABLE `classes`
  ADD CONSTRAINT `FKeerjjltjmtwpjo3jlr7037vxt` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`);

--
-- Constraints for table `departments`
--
ALTER TABLE `departments`
  ADD CONSTRAINT `FKst1h8ircnlw9q1r0bw04rq6aw` FOREIGN KEY (`establishment_id`) REFERENCES `establishments` (`id`);

--
-- Constraints for table `establishment_grades`
--
ALTER TABLE `establishment_grades`
  ADD CONSTRAINT `FKflldkd48xt8owwelhsf9ipw4n` FOREIGN KEY (`establishment_id`) REFERENCES `establishments` (`id`);

--
-- Constraints for table `establishment_time_slots`
--
ALTER TABLE `establishment_time_slots`
  ADD CONSTRAINT `FK3vp1avh6f1cpdwvdb9255cwxp` FOREIGN KEY (`establishment_id`) REFERENCES `establishments` (`id`);

--
-- Constraints for table `rooms`
--
ALTER TABLE `rooms`
  ADD CONSTRAINT `FKdvwoxcs3diway2t81gooo4846` FOREIGN KEY (`establishment_id`) REFERENCES `establishments` (`id`);

--
-- Constraints for table `timetables`
--
ALTER TABLE `timetables`
  ADD CONSTRAINT `FKkxq2wdv8anfrbu9wasnl4h1h2` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

--
-- Constraints for table `timetable_slots`
--
ALTER TABLE `timetable_slots`
  ADD CONSTRAINT `FK6ot8sov3bx1yxhgimv7h707lx` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`),
  ADD CONSTRAINT `FK9obefb0x2okio4i9xugjtpnqp` FOREIGN KEY (`timetable_id`) REFERENCES `timetables` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
