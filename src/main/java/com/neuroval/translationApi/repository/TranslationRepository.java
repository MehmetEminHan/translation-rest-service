package com.neuroval.translationApi.repository;

import com.neuroval.translationApi.model.translation.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TranslationRepository extends JpaRepository<Translation, Long> {
    // Native query to find the recnum for a given typeName
    @Query(value = "SELECT recnum FROM file_type WHERE file_type = :typeName", nativeQuery = true)
    Integer findFileTypeRecnumByTypeName(@Param("typeName") String typeName);
}
