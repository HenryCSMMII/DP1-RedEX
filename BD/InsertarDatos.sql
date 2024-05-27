/*SET time_zone = '-05:00';

INSERT INTO continent (name, activo, fecha_creacion, fecha_modificacion)
VALUES 
('América del Sur', 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Europa', 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Asia', 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'));*/

/*SET time_zone = '-05:00';

INSERT INTO country (name, shortname, continent_id, activo, fecha_creacion, fecha_modificacion)
VALUES 
('Colombia', 'COL', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Ecuador', 'ECU', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Venezuela', 'VEN', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Brasil', 'BRA', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Perú', 'PER', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Bolivia', 'BOL', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Chile', 'CHL', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Argentina', 'ARG', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Paraguay', 'PRY', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Uruguay', 'URY', 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Holanda', 'NLD', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Albania', 'ALB', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Alemania', 'DEU', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Austria', 'AUT', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Bélgica', 'BEL', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Bielorrusia', 'BLR', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Bulgaria', 'BGR', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Checa', 'CZE', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Croacia', 'HRV', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Dinamarca', 'DNK', 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('India', 'IND', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Corea del Sur', 'KOR', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Tailandia', 'THA', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Emiratos A.U', 'ARE', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('China', 'CHN', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Japón', 'JPN', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Malasia', 'MYS', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Singapur', 'SGP', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Indonesia', 'IDN', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Filipinas', 'PHL', 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'));*/

/*SET time_zone = '-05:00';

INSERT INTO city (nombre, abreviatura, zonaHoraria, id_pais, activo, fecha_creacion, fecha_modificacion)
VALUES 
('Bogotá', 'bogo', -5, 1, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Quito', 'quit', -5, 2, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Caracas', 'cara', -4, 3, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Brasilia', 'bras', -3, 4, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Lima', 'lima', -5, 5, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('La Paz', 'lapa', -4, 6, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Santiago de Chile', 'sant', -3, 7, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Buenos Aires', 'buen', -3, 8, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Asunción', 'asun', -4, 9, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Montevideo', 'mont', -3, 10, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Amsterdam', 'amst', +2, 11, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Tirana', 'tira', +2, 12, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Berlín', 'berl', +2, 13, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Viena', 'vien', +2, 14, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Bruselas', 'brus', +2, 15, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Minsk', 'mins', +3, 16, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Sofía', 'sofi', +3, 17, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Praga', 'prag', +2, 18, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Zagreb', 'zagr', +2, 19, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Copenhague', 'cope', +2, 20, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Delhi', 'delh', +5, 21, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Seúl', 'seul', +9, 22, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Bangkok', 'bang', +7, 23, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Dubai', 'emir', +4, 24, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Beijing', 'beij', +8, 25, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Tokio', 'toky', +9, 26, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Kuala Lumpur', 'kual', +8, 27, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Singapur', 'sing', +8, 28, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Yakarta', 'jaka', +7, 29, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00')),
('Manila', 'mani', +8, 30, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'));*/


/*SET time_zone = '-05:00';

INSERT INTO airport (codigoiata, latitude, longitude, capacity, activo, fecha_creacion, fecha_modificacion, city_id)
VALUES 
('SKBO', 4.7110, -74.0721, 430, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), 1),
('SEQM', -0.1807, -78.4678, 410, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), 2),
('SVMI', 10.4806, -66.9036, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-04:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-04:00'), 3),
('SBBR', -15.7942, -47.8822, 480, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-03:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-03:00'), 4),
('SPIM', -12.0464, -77.0428, 440, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-05:00'), 5),
('SLLP', -16.5000, -68.1500, 420, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-04:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-04:00'), 6),
('SCEL', -33.4489, -70.6693, 460, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-03:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-03:00'), 7),
('SABE', -34.6037, -58.3816, 460, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-03:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-03:00'), 8),
('SGAS', -25.2637, -57.5759, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-04:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-04:00'), 9),
('SUAA', -34.9011, -56.1645, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '-03:00'), CONVERT_TZ(NOW(), @@session.time_zone, '-03:00'), 10),
('EHAM', 52.3676, 4.9041, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), 11),
('LATI', 41.3275, 19.8187, 410, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), 12),
('EDDI', 52.5200, 13.4050, 480, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), 13),
('LOWW', 48.2082, 16.3738, 430, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), 14),
('EBCI', 50.8503, 4.3517, 440, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), 15),
('UMMS', 53.9045, 27.5615, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+03:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+03:00'), 16),
('LBSF', 42.6977, 23.3219, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+03:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+03:00'), 17),
('LKPR', 50.0755, 14.4378, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), 18),
('LDZA', 45.8150, 15.9819, 420, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), 19),
('EKCH', 55.6761, 12.5683, 480, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+02:00'), 20),
('VIDP', 28.7041, 77.1025, 480, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+05:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+05:00'), 21),
('RKSI', 37.5665, 126.9780, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+09:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+09:00'), 22),
('VTBS', 13.7563, 100.5018, 420, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+07:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+07:00'), 23),
('OMDB', 25.276987, 55.296249, 420, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+04:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+04:00'), 24),
('ZBAA', 39.9042, 116.4074, 480, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+08:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+08:00'), 25),
('RJTT', 35.6895, 139.6917, 460, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+09:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+09:00'), 26),
('WMKK', 3.1390, 101.6869, 420, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+08:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+08:00'), 27),
('WSSS', 1.3521, 103.8198, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+08:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+08:00'), 28),
('WIII', -6.2088, 106.8456, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+07:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+07:00'), 29),
('RPLL', 14.5995, 120.9842, 400, 1, CONVERT_TZ(NOW(), @@session.time_zone, '+08:00'), CONVERT_TZ(NOW(), @@session.time_zone, '+08:00'), 30);
*/
