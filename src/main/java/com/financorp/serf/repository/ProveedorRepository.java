package com.financorp.serf.repository;

import com.financorp.serf.model.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Proveedor
 * 
 * @author FinanCorp S.A.
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    List<Proveedor> findByActivoTrue();
    
    List<Proveedor> findByPais(String pais);
}
