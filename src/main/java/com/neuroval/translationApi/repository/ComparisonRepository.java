package com.neuroval.translationApi.repository;

import com.neuroval.translationApi.model.comparison.Comparison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComparisonRepository extends JpaRepository<Comparison, Long> {
}
