delete from users;
insert into users(username,password,enabled) values
('admin','$2a$12$BHMZFp1aQs6NJ1zpPS4zjOWkuI3I9FC3AW8k10PUpKk8HnYOkrqUG',true),
('jenkins','$2a$12$BHMZFp1aQs6NJ1zpPS4zjOWkuI3I9FC3AW8k10PUpKk8HnYOkrqUG',true),
('auditor','$2a$12$BHMZFp1aQs6NJ1zpPS4zjOWkuI3I9FC3AW8k10PUpKk8HnYOkrqUG',true);

delete from user_roles;
insert into user_roles(user_role_id,role,username) values
(-1,'ROLE_ADMIN','admin'),
(-2,'ROLE_JENKINS','jenkins'),
(-3,'ROLE_AUDITOR','auditor');

DELETE FROM INSTANCE;
INSERT INTO INSTANCE(tier,instance_name,host) VALUES
('P', 'PIIBINST1', 'unxs0616.gha.kfplc.com'),
('P', 'PIIBINST2', 'unxs0617.gha.kfplc.com'),
('P', 'PIIBINST3', 'unxs0622.gha.kfplc.com'),
('P', 'PIIBINST4', 'unxs0623.gha.kfplc.com'),
('V', 'VIIBINST1', 'unxs0624.gha.kfplc.com'),
('V', 'VIIBINST2', 'unxs0625.gha.kfplc.com'),
('V', 'VIIBINST3', 'unxs0630.gha.kfplc.com'),
('V', 'VIIBINST4', 'unxs0631.gha.kfplc.com'),
('Q', 'QIIBINST1', 'unxs0634.ghanp.kfplc.com'),
('Q', 'QIIBINST2', 'unxs0707.ghanp.kfplc.com'),
('E', 'EIIBINST1', 'unxs0612.ghanp.kfplc.com'),
('F', 'FIIBINST1', 'unxs0614.ghanp.kfplc.com');


