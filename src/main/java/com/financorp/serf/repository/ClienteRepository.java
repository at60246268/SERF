package com.financorp.serf.repository;

import com.financorp.serf.model.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Cliente
 * 
 * @author FinanCorp S.A.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByEmail(String email);
    
    List<Cliente> findByActivoTrue();
    
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
