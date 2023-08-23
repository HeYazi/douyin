package com.hyz.douyin.user.service.impl.inner;

import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.common.exception.BusinessException;
import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.common.service.InnerUserService;
import com.hyz.douyin.user.model.entity.User;
import com.hyz.douyin.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内部用户服务impl
 * todo 关注模块还没写
 *
 * @author HYZ
 * @date 2023/8/21 11:22
 */
@Service
@DubboService
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
    public Map<Long, UserVO> getUserList(List<Long> userIds) {
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
    public UserVO getUserVO(String token, Long id) {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
        Long userId;
        if (entries.isEmpty()) {
            userId = null;
        }
        userId = Long.parseLong((String) entries.get("id"));
        return userService.userQuery(userId, id);
    }

    @Override
    public Map<Long, UserVO> getUserList(String token, List<Long> userIds) {
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
}
