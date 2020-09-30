
/* crear Departamentos */
insert into departamentos (nombre_depto, descripcion) values('Gerencia de Administración y Finanzas','Departamento encargado de finanzas');
insert into departamentos (nombre_depto, descripcion) values('Gerencia de Planificación, Seguimiento y Evaluación','Departamento encargado de la planificación');
insert into departamentos (nombre_depto, descripcion) values('Unidad de Mantenimiento de Sistemas','Departamento encargado del area de sistemas');
insert into departamentos (nombre_depto, descripcion) values('Departamento de Comunicaciones','Departamento encargado del area de comunicaciones');


/* crear usuarios */
insert into users (username, password, enabled, nombr_compl, create_at, id_depto, cargo) values('chris','$2a$10$cUvviAMlaq1S0UlNLZzZO.RTARGMwCShZSq8TXuZ1gNdl18FwEpR.',1, 'Christian Sanchez',now(),3,'Asistente');
insert into users (username, password, enabled, nombr_compl, create_at, id_depto, cargo) values('amhon','$2a$10$repb9K7F5IXUNptuA7ITVet6M54hvdMVWutRXQ2gwBAyGjk5EeaY.',1, 'amhon institucion',now(),1,'Gerente Financiero');
insert into users (username, password, enabled, nombr_compl, create_at, id_depto, cargo) values('test','$2a$10$repb9K7F5IXUNptuA7ITVet6M54hvdMVWutRXQ2gwBAyGjk5EeaY.',0, 'Prueba Probado',now(),3,'Asistente De comunicaciones');


/* crear roles*/
insert into roles (user_id, authority) values(1,'ROLE_Super_ADMIN');
insert into roles (user_id, authority) values(2,'ROLE_USER');
insert into roles (user_id, authority) values(3,'ROLE_USER');


insert into facturas(descripcion, observacion, cliente_id, create_at) values('Factura Bicicleta', 'Alguna nota importante!' , 1, now());
insert into facturas_items (cantidad, factura_id, producto_id) values(3,2,6);
--insercion de unidades de medida
insert into unidades_medida (nombre_unid,  descripcion) values('Caja Grande','Caja de 12 unidades');
insert into unidades_medida (nombre_unid,  descripcion) values('Individual','unidad individual');
insert into unidades_medida (nombre_unid,  descripcion) values('Resma','Conjunto de 500 unidades de papel');

--insercion de categorias
insert into categorias (nombre, descripcion) values('Tintas y toner','Categoria para las tintas y toner de impresion');
insert into categorias (nombre, descripcion) values('Oficina','Categoria para los insumos de oficina');
insert into categorias (nombre, descripcion) values('Consumible','Categoria para los insumos consumibles');


--insercion de proveedores

insert into proveedores (nombre_prov, tel_prov, direccion, email, observacion) values('GBM','2556-5531','Centro Morazan Boulevar Morazán, Tegucigalpa', 'mercadeo@gbm.net','Excelente Proveedor de equipo informatico');
insert into proveedores (nombre_prov, tel_prov, direccion, email, observacion) values('Office Depot','2216-4999','Avenida Roble, Honduras', '	sclienteshn@officedepot.com.hn','Excelente Proveedor de Insumos de Oficina');

insert into articulos (nombre,  cod_barra, observacion, stock_min, req_max, id_unidad, id_categ, existencia, precio_prom, ultimo_precio, costo_total) values('Hp tinta',  '123846', 'Nueva sellada', 5, 2, '2', '1', 0, 0, 0, 0)
insert into articulos (nombre,  cod_barra, observacion, stock_min, req_max, id_unidad, id_categ, existencia, precio_prom, ultimo_precio, costo_total) values('Resma Papel Chamex',  '123846sa', '500 Hojas', 5, 2, '3', '2', 0, 0, 0, 0)


insert into isv(valor) values(1.15);
