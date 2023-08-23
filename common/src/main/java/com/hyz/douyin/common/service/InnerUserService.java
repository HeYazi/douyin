package com.hyz.douyin.common.service;

import com.hyz.douyin.common.model.vo.UserVO;

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
    Map<Long, UserVO> getUserList(List<Long> userIds);

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
    Map<Long, UserVO> getUserList(String token, List<Long> userIds);

    /**
     * 更新获赞数量
     *
     * @param count  数
     * @param userId 用户id
     * @return {@link Boolean}
     */
    Boolean updateTotalFavorited(Integer count, Long userId);
}
