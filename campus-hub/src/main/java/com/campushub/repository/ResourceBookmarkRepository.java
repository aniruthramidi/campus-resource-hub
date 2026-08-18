package com.campushub.repository;

import com.campushub.entity.Resource;
import com.campushub.entity.ResourceBookmark;
import com.campushub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceBookmarkRepository extends JpaRepository<ResourceBookmark, Long> {

    boolean existsByUserAndResource(User user, Resource resource);

    Optional<ResourceBookmark> findByUserAndResource(User user, Resource resource);

    void deleteByUserAndResource(User user, Resource resource);

    List<ResourceBookmark> findByUserOrderByCreatedAtDesc(User user);
}
