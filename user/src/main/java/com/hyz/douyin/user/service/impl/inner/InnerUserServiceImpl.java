package com.hyz.douyin.user.service.impl.inner;

import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.common.exception.BusinessException;
import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.common.service.InnerUserService;
import com.hyz.douyin.user.model.entity.User;
import com.hyz.douyin.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 内部用户服务impl
 *
 * @author HYZ
 * @date 2023/8/21 11:22
 */
@Service
@DubboService
@Slf4j
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;


    @Override
    public Map<Long, UserVO> getUserMap(Map<Long, Long> map) {
        // 传入一个Map集合，key 为唯一标识，value 为待查询用户的 id
        HashMap<Long, UserVO> result = new HashMap<>();
        for (Long aLong : map.keySet()) {
            Long id = map.get(aLong);
            result.put(aLong, getUserVO(id));
        }
        return result;
    }

    @Override
    public UserVO getUserVO(Long id) {
        return userService.userQuery(null, id);
    }

    @Override
    public Map<Long, UserVO> getUserByList(List<Long> userIds) {
        // 传入一个 List 集合，value 为待查询用户的 id
        Map<Long, UserVO> userVOS = new HashMap<>();
        for (Long userId : userIds) {
            UserVO userVO = getUserVO(userId);
            userVOS.put(userId, userVO);
        }
        return userVOS;
    }

    @Override
    public Map<Long, UserVO> getUserMap(Map<Long, Long> map, String token) {
        // 传入一个Map集合，key 为唯一标识，value 为待查询用户的 id
        HashMap<Long, UserVO> result = new HashMap<>();
        for (Long aLong : map.keySet()) {
            Long id = map.get(aLong);
            result.put(aLong, getUserVO(token, id));
        }
        return result;
    }

    @Override
    public UserVO getUserVO(String token, Long userId) {
        String key = UserConstant.USER_LOGIN_STATE + token;
        String idStr = stringRedisTemplate.opsForValue().get(key);
        Long id = StringUtils.isBlank(idStr) ? null : Long.parseLong(idStr);
        return userService.userQuery(id, userId);
    }

    @Override
    public Map<Long, UserVO> getUserByList(String token, List<Long> userIds) {
        // 传入一个 List 集合，value 为待查询用户的 id
        Map<Long, UserVO> userVOS = new HashMap<>();
        for (Long userId : userIds) {
            UserVO userVO = getUserVO(token, userId);
            userVOS.put(userId, userVO);
        }
        return userVOS;
    }

    @Override
    public Boolean updateTotalFavorited(Integer count, Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.INTERACTION_OPERATION_ERROR, "修改用户点赞数的用户不存在");
        }
        Long totalFavorited = user.getTotalFavorited();
        totalFavorited = totalFavorited + count;
        user.setTotalFavorited(totalFavorited);
        userService.updateById(user);
        return true;
    }

    @Override
    @Transactional
    public Boolean relationAction(Long userId, Long toUserId, Integer type) {
        // 判断两个用户是否存在
        User user = userService.getById(userId);
        User toUser = userService.getById(toUserId);
        if (!ObjectUtils.allNotNull(user, toUser)) {
            log.error("关注操作的用户不存在，请检查");
            return false;
        }
        if (type == 1) {
            // 关注数+1和粉丝数+1
            user.setFollowCount(user.getFollowCount() + 1);
            toUser.setFollowerCount(toUser.getFollowerCount() + 1);
        } else {
            // 关注数-1和粉丝数-1
            user.setFollowCount(user.getFollowCount() - 1);
            toUser.setFollowerCount(toUser.getFollowerCount() - 1);
        }
        return userService.updateBatchById(Arrays.asList(user, toUser));
    }

    @Override
    public List<UserVO> getFollowUserList(List<Long> userIds) {
        List<UserVO> list = new ArrayList<>();
        for (Long userId : userIds) {
            UserVO userVO = this.getUserVO(userId);
            userVO.setIsFollow(true);
            list.add(userVO);
        }
        return list;
    }

    @Override
    public List<UserVO> getFollowerUserList(List<Long> userIds, String token) {
        List<UserVO> userVOS = new ArrayList<>();
        for (Long userId : userIds) {
            UserVO userVO = getUserVO(token, userId);
            userVOS.add(userVO);
        }
        return userVOS;
    }

}
