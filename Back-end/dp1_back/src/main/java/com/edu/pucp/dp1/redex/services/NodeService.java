package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.model.Node;
import com.edu.pucp.dp1.redex.repository.NodeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {

    @Autowired
    private NodeRepository nodeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeService.class);

    public Node register(Node node) {
        try {
            return nodeRepository.save(node);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Node> getAll() {
        try {
            return nodeRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Node get(int id) {
        try {
            return nodeRepository.findNodeById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Node update(Node node) {
        try {
            return nodeRepository.save(node);
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
}
