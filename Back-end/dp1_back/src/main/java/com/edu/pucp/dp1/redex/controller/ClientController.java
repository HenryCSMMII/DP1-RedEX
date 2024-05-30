package com.edu.pucp.dp1.redex.controller;

import com.edu.pucp.dp1.redex.dto.ClientDTO;
import com.edu.pucp.dp1.redex.services.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/client")
@CrossOrigin
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/")
    List<ClientDTO> getAll(){
        return clientService.getAll();
    }

    @GetMapping(value = "/{id}")
    ClientDTO get(@PathVariable int id){
        return clientService.get(id);
    }

    @PostMapping(value = "/")
    ClientDTO register(@RequestBody ClientDTO clientDTO) throws SQLException{
        return clientService.register(clientDTO);
    }

    @PutMapping(value = "/")
    ClientDTO update(@RequestBody ClientDTO clientDTO) throws SQLException{
        return clientService.update(clientDTO);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        clientService.delete(id);
    }
}
