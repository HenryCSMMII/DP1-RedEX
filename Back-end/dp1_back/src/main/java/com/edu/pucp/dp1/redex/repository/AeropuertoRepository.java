package com.edu.pucp.dp1.redex.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AeropuertoRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public AeropuertoRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }




    private final AeropuertoMapper aeropuertoMapper = new AeropuertoMapper();

    public List<Airport> listarAeropuertos() {
        String procedureCall = "{call dbSanama.()};";
        return jdbcTemplate.query(procedureCall, aeropuertoMapper);
    }

    private static class AeropuertoMapper implements RowMapper<Airport> {
        @Override
        public Airport mapRow(ResultSet rs, int rowNum) throws SQLException {

            Airport airport = new Airport();

            airport.setCode(rs.getString("codigoIATA"));
            airport.setCity(rs.getString("ciudad"));
            airport.setCountry(rs.getString("pais"));
            airport.setCountry(rs.getString("continente"));
            airport.setLatitude(rs.getDouble("latitud"));
            airport.setLongitude(rs.getDouble("longitud"));
            airport.setTimezoneOffset(rs.getInt("husoHorario"));
            airport.setTimezoneOffset(rs.getInt("capacidad"));

            return airport;
        }

    }





}
