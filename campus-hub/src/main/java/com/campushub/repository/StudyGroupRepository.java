package com.campushub.repository;

import com.campushub.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    List<StudyGroup> findAllByOrderByCreatedAtDesc();

    List<StudyGroup> findBySubjectContainingIgnoreCaseOrderByCreatedAtDesc(String subject);
}
