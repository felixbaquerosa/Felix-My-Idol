-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 19, 2024 at 09:06 PM
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
-- Database: `inventory_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `id` int(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `contact` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(250) NOT NULL,
  `type` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  `Image` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`id`, `email`, `contact`, `username`, `password`, `type`, `status`, `Image`) VALUES
(13, '1', '1', 'USERNAME', 'MZ9NJuPFNrXdhxuyxS4xeA==', 'ADMIN', 'DECLINED', '0'),
(14, 'email', '1', 'user', 'EBGT1xgcyINArlsrF7uooQ==', 'Admin', 'ACTIVE', '0'),
(15, '2', '2', '2', 'nfYjWBcP90UXiA7Mgn1V1Q==', 'USER', 'ACTIVE', '0'),
(16, '3', '3', '3', '9bsMjeFGxntEurv05lhMwA==', 'USER', 'ACTIVE', '0'),
(17, '123@gmail.com', '1234567891', '123', '9bsMjeFGxntEurv05lhMwA==', 'ADMIN', 'Pending', 'src/ImageDB/gg.png'),
(18, '2@gmail.com', '1234567891', '234', '9bsMjeFGxntEurv05lhMwA==', 'ADMIN', 'Pending', 'src/ImageDB/Manacap.png'),
(19, '3@gmail.com', '12312312311', '233', '9bsMjeFGxntEurv05lhMwA==', 'ADMIN', 'Active', 'src/ImageDB/google (1).png'),
(20, '4124124@gmail.com', '12312312311', '323', '9bsMjeFGxntEurv05lhMwA==', 'ADMIN', 'Pending', '');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `o_id` int(50) NOT NULL,
  `o_cname` varchar(50) NOT NULL,
  `o_name` varchar(50) NOT NULL,
  `o_price` varchar(50) NOT NULL,
  `o_stocks` varchar(50) NOT NULL,
  `o_status` varchar(50) NOT NULL,
  `o_method` varchar(50) NOT NULL,
  `o_quantity` int(50) NOT NULL,
  `o_address` varchar(50) NOT NULL,
  `total_profit` varchar(50) DEFAULT NULL,
  `o_approve` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`o_id`, `o_cname`, `o_name`, `o_price`, `o_stocks`, `o_status`, `o_method`, `o_quantity`, `o_address`, `total_profit`, `o_approve`) VALUES
(15, '233', 'Goole', '1000', '10', 'AVAILABLE', 'CASH ON DELIVERY', 10, 'I do Not Know', '-10000.0', 'Delivered');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `p_id` int(20) NOT NULL,
  `p_name` varchar(20) NOT NULL,
  `p_price` varchar(20) NOT NULL,
  `p_stocks` varchar(20) NOT NULL,
  `p_status` varchar(20) NOT NULL,
  `p_image` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`p_id`, `p_name`, `p_price`, `p_stocks`, `p_status`, `p_image`) VALUES
(21, 'Goole', '1000', '20', 'AVAILABLE', 'src/ProductsImage/google.png');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`o_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`p_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `o_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `p_id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
