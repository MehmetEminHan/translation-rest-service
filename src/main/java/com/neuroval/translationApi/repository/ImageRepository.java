package com.neuroval.translationApi.repository;

import com.neuroval.translationApi.model.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // Native query to find the recnum for a given typeName
    @Query(value = "SELECT recnum FROM image_type WHERE image_type = :typeName", nativeQuery = true)
    Integer findImageTypeRecnumByTypeName(@Param("typeName") String typeName);
}
