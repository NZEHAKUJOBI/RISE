package com.matchrate.RISE.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.matchrate.RISE.model.Analysis;

public interface AnalysisRepository extends JpaRepository <Analysis, UUID> {
}



