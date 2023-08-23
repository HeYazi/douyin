package com.hyz.douyin.social.service;

import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.social.model.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author heguande
 * @description 针对表【tb_follow(关注表)】的数据库操作Service
 * @createDate 2023-08-23 23:16:42
 */
public interface FollowService extends IService<Follow> {

    /**
     * 社交关系动作
     *
     * @param token      令牌
     * @param toUserId   用户id
     * @param actionType 动作类型
     */
    void relationAction(String token, Long toUserId, Integer actionType);

    /**
     * 关注列表
     *
     * @param token  令牌
     * @param userId 用户id
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> relationFollowList(String token, Long userId);

    /**
     * 粉丝列表
     *
     * @param token  令牌
     * @param userId 用户id
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> relationFollowerList(String token, Long userId);

}
