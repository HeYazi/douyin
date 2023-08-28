package com.hyz.douyin.common.service;

import com.hyz.douyin.common.model.vo.UserVO;
import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;
import org.apache.catalina.User;

import java.util.List;
import java.util.Map;

/**
 * 内部用户服务
 *
 * @author HYZ
 * @date 2023/8/21 11:15
 */
public interface InnerUserService {
    /**
     * 根据 id 和 用户 id、token 获取用户数据
     *
     * @param map 地图
     * @return {@link Map}<{@link Long}, {@link UserVO}>
     */
    Map<Long, UserVO> getUserMap(Map<Long, Long> map);

    /**
     * 获取用户签证官
     *
     * @param id id
     * @return {@link UserVO}
     */
    UserVO getUserVO(Long id);

    /**
     * 获取用户列表
     *
     * @param userIds 用户id
     * @return {@link Map}<{@link Long}, {@link UserVO}>
     */
    Map<Long, UserVO> getUserByList(List<Long> userIds);

    /**
     * 根据 id 和 用户 id、token 获取用户数据
     *
     * @param map   地图
     * @param token 令牌
     * @return {@link Map}<{@link Long}, {@link UserVO}>
     */
    Map<Long, UserVO> getUserMap(Map<Long, Long> map, String token);

    /**
     * 获取用户签证官
     *
     * @param token 令牌
     * @param id    id
     * @return {@link UserVO}
     */
    UserVO getUserVO(String token, Long id);

    /**
     * 获取用户列表
     *
     * @param token   令牌
     * @param userIds 用户id
     * @return {@link Map}<{@link Long}, {@link UserVO}>
     */
    Map<Long, UserVO> getUserByList(String token, List<Long> userIds);

    /**
     * 更新获赞数量
     *
     * @param count  数
     * @param userId 用户id
     * @return {@link Boolean}
     */
    Boolean updateTotalFavorited(Integer count, Long userId);

    /**
     * 用户关系动作（关注与取关）
     *
     * @param userId   用户id
     * @param toUserId 用户id
     * @param type     类型
     * @return {@link Boolean}
     */
    Boolean relationAction(Long userId, Long toUserId, Integer type);

    /**
     * 获取关注用户列表
     *
     * @param userIds 用户id
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> getFollowUserList(List<Long> userIds);

    /**
     * 获取关注用户列表
     *
     * @param userIds 用户id
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> getFollowerUserList(List<Long> userIds, String token);

    /**
     * 更新用户关注数
     *
     * @param userId   用户id
     * @param constant 常数
     * @return {@link Boolean}
     */
    Boolean updateUserFollowCount(Long userId, Long constant);

    /**
     * 更新用户粉丝数
     *
     * @param userId   用户id
     * @param constant 常数
     * @return {@link Boolean}
     */
    Boolean updateUserFollowerCount(Long userId, Long constant);

    /**
     * 更新用户获赞数
     *
     * @param userId   用户id
     * @param constant 常数
     * @return {@link Boolean}
     */
    Boolean updateUserFavoritedCount(Long userId, Long constant);

    /**
     * 更新用户点赞数
     *
     * @param userId   用户id
     * @param constant 常数
     * @return {@link Boolean}
     */
    Boolean updateUserFavoriteCount(Long userId, Long constant);
}
