package com.edu.pucp.dp1.redex.repository;
import com.edu.pucp.dp1.redex.model.Client;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Integer> {

    public List<Client> findAll();
    public Client findClientById(int id);
}
