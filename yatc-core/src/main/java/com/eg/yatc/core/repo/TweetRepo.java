package com.eg.yatc.core.repo;


import com.eg.yatc.core.entity.Tweet;
import com.eg.yatc.core.projection.ReadTweet;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TweetRepo extends JpaRepository<Tweet, Long> {

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

    @Query(value = """
        SELECT new com.eg.yatc.core.projection.ReadTweet(t.id AS id, t.content AS content, up.id AS userId, up.username AS username)
        FROM Tweet t
        INNER JOIN UserProjection up ON t.userId = up.id
        WHERE t.id = :tweetId
    """)
    ReadTweet findOneByIdRO(@Param("tweetId") long tweetId);

    @Query(value = """
        SELECT new com.eg.yatc.core.projection.ReadTweet(t.id AS id, t.content AS content, t.userId AS userId, up.username AS username)
        FROM Tweet t
        INNER JOIN FollowProjection fp ON t.userId = fp.followeeId
        INNER JOIN UserProjection up ON t.userId = up.id
        WHERE fp.followerId = :followerId
        ORDER BY t.id DESC
    """)
    List<ReadTweet> getTimeline(@Param("followerId")long followerId, Pageable pageRequest);
}
