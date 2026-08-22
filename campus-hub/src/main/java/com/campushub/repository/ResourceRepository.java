package com.campushub.repository;

import com.campushub.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("SELECT r FROM Resource r WHERE " +
           "(:query IS NULL OR :query = '' OR LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.subjectCode) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:semester IS NULL OR r.semester = :semester) AND " +
           "(:category IS NULL OR :category = '' OR UPPER(r.category) = UPPER(:category)) " +
           "ORDER BY r.createdAt DESC")
    List<Resource> searchResources(@Param("query") String query,
                                   @Param("semester") Integer semester,
                                   @Param("category") String category);

    List<Resource> findTop10ByOrderByUpvotesDesc();

    List<Resource> findBySubjectCodeIgnoreCase(String subjectCode);

    List<Resource> findByUploader_UserIdOrderByCreatedAtDesc(Long uploaderId);
}
