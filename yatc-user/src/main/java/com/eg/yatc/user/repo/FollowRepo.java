package com.eg.yatc.user.repo;


import com.eg.yatc.user.entity.AppUser;
import com.eg.yatc.user.entity.Follow;
import com.eg.yatc.user.resp.ReadFollowerResp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepo extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFolloweeId(long followerId, long followeeId);


    @Query(value = """
        SELECT new com.eg.yatc.user.resp.ReadFollowerResp(f.followee.id AS userId, f.followee.username AS username)
        FROM Follow f
        WHERE f.followee.id = :followeeId
    """)
    List<ReadFollowerResp> findAllByFolloweeId(long followeeId);

    //Optional<AppUser> findByUsername(String username);
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
