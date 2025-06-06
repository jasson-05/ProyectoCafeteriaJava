USE [master]
GO

CREATE DATABASE [Cafeteria_BD]

ALTER DATABASE [Cafeteria_BD] SET COMPATIBILITY_LEVEL = 160
GO

IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Cafeteria_BD].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO

ALTER DATABASE [Cafeteria_BD] SET ANSI_NULL_DEFAULT OFF
GO
ALTER DATABASE [Cafeteria_BD] SET ANSI_NULLS OFF
GO
ALTER DATABASE [Cafeteria_BD] SET ANSI_PADDING OFF
GO
ALTER DATABASE [Cafeteria_BD] SET ANSI_WARNINGS OFF
GO
ALTER DATABASE [Cafeteria_BD] SET ARITHABORT OFF
GO
ALTER DATABASE [Cafeteria_BD] SET AUTO_CLOSE ON
GO
ALTER DATABASE [Cafeteria_BD] SET AUTO_SHRINK OFF
GO
ALTER DATABASE [Cafeteria_BD] SET AUTO_UPDATE_STATISTICS ON
GO
ALTER DATABASE [Cafeteria_BD] SET CURSOR_CLOSE_ON_COMMIT OFF
GO
ALTER DATABASE [Cafeteria_BD] SET CURSOR_DEFAULT  GLOBAL
GO
ALTER DATABASE [Cafeteria_BD] SET CONCAT_NULL_YIELDS_NULL OFF
GO
ALTER DATABASE [Cafeteria_BD] SET NUMERIC_ROUNDABORT OFF
GO
ALTER DATABASE [Cafeteria_BD] SET QUOTED_IDENTIFIER OFF
GO
ALTER DATABASE [Cafeteria_BD] SET RECURSIVE_TRIGGERS OFF
GO
ALTER DATABASE [Cafeteria_BD] SET  ENABLE_BROKER
GO
ALTER DATABASE [Cafeteria_BD] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO
ALTER DATABASE [Cafeteria_BD] SET DATE_CORRELATION_OPTIMIZATION OFF
GO
ALTER DATABASE [Cafeteria_BD] SET TRUSTWORTHY OFF
GO
ALTER DATABASE [Cafeteria_BD] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO
ALTER DATABASE [Cafeteria_BD] SET PARAMETERIZATION SIMPLE
GO
ALTER DATABASE [Cafeteria_BD] SET READ_COMMITTED_SNAPSHOT OFF
GO
ALTER DATABASE [Cafeteria_BD] SET HONOR_BROKER_PRIORITY OFF
GO
ALTER DATABASE [Cafeteria_BD] SET RECOVERY SIMPLE
GO
ALTER DATABASE [Cafeteria_BD] SET  MULTI_USER
GO
ALTER DATABASE [Cafeteria_BD] SET PAGE_VERIFY CHECKSUM
GO
ALTER DATABASE [Cafeteria_BD] SET DB_CHAINING OFF
GO
ALTER DATABASE [Cafeteria_BD] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF )
GO
ALTER DATABASE [Cafeteria_BD] SET TARGET_RECOVERY_TIME = 60 SECONDS
GO
ALTER DATABASE [Cafeteria_BD] SET DELAYED_DURABILITY = DISABLED
GO
ALTER DATABASE [Cafeteria_BD] SET ACCELERATED_DATABASE_RECOVERY = OFF
GO
ALTER DATABASE [Cafeteria_BD] SET QUERY_STORE = ON
GO
ALTER DATABASE [Cafeteria_BD] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO

