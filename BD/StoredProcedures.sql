-- Drop procedures if they exist
DROP PROCEDURE IF EXISTS ListarAeropuertos;
DROP PROCEDURE IF EXISTS ListarPaquetesxEnvio;
DROP PROCEDURE IF EXISTS ListarPaquetesxVuelo;
DROP PROCEDURE IF EXISTS ListarHistorialPaquetes;
DROP PROCEDURE IF EXISTS RegistrarVuelos;
DROP PROCEDURE IF EXISTS RegistrarPaquetes;
DROP PROCEDURE IF EXISTS RegistrarEnvios;
DROP PROCEDURE IF EXISTS RegistrarHistorialDePaquete;

-- Procedimiento ListarAeropuertos
DELIMITER $$
CREATE PROCEDURE ListarAeropuertos()
BEGIN
    SELECT * FROM Aeropuerto;
END $$
DELIMITER ;

-- Procedimiento ListarPaquetesxEnvio
DELIMITER $$
CREATE PROCEDURE ListarPaquetesxEnvio(IN envioId INT)
BEGIN
    SELECT p.* 
    FROM Paquete p
    INNER JOIN Envio e ON p.id = e.idPaquete
    WHERE e.id = envioId;
END $$
DELIMITER ;

-- Procedimiento ListarPaquetesxVuelo
DELIMITER $$
CREATE PROCEDURE ListarPaquetesxVuelo(IN vueloId INT)
BEGIN
    SELECT p.* 
    FROM Paquete p
    INNER JOIN Paquete_Vuelo pv ON p.id = pv.idPaquete
    WHERE pv.idVuelo = vueloId;
END $$
DELIMITER ;

-- Procedimiento ListarHistorialPaquetes
DELIMITER $$
CREATE PROCEDURE ListarHistorialPaquetes(IN paqueteId INT)
BEGIN
    SELECT pv.*, v.*
    FROM Paquete_Vuelo pv
    INNER JOIN Vuelo v ON pv.idVuelo = v.id
    WHERE pv.idPaquete = paqueteId;
END $$
DELIMITER ;

-- Procedimiento RegistrarVuelos
DELIMITER $$
CREATE PROCEDURE RegistrarVuelos(
    IN fechaInicio DATETIME,
    IN fechaFin DATETIME,
    IN horaPartidaCalculada TIME,
    IN horaPartidaMedida TIME,
    IN ciudadOrigenId INT,
    IN ciudadDestinoId INT,
    IN estado ENUM('PENDIENTE', 'EN RUTA', 'FINALIZADO'),
    IN capacidad INT
)
BEGIN
    INSERT INTO Vuelo (fechaInicio, fechaFin, horaPartidaCalculada, horaPartidaMedida, id_ciudadOrigen, id_ciudadDestino, estado, capacidad)
    VALUES (fechaInicio, fechaFin, horaPartidaCalculada, horaPartidaMedida, ciudadOrigenId, ciudadDestinoId, estado, capacidad);
END $$
DELIMITER ;

-- Procedimiento RegistrarPaquetes
DELIMITER $$
CREATE PROCEDURE RegistrarPaquetes(
    IN codigo VARCHAR(100),
    IN estado ENUM('EN ESPERA', 'EN CAMINO', 'ENTREGADO')
)
BEGIN
    INSERT INTO Paquete (codigo, estado)
    VALUES (codigo, estado);
END $$
DELIMITER ;

-- Procedimiento RegistrarEnvios
DELIMITER $$
CREATE PROCEDURE RegistrarEnvios(
    IN cantidad INT,
    IN origenId INT,
    IN destinoId INT,
    IN paqueteId INT,
    IN peso DOUBLE,
    IN fechaInicio DATETIME,
    IN fechaFin DATETIME,
    IN tiempoActivo DOUBLE
)
BEGIN
    INSERT INTO Envio (cantidad, idOrigen, idDestino, idPaquete, peso, fechaInicio, fechaFin, tiempoActivo)
    VALUES (cantidad, origenId, destinoId, paqueteId, peso, fechaInicio, fechaFin, tiempoActivo);
END $$
DELIMITER ;

-- Procedimiento RegistrarHistorialDePaquete
DELIMITER $$
CREATE PROCEDURE RegistrarHistorialDePaquete(
    IN paqueteId INT,
    IN vueloId INT
)
BEGIN
    INSERT INTO Paquete_Vuelo (idPaquete, idVuelo)
    VALUES (paqueteId, vueloId);
END $$
DELIMITER ;