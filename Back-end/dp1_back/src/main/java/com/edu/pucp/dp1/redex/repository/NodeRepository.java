package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NodeRepository extends JpaRepository<Node, Integer> {
    Node findNodeById(int id);
}
