package com.eg.yatc.auth.repo;




import com.eg.yatc.auth.entity.AppIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppIdentityRepo extends JpaRepository<AppIdentity, Long> {

    Optional<AppIdentity> findByUsername(String username);
//    @Query(value = "SELECT new com.eg.yafi.projection.ReadThread(t.id AS id, t.topic.id AS topicId, t.content AS content, t.appUser.username AS username)" +
//            "    FROM Thread t" +
//            "    WHERE t.id = :threadId")
//public ReadUser(Long id, String username, String role, boolean enabled)
//    @Query(value = "SELECT new com.eg.yafi.projection.ReadUser(a.id AS id, a.username AS username, a.password AS password," +
//                    "   a.role AS role, a.enabled AS enabled)" +
//                    "       FROM AppUser a" +
//                    "       WHERE a.username = :username")
//    ReadUser findOneByUsernameRO(@Param("username")String username);
}
