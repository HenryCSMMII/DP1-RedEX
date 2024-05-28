package com.edu.pucp.dp1.redex.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.dto.NodeDTO;
import com.edu.pucp.dp1.redex.services.NodeService;

@RestController
@RequestMapping("/nodes")
@CrossOrigin
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @GetMapping(value = "/")
    public List<NodeDTO> getAll() {
        return nodeService.getAll();
    }

    @GetMapping(value = "/{id}")
    public NodeDTO get(@PathVariable int id) {
        return nodeService.get(id);
    }

    @PostMapping(value = "/")
    public NodeDTO register(@RequestBody NodeDTO nodeDTO) throws SQLException {
        return nodeService.register(nodeDTO);
    }

    @PutMapping(value = "/")
    public NodeDTO update(@RequestBody NodeDTO nodeDTO) throws SQLException {
        return nodeService.update(nodeDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        nodeService.delete(id);
    }
}
