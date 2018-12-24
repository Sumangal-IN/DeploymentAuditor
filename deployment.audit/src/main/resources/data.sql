delete from deployment;
/*Product_Gateway_V2*/
insert into deployment
(deployment_id,application_name,environment,integration_server,instance_name,bar_release_id) values
(1,'Product_Gateway_V2','P','PIIBINST(4)','SWEB_CNC1','1.0.25'),
(2,'Product_Gateway_V2','F','FIIBINST1','SWEB_CNC1','1.0.26'),
(3,'Product_Gateway_V2','V','VIIBINST(4)','SWEB_CNC1','1.0.26'),
(4,'Product_Gateway_V2','Q','QIIBINST1','SWEB_CNC1','1.0.26');
/*Supplier_Gateway*/
insert into deployment
(deployment_id,application_name,environment,integration_server,instance_name,bar_release_id) values 
(5,'Supplier_Gateway','P','PIIBINST(4)','SWEB_CNC1','1.0.4'),
(6,'Supplier_Gateway','F','FIIBINST1','SWEB_CNC1','1.0.4'),
(7,'Supplier_Gateway','V','VIIBINST(4)','SWEB_CNC1','1.0.4'),
(8,'Supplier_Gateway','Q','QIIBINST1','SWEB_CNC1','1.0.3');