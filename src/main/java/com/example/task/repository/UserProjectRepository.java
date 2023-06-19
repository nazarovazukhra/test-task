package com.example.task.repository;

import com.example.task.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    List<UserProject> findByProjectId(Long projectId);

    List<UserProject> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query(value = "delete from UserProject up where (up.user.id=?1 and up.project.id=?2)")
    int deleteUserFromProject(Long userId, Long projectId);


}
