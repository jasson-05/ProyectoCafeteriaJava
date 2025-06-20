CREATE DATABASE Cafeteria_BD

USE Cafeteria_BD
GO

CREATE TABLE datosclientes(
	iddcliente int IDENTITY(1,1) NOT NULL,
	nombre nvarchar(50) NULL,
	apellido nvarchar(50) NULL,
	correo nvarchar(50) NULL
)
GO

-- 'precio' ya es 'float' que es doble precisión en SQL Server
CREATE TABLE menuss(
	codpro int NOT NULL,
	nompro varchar (40) NULL,
	precio float NULL,
	categoria varchar(40) NULL,
	tipo varchar(40) NULL,
	estado nchar(10) NULL
)
GO

CREATE TABLE vent(
	idventa int IDENTITY(1,1) NOT NULL,
	idprod int NULL,
	precio float NULL,
	correo nvarchar(50) NULL,
	estado nchar(10) NULL,
	fecha datetime2 NULL
)
GO