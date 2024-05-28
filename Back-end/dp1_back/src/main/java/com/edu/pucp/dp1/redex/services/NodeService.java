package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.dto.NodeDTO;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.Node;
import com.edu.pucp.dp1.redex.repository.AirportRepository;
import com.edu.pucp.dp1.redex.repository.FlightRepository;
import com.edu.pucp.dp1.redex.repository.NodeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NodeService {

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeService.class);

    public NodeDTO register(NodeDTO nodeDTO) {
        try {
            Node node = convertToEntity(nodeDTO);
            node = nodeRepository.save(node);
            return convertToDTO(node);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<NodeDTO> getAll() {
        try {
            return nodeRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public NodeDTO get(int id) {
        try {
            Node node = nodeRepository.findNodeById(id);
            return node != null ? convertToDTO(node) : null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public NodeDTO update(NodeDTO nodeDTO) {
        try {
            Node node = convertToEntity(nodeDTO);
            node = nodeRepository.save(node);
            return convertToDTO(node);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id) {
        try {
            nodeRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private NodeDTO convertToDTO(Node node) {
        return new NodeDTO(
                node.getId(),
                node.getLatitud(),
                node.getLongitud(),
                node.getAirport().getId(),
                node.getFlight().getId()
        );
    }

    private Node convertToEntity(NodeDTO nodeDTO) {
        Node node = new Node();
        node.setId(nodeDTO.getId());
        node.setLatitud(nodeDTO.getLatitud());
        node.setLongitud(nodeDTO.getLongitud());
        Airport airport = airportRepository.findById(nodeDTO.getAirportId()).orElseThrow(() -> new RuntimeException("Airport not found"));
        Flight flight = flightRepository.findById(nodeDTO.getFlightId()).orElseThrow(() -> new RuntimeException("Flight not found"));
        node.setAirport(airport);
        node.setFlight(flight);
        return node;
    }
}
