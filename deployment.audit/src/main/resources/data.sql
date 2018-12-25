delete from deployment;
/*ArticleHierarchy_notify-RS*/
insert into deployment
(deployment_id,application_name,environment,instance_name,integration_server,bar_release_id,created_time) values
(20,'ArticleHierarchy_notify-RS','P','*','MDAT','F2522.12.12',sysdate),
(21,'ArticleHierarchy_notify-RS','F','*','MDAT','F2522.12.12',sysdate+1),
(22,'ArticleHierarchy_notify-RS','V','*','MDAT','F2522.12.12',sysdate+2),
(23,'ArticleHierarchy_notify-RS','Q','*','MDAT','F2522.12.12',sysdate+3);
/*Product_Gateway_V2*/
insert into deployment
(deployment_id,application_name,environment,instance_name,integration_server,bar_release_id,created_time) values
(1,'Product_Gateway_V2','P','PIIBINST(4)','SWEB_CNC1','1.0.25',sysdate),
(2,'Product_Gateway_V2','F','FIIBINST1','SWEB_CNC1','1.0.26',sysdate+1),
(3,'Product_Gateway_V2','V','VIIBINST(4)','SWEB_CNC1','1.0.26',sysdate+2),
(4,'Product_Gateway_V2','Q','QIIBINST1','SWEB_CNC1','1.0.26',sysdate+3);
/*Supplier_Gateway*/
insert into deployment
(deployment_id,application_name,environment,instance_name,integration_server,bar_release_id,created_time) values 
(5,'Supplier_Gateway','P','PIIBINST(4)','SWEB_CNC1','1.0.4',sysdate+4),
(6,'Supplier_Gateway','F','FIIBINST1','SWEB_CNC1','1.0.4',sysdate+5),
(7,'Supplier_Gateway','V','VIIBINST(4)','SWEB_CNC1','1.0.4',sysdate+6),
(8,'Supplier_Gateway','Q','QIIBINST1','SWEB_CNC1','1.0.3',sysdate+7);
/*Bay_Gateway*/
insert into deployment
(deployment_id,application_name,environment,instance_name,integration_server,bar_release_id,created_time) values 
(9,'Bay_Gateway','P','PIIBINST(4)','SWEB_CNC1','1.0.1',sysdate+8),
(10,'Bay_Gateway','F','FIIBINST1','TST_CNC2','1.0.10',sysdate+9),
(11,'Bay_Gateway','V','VIIBINST(1,3,4)','SWEB_CNC1','1.0.10',sysdate-1),
(12,'Bay_Gateway','Q','QIIBINST1','SWEB_CNC1','1.0.1',sysdate-2);
/*Quote_notify*/
insert into deployment
(deployment_id,application_name,environment,instance_name,integration_server,bar_release_id,created_time) values 
(13,'Quote_notify','P','PIIBINST(4)','RETL','1.0.3',sysdate-3),
(14,'Quote_notify','F','FIIBINST1','RETL','1.0.3',sysdate-4),
(15,'Quote_notify','F','FIIBINST1','TEST','-',sysdate-5),
(16,'Quote_notify','V','VIIBINST(4)','RETL','1.0.3',sysdate-6),
(17,'Quote_notify','Q','QIIBINST1','RETL','1.0.3',sysdate-7);
/*StoreTransfer_Gateway*/
insert into deployment
(deployment_id,application_name,environment,instance_name,integration_server,bar_release_id,created_time) values 
(18,'StoreTransfer_Gateway','P','PIIBINST(1,3)','SWEB_CNC1','1.0.2',sysdate-8),
(19,'StoreTransfer_Gateway','P','PIIBINST(2,4)','SWEB_CNC1','1.0.1',sysdate-9);

