-- noinspection SqlNoDataSourceInspectionForFile
INSERT INTO retailproduct(id, code, name, price, stock) VALUES (nextval('hibernate_sequence'), 'c123','Skirt Party','50.0','20');
INSERT INTO retailproduct(id, code, name, price, stock) VALUES (nextval('hibernate_sequence'), 'c456','Pants Party','20.0','10');
INSERT INTO retailproduct(id, code, name, price, stock) VALUES (nextval('hibernate_sequence'), 'c789','Dress Party','90.0','20');
INSERT INTO customer(id, name, email, phone, country) VALUES (nextval('hibernate_sequence'), 'Maria Rosario Frechilla','mfrechilla@quarkus.io','+34 666','Spain');