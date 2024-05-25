DROP TABLE IF EXISTS GeneticoEnjambre;
DROP TABLE IF EXISTS Envio;
DROP TABLE IF EXISTS Paquete_Vuelo;
DROP TABLE IF EXISTS Vuelo;
DROP TABLE IF EXISTS Paquete;
DROP TABLE IF EXISTS Aeropuerto;
DROP TABLE IF EXISTS ParticulaNodo;
DROP TABLE IF EXISTS Ciudad;
DROP TABLE IF EXISTS Pais;
DROP TABLE IF EXISTS Continente;

CREATE DATABASE IF NOT EXISTS RedEx5E;
USE RedEx5E;

-- Tabla Continente
CREATE TABLE IF NOT EXISTS Continente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla País
CREATE TABLE IF NOT EXISTS Pais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    abreviatura VARCHAR(10) NOT NULL,
    id_continente INT,
    FOREIGN KEY (id_continente) REFERENCES Continente(id)
);

-- Tabla Ciudad
CREATE TABLE IF NOT EXISTS Ciudad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    abreviatura VARCHAR(10) NOT NULL,
    zonaHoraria VARCHAR(50) NOT NULL,
    id_pais INT,
    FOREIGN KEY (id_pais) REFERENCES Pais(id)
);

-- Tabla ParticulaNodo
CREATE TABLE IF NOT EXISTS ParticulaNodo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitud DOUBLE NOT NULL,
    longitud DOUBLE NOT NULL
);

-- Tabla Aeropuerto
CREATE TABLE IF NOT EXISTS Aeropuerto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitud DOUBLE NOT NULL,
    longitud DOUBLE NOT NULL,
    capacidad INT NOT NULL,
    codigoIATA VARCHAR(10) NOT NULL,
    id_ciudad INT UNIQUE,
    FOREIGN KEY (id_ciudad) REFERENCES Ciudad(id)
);

-- Tabla Paquete
CREATE TABLE IF NOT EXISTS Paquete (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(100) NOT NULL,
    estado ENUM('EN ESPERA', 'EN CAMINO', 'ENTREGADO') NOT NULL
);

-- Tabla Vuelo
CREATE TABLE IF NOT EXISTS Vuelo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fechaInicio DATETIME NOT NULL,
    fechaFin DATETIME NOT NULL,
    horaPartidaCalculada TIME NOT NULL,
    horaPartidaMedida TIME NOT NULL,
    id_ciudadOrigen INT,
    id_ciudadDestino INT,
    estado ENUM('PENDIENTE', 'EN RUTA', 'FINALIZADO') NOT NULL,
    capacidad INT NOT NULL,
    FOREIGN KEY (id_ciudadOrigen) REFERENCES Ciudad(id),
    FOREIGN KEY (id_ciudadDestino) REFERENCES Ciudad(id)
);

-- Tabla intermedia para muchos a muchos entre Paquete y Vuelo
CREATE TABLE IF NOT EXISTS Paquete_Vuelo (
    idPaquete INT,
    idVuelo INT,
    PRIMARY KEY (idPaquete, idVuelo),
    FOREIGN KEY (idPaquete) REFERENCES Paquete(id),
    FOREIGN KEY (idVuelo) REFERENCES Vuelo(id)
);

-- Tabla Envio
CREATE TABLE IF NOT EXISTS Envio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    idOrigen INT,
    idDestino INT,
    idPaquete INT,
    peso DOUBLE NOT NULL,
    fechaInicio DATETIME NOT NULL,
    fechaFin DATETIME NOT NULL,
    tiempoActivo DOUBLE NOT NULL,
    FOREIGN KEY (idPaquete) REFERENCES Paquete(id),
    FOREIGN KEY (idOrigen) REFERENCES Ciudad(id),
    FOREIGN KEY (idDestino) REFERENCES Ciudad(id)
);

-- Tabla GeneticoEnjambre
CREATE TABLE IF NOT EXISTS GeneticoEnjambre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idAeropuerto INT,
    idParticulaNodo INT,
    idVuelo INT,
    idEnvio INT,
    idMiembro INT,
    idParticula INT,
    idPorGlobal INT,
    FOREIGN KEY (idAeropuerto) REFERENCES Aeropuerto(id),
    FOREIGN KEY (idParticulaNodo) REFERENCES ParticulaNodo(id),
    FOREIGN KEY (idVuelo) REFERENCES Vuelo(id),
    FOREIGN KEY (idEnvio) REFERENCES Envio(id),
    FOREIGN KEY (idMiembro) REFERENCES ParticulaNodo(id),
    FOREIGN KEY (idParticula) REFERENCES ParticulaNodo(id),
    FOREIGN KEY (idPorGlobal) REFERENCES ParticulaNodo(id)
);