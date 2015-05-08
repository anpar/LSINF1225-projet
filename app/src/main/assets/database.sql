    DROP TABLE IF EXISTS "bills";
CREATE TABLE "bills" ("id_bill" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , "date" DATETIME NOT NULL  DEFAULT CURRENT_DATE, "table_number" INTEGER NOT NULL );
INSERT INTO "bills" VALUES(1,'2015-02-27',1);
INSERT INTO "bills" VALUES(2,'2015-03-04',2);
DROP TABLE IF EXISTS "drinks";
CREATE TABLE "drinks" ("id_drink" INTEGER PRIMARY KEY NOT NULL UNIQUE ,"name_drink" CHAR NOT NULL ,"price" REAL NOT NULL ,"available_quantity" INTEGER NOT NULL ,"volume" INTEGER NOT NULL ,"description" TEXT NOT NULL ,"icon" TEXT NOT NULL ,"max_stock" INTEGER NOT NULL ,"threshold" INTEGER NOT NULL ,"category" CHAR NOT NULL ,"subcategory" CHAR);
INSERT INTO "drinks" VALUES(1,'Jupiler',2,18,25,'La Jupiler est une bière belge blonde de fermentation basse de type pils. Elle a été créée et fabriquée par la brasserie Piedboeuf dans le village de Jupille-sur-Meuse, banlieue de Liège dont elle tire son nom.','icon/JUP25.jpg',200,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(2,'Jupiler',2.5,75,33,'Jupiler au fût.','icon/JUP25.jpg',200,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(3,'Jupiler',3.5,200,50,'Jupiler au fût.','icon/JUP.jpg',200,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(4,'Leffe Blonde',3,100,25,'Leffe en bouteille.','icon/LEF.jpg',100,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(5,'Leffe Blonde',3.5,100,33,'Leffe en bouteille.','icon/LEF.jpg',100,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(6,'Leffe Brune',3,100,25,'Leffe en bouteille.','icon/LEF.jpg',100,20,'Bières','Brunes');
INSERT INTO "drinks" VALUES(7,'Leffe Brune',3.5,100,33,'Leffe en bouteille.','icon/LEF.jpg',100,20,'Bières','Brunes');
INSERT INTO "drinks" VALUES(8,'Côte du Rhône rouge',8,50,75,'Vin rouge de France','icon/COTRHONE',50,5,'Vin','Rouge');
INSERT INTO "drinks" VALUES(9,'Côte du Rhône rosé',8,25,75,'Vin rosé de France','icon/COTRHONE',25,5,'Vin','Rosé');
INSERT INTO "drinks" VALUES(10,'Côte du Rhône blanc',8,50,75,'Vin blanc de France','icon/COTRHONE',50,5,'Vin','Blanc');
INSERT INTO "drinks" VALUES(11,'Château Pourcieux rouge',7,25,75,'Vin de Provence','icon/CHATPOURS.jpg',25,5,'Vin','Rouge');
INSERT INTO "drinks" VALUES(12,'Château Pourcieux rosé',7,25,75,'Vin de Provence','icon/CHATPOURS.jpg',25,5,'Vin','Rosé');
INSERT INTO "drinks" VALUES(13,'Château Pourcieux blanc',7,25,75,'Vin de Provence','icon/CHATPOURS.jpg',25,5,'Vin','Blanc');
INSERT INTO "drinks" VALUES(14,'Eau plate',2,200,50,'Eau plate Chaudefontaine','',200,20,'Eau','Plate');
INSERT INTO "drinks" VALUES(15,'Eau plate litre',4,200,50,'Eau plate Chaudefontaine','icon/eau.jpg',200,20,'Eau','Plate');
INSERT INTO "drinks" VALUES(16,'Eau pétillante demi-litre',3,200,50,'Eau pétillante san pellegrino un demi litre','icon/eau.jpg',200,20,'Eau','Pétillante');
INSERT INTO "drinks" VALUES(17,'Eau pétillante litre',6,200,50,'Eau pétillante san pellegrino un litre','icon/eau.jpg',200,20,'Eau','Pétillante');
DROP TABLE IF EXISTS "order_details";
CREATE TABLE "order_details" ("id_order" INTEGER NOT NULL, "id_drink" INTEGER NOT NULL, "quantity" INTEGER NOT NULL , PRIMARY KEY ("id_order", "id_drink"));
INSERT INTO "order_details" VALUES(1,1,3);
INSERT INTO "order_details" VALUES(2,4,1);
INSERT INTO "order_details" VALUES(1,5,1);
INSERT INTO "order_details" VALUES(2,6,3);
INSERT INTO "order_details" VALUES(2,1,4);
INSERT INTO "order_details" VALUES(3,5,12);
DROP TABLE IF EXISTS "orders";
CREATE TABLE "orders" ("id_order" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , "date" DATETIME NOT NULL  DEFAULT CURRENT_DATE, "login_waiter" CHAR(30) NOT NULL , "table_number" INTEGER NOT NULL );
INSERT INTO "orders" VALUES(1,'2015-02-27','john',1);
INSERT INTO "orders" VALUES(2,'2015-02-27','john',2);
INSERT INTO "orders" VALUES(3,'2015-02-27','john',3);
DROP TABLE IF EXISTS "ratings";
CREATE TABLE "ratings" ("id_drink" INTEGER NOT NULL ,"login_client" CHAR(30) NOT NULL ,"value" INTEGER NOT NULL ,"comment" TEXT, PRIMARY KEY ("id_drink", "login_client"));
INSERT INTO "ratings" VALUES(1,'anparis',4.5,'Excellente bière ! Bien meilleure que le Cara Pils !');
INSERT INTO "ratings" VALUES(1, 'test', 2, 'Elle avait le goût de savon, c''est pas que j''aime pas la mousse mais quand même!');
INSERT INTO "ratings" VALUES(3,'anparis',4,'');
INSERT INTO "ratings" VALUES(3,'test', 1.5, '');
DROP TABLE IF EXISTS "users";
CREATE TABLE "users" ("u_id" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , "u_login" VARCHAR NOT NULL  UNIQUE , "u_password" VARCHAR NOT NULL , "u_type" VARCHAR NOT NULL );
INSERT INTO "users" VALUES(1,'anparis','041195','customer');
INSERT INTO "users" VALUES(2,'john','041195','waiter');
INSERT INTO "users" VALUES(3, 'test', 'test', 'customer');
