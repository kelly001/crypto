-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Дек 09 2014 г., 06:52
-- Версия сервера: 5.5.25
-- Версия PHP: 5.3.13

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `crypto`
--
DROP DATABASE `crypto`;
CREATE DATABASE `crypto` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `crypto`;

-- --------------------------------------------------------

--
-- Структура таблицы `certificate`
--
-- Создание: Ноя 18 2014 г., 04:12
--

DROP TABLE IF EXISTS `certificate`;
CREATE TABLE IF NOT EXISTS `certificate` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `filename` varchar(128) NOT NULL,
  `organization` varchar(256) NOT NULL,
  `department` varchar(256) NOT NULL,
  `username` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  `comment` varchar(256) NOT NULL,
  `locality` varchar(256) NOT NULL,
  `state` varchar(256) NOT NULL,
  `country` varchar(256) DEFAULT NULL,
  `timestamp` bigint(10) unsigned NOT NULL,
  `status` tinyint(1) NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`filename`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=30 ;

-- --------------------------------------------------------

--
-- Структура таблицы `user`
--
-- Создание: Ноя 12 2014 г., 06:07
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(10) unsigned NOT NULL,
  `email` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `salt` varchar(256) DEFAULT NULL,
  `timestamp` bigint(11) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `username` varchar(256) NOT NULL,
  `country` varchar(256) DEFAULT NULL,
  `region` varchar(256) DEFAULT NULL,
  `city` varchar(256) DEFAULT NULL,
  `company_id` int(10) unsigned DEFAULT NULL,
  `department` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `email` (`email`(255)),
  KEY `username` (`username`(255))
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=38 ;
SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

GRANT USAGE ON *.* TO 'crypto'@'localhost' IDENTIFIED BY PASSWORD '*971E94AD87E69D62BB31BA2C5058150051FCE452';

GRANT ALL PRIVILEGES ON `crypto`.* TO 'crypto'@'localhost';
