INSERT INTO Pais (nombre, abreviatura, id_continente) VALUES ('Estados Unidos', 'USA', 1);
INSERT INTO Pais (nombre, abreviatura, id_continente) VALUES ('España', 'ESP', 2);
INSERT INTO Ciudad (nombre, abreviatura, zonaHoraria, id_pais) VALUES ('Nueva York', 'NY', 'EST', 1);
INSERT INTO Ciudad (nombre, abreviatura, zonaHoraria, id_pais) VALUES ('Madrid', 'MAD', 'CET', 2);
INSERT INTO ParticulaNodo (latitud, longitud) VALUES (40.7128, -74.0060);
INSERT INTO ParticulaNodo (latitud, longitud) VALUES (40.4168, -3.7038);
INSERT INTO Aeropuerto (latitud, longitud, capacidad, codigoIATA, id_ciudad) VALUES (40.6413, -73.7781, 60000, 'JFK', 1);
INSERT INTO Aeropuerto (latitud, longitud, capacidad, codigoIATA, id_ciudad) VALUES (40.4918, -3.5695, 45000, 'MAD', 2);
INSERT INTO Paquete (codigo, estado) VALUES ('PKG12345', 'EN ESPERA');
INSERT INTO Paquete (codigo, estado) VALUES ('PKG67890', 'EN CAMINO');
INSERT INTO Vuelo (fechaInicio, fechaFin, horaPartidaCalculada, horaPartidaMedida, id_ciudadOrigen, id_ciudadDestino, estado, capacidad)
VALUES ('2023-01-01 10:00:00', '2023-01-01 12:00:00', '10:00:00', '10:05:00', 1, 2, 'PENDIENTE', 100);
INSERT INTO Envio (cantidad, idOrigen, idDestino, idPaquete, peso, fechaInicio, fechaFin, tiempoActivo)
VALUES (10, 1, 2, 1, 5.5, '2023-01-01 08:00:00', '2023-01-01 10:00:00', 2.0);
INSERT INTO Paquete_Vuelo (idPaquete, idVuelo) VALUES (1, 1);
INSERT INTO Paquete_Vuelo (idPaquete, idVuelo) VALUES (2, 1);

CALL ListarAeropuertos();
CALL ListarPaquetesxEnvio(1);
CALL ListarPaquetesxVuelo(1);
CALL ListarHistorialPaquetes(1);

CALL RegistrarVuelos(
    '2023-01-02 10:00:00',
    '2023-01-02 12:00:00',
    '10:00:00',
    '10:05:00',
    1,
    2,
    'PENDIENTE',
    100
);
CALL RegistrarPaquetes(
    'COD67890',
    'EN ESPERA'
);
CALL RegistrarEnvios(
    15,
    1,
    2,
    2,
    7.5,
    '2023-01-02 08:00:00',
    '2023-01-02 10:00:00',
    2.0
);
CALL RegistrarHistorialDePaquete(
    2,
    1
);
