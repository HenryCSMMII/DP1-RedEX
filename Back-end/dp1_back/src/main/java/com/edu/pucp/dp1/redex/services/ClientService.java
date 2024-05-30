package com.edu.pucp.dp1.redex.services;
import com.edu.pucp.dp1.redex.dto.ClientDTO;
import com.edu.pucp.dp1.redex.model.Client;
import com.edu.pucp.dp1.redex.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    public ClientDTO register(ClientDTO clientDTO){
        try {
            Client client = convertToEntity(clientDTO);
            Client savedClient = clientRepository.save(client);
            return convertToDTO(savedClient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public List<ClientDTO> getAll(){
        try {
            return clientRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public ClientDTO get(int id){
        try {
            Client client = clientRepository.findClientById(id);
            return convertToDTO(client);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public ClientDTO update(ClientDTO clientDTO){
        try {
            Client client = convertToEntity(clientDTO);
            Client updatedClient = clientRepository.save(client);
            return convertToDTO(updatedClient);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void delete(int id){
        try {
            clientRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private ClientDTO convertToDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getFullName(),
                client.getDocType(),
                client.getDocNumber(),
                client.getEmail(),
                client.getCellphone()
        );
    }

    private Client convertToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setFullName(clientDTO.getFullName());
        client.setDocType(clientDTO.getDocType());
        client.setDocNumber(clientDTO.getDocNumber());
        client.setEmail(clientDTO.getEmail());
        client.setCellphone(clientDTO.getCellphone());
        return client;
    }
}