USE [Cafeteria_BD]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[datosclientes](
	[iddcliente] [int] IDENTITY(1,1) NOT NULL,
	[nombre] [nvarchar](50) NULL,
	[apellido] [nvarchar](50) NULL,
	[correo] [nvarchar](50) NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- 'precio' ya es 'float' que es doble precisi�n en SQL Server
CREATE TABLE [dbo].[menuss](
	[codpro] [int] NOT NULL,
	[nompro] [varchar](40) NULL,
	[precio] [float] NULL,
	[categoria] [varchar](40) NULL,
	[tipo] [varchar](40) NULL,
	[estado] [nchar](10) NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[vent](
	[idventa] [int] IDENTITY(1,1) NOT NULL,
	[idprod] [int] NULL,
	[precio] [float] NULL, 
	[correo] [nvarchar](50) NULL,
	[estado] [nchar](10) NULL,
	[fecha] [datetime2] NULL
) ON [PRIMARY]
GO

SET IDENTITY_INSERT [dbo].[datosclientes] ON

INSERT [dbo].[datosclientes] ([iddcliente], [nombre], [apellido], [correo]) VALUES (1, N'Efrain', N'Arenivar Granados', N'efraingranados.a@gmail.com')
INSERT [dbo].[datosclientes] ([iddcliente], [nombre], [apellido], [correo]) VALUES (2, N'Efrain', N'Arenivar Granados', N'efraingranados@gmail.com')
INSERT [dbo].[datosclientes] ([iddcliente], [nombre], [apellido], [correo]) VALUES (3, N'Efrain', N'Arenivar', N'efraingranados.a')
INSERT [dbo].[datosclientes] ([iddcliente], [nombre], [apellido], [correo]) VALUES (4, N'Efrain', N'Arenivar', N'efraingrandos.a@gmail.com')
INSERT [dbo].[datosclientes] ([iddcliente], [nombre], [apellido], [correo]) VALUES (5, N'EfrainEfra', N'Arenivar G', N'efrainarenivargranados@gmail.com')
INSERT [dbo].[datosclientes] ([iddcliente], [nombre], [apellido], [correo]) VALUES (6, N'Sara Jemi', N'Arenivar', N'jemiare.a@hotmail.com')
SET IDENTITY_INSERT [dbo].[datosclientes] OFF
GO

INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (1, N'Frap Fresa', 2.5, N'BFrias', N'Frap', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (2, N'Frap Caf�', 2.5, N'BFrias', N'Frap', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (3, N'Frap Mocca', 2.5, N'BFrias', N'Frap', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (4, N'Frap Caramelo', 2.5, N'BFrias', N'Frap', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (5, N'Crema Batida', 2.5, N'BFrias', N'CafHela', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (6, N'Cacao', 2.5, N'BFrias', N'CafHela', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (7, N'Canela', 2.5, N'BFrias', N'CafHela', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (8, N'CCLeche', 2.5, N'BFrias', N'CafHela', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (9, N'Froz Fresa', 2.25, N'BFrias', N'Frozen', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (10, N'Froz Mango', 2.25, N'BFrias', N'Frozen', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (11, N'Froz Caf�', 2.25, N'BFrias', N'Frozen', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (12, N'Froz Choco', 2.25, N'BFrias', N'Frozen', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (13, N'Latt Caf�', 3, N'BFrias', N'LattHela', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (14, N'Latt Dulce Leche', 3, N'BFrias', N'LattHela', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (15, N'Latt Choco', 3, N'BFrias', N'LattHela', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (16, N'Latt Fresa', 3, N'BFrias', N'LattHela', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (17, N'Match Leche', 3, N'BFrias', N'Matcha', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (18, N'Match Yogurt', 3, N'BFrias', N'Matcha', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (19, N'Match Choco', 3, N'BFrias', N'Matcha', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (20, N'Match Fruta', 3, N'BFrias', N'Matcha', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (21, N'TMazanilla', 1.5, N'BCalie', N'Te', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (22, N'TMenta', 1.5, N'BCalie', N'Te', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (23, N'TEucalipto', 1.5, N'BCalie', N'Te', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (24, N'TRomero', 1.5, N'BCalie', N'Te', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (25, N'CExpreso', 2, N'BCalie', N'Cafe', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (26, N'Flat White', 2, N'BCalie', N'Cafe', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (27, N'Americano', 2, N'BCalie', N'Cafe', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (28, N'Mocha', 2, N'BCalie', N'Cafe', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (29, N'Capuccino', 2, N'BCalie', N'Cafe', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (30, N'Caramelo Macchiato', 2, N'BCalie', N'Cafe', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (31, N'Macchiato', 2, N'BCalie', N'Cafe', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (32, N'Vainilla', 1.75, N'BCalie', N'Capuccino', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (33, N'Chocolate', 1.75, N'BCalie', N'Capuccino', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (34, N'Choco Menta', 1.75, N'BCalie', N'Capuccino', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (35, N'Cap Fresa', 1.75, N'BCalie', N'Capuccino', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (36, N'Chocolate', 1, N'BCalie', N'Chocolate', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (37, N'Choco Leche', 1, N'BCalie', N'Chocolate', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (38, N'Choco Canela Crema Bat', 1, N'BCalie', N'Chocolate', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (39, N'Choco Menta', 1, N'BCalie', N'Chocolate', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (40, N'Choco Queso', 1, N'BCalie', N'Chocolate', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (41, N'Expr Sencillo', 2.5, N'BCalie', N'Expresso', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (42, N'Ristretto', 2.5, N'BCalie', N'Expresso', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (43, N'Doppio', 2.5, N'BCalie', N'Expresso', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (44, N'Lungo', 2.5, N'BCalie', N'Expresso', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (45, N'Expr Cortado', 2.5, N'BCalie', N'Expresso', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (46, N'Expr Macchiato', 2.5, N'BCalie', N'Expresso', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (47, N'Tres Leches', 2.5, N'Postres', N'3Leches', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (48, N'Alf Marplatenses', 2.5, N'Postres', N'Alfajor', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (49, N'Alf Santafesinos', 2.5, N'Postres', N'Alfajor', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (50, N'Alf Mendosinos', 2.5, N'Postres', N'Alfajor', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (51, N'Alf Correntinos', 2.5, N'Postres', N'Alfajor', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (52, N'Flan Queso', 1.99, N'Postres', N'Flan', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (53, N'Flan Choco', 1.99, N'Postres', N'Flan', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (54, N'Flan Vainilla', 1.99, N'Postres', N'Flan', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (55, N'Flan Caf�', 1.99, N'Postres', N'Flan', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (56, N'Chocolate', 1.99, N'Postres', N'Pastel', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (57, N'Frutas', 1.99, N'Postres', N'Pastel', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (58, N'Melocot�n', 1.99, N'Postres', N'Pastel', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (59, N'Ceresas', 1.99, N'Postres', N'Pastel', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (60, N'Br Frambuesa', 1.99, N'Postres', N'Browne', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (61, N'Br Choco Blanco', 1.99, N'Postres', N'Browne', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (62, N'Pie Manzana', 3, N'Postres', N'Pie', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (63, N'Pie Lim�n', 3, N'Postres', N'Pie', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (64, N'Pie Fresa', 3, N'Postres', N'Pie', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (65, N'Pie Banana', 3, N'Postres', N'Pie', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (66, N'Chees Oreo', 4.35, N'Postres', N'Chees', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (67, N'Chees Fresa', 4.35, N'Postres', N'Chees', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (68, N'Chees Lim�n', 4.35, N'Postres', N'Chees', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (69, N'Chees Maracuya', 4.35, N'Postres', N'Chees', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (70, N'Chees Choco', 4.35, N'Postres', N'Chees', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (71, N'Sand Pollo', 4.5, N'Croissant', N'Sandwich', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (72, N'Sand Jam�n', 4.5, N'Croissant', N'Sandwich', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (73, N'Sand Queso', 4.5, N'Croissant', N'Sandwich', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (74, N'Sand Huevo', 4.5, N'Croissant', N'Sandwich', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (75, N'Croas Pollo', 4.5, N'Croissant', N'Croasandwich', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (76, N'Croas Jam�n', 4.5, N'Croissant', N'Croasandwich', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (77, N'Croas Queso', 4.5, N'Croissant', N'Croasandwich', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (78, N'Croas Huevo', 4.5, N'Croissant', N'Croasandwich', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (79, N'Pupusas FQ Rev', 0.5, N'DTipicos', N'Pupusas', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (80, N'Especialidades', 0.9, N'DTipicos', N'Pupusas', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (81, N'Combo 1', 3.5, N'DTipicos', N'PTipico1', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (82, N'Combo 2', 4.5, N'DTipicos', N'PTipico1', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (83, N'Combo 3', 5.5, N'DTipicos', N'PTipico1', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (84, N'Tamal Pollo', 0.5, N'DTipicos', N'Tamal', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (85, N'Tamal Carne', 0.5, N'DTipicos', N'Tamal', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (86, N'Tamal Pisque', 0.5, N'DTipicos', N'Tamal', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (87, N'Waffles', 1.5, N'DTipicos', N'Waffles', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (88, N'Ensalada de Pollo', 2.5, N'Ensaladas', N'EnsPollo', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (89, N'Ensalada Rusa', 2.5, N'Ensaladas', N'EnsRusa', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (90, N'Ensalada Cesar', 2.5, N'Ensaladas', N'EnsCesar', N'activo    ')
INSERT [dbo].[menuss] ([codpro], [nompro], [precio], [categoria], [tipo], [estado]) VALUES (91, N'Ensalada Original', 2.5, N'Ensaladas', N'EnsOriginal', N'activo    ')
GO

SET IDENTITY_INSERT [dbo].[vent] ON

INSERT [dbo].[vent] ([idventa], [idprod], [precio], [correo], [estado], [fecha]) VALUES (1, NULL, 13.25, N'efraingranados.a@gmail.com', N'Tarjeta   ', CAST(N'2024-09-08' AS datetime2))
INSERT [dbo].[vent] ([idventa], [idprod], [precio], [correo], [estado], [fecha]) VALUES (2, NULL, 8.75, N'efraingranados@gmail.com', N'Efectivo  ', CAST(N'2024-09-08' AS datetime2))
INSERT [dbo].[vent] ([idventa], [idprod], [precio], [correo], [estado], [fecha]) VALUES (3, NULL, 15.99, N'efrainarenivargranados@gmail.com', N'Efectivo  ', CAST(N'2024-09-09' AS datetime2))
INSERT [dbo].[vent] ([idventa], [idprod], [precio], [correo], [estado], [fecha]) VALUES (4, NULL, 10.25, N'jemiare.a@hotmail.com', N'Efectivo  ', CAST(N'2024-09-10' AS datetime2))
INSERT [dbo].[vent] ([idventa], [idprod], [precio], [correo], [estado], [fecha]) VALUES (5, NULL, 10.25, N'jemiare.a@hotmail.com', N'Tarjeta   ', CAST(N'2024-09-10' AS datetime2))
SET IDENTITY_INSERT [dbo].[vent] OFF
GO

USE [master]
GO

ALTER DATABASE [Cafeteria_BD] SET  READ_WRITE
GO