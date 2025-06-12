CREATE DATABASE Cafeteria_BD

USE Cafeteria_BD
GO

CREATE TABLE Users(
	id INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
	name VARCHAR (100) NULL,
	passwordHash VARCHAR (64) NOT NULL,
	email VARCHAR (200) NOT NULL UNIQUE,
	status TINYINT NOT NULL
)
GO

-- 'precio' ya es 'float' que es doble precisión en SQL Server
CREATE TABLE menuss(
	id_codpro INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
	nompro varchar (40) NULL,
	precio float NULL,
	categoria varchar(40) NULL,
	tipo varchar(40) NULL
)
GO

CREATE TABLE vent(
	idventa int primary key IDENTITY(1,1) NOT NULL,
	nomProduct int NULL,
	precioProduct float NULL,
	nombreClient VARCHAR(100) NULL,
	estado nchar(10) NULL,
	fecha datetime NULL
) ;

ALTER TABLE menuss
ALTER COLUMN 	id INT PRIMARY KEY IDENTITY(1,1) NOT NULL;

CREATE TABLE Employees (
    id INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(50) NULL,
    salary FLOAT NULL,
    phoneNumber VARCHAR(20) NULL,
    addressemployess VARCHAR(255) NULL,
   );


   CREATE TABLE Orders (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL,
    TotalAmount DECIMAL(10,2) NOT NULL,
    OrderDate DATETIME NOT NULL
);