delete from deployment;
/*ArticleHierarchy_notify-RS*/
insert into deployment(deployment_id,application_name,environment,instance_name,integration_server,bar_release_id,created_time) values
(1,'ArticleHierarchy_notify-RS','P','PIIBINST1','MDAT','F2522.12.12', sysdate+random()*100),
(2,'ArticleHierarchy_notify-RS','P','PIIBINST2','MDAT','F2522.12.12', sysdate+random()*100),
(3,'ArticleHierarchy_notify-RS','P','PIIBINST3','MDAT','F2522.12.12', sysdate+random()*100),
(4,'ArticleHierarchy_notify-RS','P','PIIBINST4','MDAT','F2522.12.12', sysdate+random()*100),
(5,'ArticleHierarchy_notify-RS','F','FIIBINST1','MDAT','F2522.12.12', sysdate+random()*100),
(6,'ArticleHierarchy_notify-RS','V','VIIBINST1','MDAT','F2522.12.12', sysdate+random()*100),
(7,'ArticleHierarchy_notify-RS','Q','QIIBINST1','MDAT','F2522.12.12', sysdate+random()*100),
(8,'ArticleHierarchy_notify-RS','Q','QIIBINST2','MDAT','F2522.12.12', sysdate+random()*100),
(9,'ArticleHierarchy_notify-RS','Q','QIIBINST3','MDAT','F2522.12.12', sysdate+random()*100),
(10,'ArticleHierarchy_notify-RS','Q','QIIBINST4','MDAT','F2522.12.12', sysdate+random()*100),
(11,'Product_Gateway_V2','P','PIIBINST1','SWEB_CNC1','1.0.25',sysdate+random()*100),
(12,'Product_Gateway_V2','P','PIIBINST2','SWEB_CNC1','1.0.25',sysdate+random()*100),
(13,'Product_Gateway_V2','P','PIIBINST3','SWEB_CNC1','1.0.25',sysdate+random()*100),
(14,'Product_Gateway_V2','P','PIIBINST4','SWEB_CNC1','1.0.25',sysdate+random()*100),
(15,'Product_Gateway_V2','F','FIIBINST1','SWEB_CNC1','1.0.26',sysdate+random()*100),
(16,'Product_Gateway_V2','V','VIIBINST1','SWEB_CNC1','1.0.26',sysdate+random()*100),
(17,'Product_Gateway_V2','V','VIIBINST2','SWEB_CNC1','1.0.26',sysdate+random()*100),
(18,'Product_Gateway_V2','V','VIIBINST3','SWEB_CNC1','1.0.26',sysdate+random()*100),
(19,'Product_Gateway_V2','V','VIIBINST4','SWEB_CNC1','1.0.26',sysdate+random()*100),
(20,'Product_Gateway_V2','Q','QIIBINST1','SWEB_CNC1','1.0.26',sysdate+random()*100),
(21,'Supplier_Gateway','P','PIIBINST1','SWEB_CNC1','1.0.4',sysdate+random()*100),
(22,'Supplier_Gateway','P','PIIBINST2','SWEB_CNC1','1.0.4',sysdate+random()*100),
(23,'Supplier_Gateway','P','PIIBINST3','SWEB_CNC1','1.0.4',sysdate+random()*100),
(24,'Supplier_Gateway','P','PIIBINST4','SWEB_CNC1','1.0.4',sysdate+random()*100),
(25,'Supplier_Gateway','F','FIIBINST1','SWEB_CNC1','1.0.4',sysdate+random()*100),
(26,'Supplier_Gateway','V','VIIBINST1','SWEB_CNC1','1.0.4',sysdate+random()*100),
(27,'Supplier_Gateway','V','VIIBINST2','SWEB_CNC1','1.0.4',sysdate+random()*100),
(28,'Supplier_Gateway','V','VIIBINST3','SWEB_CNC1','1.0.4',sysdate+random()*100),
(29,'Supplier_Gateway','V','VIIBINST4','SWEB_CNC1','1.0.4',sysdate+random()*100),
(30,'Supplier_Gateway','Q','QIIBINST1','SWEB_CNC1','1.0.3',sysdate+random()*100),
(31,'Bay_Gateway','P','PIIBINST1','SWEB_CNC1','1.0.1',sysdate+random()*100),
(32,'Bay_Gateway','P','PIIBINST2','SWEB_CNC1','1.0.1',sysdate+random()*100),
(33,'Bay_Gateway','P','PIIBINST3','SWEB_CNC1','1.0.1',sysdate+random()*100),
(34,'Bay_Gateway','P','PIIBINST4','SWEB_CNC1','1.0.1',sysdate+random()*100),
(35,'Bay_Gateway','F','FIIBINST1','TST_CNC2','1.0.10',sysdate+random()*100),
(36,'Bay_Gateway','V','VIIBINST1','SWEB_CNC1','1.0.10',sysdate+random()*100),
(37,'Bay_Gateway','V','VIIBINST3','SWEB_CNC1','1.0.10',sysdate+random()*100),
(38,'Bay_Gateway','V','VIIBINST4','SWEB_CNC1','1.0.10',sysdate+random()*100),
(39,'Bay_Gateway','Q','QIIBINST1','SWEB_CNC1','1.0.1',sysdate+random()*100),
(40,'Quote_notify','P','PIIBINST1','RETL','1.0.3',sysdate+random()*100),
(41,'Quote_notify','P','PIIBINST2','RETL','1.0.3',sysdate+random()*100),
(42,'Quote_notify','P','PIIBINST3','RETL','1.0.3',sysdate+random()*100),
(43,'Quote_notify','P','PIIBINST4','RETL','1.0.3',sysdate+random()*100),
(44,'Quote_notify','F','FIIBINST1','RETL','1.0.3',sysdate+random()*100),
(45,'Quote_notify','F','FIIBINST1','TEST','-',sysdate+random()*100),
(46,'Quote_notify','V','VIIBINST1','RETL','1.0.3',sysdate+random()*100),
(47,'Quote_notify','V','VIIBINST2','RETL','1.0.3',sysdate+random()*100),
(48,'Quote_notify','V','VIIBINST3','RETL','1.0.3',sysdate+random()*100),
(49,'Quote_notify','V','VIIBINST4','RETL','1.0.3',sysdate+random()*100),
(50,'Quote_notify','Q','QIIBINST1','RETL','1.0.3',sysdate+random()*100),
(51,'StoreTransfer_Gateway','P','PIIBINST1','SWEB_CNC1','1.0.2',sysdate+random()*100),
(52,'StoreTransfer_Gateway','P','PIIBINST3','SWEB_CNC1','1.0.2',sysdate+random()*100),
(53,'StoreTransfer_Gateway','P','PIIBINST2','SWEB_CNC1','1.0.1',sysdate+random()*100),
(54,'StoreTransfer_Gateway','P','PIIBINST4','SWEB_CNC1','1.0.1',sysdate+random()*100),
(55,'test','P','PIIBINST1','SWEB_CNC1','1.0.1',sysdate+random()*100);