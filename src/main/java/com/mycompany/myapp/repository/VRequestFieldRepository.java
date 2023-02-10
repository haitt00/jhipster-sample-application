package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VRequestField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VRequestField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VRequestFieldRepository extends JpaRepository<VRequestField, Long> {}
