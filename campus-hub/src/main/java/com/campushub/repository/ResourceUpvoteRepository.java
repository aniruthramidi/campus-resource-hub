package com.campushub.repository;

import com.campushub.entity.Resource;
import com.campushub.entity.ResourceUpvote;
import com.campushub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceUpvoteRepository extends JpaRepository<ResourceUpvote, Long> {

    boolean existsByUserAndResource(User user, Resource resource);

    Optional<ResourceUpvote> findByUserAndResource(User user, Resource resource);

    void deleteByUserAndResource(User user, Resource resource);

    int countByResource(Resource resource);
}
