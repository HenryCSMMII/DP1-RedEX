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

import com.edu.pucp.dp1.redex.model.Node;
import com.edu.pucp.dp1.redex.services.NodeService;

@RestController
@RequestMapping("/nodes")
@CrossOrigin
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @GetMapping(value = "/")
    public List<Node> getAll() {
        return nodeService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Node get(@PathVariable int id) {
        return nodeService.get(id);
    }

    @PostMapping(value = "/")
    public Node register(@RequestBody Node node) throws SQLException {
        return nodeService.register(node);
    }

    @PutMapping(value = "/")
    public Node update(@RequestBody Node node) throws SQLException {
        return nodeService.update(node);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        nodeService.delete(id);
    }
}
