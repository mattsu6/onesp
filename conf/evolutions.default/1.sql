# --- !Ups

CREATE TABLE `creative` (
   `creative_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
   `file_name` varchar(255) NOT NULL,
   PRIMARY KEY (`creative_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8
# --- !Downs

DROP TABLE "creative";

# --- !Insert
insert into creative(file_name) values("grey.png")
insert into creative(file_name) values("pink.png")
insert into creative(file_name) values("purple.png")
insert into creative(file_name) values("brown.png")