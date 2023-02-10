package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VResponseField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VResponseField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VResponseFieldRepository extends JpaRepository<VResponseField, Long> {}
