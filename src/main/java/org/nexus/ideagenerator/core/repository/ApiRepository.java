package org.nexus.ideagenerator.core.repository;

import org.nexus.ideagenerator.core.models.Api;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiRepository extends JpaRepository<Api, Long> {
    List<Api> findAllByCategory(String category);
}
