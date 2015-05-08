    DROP TABLE IF EXISTS "bills";
CREATE TABLE "bills" ("id_bill" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , "date" DATETIME NOT NULL  DEFAULT CURRENT_DATE, "table_number" INTEGER NOT NULL );
INSERT INTO "bills" VALUES(1,'2015-02-27',1);
INSERT INTO "bills" VALUES(2,'2015-03-04',2);
DROP TABLE IF EXISTS "drinks";
CREATE TABLE "drinks" ("id_drink" INTEGER PRIMARY KEY NOT NULL UNIQUE ,"name_drink" CHAR NOT NULL ,"price" REAL NOT NULL ,"available_quantity" INTEGER NOT NULL ,"volume" INTEGER NOT NULL ,"description" TEXT NOT NULL ,"icon" TEXT NOT NULL ,"max_stock" INTEGER NOT NULL ,"threshold" INTEGER NOT NULL ,"category" CHAR NOT NULL ,"subcategory" CHAR);
INSERT INTO "drinks" VALUES(1,'Jupiler',2,18,25,'La Jupiler est une bière belge blonde de fermentation basse de type pils. Elle a été créée et fabriquée par la brasserie Piedboeuf dans le village de Jupille-sur-Meuse, banlieue de Liège dont elle tire son nom.','icon/JUP25.jpg',200,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(2,'Jupiler',2.5,75,33,'La Jupiler est une bière belge blonde de fermentation basse de type pils. Elle a été créée et fabriquée par la brasserie Piedboeuf dans le village de Jupille-sur-Meuse, banlieue de Liège dont elle tire son nom.','icon/JUP25.jpg',200,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(3,'Jupiler',3.5,200,50,'La Jupiler est une bière belge blonde de fermentation basse de type pils. Elle a été créée et fabriquée par la brasserie Piedboeuf dans le village de Jupille-sur-Meuse, banlieue de Liège dont elle tire son nom.','icon/JUP.jpg',200,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(4,'Leffe Blonde',3,100,25,'La Leffe Blonde est une authentique bière blonde d''Abbaye reconue à la douce amertume qui se savoure à tout moment de la journée. 6,6 pourcent','icon/LEF.jpg',100,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(5,'Leffe Blonde',3.5,100,33,'La Leffe Blonde est une authentique bière blonde d''Abbaye reconue à la douce amertume qui se savoure à tout moment de la journée. 6,6 pourcent','icon/LEF.jpg',100,20,'Bières','Blondes');
INSERT INTO "drinks" VALUES(6,'Leffe Brune',3,100,25,'La Leffe Brune est une authentique bière d''Abbaye à la belle rabe acajou et à la saveur pleine et légèrement sucrée qui fait de chaque gorgée un moment d''exception. Une couleur et saveur unique provenant de l''utilisation de malt torréfié.','icon/LEF.jpg',100,20,'Bières','Brunes');
INSERT INTO "drinks" VALUES(7,'Leffe Brune',3.5,100,33,'La Leffe Brune est une authentique bière d''Abbaye à la belle rabe acajou et à la saveur pleine et légèrement sucrée qui fait de chaque gorgée un moment d''exception. Une couleur et saveur unique provenant de l''utilisation de malt torréfié.','icon/LEF.jpg',100,20,'Bières','Brunes');
INSERT INTO "drinks" VALUES(8,'Côte du Rhône rouge',20.50,50,75,'Le Côte du Rhône rouge est un vin produit sur les rives droite et gauchedu Rhône entre Vienne et Avignon.','icon/COTRHONE',50,5,'Vin','Rouge');
INSERT INTO "drinks" VALUES(9,'Côte du Rhône rosé',20.50,25,75,'Le Côte du Rhône rosé est un vin produit sur les rives droite et gauchedu Rhône entre Vienne et Avignon.','icon/COTRHONE',25,5,'Vin','Rosé');
INSERT INTO "drinks" VALUES(10,'Côte du Rhône blanc',20.50,50,75,'Le Côte du Rhône blanc est un vin produit sur les rives droite et gauchedu Rhône entre Vienne et Avignon.','icon/COTRHONE',50,5,'Vin','Blanc');
INSERT INTO "drinks" VALUES(11,'Château Pourcieux rouge',7,25,75,'Vin de couleur rouge rubis avec reflets grenat. C''est un vin généreux avec une belle persistance. A déguster sur un carré d’agneau, un filet de bœuf, un magret de canard.','icon/CHATPOURS.jpg',25,5,'Vin','Rouge');
INSERT INTO "drinks" VALUES(12,'Château Pourcieux rosé',7,25,75,'Vin d’une couleur rose pâle aux reflets brillants. Equilibre et élégance caractérisent ce vin. On profite d’une belle persistance aromatique en finale.','icon/CHATPOURS.jpg',25,5,'Vin','Rosé');
INSERT INTO "drinks" VALUES(13,'Château Pourcieux blanc',7,25,75,'Vin de couleur jaune pâle aux reflets verts. La bouche est délicate et révèle une belle minéralité. Des arômes d''agrumes dominent. La finale est longue et toute en fraicheur.','icon/CHATPOURS.jpg',25,5,'Vin','Blanc');
INSERT INTO "drinks" VALUES(14,'Eau plate',2,200,50,'L''eau plate Chaudefontaine est une eau minérale naturelle faiblement minéralisée','',200,20,'Eau','Plate');
INSERT INTO "drinks" VALUES(15,'Eau plate',4,200,100,'L''eau plate Chaudefontaine est une eau minérale naturelle faiblement minéralisée','icon/eau.jpg',200,20,'Eau','Plate');
INSERT INTO "drinks" VALUES(16,'Eau pétillante',3,200,50,'S.Pellegrino est une eau délicieuse. Impression d''une fraîcheur qui picote sur la langue, puis d''une acidité modérée et une forte teneur en minéraux qui laisse un arrière-goût agréable, confèrent aujourd''hui à S.Pellegrino sa renommée internationale.','icon/eau.jpg',200,20,'Eau','Pétillante');
INSERT INTO "drinks" VALUES(17,'Eau pétillante',6,200,100,'S.Pellegrino est une eau délicieuse. Impression d''une fraîcheur qui picote sur la langue, puis d''une acidité modérée et une forte teneur en minéraux qui laisse un arrière-goût agréable, confèrent aujourd''hui à S.Pellegrino sa renommée internationale.','icon/eau.jpg',200,20,'Eau','Pétillante');
INSERT INTO "drinks" VALUES(18,'Fanta orange',2.10,200,33,'Soft de Coca Cola companie. Goût orange.','icon/FANTA',200,50,'Soft', 'Soda');
INSERT INTO "drinks" VALUES(19,'Fanta citron',2.10,200,33,'Soft de Coca Cola companie. Goût citron.','icon/FANTA',200,50,'Soft', 'Soda');
INSERT INTO "drinks" VALUES(20,'Coca Cola',2.10,200,33,'Soft de Coca Cola companie.','icon/COCA',200,50,'Soft', 'Soda');
INSERT INTO "drinks" VALUES(21,'Expresso',2.50,100,15,'Café corsé avec un fort arôme','icon/CAFE',100,20,'Soft', 'Café');
INSERT INTO "drinks" VALUES(22,'Cappuccino',4.50,100,15,'Le cappuccino est un café, servi dans une grande tasse, à base d''un expresso et coiffé de lait préalablement chauffé à la vapeur jusqu''à le faire mousser.','icon/CAFE',100,20,'Soft', 'Café');
INSERT INTO "drinks" VALUES(23,'Thé vert',2.50,100,15,'Le thé vert provient du théier, un arbre à feuillage persistant, originaire des régions montagneuses du sud-ouest de la Chine, du nord de la Thaïlande et du Myanmar.','icon/THE',100,20,'Soft', 'Thé');
INSERT INTO "drinks" VALUES(24,'Thé citron',2.50,100,15,'Le thé citron a de vertu anti-oxydant','icon/THE',100,20,'Soft', 'Thé');
INSERT INTO "drinks" VALUES(25,'Kriek',2.90,100,25,'La Kriek est une bière belge aromatisée avec des cerises acides','icon/KRIEK.jpg',100,20,'Bières','Fruitée');
DROP TABLE IF EXISTS "order_details";
CREATE TABLE "order_details" ("id_order" INTEGER NOT NULL, "id_drink" INTEGER NOT NULL, "quantity" INTEGER NOT NULL , PRIMARY KEY ("id_order", "id_drink"));
INSERT INTO "order_details" VALUES(1,1,3);
INSERT INTO "order_details" VALUES(2,4,1);
INSERT INTO "order_details" VALUES(1,5,1);
INSERT INTO "order_details" VALUES(2,6,3);
INSERT INTO "order_details" VALUES(2,1,4);
INSERT INTO "order_details" VALUES(3,5,12);
INSERT INTO "order_details" VALUES(4,4,12);
DROP TABLE IF EXISTS "orders";
CREATE TABLE "orders" ("id_order" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , "date" DATETIME NOT NULL  DEFAULT CURRENT_DATE, "login_waiter" CHAR(30) NOT NULL , "table_number" INTEGER NOT NULL );
INSERT INTO "orders" VALUES(1,'2015-02-27','john',1);
INSERT INTO "orders" VALUES(2,'2015-02-27','john',2);
INSERT INTO "orders" VALUES(3,'2015-02-27','john',3);
INSERT INTO "orders" VALUES(4,'2015-02-27','testthib',4);
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
INSERT INTO "users" VALUES(4, 'testthib', '', 'waiter');
