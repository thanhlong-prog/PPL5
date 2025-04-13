-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: shopee
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS cart;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE cart (
  id int NOT NULL AUTO_INCREMENT,
  created_date date DEFAULT NULL,
  modified_date date DEFAULT NULL,
  order_quantity int DEFAULT NULL,
  `status` int DEFAULT NULL,
  product_id int DEFAULT NULL,
  user_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK3d704slv66tw6x5hmbm6p2x3u (product_id),
  KEY FKl70asp4l4w0jmbm1tqyofho4o (user_id),
  CONSTRAINT FK3d704slv66tw6x5hmbm6p2x3u FOREIGN KEY (product_id) REFERENCES product (id),
  CONSTRAINT FKl70asp4l4w0jmbm1tqyofho4o FOREIGN KEY (user_id) REFERENCES `user` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES cart WRITE;
/*!40000 ALTER TABLE cart DISABLE KEYS */;
/*!40000 ALTER TABLE cart ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS category;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE category (
  id int NOT NULL AUTO_INCREMENT,
  created_date date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  modified_date date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  image varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES category WRITE;
/*!40000 ALTER TABLE category DISABLE KEYS */;
INSERT INTO category VALUES (1,'2025-04-13','Thời trang dành cho Nam giới','2025-04-13','Thời Trang Nam',_binary '','https://down-vn.img.susercontent.com/file/687f3967b7c2fe6a134a2c11894eea4b@resize_w320_nl.webp'),(2,'2025-04-13','Điện thoại và phụ kiện','2025-04-13','Điện Thoại & Phụ Kiện',_binary '','https://down-vn.img.susercontent.com/file/31234a27876fb89cd522d7e3db1ba5ca@resize_w320_nl.webp'),(3,'2025-04-13','Thiết bị điện tử','2025-04-13','Thiết Bị Điện Tử',_binary '','https://down-vn.img.susercontent.com/file/978b9e4cb61c611aaaf58664fae133c5@resize_w320_nl.webp'),(4,'2025-04-13','Máy tính và Laptop','2025-04-13','Máy Tính & Laptop',_binary '','https://down-vn.img.susercontent.com/file/c3f3edfaa9f6dafc4825b77d8449999d@resize_w320_nl.webp'),(5,'2025-04-13','Máy ảnh và máy quay phim','2025-04-13','Máy Ảnh & Máy Quay Phim',_binary '','https://down-vn.img.susercontent.com/file/ec14dd4fc238e676e43be2a911414d4d@resize_w320_nl.webp'),(6,'2025-04-13','Đồng hồ','2025-04-13','Đồng Hồ',_binary '','https://down-vn.img.susercontent.com/file/86c294aae72ca1db5f541790f7796260@resize_w320_nl.webp'),(7,'2025-04-13','Giày dép nam','2025-04-13','Giày Dép Nam',_binary '','https://down-vn.img.susercontent.com/file/74ca517e1fa74dc4d974e5d03c3139de@resize_w320_nl.webp'),(8,'2025-04-13','Thiết bị điện','2025-04-13','Thiết Bị Điện Gia Dụng',_binary '','https://down-vn.img.susercontent.com/file/7abfbfee3c4844652b4a8245e473d857@resize_w320_nl.webp'),(9,'2025-04-13','Thể thao và du lịch','2025-04-13','Thể Thao & Du Lịch',_binary '','https://down-vn.img.susercontent.com/file/6cb7e633f8b63757463b676bd19a50e4@resize_w320_nl.webp'),(10,'2025-04-13','Ô tô, xe máy và xe đạp','2025-04-13','Ô TÔ & Xe Máy & Xe Đạp',_binary '','https://down-vn.img.susercontent.com/file/3fb459e3449905545701b418e8220334@resize_w320_nl.webp'),(11,'2025-04-13','Thời trang dành cho Nữ giới','2025-04-13','Thời Trang Nữ',_binary '','https://down-vn.img.susercontent.com/file/75ea42f9eca124e9cb3cde744c060e4d@resize_w320_nl.webp'),(12,'2025-04-13','Mẹ và Bé','2025-04-13','Mẹ & Bé',_binary '','https://down-vn.img.susercontent.com/file/099edde1ab31df35bc255912bab54a5e@resize_w320_nl.webp'),(13,'2025-04-13','Nhà cửa và đời sống','2025-04-13','Nhà Cửa & Đời Sống',_binary '','https://down-vn.img.susercontent.com/file/24b194a695ea59d384768b7b471d563f@resize_w320_nl.webp'),(14,'2025-04-13','Sắc đẹp','2025-04-13','Sắc Đẹp',_binary '','https://down-vn.img.susercontent.com/file/ef1f336ecc6f97b790d5aae9916dcb72@resize_w320_nl.webp'),(15,'2025-04-13','Sức khỏe','2025-04-13','Sức Khỏe',_binary '','https://down-vn.img.susercontent.com/file/49119e891a44fa135f5f6f5fd4cfc747@resize_w320_nl.webp'),(16,'2025-04-13','Giày dép nữ','2025-04-13','Giày Dép Nữ',_binary '','https://down-vn.img.susercontent.com/file/48630b7c76a7b62bc070c9e227097847@resize_w320_nl.webp'),(17,'2025-04-13','Túi ví nữ','2025-04-13','Túi Ví Nữ',_binary '','https://down-vn.img.susercontent.com/file/fa6ada2555e8e51f369718bbc92ccc52@resize_w320_nl.webp'),(18,'2025-04-13','Phụ kiện trang sức','2025-04-13','Phụ Kiện & Trang Sức Nữ',_binary '','https://down-vn.img.susercontent.com/file/8e71245b9659ea72c1b4e737be5cf42e@resize_w320_nl.webp'),(19,'2025-04-13','Bách hóa online','2025-04-13','Bách Hóa Online',_binary '','https://down-vn.img.susercontent.com/file/c432168ee788f903f1ea024487f2c889@resize_w320_nl.webp'),(20,'2025-04-13','Nhà sách online','2025-04-13','Nhà Sách Online',_binary '','https://down-vn.img.susercontent.com/file/36013311815c55d303b0e6c62d6a8139@resize_w320_nl.webp'),(21,'2025-04-13','Balo túi ví nam','2025-04-13','Balo & Túi Ví Nam',_binary '','https://down-vn.img.susercontent.com/file/18fd9d878ad946db2f1bf4e33760c86f@resize_w320_nl.webp'),(22,'2025-04-13','Thời trang trẻ em','2025-04-13','Thời Trang Trẻ Em',_binary '','https://down-vn.img.susercontent.com/file/4540f87aa3cbe99db739f9e8dd2cdaf0@resize_w320_nl.webp'),(23,'2025-04-13','Đồ chơi','2025-04-13','Đồ Chơi',_binary '','https://down-vn.img.susercontent.com/file/ce8f8abc726cafff671d0e5311caa684@resize_w320_nl.webp'),(24,'2025-04-13','Giặc giũ & chăm sóc nhà','2025-04-13','Giặc Giũ & Chăm Sóc Nhà Cửa',_binary '','https://down-vn.img.susercontent.com/file/cd8e0d2e6c14c4904058ae20821d0763@resize_w320_nl.webp'),(25,'2025-04-13','Chăm sóc thú cưng','2025-04-13','Chăm Sóc Thú Cưng',_binary '','https://down-vn.img.susercontent.com/file/cdf21b1bf4bfff257efe29054ecea1ec@resize_w320_nl.webp'),(26,'2025-04-13','Voucher & Dịch Vụ','2025-04-13','Voucher & Dịch Vụ',_binary '','https://down-vn.img.susercontent.com/file/b0f78c3136d2d78d49af71dd1c3f38c1@resize_w320_nl.webp'),(27,'2025-04-13','Dụng cụ và thiết bị','2025-04-13','Dụng Cụ & Thiết Bị Tiện Ích',_binary '','https://down-vn.img.susercontent.com/file/e4fbccba5e1189d1141b9d6188af79c0@resize_w320_nl.webp');
/*!40000 ALTER TABLE category ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS coupon;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE coupon (
  id int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  created_date date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  discount int DEFAULT NULL,
  expired_date date DEFAULT NULL,
  max_discount int DEFAULT NULL,
  min_price int DEFAULT NULL,
  modified_date date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  type_coupon int DEFAULT NULL,
  type_discount int DEFAULT NULL,
  created_by int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK519nf6gayx1u6k8wdanyolrjp (created_by),
  CONSTRAINT FK519nf6gayx1u6k8wdanyolrjp FOREIGN KEY (created_by) REFERENCES `user` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon`
--

LOCK TABLES coupon WRITE;
/*!40000 ALTER TABLE coupon DISABLE KEYS */;
/*!40000 ALTER TABLE coupon ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follow`
--

DROP TABLE IF EXISTS follow;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE follow (
  id int NOT NULL AUTO_INCREMENT,
  created_date date DEFAULT NULL,
  `status` int DEFAULT NULL,
  followed_user_id int DEFAULT NULL,
  following_user_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK6cslhpdvbb0qu7n4a5hyx3oag (followed_user_id),
  KEY FKmob5gqk7nmxu9p9xpiae8txeb (following_user_id),
  CONSTRAINT FK6cslhpdvbb0qu7n4a5hyx3oag FOREIGN KEY (followed_user_id) REFERENCES `user` (id),
  CONSTRAINT FKmob5gqk7nmxu9p9xpiae8txeb FOREIGN KEY (following_user_id) REFERENCES `user` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow`
--

LOCK TABLES follow WRITE;
/*!40000 ALTER TABLE follow DISABLE KEYS */;
/*!40000 ALTER TABLE follow ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS product;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE product (
  id int NOT NULL AUTO_INCREMENT,
  created_date date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  discount int DEFAULT NULL,
  liked int DEFAULT NULL,
  modified_date date DEFAULT NULL,
  price double DEFAULT NULL,
  quantity int DEFAULT NULL,
  rated int DEFAULT NULL,
  rating double DEFAULT NULL,
  refund_type int DEFAULT NULL,
  sold int DEFAULT NULL,
  `status` int DEFAULT NULL,
  subcategory_id int DEFAULT NULL,
  title varchar(255) DEFAULT NULL,
  seller_id int DEFAULT NULL,
  thumbnail varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKmsvavr0t3lra70gf2ymxdi5te (seller_id),
  CONSTRAINT FKmsvavr0t3lra70gf2ymxdi5te FOREIGN KEY (seller_id) REFERENCES `user` (id)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES product WRITE;
/*!40000 ALTER TABLE product DISABLE KEYS */;
INSERT INTO product VALUES (1,'2025-04-13','Thắt lưng mặt xoay nam thắt lưng nam cao cấp thời trang dây nịt da khóa tự động X003',NULL,209,'2025-04-13',22900,2383028,5100,4.6,NULL,18300,1,19,'Thắt Lưng Nam Khóa Tự Động Cao Cấp Mặt Xoay Chính Hãng , Dây Nịt Nam Phong Cách Hàn Quốc',18,'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lupvwpc810cyf9.webp'),(2,'2025-04-13','⏺️ Áo thun nam tay dài, áo giữ nhiệt thể thao nam vải thun co giãn ??? giữ ấm, màu sắc trẻ trung và năng động thời trang',NULL,93,'2025-04-13',21280,190331,1300,4.4,NULL,8400,1,9,'Áo thun nam tay dài, áo giữ nhiệt thể thao nam vải thun co giãn HMMENSHOP giữ ấm, màu sắc trẻ trung và năng động',18,'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lvsooq304uwpfd.webp'),(3,'2025-04-13','MÔ TẢ SẢN PHẨM;Quần Short unisex chất cotton cao cấp,Quần Short nam nữ phong cách thể thao -NANA SHOP.',NULL,27700,'2025-04-13',58800,175898,7900,4.7,NULL,42400,1,6,'Quần Short unisex chất cotton cao cấp,Quần Short nam nữ phong cách thể thao -NANA SHOP',18,'https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lju0wkebupz659.webp'),(4,'2025-04-13','YODY SHOP chuyên cung cấp quần áo,đồ lót, phụ kiện thời trang nam nữ',NULL,3500,'2025-04-13',58500,199847,24100,4.7,NULL,85100,1,9,'Combo 5 Quần sịp đùi nam, quần lót boxer nam cotton thun lạnh co dãn 4 chiều thấm hút tối đa',18,'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m847m2by70ciac.webp'),(5,'2025-04-13','Cường lực Baiko Vivo Y12s T1x T1 Y15a Y15s Y35 Y53s Y55 Y33s Y22s Y16 Y21 Y21s Y02s Y01 Y20s V25 V25E V23E V21 V20 V19 - FULL MÀN HÌNH BAIKO - ĐỘ CỨNG SIÊU BỀN - ĐỘ TRONG SUỐT CAO.',NULL,93400,'2025-04-13',1000,5370409,13400,4.8,NULL,62100,1,27,'Cường lực Baiko Vivo Y12s T1x T1 Y15a Y15s Y35 Y53s Y55 Y33s Y22s Y16 Y21 Y21s Y02s Y01 Y20s V25 V25E V23E V21 V20 V19',18,'https://down-vn.img.susercontent.com/file/28b1c1f66c13f48371a32985274ff9df.webp'),(6,'2025-04-13','Áo Thun Cổ Trễ Form Xinh Chất Cotton Bozip QC Dầy Dặn Mềm Mại Co Giãn',NULL,2400,'2025-04-13',29000,55320,1700,4.8,NULL,9100,1,116,'Áo Thun Cổ Trễ Form Xinh Chất Cotton Bozip QC Dầy Dặn Mềm Mại Co Giãn MA124',18,'	https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1nmvz9ljsu7fb.webp'),(7,'2025-04-13','Tất nữ màu trơn lolita Jengiang cosplay phong cách học sinh Nhật Bản',NULL,1700,'2025-04-13',13000,12830,9900,4.9,NULL,91800,1,122,'Tất nữ màu trơn lolita Jengiang cosplay phong cách học sinh Nhật Bản',18,'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1jg3jlwaitbaa.webp'),(8,'2025-04-13','Quần jeans nữ ống rộng may viền chắp thân cực xinh xắn hot trend là một trong những sản phẩm thời trang không thể thiếu trong tủ đồ của phái đẹp. Chất liệu denim cao cấp mang lại sự thoải mái cho người mặc và khóa kéo tiện lợi giúp bạn dễ dàng di chuyển.',NULL,2160,'2025-04-13',107000,1337686,2000,4.1,NULL,18000,1,106,'Quần jeans nữ ống rộng may viền chắp thân cực xinh xắn hot trend',18,'https://down-vn.img.susercontent.com/file/sg-11134201-7rdx7-m0x1ma8c345gbb.webp');
/*!40000 ALTER TABLE product ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS product_image;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE product_image (
  id int NOT NULL AUTO_INCREMENT,
  created_date date DEFAULT NULL,
  image_url varchar(255) DEFAULT NULL,
  modified_date date DEFAULT NULL,
  `status` int DEFAULT NULL,
  product_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK6oo0cvcdtb6qmwsga468uuukk (product_id),
  CONSTRAINT FK6oo0cvcdtb6qmwsga468uuukk FOREIGN KEY (product_id) REFERENCES product (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES product_image WRITE;
/*!40000 ALTER TABLE product_image DISABLE KEYS */;
INSERT INTO product_image VALUES (1,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lupvwpc810cyf9@resize_w82_nl.webp','2025-04-13',1,1),(2,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrsmvmor7r9g15@resize_w82_nl.webp','2025-04-13',1,1),(3,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrsmvmor6cp0a0@resize_w82_nl.webp','2025-04-13',1,1),(4,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrsmvmor3jk44d@resize_w82_nl.webp','2025-04-13',1,1),(5,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrsmvmor4y4kc2@resize_w82_nl.webp','2025-04-13',1,1),(6,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrsmvmor95tw99@resize_w82_nl.webp','2025-04-13',1,1),(7,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lrsmvmorakec38@resize_w82_nl.webp','2025-04-13',1,1),(8,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lvsooq304uwpfd@resize_w82_nl.webp','2025-04-13',1,2),(9,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lvsooq303gc9fa@resize_w82_nl.webp','2025-04-13',1,2),(10,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lvsooq3069h581@resize_w82_nl.webp','2025-04-13',1,2),(11,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lvsooq2zz8mx59@resize_w82_nl.webp','2025-04-13',1,2),(12,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lvsooq2zwfi1a2@resize_w82_nl.webp','2025-04-13',1,2),(13,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lvsooq2zxu2hf5@resize_w82_nl.webp','2025-04-13',1,2),(14,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lju0wkebupz659@resize_w82_nl.webp','2025-04-13',1,3),(15,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lju0wkebxj90a3@resize_w82_nl.webp','2025-04-13',1,3),(16,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lju0wkebyxtg76@resize_w82_nl.webp','2025-04-13',1,3),(17,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lju0wkec1qyc9a@resize_w82_nl.webp','2025-04-13',1,3),(18,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lju0wkec35is86@resize_w82_nl.webp','2025-04-13',1,3),(19,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lju0wkec5ynof5@resize_w82_nl.webp','2025-04-13',1,3),(20,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lju0wkec4k3868@resize_w82_nl.webp','2025-04-13',1,3),(21,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134201-7qukw-ljeegc4hqoss13@resize_w82_nl.webp','2025-04-13',1,3),(22,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m847m2by70ciac@resize_w82_nl.webp','2025-04-13',1,4),(23,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m860kmdp6fzi05@resize_w82_nl.webp','2025-04-13',1,4),(24,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m860ky4u0n7r61@resize_w82_nl.webp','2025-04-13',1,4),(25,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m860krirnm9z30@resize_w82_nl.webp','2025-04-13',1,4),(26,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m860l4eou4q6b0@resize_w82_nl.webp','2025-04-13',1,4),(27,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m860lrahb7gn7d@resize_w82_nl.webp','2025-04-13',1,4),(28,'2025-04-13','https://down-vn.img.susercontent.com/file/28b1c1f66c13f48371a32985274ff9df@resize_w82_nl.webp','2025-04-13',1,5),(29,'2025-04-13','	https://down-vn.img.susercontent.com/file/sg-11134201-22110-8uvl7f08lyjv61@resize_w82_nl.webp','2025-04-13',1,5),(30,'2025-04-13','	https://down-vn.img.susercontent.com/file/sg-11134201-22110-9y3hvbkicljv5f@resize_w82_nl.webp','2025-04-13',1,5),(31,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134201-23030-69osejsk24nvb9@resize_w82_nl.webp','2025-04-13',1,5),(32,'2025-04-13','https://down-vn.img.susercontent.com/file/sg-11134201-22110-m9lcnekicljvfb@resize_w82_nl.webp','2025-04-13',1,5),(33,'2025-04-13','	https://down-vn.img.susercontent.com/file/sg-11134201-22110-o1zxmokicljv1b@resize_w82_nl.webp','2025-04-13',1,5),(34,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1nmvz9ljsu7fb@resize_w82_nl.webp','2025-04-13',1,6),(35,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1nmvz9lgzf7cc@resize_w82_nl.webp','2025-04-13',1,6),(36,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1nmvz9ll7en30@resize_w82_nl.webp','2025-04-13',1,6),(37,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1nmvz9lfl4vf0@resize_w82_nl.webp','2025-04-13',1,6),(38,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1nmvz9ljsk3bb@resize_w82_nl.webp','2025-04-13',1,6),(39,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1nmvz9lfkur70@resize_w82_nl.webp','2025-04-13',1,6),(40,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1nmvz9lie9rcb@resize_w82_nl.webp','2025-04-13',1,6),(41,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1jg3jlwaitbaa@resize_w82_nl.webp','2025-04-13',1,7),(42,'2025-04-13','	https://down-vn.img.susercontent.com/file/sg-11134201-22120-ejmqqrltkokvdf@resize_w82_nl.webp','2025-04-13',1,7),(43,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lke47nx3f63s29@resize_w82_nl.webp','2025-04-13',1,7),(44,'2025-04-13','https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-lke47nx3drjc39@resize_w82_nl.webp','2025-04-13',1,7),(45,'2025-04-13','	https://down-vn.img.susercontent.com/file/8e8999d04cc8c99e9b471eded2cdad92@resize_w82_nl.webp','2025-04-13',1,7),(46,'2025-04-13','	https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-llcasic26xu07b@resize_w82_nl.webp','2025-04-13',1,7),(47,'2025-04-13','	https://down-vn.img.susercontent.com/file/sg-11134201-7rdx7-m0x1ma8c345gbb@resize_w82_nl.webp','2025-04-13',1,8),(48,'2025-04-13','	https://down-vn.img.susercontent.com/file/sg-11134201-7rdxm-m0x1mbawio0of0@resize_w82_nl.webp','2025-04-13',1,8),(49,'2025-04-13','https://down-vn.img.susercontent.com/file/sg-11134201-7rdwf-m0x1mbt7rvayfa@resize_w82_nl.webp','2025-04-13',1,8),(50,'2025-04-13','https://down-vn.img.susercontent.com/file/sg-11134201-7rdw0-m0x1mcbj12b26e@resize_w82_nl.webp','2025-04-13',1,8),(51,'2025-04-13','https://down-vn.img.susercontent.com/file/sg-11134201-7rdvk-m0x1mctua9so9d@resize_w82_nl.webp','2025-04-13',1,8),(52,'2025-04-13','https://down-vn.img.susercontent.com/file/sg-11134201-7rdye-m0x1mdfre7d06e@resize_w82_nl.webp','2025-04-13',1,8);
/*!40000 ALTER TABLE product_image ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_option`
--

DROP TABLE IF EXISTS product_option;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE product_option (
  id int NOT NULL AUTO_INCREMENT,
  created_date date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  modified_date date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  thumbnail varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  created_by int DEFAULT NULL,
  product_id int DEFAULT NULL,
  price double DEFAULT NULL,
  quantity int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKfbgjd29ih1vuf32q6eauvx0d0 (created_by),
  KEY FKn4hmm6ex1vgn60c6uiqte400f (product_id),
  CONSTRAINT FKfbgjd29ih1vuf32q6eauvx0d0 FOREIGN KEY (created_by) REFERENCES `user` (id),
  CONSTRAINT FKn4hmm6ex1vgn60c6uiqte400f FOREIGN KEY (product_id) REFERENCES product (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_option`
--

LOCK TABLES product_option WRITE;
/*!40000 ALTER TABLE product_option DISABLE KEYS */;
/*!40000 ALTER TABLE product_option ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_preview`
--

DROP TABLE IF EXISTS product_preview;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE product_preview (
  id int NOT NULL AUTO_INCREMENT,
  content varchar(255) DEFAULT NULL,
  created_date date DEFAULT NULL,
  liked int DEFAULT NULL,
  modified_date date DEFAULT NULL,
  star int DEFAULT NULL,
  `status` int DEFAULT NULL,
  previewer int DEFAULT NULL,
  product_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKaep4eivtg40emawmkowqi12n2 (previewer),
  KEY FK8l6cn43kph3k7fag5hrkwol9x (product_id),
  CONSTRAINT FK8l6cn43kph3k7fag5hrkwol9x FOREIGN KEY (product_id) REFERENCES product (id),
  CONSTRAINT FKaep4eivtg40emawmkowqi12n2 FOREIGN KEY (previewer) REFERENCES `user` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_preview`
--

LOCK TABLES product_preview WRITE;
/*!40000 ALTER TABLE product_preview DISABLE KEYS */;
/*!40000 ALTER TABLE product_preview ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS roles;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE roles (
  id int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES roles WRITE;
/*!40000 ALTER TABLE roles DISABLE KEYS */;
INSERT INTO roles VALUES (1,'admin','admin'),(2,'buyer','buyer'),(3,'seller','seller');
/*!40000 ALTER TABLE roles ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcategory`
--

DROP TABLE IF EXISTS subcategory;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE subcategory (
  id int NOT NULL AUTO_INCREMENT,
  created_date date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  modified_date date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  category_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKe4hdbsmrx9bs9gpj1fh4mg0ku (category_id),
  CONSTRAINT FKe4hdbsmrx9bs9gpj1fh4mg0ku FOREIGN KEY (category_id) REFERENCES category (id)
) ENGINE=InnoDB AUTO_INCREMENT=284 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategory`
--

LOCK TABLES subcategory WRITE;
/*!40000 ALTER TABLE subcategory DISABLE KEYS */;
INSERT INTO subcategory VALUES (1,'2025-04-13','Áo khoác','2025-04-13','Áo Khoác',_binary '',1),(2,'2025-04-13','Áo Vest','2025-04-13','Áo Vest và Blazer',_binary '',1),(3,'2025-04-13','Áo tổng hợp','2025-04-13','Áo Hoodie, Áo Len & Áo Nỉ',_binary '',1),(4,'2025-04-13','Quần Jeans','2025-04-13','Quần Jeans',_binary '',1),(5,'2025-04-13','Quần dài hoặc quần âu','2025-04-13','Quần Dài/ Quần Âu',_binary '',1),(6,'2025-04-13','Quần Short','2025-04-13','Quần Short',_binary '',1),(7,'2025-04-13','Áo','2025-04-13','Áo',_binary '',1),(8,'2025-04-13','Áo Ba lỗ','2025-04-13','Áo Ba Lỗ',_binary '',1),(9,'2025-04-13','Đồ Lót','2025-04-13','Đồ Lót',_binary '',1),(10,'2025-04-13','Đồ Ngủ','2025-04-13','Đồ Ngủ',_binary '',1),(11,'2025-04-13','Đồ Bộ','2025-04-13','Đồ Bộ',_binary '',1),(12,'2025-04-13','Vớ tất','2025-04-13','Vớ/Tất',_binary '',1),(13,'2025-04-13','Trang phục truyền thống','2025-04-13','Trang Phục Truyền Thống',_binary '',1),(14,'2025-04-13','Đồ hóa trang','2025-04-13','Đồ Hóa Trang',_binary '',1),(15,'2025-04-13','Trang phục hành nghề','2025-04-13','Trang Phục Ngành Nghề',_binary '',1),(16,'2025-04-13','Khác','2025-04-13','Khác',_binary '',1),(17,'2025-04-13','Trang sức nam','2025-04-13','Trang Sức Nam',_binary '',1),(18,'2025-04-13','Kính mắt Nam','2025-04-13','Kính Mắt Nam',_binary '',1),(19,'2025-04-13','Thắt Lưng Nam','2025-04-13','Thắt Lưng Nam',_binary '',1),(20,'2025-04-13','Cà vạt','2025-04-13','Cà Vạt & Nơ Cổ',_binary '',1),(21,'2025-04-13','Phụ kiện nam','2025-04-13','Phụ Kiện Nam',_binary '',1),(22,'2025-04-13','Điện thoại','2025-04-13','Điện Thoại',_binary '',2),(23,'2025-04-13','Máy tính bảng','2025-04-13','Máy Tính Bảng',_binary '',2),(24,'2025-04-13','Pin dự phòng','2025-04-13','Pin Dự Phòng ',_binary '',2),(25,'2025-04-13','Pin gắn trong, cáp','2025-04-13','Pin Gắn Trong, Cáp và Bộ Sạc',_binary '',2),(26,'2025-04-13','Ốp Lưng, Bao Da, Miếng Dán Điện Thoại','2025-04-13','Ốp Lưng, Bao Da, Miếng Dán Điện Thoại',_binary '',2),(27,'2025-04-13','Bảo Vệ Màn hình','2025-04-13','Bảo Vệ Màn Hình',_binary '',2),(28,'2025-04-13','Đế Giữ Điện Thoại','2025-04-13','Đế Giữ Điện Thoại',_binary '',2),(29,'2025-04-13','Thẻ nhớ','2025-04-13','Thẻ Nhớ',_binary '',2),(30,'2025-04-13','Sim','2025-04-13','Sim',_binary '',2),(31,'2025-04-13','Phụ kiện khác','2025-04-13','Phụ Kiện Khác',_binary '',2),(32,'2025-04-13','Thiết Bị Khác','2025-04-13','Thiết Bị Khác',_binary '',2),(33,'2025-04-13','Thiết bị đeo thông minh','2025-04-13','Thiết Bị Đeo Thông Minh',_binary '',3),(34,'2025-04-13','Phụ kiện tivi','2025-04-13','Phụ Kiện Tivi',_binary '',3),(35,'2025-04-13','Máy game console','2025-04-13','Máy Game Console',_binary '',3),(36,'2025-04-13','Phụ kiện Console','2025-04-13','Phụ Kiện Console',_binary '',3),(37,'2025-04-13','Đĩa game','2025-04-13','Đĩa Game',_binary '',3),(38,'2025-04-13','Linh Phụ Kiện','2025-04-13','Linh Phụ Kiện',_binary '',3),(39,'2025-04-13','Tai Nghe Nhét Tai','2025-04-13','Tai Nghe Nhét Tai',_binary '',3),(40,'2025-04-13','Loa','2025-04-13','Loa',_binary '',3),(41,'2025-04-13','Tivi','2025-04-13','Tivi',_binary '',3),(42,'2025-04-13','Tivi Box','2025-04-13','Tivi Box',_binary '',3),(43,'2025-04-13','Headphones','2025-04-13','Headphones',_binary '',3),(44,'2025-04-13','Máy tính bàn','2025-04-13','Máy Tính Bàn',_binary '',4),(45,'2025-04-13','Màn Hình','2025-04-13','Màn Hình',_binary '',4),(46,'2025-04-13','Linh Kiện Máy Tính','2025-04-13','Linh Kiện Máy Tính',_binary '',4),(47,'2025-04-13','Thiết bị lưu trữ','2025-04-13','Thiết Bị Lưu Trữ',_binary '',4),(48,'2025-04-13','Thiết Bị Mạng','2025-04-13','Thiết Bị Mạng',_binary '',4),(49,'2025-04-13','Máy in v.v','2025-04-13','Máy In, Máy Scan & Máy Chiếu',_binary '',4),(50,'2025-04-13','Phụ kiện máy','2025-04-13','Phụ Kiện Máy Tính',_binary '',4),(51,'2025-04-13','Laptop','2025-04-13','Laptop',_binary '',4),(52,'2025-04-13','Khác','2025-04-13','Khác',_binary '',4),(53,'2025-04-13','Gaming','2025-04-13','Gaming',_binary '',4),(54,'2025-04-13','Máy ảnh v.v','2025-04-13','Máy Ảnh - Máy Quay Phim',_binary '',5),(55,'2025-04-13','Camera','2025-04-13','Camera',_binary '',5),(56,'2025-04-13','Thẻ Nhớ','2025-04-13','Thẻ Nhớ',_binary '',5),(57,'2025-04-13','Ống Kính','2025-04-13','Ống Kính ',_binary '',5),(58,'2025-04-13','Phụ kiện máy ảnh','2025-04-13','Phụ Kiện Máy Ảnh',_binary '',5),(59,'2025-04-13','Drone','2025-04-13','Máy Bay Camera & Phụ Kiện',_binary '',5),(60,'2025-04-13','Đồng hồ Nam','2025-04-13','Đồng Hồ Nam',_binary '',6),(61,'2025-04-13','Đồng Hồ Nữ','2025-04-13','Đồng Hồ Nữ',_binary '',6),(62,'2025-04-13','Bộ Đồng Hồ & Đồng hồ cặp','2025-04-13','Bộ Đồng Hồ & Đồng Hồ Cặp',_binary '',6),(63,'2025-04-13','Đồng hồ trẻ em','2025-04-13','Đồng Hồ Trẻ Em',_binary '',6),(64,'2025-04-13','Phụ Kiện Đồng Hồ','2025-04-13','Phụ Kiện Đồng Hồ',_binary '',6),(65,'2025-04-13','Khác','2025-04-13','Khác',_binary '',6),(66,'2025-04-13','Bốt','2025-04-13','Bốt',_binary '',7),(67,'2025-04-13','Sneakers','2025-04-13','Giày Thể Thao/Sneakers',_binary '',7),(68,'2025-04-13','Giày Sục','2025-04-13','Giày Sục',_binary '',7),(69,'2025-04-13','Giày tây lười','2025-04-13','Giày Tây Lười',_binary '',7),(70,'2025-04-13','Giày Oxfords ','2025-04-13','Giày Oxfords & Giày Buộc Dây',_binary '',7),(71,'2025-04-13','Xăng-đan dép','2025-04-13','Xăng-đan & Dép',_binary '',7),(72,'2025-04-13','Phụ kiện','2025-04-13','Phụ Kiện Giày Dép',_binary '',7),(73,'2025-04-13','Khác','2025-04-13','Khác',_binary '',7),(74,'2025-04-13','Đồ Gia Dụng Nhà Bếp','2025-04-13','Đồ Gia Dụng Nhà Bếp',_binary '',8),(75,'2025-04-13','Đồ Gia Dụng Lớn','2025-04-13','Đồ Gia Dụng Lớn',_binary '',8),(76,'2025-04-13','Thiết bị làm sạch','2025-04-13','Máy Hút Bụi & Thiết Bị Làm Sạch',_binary '',8),(77,'2025-04-13','Quạt, máy nóng lạnh','2025-04-13','Quạt & Máy Nóng Lạnh',_binary '',8),(78,'2025-04-13','Thiết Bị Chăm Sóc Quần Áo','2025-04-13','Thiết Bị Chăm Sóc Quần Áo',_binary '',8),(79,'2025-04-13','Khác','2025-04-13','Khác',_binary '',8),(80,'2025-04-13','v.v','2025-04-13','Máy Xay, Ép, Máy Đánh Trứng Trộn Bột, Máy Xay Thực Phẩm',_binary '',8),(81,'2025-04-13','Bếp điện','2025-04-13','Bếp Điện',_binary '',8),(82,'2025-04-13','Vali','2025-04-13','Vali',_binary '',9),(83,'2025-04-13','Túi Du Lịch','2025-04-13','Túi Du Lịch',_binary '',9),(84,'2025-04-13','Phụ Kiện Du Lịch','2025-04-13','Phụ Kiện Du Lịch',_binary '',9),(85,'2025-04-13','Dụng Cụ thể thao','2025-04-13','Dụng Cụ Thể Thao & Dã Ngoại',_binary '',9),(86,'2025-04-13','Giày thể thao','2025-04-13','Giày Thể Thao',_binary '',9),(87,'2025-04-13','Thời trang thể thao','2025-04-13','Thời Trang Thể Thao & Dã Ngoại',_binary '',9),(88,'2025-04-13','Phụ kiện thể thao','2025-04-13','Phụ Kiện Thể Thao & Dã Ngoại',_binary '',9),(89,'2025-04-13','Khác','2025-04-13','Khác',_binary '',9),(90,'2025-04-13','Xe','2025-04-13','Xe Đạp, Xe Điện',_binary '',10),(91,'2025-04-13','Mô tô, xe máy','2025-04-13','Mô Tô, Xe Máy',_binary '',10),(92,'2025-04-13','Xe Ô Tô','2025-04-13','Xe Ô Tô',_binary '',10),(93,'2025-04-13','Mũ bảo hiểm','2025-04-13','Mũ Bảo hiểm',_binary '',10),(94,'2025-04-13','Phụ kiện xe máy','2025-04-13','Phụ Kiện Xe Máy',_binary '',10),(95,'2025-04-13','Phụ Kiện xe đạp','2025-04-13','Phụ Kiện Xe Đạp',_binary '',10),(96,'2025-04-13','Phụ Kiện bên trong','2025-04-13','Phụ Kiện Bên Trong Ô Tô',_binary '',10),(97,'2025-04-13','Dầu','2025-04-13','Dầu Nhớt & Dầu Nhờn',_binary '',10),(98,'2025-04-13','Phụ Tùng Ô TÔ','2025-04-13','Phụ Tùng Ô Tô',_binary '',10),(99,'2025-04-13','Phụ tùng xe máy','2025-04-13','Phụ Tùng Xe Máy',_binary '',10),(100,'2025-04-13','Phụ kiện bên ngoài','2025-04-13','Phụ Kiện Bên Ngoài Ô Tô',_binary '',10),(101,'2025-04-13','Chăm Sóc Ô tô','2025-04-13','Chăm Sóc Ô Tô',_binary '',10),(102,'2025-04-13','Dịch vụ cho xe','2025-04-13','Dịch Vụ Cho Xe',_binary '',10),(103,'2025-04-13','Quần','2025-04-13','Quần',_binary '',11),(104,'2025-04-13','Quần đùi','2025-04-13','Quần Đùi',_binary '',11),(105,'2025-04-13','Chân Váy','2025-04-13','Chân Váy',_binary '',11),(106,'2025-04-13','Quần jeans','2025-04-13','Quần Jeans',_binary '',11),(107,'2025-04-13','Đầm váy','2025-04-13','Đầm/Váy',_binary '',11),(108,'2025-04-13','Váy cưới','2025-04-13','Váy Cưới',_binary '',11),(109,'2025-04-13','Đồ liền thân','2025-04-13','Đồ Liền Thân',_binary '',11),(110,'2025-04-13','Áo v.v','2025-04-13','Áo Khoác, Áo Choàng & Vest',_binary '',11),(111,'2025-04-13','Áo cardigan','2025-04-13','Áo Len & Cardigan',_binary '',11),(112,'2025-04-13','Hoodie','2025-04-13','Hoodie Và Áo Nỉ',_binary '',11),(113,'2025-04-13','Bộ','2025-04-13','Bộ',_binary '',11),(114,'2025-04-13','Đồ Lót','2025-04-13','Đồ Lót',_binary '',11),(115,'2025-04-13','Đồ Ngủ','2025-04-13','Đồ Ngủ',_binary '',11),(116,'2025-04-13','Áo','2025-04-13','Áo',_binary '',11),(117,'2025-04-13','Đồ Tập','2025-04-13','Đồ Tập',_binary '',11),(118,'2025-04-13','Đồ Bầu','2025-04-13','Đồ Bầu',_binary '',11),(119,'2025-04-13','Đồ truyền thống','2025-04-13','Đồ Truyền Thống',_binary '',11),(120,'2025-04-13','Đồ Hóa Trang','2025-04-13','Đồ Hóa Trang',_binary '',11),(121,'2025-04-13','Vải','2025-04-13','Vải',_binary '',11),(122,'2025-04-13','Vớ tất','2025-04-13','Vớ/Tất',_binary '',11),(123,'2025-04-13','Khác','2025-04-13','Khác',_binary '',11),(124,'2025-04-13','Đồ dùng','2025-04-13','Đồ Dùng Du Lịch Cho Bé',_binary '',12),(125,'2025-04-13','Ăn dặm','2025-04-13','Đồ Dùng Ăn Dặm Cho Bé',_binary '',12),(126,'2025-04-13','Phụ Kiện Cho Mẹ','2025-04-13','Phụ Kiện Cho Mẹ',_binary '',12),(127,'2025-04-13','Chăm Sóc SK','2025-04-13','Chăm Sóc Sức Khỏe Mẹ',_binary '',12),(128,'2025-04-13','Đồ dùng phòng tắm','2025-04-13','Đồ Dùng Phòng Tắm & Chăm Sóc Cơ Thể Bé',_binary '',12),(129,'2025-04-13','An Toàn cho bé','2025-04-13','An Toàn Cho Bé',_binary '',12),(130,'2025-04-13','Thực Phẩm Cho Bé','2025-04-13','Thực Phẩm Cho Bé',_binary '',12),(131,'2025-04-13','Chăm Sóc Sức Khỏe Bé','2025-04-13','Chăm Sóc Sức Khỏe Bé',_binary '',12),(132,'2025-04-13','Tã và bô','2025-04-13','Tã & Bô Em Bé',_binary '',12),(133,'2025-04-13','Đồ chơi','2025-04-13','Đồ Chơi',_binary '',12),(134,'2025-04-13','Khác','2025-04-13','Khác',_binary '',12),(135,'2025-04-13','Chăn ga gối nệm','2025-04-13','Chăn, Ga, Gối, & Nệm',_binary '',13),(136,'2025-04-13','Đồ Nội Thất','2025-04-13','Đồ Nội Thất',_binary '',13),(137,'2025-04-13','Trang Trí Nhà Cửa','2025-04-13','Trang Trí Nhà Cửa',_binary '',13),(138,'2025-04-13','Dụng cụ và thiết bị tiện ích','2025-04-13','Dụng Cụ & Thiết Bị Tiện Ích',_binary '',13),(139,'2025-04-13','Đồ Dùng Nhà Bếp và Hộp Đựng Thực Phẩm','2025-04-13','Đồ Dùng Nhà Bếp và Hộp Đựng Thực Phẩm',_binary '',13),(140,'2025-04-13','Đèn','2025-04-13','Đèn',_binary '',13),(141,'2025-04-13','Ngoài trời & Sân vườn','2025-04-13','Ngoài trời & Sân vườn',_binary '',13),(142,'2025-04-13','Đồ dùng phòng tắm','2025-04-13','Đồ dùng phòng tắm',_binary '',13),(143,'2025-04-13','Vật phẩm thờ cúng','2025-04-13','Vật phẩm thờ cúng',_binary '',13),(144,'2025-04-13','Đồ trang trí tiệc','2025-04-13','Đồ trang trí tiệc',_binary '',13),(145,'2025-04-13','Chăm sóc nhà cửa và giặt ủi','2025-04-13','Chăm sóc nhà cửa và giặt ủi',_binary '',13),(146,'2025-04-13','Sắp xếp nhà cửa','2025-04-13','Sắp xếp nhà cửa',_binary '',13),(147,'2025-04-13','Dụng cụ pha chế','2025-04-13','Dụng cụ pha chế',_binary '',13),(148,'2025-04-13','Tinh dầu thơm phòng','2025-04-13','Tinh dầu thơm phòng',_binary '',13),(149,'2025-04-13','Đồ dùng phòng ăn','2025-04-13','Đồ dùng phòng ăn',_binary '',13),(150,'2025-04-13','Chăm sóc da mặt','2025-04-13','Chăm sóc da mặt',_binary '',14),(151,'2025-04-13','Tắm & chăm sóc cơ thể','2025-04-13','Tắm & chăm sóc cơ thể',_binary '',14),(152,'2025-04-13','Trang điểm','2025-04-13','Trang điểm',_binary '',14),(153,'2025-04-13','Chăm sóc tóc','2025-04-13','Chăm sóc tóc',_binary '',14),(154,'2025-04-13','Dụng cụ & Phụ kiện Làm đẹp','2025-04-13','Dụng cụ & Phụ kiện Làm đẹp',_binary '',14),(155,'2025-04-13','Vệ sinh răng miệng','2025-04-13','Vệ sinh răng miệng',_binary '',14),(156,'2025-04-13','Nước hoa','2025-04-13','Nước hoa',_binary '',14),(157,'2025-04-13','Chăm sóc nam giới','2025-04-13','Chăm sóc nam giới',_binary '',14),(158,'2025-04-13','Khác','2025-04-13','Khác',_binary '',14),(159,'2025-04-13','Chăm sóc phụ nữ','2025-04-13','Chăm sóc phụ nữ',_binary '',14),(160,'2025-04-13','Bộ sản phẩm làm đẹp','2025-04-13','Bộ sản phẩm làm đẹp',_binary '',14),(161,'2025-04-13','Vật tư y tế','2025-04-13','Vật tư y tế',_binary '',15),(162,'2025-04-13','Chống muỗi & xua đuổi côn trùng','2025-04-13','Chống muỗi & xua đuổi côn trùng',_binary '',15),(163,'2025-04-13','Thực phẩm chức năng','2025-04-13','Thực phẩm chức năng',_binary '',15),(164,'2025-04-13','Tã người lớn','2025-04-13','Tã người lớn',_binary '',15),(165,'2025-04-13','Hỗ trợ làm đẹp','2025-04-13','Hỗ trợ làm đẹp',_binary '',15),(166,'2025-04-13','Hỗ trợ tình dục','2025-04-13','Hỗ trợ tình dục',_binary '',15),(167,'2025-04-13','Dụng cụ massage và trị liệu','2025-04-13','Dụng cụ massage và trị liệu',_binary '',15),(168,'2025-04-13','Khác','2025-04-13','Khác',_binary '',15),(169,'2025-04-13','Bốt','2025-04-13','Bốt',_binary '',16),(170,'2025-04-13','Giày Thể Thao/ Sneaker','2025-04-13','Giày Thể Thao/ Sneaker',_binary '',16),(171,'2025-04-13','Giày Đế Bằng','2025-04-13','Giày Đế Bằng',_binary '',16),(172,'2025-04-13','Giày Cao Gót','2025-04-13','Giày Cao Gót',_binary '',16),(173,'2025-04-13','Giày Đế Xuồng','2025-04-13','Giày Đế Xuồng',_binary '',16),(174,'2025-04-13','Xăng-đan Và Dép','2025-04-13','Xăng-đan Và Dép',_binary '',16),(175,'2025-04-13','Phụ Kiện Giày','2025-04-13','Phụ Kiện Giày',_binary '',16),(176,'2025-04-13','Giày Khác','2025-04-13','Giày Khác',_binary '',16),(177,'2025-04-13','Ba Lô Nữ','2025-04-13','Ba Lô Nữ',_binary '',17),(178,'2025-04-13','Cặp Laptop','2025-04-13','Cặp Laptop',_binary '',17),(179,'2025-04-13','Ví Dự Tiệc & Ví Cầm Tay','2025-04-13','Ví Dự Tiệc & Ví Cầm Tay',_binary '',17),(180,'2025-04-13','Túi Đeo Hông & Túi Đeo Ngực','2025-04-13','Túi Đeo Hông & Túi Đeo Ngực',_binary '',17),(181,'2025-04-13','Túi Tote','2025-04-13','Túi Tote',_binary '',17),(182,'2025-04-13','Túi Quai Xách','2025-04-13','Túi Quai Xách',_binary '',17),(183,'2025-04-13','Túi Đeo Chéo & Túi Đeo Vai','2025-04-13','Túi Đeo Chéo & Túi Đeo Vai',_binary '',17),(184,'2025-04-13','Ví/Bóp Nữ','2025-04-13','Ví/Bóp Nữ',_binary '',17),(185,'2025-04-13','Phụ Kiện Túi','2025-04-13','Phụ Kiện Túi',_binary '',17),(186,'2025-04-13','Khác','2025-04-13','Khác',_binary '',17),(187,'2025-04-13','Nhẫn','2025-04-13','Nhẫn',_binary '',18),(188,'2025-04-13','Bông tai','2025-04-13','Bông tai',_binary '',18),(189,'2025-04-13','Khăn choàng','2025-04-13','Khăn choàng',_binary '',18),(190,'2025-04-13','Găng tay','2025-04-13','Găng tay',_binary '',18),(191,'2025-04-13','Phụ kiện tóc','2025-04-13','Phụ kiện tóc',_binary '',18),(192,'2025-04-13','Vòng tay & Lắc tay','2025-04-13','Vòng tay & Lắc tay',_binary '',18),(193,'2025-04-13','Lắc chân','2025-04-13','Lắc chân',_binary '',18),(194,'2025-04-13','Mũ','2025-04-13','Mũ',_binary '',18),(195,'2025-04-13','Dây chuyền','2025-04-13','Dây chuyền',_binary '',18),(196,'2025-04-13','Kính mắt','2025-04-13','Kính mắt',_binary '',18),(197,'2025-04-13','Kim loại quý','2025-04-13','Kim loại quý',_binary '',18),(198,'2025-04-13','Thắt lưng','2025-04-13','Thắt lưng',_binary '',18),(199,'2025-04-13','Cà vạt & Nơ cổ','2025-04-13','Cà vạt & Nơ cổ',_binary '',18),(200,'2025-04-13','Phụ kiện thêm','2025-04-13','Phụ kiện thêm',_binary '',18),(201,'2025-04-13','Bộ phụ kiện','2025-04-13','Bộ phụ kiện',_binary '',18),(202,'2025-04-13','Khác','2025-04-13','Khác',_binary '',18),(203,'2025-04-13','Vớ/ Tất','2025-04-13','Vớ/ Tất',_binary '',18),(204,'2025-04-13','Ô/Dù','2025-04-13','Ô/Dù',_binary '',18),(205,'2025-04-13','Đồ ăn vặt','2025-04-13','Đồ ăn vặt',_binary '',19),(206,'2025-04-13','Đồ chế biến sẵn','2025-04-13','Đồ chế biến sẵn',_binary '',19),(207,'2025-04-13','Nhu yếu phẩm','2025-04-13','Nhu yếu phẩm',_binary '',19),(208,'2025-04-13','Nguyên liệu nấu ăn','2025-04-13','Nguyên liệu nấu ăn',_binary '',19),(209,'2025-04-13','Đồ làm bánh','2025-04-13','Đồ làm bánh',_binary '',19),(210,'2025-04-13','Sữa - trứng','2025-04-13','Sữa - trứng',_binary '',19),(211,'2025-04-13','Đồ uống','2025-04-13','Đồ uống',_binary '',19),(212,'2025-04-13','Ngũ cốc & mứt','2025-04-13','Ngũ cốc & mứt',_binary '',19),(213,'2025-04-13','Các loại bánh','2025-04-13','Các loại bánh',_binary '',19),(214,'2025-04-13','Đồ uống có cồn','2025-04-13','Đồ uống có cồn',_binary '',19),(215,'2025-04-13','Bộ quà tặng','2025-04-13','Bộ quà tặng',_binary '',19),(216,'2025-04-13','Thực phẩm tươi sống và thực phẩm đông lạnh','2025-04-13','Thực phẩm tươi sống và thực phẩm đông lạnh',_binary '',19),(217,'2025-04-13','Khác','2025-04-13','Khác',_binary '',19),(218,'2025-04-13','Sách Tiếng Việt','2025-04-13','Sách Tiếng Việt',_binary '',20),(219,'2025-04-13','Sách ngoại văn','2025-04-13','Sách ngoại văn',_binary '',20),(220,'2025-04-13','Gói Quà','2025-04-13','Gói Quà',_binary '',20),(221,'2025-04-13','Bút viết','2025-04-13','Bút viết',_binary '',20),(222,'2025-04-13','Dụng cụ học sinh & văn phòng','2025-04-13','Dụng cụ học sinh & văn phòng',_binary '',20),(223,'2025-04-13','Màu, Họa Cụ và Đồ Thủ Công','2025-04-13','Màu, Họa Cụ và Đồ Thủ Công',_binary '',20),(224,'2025-04-13','Sổ và Giấy Các Loại','2025-04-13','Sổ và Giấy Các Loại',_binary '',20),(225,'2025-04-13','Quà Lưu Niệm','2025-04-13','Quà Lưu Niệm',_binary '',20),(226,'2025-04-13','Nhạc cụ và phụ kiện âm nhạc','2025-04-13','Nhạc cụ và phụ kiện âm nhạc',_binary '',20),(227,'2025-04-13','Ba Lô Nam','2025-04-13','Ba Lô Nam',_binary '',21),(228,'2025-04-13','Ba Lô Laptop Nam','2025-04-13','Ba Lô Laptop Nam',_binary '',21),(229,'2025-04-13','Túi & Cặp Đựng Laptop','2025-04-13','Túi & Cặp Đựng Laptop',_binary '',21),(230,'2025-04-13','Túi Chống Sốc Laptop Nam','2025-04-13','Túi Chống Sốc Laptop Nam',_binary '',21),(231,'2025-04-13','Túi Tote Nam','2025-04-13','Túi Tote Nam',_binary '',21),(232,'2025-04-13','Cặp Xách Công Sở Nam','2025-04-13','Cặp Xách Công Sở Nam',_binary '',21),(233,'2025-04-13','Ví Cầm Tay Nam','2025-04-13','Ví Cầm Tay Nam',_binary '',21),(234,'2025-04-13','Túi Đeo Hông & Túi Đeo Ngực Nam','2025-04-13','Túi Đeo Hông & Túi Đeo Ngực Nam',_binary '',21),(235,'2025-04-13','Túi Đeo Chéo Nam','2025-04-13','Túi Đeo Chéo Nam',_binary '',21),(236,'2025-04-13','Bóp/Ví Nam','2025-04-13','Bóp/Ví Nam',_binary '',21),(237,'2025-04-13','Khác','2025-04-13','Khác',_binary '',21),(238,'2025-04-13','Trang phục bé trai','2025-04-13','Trang phục bé trai',_binary '',22),(239,'2025-04-13','Trang phục bé gái','2025-04-13','Trang phục bé gái',_binary '',22),(240,'2025-04-13','Giày dép bé trai','2025-04-13','Giày dép bé trai',_binary '',22),(241,'2025-04-13','Giày dép bé gái','2025-04-13','Giày dép bé gái',_binary '',22),(242,'2025-04-13','Khác','2025-04-13','Khác',_binary '',22),(243,'2025-04-13','Quần áo em bé','2025-04-13','Quần áo em bé',_binary '',22),(244,'2025-04-13','Giày tập đi & Tất sơ sinh','2025-04-13','Giày tập đi & Tất sơ sinh',_binary '',22),(245,'2025-04-13','Phụ kiện trẻ em','2025-04-13','Phụ kiện trẻ em',_binary '',22),(246,'2025-04-13','Sở thích & Sưu tầm','2025-04-13','Sở thích & Sưu tầm',_binary '',23),(247,'2025-04-13','Đồ chơi giải trí','2025-04-13','Đồ chơi giải trí',_binary '',23),(248,'2025-04-13','Đồ chơi giáo dục','2025-04-13','Đồ chơi giáo dục',_binary '',23),(249,'2025-04-13','Đồ chơi cho trẻ sơ sinh & trẻ nhỏ','2025-04-13','Đồ chơi cho trẻ sơ sinh & trẻ nhỏ',_binary '',23),(250,'2025-04-13','Đồ chơi vận động & ngoài trời','2025-04-13','Đồ chơi vận động & ngoài trời',_binary '',23),(251,'2025-04-13','Búp bê & Đồ chơi nhồi bông','2025-04-13','Búp bê & Đồ chơi nhồi bông',_binary '',23),(252,'2025-04-13','Giặt giũ & Chăm sóc nhà cửa','2025-04-13','Giặt giũ & Chăm sóc nhà cửa',_binary '',24),(253,'2025-04-13','Giấy vệ sinh, khăn giấy','2025-04-13','Giấy vệ sinh, khăn giấy',_binary '',24),(254,'2025-04-13','Vệ sinh nhà cửa','2025-04-13','Vệ sinh nhà cửa',_binary '',24),(255,'2025-04-13','Vệ sinh bát đĩa','2025-04-13','Vệ sinh bát đĩa',_binary '',24),(256,'2025-04-13','Dụng cụ vệ sinh','2025-04-13','Dụng cụ vệ sinh',_binary '',24),(257,'2025-04-13','Chất khử mùi, làm thơm','2025-04-13','Chất khử mùi, làm thơm',_binary '',24),(258,'2025-04-13','Thuốc diệt côn trùng','2025-04-13','Thuốc diệt côn trùng',_binary '',24),(259,'2025-04-13','Túi, màng bọc thực phẩm','2025-04-13','Túi, màng bọc thực phẩm',_binary '',24),(260,'2025-04-13','Bao bì, túi đựng rác','2025-04-13','Bao bì, túi đựng rác',_binary '',24),(261,'2025-04-13','Thức ăn cho thú cưng','2025-04-13','Thức ăn cho thú cưng',_binary '',25),(262,'2025-04-13','Phụ kiện cho thú cưng','2025-04-13','Phụ kiện cho thú cưng',_binary '',25),(263,'2025-04-13','Vệ sinh cho thú cưng','2025-04-13','Vệ sinh cho thú cưng',_binary '',25),(264,'2025-04-13','Quần áo thú cưng','2025-04-13','Quần áo thú cưng',_binary '',25),(265,'2025-04-13','Chăm sóc sức khỏe','2025-04-13','Chăm sóc sức khỏe',_binary '',25),(266,'2025-04-13','Làm đẹp cho thú cưng','2025-04-13','Làm đẹp cho thú cưng',_binary '',25),(267,'2025-04-13','Khác','2025-04-13','Khác',_binary '',25),(268,'2025-04-13','Nhà hàng & Ăn uống','2025-04-13','Nhà hàng & Ăn uống',_binary '',26),(269,'2025-04-13','Sự kiện & Giải trí','2025-04-13','Sự kiện & Giải trí',_binary '',26),(270,'2025-04-13','Nạp tiền tài khoản','2025-04-13','Nạp tiền tài khoản',_binary '',26),(271,'2025-04-13','Sức khỏe & Làm đẹp','2025-04-13','Sức khỏe & Làm đẹp',_binary '',26),(272,'2025-04-13','Gọi xe','2025-04-13','Gọi xe',_binary '',26),(273,'2025-04-13','Khóa học','2025-04-13','Khóa học',_binary '',26),(274,'2025-04-13','Du lịch & Khách sạn','2025-04-13','Du lịch & Khách sạn',_binary '',26),(275,'2025-04-13','Mua sắm','2025-04-13','Mua sắm',_binary '',26),(276,'2025-04-13','Mã quà tặng Shopee','2025-04-13','Mã quà tặng Shopee',_binary '',26),(277,'2025-04-13','Thanh toán hóa đơn','2025-04-13','Thanh toán hóa đơn',_binary '',26),(278,'2025-04-13','Dịch vụ khác','2025-04-13','Dịch vụ khác',_binary '',26),(279,'2025-04-13','Dụng cụ cầm tay','2025-04-13','Dụng cụ cầm tay',_binary '',27),(280,'2025-04-13','Dụng cụ điện và thiết bị lớn','2025-04-13','Dụng cụ điện và thiết bị lớn',_binary '',27),(281,'2025-04-13','Thiết bị mạch điện','2025-04-13','Thiết bị mạch điện',_binary '',27),(282,'2025-04-13','Vật liệu xây dựng','2025-04-13','Vật liệu xây dựng',_binary '',27),(283,'2025-04-13','Thiết bị và phụ kiện xây dựng','2025-04-13','Thiết bị và phụ kiện xây dựng',_binary '',27);
/*!40000 ALTER TABLE subcategory ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS user;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  id int NOT NULL AUTO_INCREMENT,
  become_seller_date datetime(6) DEFAULT NULL,
  birthday datetime(6) DEFAULT NULL,
  created_date datetime(6) DEFAULT NULL,
  `enable` bit(1) DEFAULT NULL,
  facebook varchar(255) DEFAULT NULL,
  gmail varchar(255) DEFAULT NULL,
  modified_date datetime(6) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  verify bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  avatar varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY facebook_UNIQUE (facebook),
  UNIQUE KEY gmail_UNIQUE (gmail),
  UNIQUE KEY username_UNIQUE (username)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES user WRITE;
/*!40000 ALTER TABLE user DISABLE KEYS */;
INSERT INTO user VALUES (1,NULL,NULL,NULL,_binary '',NULL,NULL,NULL,'$2a$10$6IWH522.n7.MyOm4d5X1tOntv.0KARt75gubmXLRQ/xlT9n2FF/4W',NULL,_binary '','admin',NULL,NULL,NULL),(2,NULL,NULL,NULL,_binary '',NULL,NULL,NULL,'$2a$10$6IWH522.n7.MyOm4d5X1tOntv.0KARt75gubmXLRQ/xlT9n2FF/4W',NULL,_binary '','thanh',_binary '',NULL,'https://res.cloudinary.com/dowc3tobj/image/upload/v1744467877/Image/zaqfhfcloizdf83uu8gl.jpg'),(3,NULL,NULL,'2025-03-30 12:10:27.062806',_binary '',NULL,NULL,'2025-03-30 12:10:27.062806','$2a$10$87onX7TxK7YsB.sHfka1zer3GuwZMwAFFiQ2mgpdY9HK4pr1KGooq',NULL,_binary '','thanhngu',_binary '\0',NULL,'https://res.cloudinary.com/dowc3tobj/image/upload/v1744467877/Image/zaqfhfcloizdf83uu8gl.jpg'),(5,NULL,NULL,'2025-03-31 22:14:53.243843',_binary '',NULL,NULL,'2025-03-31 22:14:53.243843','$2a$10$GES/GxOY83dInQeVBrZZO.H/XNvYTSP0KrvbpjZo/.7uW28gduBlu',NULL,_binary '','hghg',_binary '\0',NULL,'https://res.cloudinary.com/dowc3tobj/image/upload/v1744467877/Image/zaqfhfcloizdf83uu8gl.jpg'),(9,NULL,NULL,'2025-04-01 21:18:31.246750',_binary '','1236191534476912','example@gmail.com','2025-04-01 21:18:31.246750',NULL,NULL,_binary '','user1236191534476912',_binary '\0','Pham Phan Thanh',NULL),(10,NULL,NULL,'2025-04-01 21:40:22.920357',_binary '',NULL,'thanhmymum@gmail.com','2025-04-01 21:40:22.920357',NULL,NULL,_binary '','thanhmymum@gmail.com',_binary '\0','Phạm Phan Thành',NULL),(11,NULL,NULL,'2025-04-01 21:40:51.763572',_binary '',NULL,'thanhtyu147@gmail.com','2025-04-01 21:40:51.763572',NULL,NULL,_binary '','thanhtyu147@gmail.com',_binary '\0','Thanh Phao',NULL),(12,NULL,NULL,'2025-04-01 23:50:42.447078',_binary '','690959796787870','thanhlongtivip@gmail.com','2025-04-07 08:30:24.008919',NULL,'0814799346',_binary '','user690959796787870',_binary '','thanh ngu ngơ','https://res.cloudinary.com/dowc3tobj/image/upload/v1744467877/Image/zaqfhfcloizdf83uu8gl.jpg'),(13,NULL,NULL,'2025-04-05 21:10:43.929985',_binary '',NULL,NULL,'2025-04-05 21:10:43.929985','$2a$10$wubnip291M/MDPCVUzBnZOSAYw5BGPoFyEfhVyovYjHo8FcpgCyyS',NULL,_binary '','trtr',_binary '\0','thành Dương Quá',NULL),(14,NULL,NULL,'2025-04-06 09:40:03.343423',_binary '',NULL,NULL,'2025-04-06 09:40:03.343423','$2a$10$WBnn14iGExs86Em8MX/bYektSMw2sPQ2emKe.9k70rEPDdawKVzGe',NULL,_binary '','thanhdeptrai',_binary '\0',NULL,NULL),(15,NULL,NULL,'2025-04-06 11:49:16.345972',_binary '',NULL,NULL,'2025-04-06 11:49:16.345972','$2a$10$2PB.qGHaOS3P/b74WdbUx.e7mKMgjTAauQhcuX67CnWQU6uBCUoeK',NULL,_binary '','thanhbungbeo',_binary '\0',NULL,NULL),(17,NULL,NULL,'2025-04-12 19:21:10.361733',_binary '',NULL,NULL,'2025-04-12 19:21:10.361733','$2a$10$922gZfY9v8FOU29XsLeiuu13n2gc16CIeqmVu6H9Ikoq0azaJnHoi',NULL,_binary '','thanhdeptraiqua',_binary '\0',NULL,'https://res.cloudinary.com/dowc3tobj/image/upload/v1744467877/Image/zaqfhfcloizdf83uu8gl.jpg'),(18,NULL,NULL,'2025-04-12 21:35:54.144686',_binary '',NULL,NULL,'2025-04-12 21:35:54.144686','$2a$10$spLmPNbs//DEMCpl5SWRs.rbjW.KVMZhhINELuTbQA1Bo3vt4P7Ki',NULL,_binary '','hahaha',_binary '',NULL,'https://res.cloudinary.com/dowc3tobj/image/upload/v1744467877/Image/zaqfhfcloizdf83uu8gl.jpg');
/*!40000 ALTER TABLE user ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS user_address;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE user_address (
  id int NOT NULL AUTO_INCREMENT,
  created_date date DEFAULT NULL,
  full_address varchar(255) DEFAULT NULL,
  fullname varchar(255) DEFAULT NULL,
  is_default int DEFAULT NULL,
  location varchar(255) DEFAULT NULL,
  modified_date date DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  user_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKk2ox3w9jm7yd6v1m5f68xibry (user_id),
  CONSTRAINT FKk2ox3w9jm7yd6v1m5f68xibry FOREIGN KEY (user_id) REFERENCES `user` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_address`
--

LOCK TABLES user_address WRITE;
/*!40000 ALTER TABLE user_address DISABLE KEYS */;
/*!40000 ALTER TABLE user_address ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS user_role;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE user_role (
  user_id int NOT NULL,
  role_id int NOT NULL,
  PRIMARY KEY (user_id,role_id),
  KEY FKt7e7djp752sqn6w22i6ocqy6q (role_id),
  CONSTRAINT FK859n2jvi8ivhui0rl0esws6o FOREIGN KEY (user_id) REFERENCES `user` (id),
  CONSTRAINT FKt7e7djp752sqn6w22i6ocqy6q FOREIGN KEY (role_id) REFERENCES roles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES user_role WRITE;
/*!40000 ALTER TABLE user_role DISABLE KEYS */;
INSERT INTO user_role VALUES (1,1),(18,1),(2,2),(3,2),(5,2),(9,2),(10,2),(11,2),(12,2),(13,2),(14,2),(15,2),(17,2),(18,2),(18,3);
/*!40000 ALTER TABLE user_role ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-13 22:01:45
