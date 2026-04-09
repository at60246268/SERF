package com.financorp.serf.repository;

import com.financorp.serf.model.entities.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Filial
 * 
 * @author FinanCorp S.A.
 */
@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
    
    List<Filial> findByActivoTrue();
    
    List<Filial> findByPais(String pais);
}
