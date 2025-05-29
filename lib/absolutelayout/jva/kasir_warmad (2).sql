-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 28, 2025 at 09:28 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kasir_warmad`
--

-- --------------------------------------------------------

--
-- Table structure for table `barang_jual`
--

CREATE TABLE `barang_jual` (
  `id_barang_jual` varchar(15) NOT NULL,
  `nama_barang` varchar(100) NOT NULL,
  `varian` varchar(15) DEFAULT NULL,
  `kategori_barang` varchar(20) NOT NULL,
  `barcode_barang` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `barang_jual`
--

INSERT INTO `barang_jual` (`id_barang_jual`, `nama_barang`, `varian`, `kategori_barang`, `barcode_barang`) VALUES
('BRNGBR25001', 'Susu', 'Coklat', 'Minuman', '9999'),
('BRNGBR25002', 'Nabati', 'Coklat', 'Snack', '2222'),
('BRNGBR25003', 'Pel Pelan', 'Hijaiu', 'Alat Rumah Tangga', '5555');

-- --------------------------------------------------------

--
-- Table structure for table `detail_transaksi_pelanggan`
--

CREATE TABLE `detail_transaksi_pelanggan` (
  `id_detail_transaksi_pelanggan` int(11) NOT NULL,
  `id_transaksi_kasir` int(11) NOT NULL,
  `id_barang_jual` varchar(15) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `harga_satuan` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `diskon` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `detail_transaksi_pelanggan`
--

INSERT INTO `detail_transaksi_pelanggan` (`id_detail_transaksi_pelanggan`, `id_transaksi_kasir`, `id_barang_jual`, `jumlah`, `harga_satuan`, `subtotal`, `diskon`) VALUES
(26, 29, 'BRNGBR25002', 100, 2000.00, 200000.00, 0.00),
(27, 30, 'BRNGBR25003', 5, 20000.00, 100000.00, 0.00),
(28, 30, 'BRNGBR25003', 100, 20000.00, 2000000.00, 0.00),
(29, 31, 'BRNGBR25002', 200, 2000.00, 400000.00, 0.00),
(30, 32, 'BRNGBR25001', 2, 5000.00, 10000.00, 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `pengguna_aplikasi`
--

CREATE TABLE `pengguna_aplikasi` (
  `id_pengguna` int(11) NOT NULL,
  `username` varchar(15) NOT NULL,
  `password` varchar(70) NOT NULL,
  `email` varchar(20) NOT NULL,
  `rfid_kode` varchar(20) DEFAULT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'kasir'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengguna_aplikasi`
--

