delete from deployment;
/*Product_Gateway_V2*/
insert into deployment
(deployment_id,application_name,environment,integration_server,instance_name,bar_release_id,created_time) values
(1,'Product_Gateway_V2','P','PIIBINST(4)','SWEB_CNC1','1.0.25',sysdate),
(2,'Product_Gateway_V2','F','FIIBINST1','SWEB_CNC1','1.0.26',sysdate),
(3,'Product_Gateway_V2','V','VIIBINST(4)','SWEB_CNC1','1.0.26',sysdate),
(4,'Product_Gateway_V2','Q','QIIBINST1','SWEB_CNC1','1.0.26',sysdate);
/*Supplier_Gateway*/
insert into deployment
(deployment_id,application_name,environment,integration_server,instance_name,bar_release_id,created_time) values 
(5,'Supplier_Gateway','P','PIIBINST(4)','SWEB_CNC1','1.0.4',sysdate),
(6,'Supplier_Gateway','F','FIIBINST1','SWEB_CNC1','1.0.4',sysdate),
(7,'Supplier_Gateway','V','VIIBINST(4)','SWEB_CNC1','1.0.4',sysdate),
(8,'Supplier_Gateway','Q','QIIBINST1','SWEB_CNC1','1.0.3',sysdate),
(9,'Product_Gateway_V2','P','PIIBINST(4)','SWEB_CNC1','1.0.26',sysdate+1);