package com.eg.yatc.core.repo;


import com.eg.yatc.core.entity.FollowProjection;
import com.eg.yatc.core.entity.Tweet;
import com.eg.yatc.core.projection.ReadTweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowProjectionRepo extends JpaRepository<FollowProjection, Long> {
    boolean existsByFollowerIdAndFolloweeId(long followerId, long followeeId);

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

//    @Query(value = """
//        SELECT new com.eg.yatc.core.projection.ReadTweet(t.id AS id, t.content AS content, up.id AS userId, up.username AS username)
//        FROM Tweet t
//        INNER JOIN UserProjection up ON t.userProjection.id = up.id
//        WHERE t.id = :tweetId
//    """)
//    ReadTweet findOneByIdRO(@Param("tweetId")long tweetId);
}