INSERT INTO `pengguna_aplikasi` (`id_pengguna`, `username`, `password`, `email`, `rfid_kode`, `role`) VALUES
(1, 'admin1', 'admin', 'admin@gmail.com', '3699556921', 'kasir'),
(3, 'kasirku', '12345', ' kasirku@gmail.com', '1044221550', 'kasir'),
(4, 'air', '123', 'air@gmail.com', NULL, 'kasir'),
(6, 'ratna', '123', 'na@gmail.com', '1222', 'kasir'),
(7, 'ra', '$2a$10$TalJB1axNPgcPZuVkonAdO/4ZVarCcV.6sf97BkGuUtdBvTl7p4LO', 'na@gmail.com', '221', 'kasir'),
(8, 'admin', '$2a$10$U1hfhLfEw6OjlZl6ypr7pOx2jIHouB1R9N3VjgSOcXbMfr6i/Xx8.', 'admin@example.com', 'RFID_ADMIN', 'admin'),
(9, 'kasir', '$2a$10$1XIpOZGuZfJxRVKXy0Wk8ev0KU8Aq9Er0rm5u9W8e18tF2oiM2RCW', 'kasir@example.com', 'RFID_KASIR', 'kasir'),
(10, 'vivi', '$2a$10$bN2siLDFxtUNzReMEFEXY.fwhvVXPCHSvVW5NdmnUu8rv22f..lDy', 'vivi@gmail.com', '12345', 'kasir'),
(12, 'ovi', '$2a$10$LQmzmBsl8BhlB7NSx4madekKFNxAEUonnd9..y7/qxqe29lQSJgEy', 'ovi@gmail.com', '5678', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `retur_barang`
--

CREATE TABLE `retur_barang` (
  `id_retur` varchar(15) NOT NULL,
  `id_barang_jual` varchar(20) DEFAULT NULL,
  `nama_barang` varchar(20) DEFAULT NULL,
  `jumlah_retur` int(11) DEFAULT NULL,
  `tanggal_retur` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `stok_gudang`
--

CREATE TABLE `stok_gudang` (
  `id_stok_gudang` varchar(15) NOT NULL,
  `id_barang_jual` varchar(15) NOT NULL,
  `id_supplier_barang` varchar(15) NOT NULL,
  `harga_beli` decimal(10,2) NOT NULL,
  `harga_jual` decimal(10,2) NOT NULL,
  `jumlah_stok` int(11) NOT NULL,
  `tanggal_kadaluarsa` date NOT NULL,
  `tanggal_input` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stok_gudang`
--

INSERT INTO `stok_gudang` (`id_stok_gudang`, `id_barang_jual`, `id_supplier_barang`, `harga_beli`, `harga_jual`, `jumlah_stok`, `tanggal_kadaluarsa`, `tanggal_input`) VALUES
('STKBRNG25001', 'BRNGBR25001', 'SPLRBRNG25002', 3000.00, 5000.00, 174, '2027-05-27', '2025-05-27'),
('STKBRNG25002', 'BRNGBR25002', 'SPLRBRNG25001', 1000.00, 2000.00, 0, '2027-05-27', '2025-05-27'),
('STKBRNG25003', 'BRNGBR25003', 'SPLRBRNG25003', 10000.00, 20000.00, -5, '2027-05-25', '2025-05-27'),
('STKBRNG25004', 'BRNGBR25003', 'SPLRBRNG25003', 10000.00, 15000.00, 205, '2027-05-25', '2025-05-27');

-- --------------------------------------------------------

--
-- Table structure for table `supplier_barang`
--

CREATE TABLE `supplier_barang` (
  `id_supplier_barang` varchar(15) NOT NULL,
  `nama_supplier` varchar(20) NOT NULL,
  `kontak_supplier` varchar(15) DEFAULT NULL,
  `alamat_supplier` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `supplier_barang`
--

INSERT INTO `supplier_barang` (`id_supplier_barang`, `nama_supplier`, `kontak_supplier`, `alamat_supplier`) VALUES
('SPLRBRNG25001', 'Awanda', '(+62) 998877', 'Situbondo'),
('SPLRBRNG25002', 'Hais', '(+62) 3333', 'Probolinggo'),
('SPLRBRNG25003', 'Firman', '(+62) 33333', 'Balung');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi_kasir`
--

CREATE TABLE `transaksi_kasir` (
  `id_transaksi_kasir` int(11) NOT NULL,
  `id_pengguna_aplikasi` int(11) NOT NULL,
  `total_harga` decimal(10,2) NOT NULL,
  `diskon` decimal(10,2) DEFAULT 0.00,
  `pembayaran` decimal(10,2) NOT NULL,
  `kembalian` decimal(10,2) NOT NULL,
  `metode_pembayaran` varchar(20) NOT NULL,
  `struk` varchar(20) DEFAULT NULL,
  `tanggal` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi_kasir`
--

INSERT INTO `transaksi_kasir` (`id_transaksi_kasir`, `id_pengguna_aplikasi`, `total_harga`, `diskon`, `pembayaran`, `kembalian`, `metode_pembayaran`, `struk`, `tanggal`) VALUES
(29, 3, 200000.00, 50000.00, 150000.00, 0.00, 'Cash', NULL, '2025-05-27 13:16:59'),
(30, 3, 2100000.00, 0.00, 3000000.00, 900000.00, 'Cash', NULL, '2025-05-27 13:20:40'),
(31, 3, 400000.00, 0.00, 400000.00, 0.00, 'Cash', NULL, '2025-05-27 14:39:27'),
(32, 3, 10000.00, 0.00, 10000.00, 0.00, 'Cash', NULL, '2025-05-28 00:51:52');

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_barcodebarang`
-- (See below for the actual view)
--
CREATE TABLE `v_barcodebarang` (
`barcode_barang` varchar(50)
,`nama_barang` varchar(100)
,`harga_jual` decimal(10,2)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_stok`
-- (See below for the actual view)
--
CREATE TABLE `v_stok` (
`id_stok_gudang` varchar(15)
,`nama_barang` varchar(100)
,`barcode_barang` varchar(50)
,`kategori_barang` varchar(20)
,`harga_beli` decimal(10,2)
,`harga_jual` decimal(10,2)
,`jumlah_stok` int(11)
,`nama_supplier` varchar(20)
,`tanggal_kadaluarsa` date
);

-- --------------------------------------------------------

--
-- Structure for view `v_barcodebarang`
--
DROP TABLE IF EXISTS `v_barcodebarang`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_barcodebarang`  AS SELECT `bj`.`barcode_barang` AS `barcode_barang`, `bj`.`nama_barang` AS `nama_barang`, `sg`.`harga_jual` AS `harga_jual` FROM (`barang_jual` `bj` join `stok_gudang` `sg` on(`bj`.`id_barang_jual` = `sg`.`id_barang_jual`)) ;

-- --------------------------------------------------------

--
-- Structure for view `v_stok`
--
DROP TABLE IF EXISTS `v_stok`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_stok`  AS SELECT `sg`.`id_stok_gudang` AS `id_stok_gudang`, `bj`.`nama_barang` AS `nama_barang`, `bj`.`barcode_barang` AS `barcode_barang`, `bj`.`kategori_barang` AS `kategori_barang`, `sg`.`harga_beli` AS `harga_beli`, `sg`.`harga_jual` AS `harga_jual`, `sg`.`jumlah_stok` AS `jumlah_stok`, `sb`.`nama_supplier` AS `nama_supplier`, `sg`.`tanggal_kadaluarsa` AS `tanggal_kadaluarsa` FROM ((`barang_jual` `bj` join `stok_gudang` `sg` on(`bj`.`id_barang_jual` = `sg`.`id_barang_jual`)) join `supplier_barang` `sb` on(`sg`.`id_supplier_barang` = `sb`.`id_supplier_barang`)) ORDER BY `sg`.`tanggal_kadaluarsa` ASC ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang_jual`
--
ALTER TABLE `barang_jual`
  ADD PRIMARY KEY (`id_barang_jual`),
  ADD UNIQUE KEY `barcode_barang` (`barcode_barang`);

--
-- Indexes for table `detail_transaksi_pelanggan`
--
ALTER TABLE `detail_transaksi_pelanggan`
  ADD PRIMARY KEY (`id_detail_transaksi_pelanggan`),
  ADD KEY `id_transaksi_kasir` (`id_transaksi_kasir`),
  ADD KEY `id_barang_jual` (`id_barang_jual`);

--
-- Indexes for table `pengguna_aplikasi`
--
ALTER TABLE `pengguna_aplikasi`
  ADD PRIMARY KEY (`id_pengguna`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `retur_barang`
--
ALTER TABLE `retur_barang`
  ADD PRIMARY KEY (`id_retur`);

--
-- Indexes for table `stok_gudang`
--
ALTER TABLE `stok_gudang`
  ADD PRIMARY KEY (`id_stok_gudang`),
  ADD KEY `stok_gudang_ibfk_1` (`id_barang_jual`),
  ADD KEY `id_supplier_barang_ibfk_2` (`id_supplier_barang`);

--
-- Indexes for table `supplier_barang`
--
ALTER TABLE `supplier_barang`
  ADD PRIMARY KEY (`id_supplier_barang`);

--
-- Indexes for table `transaksi_kasir`
--
ALTER TABLE `transaksi_kasir`
  ADD PRIMARY KEY (`id_transaksi_kasir`),
  ADD KEY `id_pengguna_aplikasi` (`id_pengguna_aplikasi`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detail_transaksi_pelanggan`
--
ALTER TABLE `detail_transaksi_pelanggan`
  MODIFY `id_detail_transaksi_pelanggan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `pengguna_aplikasi`
--
ALTER TABLE `pengguna_aplikasi`
  MODIFY `id_pengguna` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `transaksi_kasir`
--
ALTER TABLE `transaksi_kasir`
  MODIFY `id_transaksi_kasir` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detail_transaksi_pelanggan`
--
ALTER TABLE `detail_transaksi_pelanggan`
  ADD CONSTRAINT `detail_transaksi_pelanggan_ibfk_1` FOREIGN KEY (`id_transaksi_kasir`) REFERENCES `transaksi_kasir` (`id_transaksi_kasir`) ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_transaksi_pelanggan_ibfk_2` FOREIGN KEY (`id_barang_jual`) REFERENCES `barang_jual` (`id_barang_jual`);

--
-- Constraints for table `stok_gudang`
--
ALTER TABLE `stok_gudang`
  ADD CONSTRAINT `stok_gudang_ibfk_1` FOREIGN KEY (`id_barang_jual`) REFERENCES `barang_jual` (`id_barang_jual`),
  ADD CONSTRAINT `stok_gudang_ibfk_2` FOREIGN KEY (`id_supplier_barang`) REFERENCES `supplier_barang` (`id_supplier_barang`);

--
-- Constraints for table `transaksi_kasir`
--
ALTER TABLE `transaksi_kasir`
  ADD CONSTRAINT `transaksi_kasir_ibfk_1` FOREIGN KEY (`id_pengguna_aplikasi`) REFERENCES `pengguna_aplikasi` (`id_pengguna`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
