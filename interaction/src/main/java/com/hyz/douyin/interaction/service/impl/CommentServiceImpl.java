package com.hyz.douyin.interaction.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.common.exception.BusinessException;
import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.common.service.InnerUserService;
import com.hyz.douyin.common.utils.ThrowUtils;
import com.hyz.douyin.interaction.constant.CommentConstant;
import com.hyz.douyin.interaction.model.dto.comment.CommentActionRequest;
import com.hyz.douyin.interaction.model.entity.Comment;
import com.hyz.douyin.interaction.model.vo.comment.CommentActionVO;
import com.hyz.douyin.interaction.model.vo.comment.CommentListVO;
import com.hyz.douyin.interaction.model.vo.comment.CommentVO;
import com.hyz.douyin.interaction.service.CommentService;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hyz.douyin.common.constant.UserConstant.LOGIN_USER_TTL;


/**
 * 评论服务服务实现类
 *
 * @author HYZ
 * @date 2023/7/27 21:17
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MongoTemplate mongoTemplate;
    @DubboReference
    private InnerUserService innerUserService;

    @Override
    public CommentActionVO commentAction(CommentActionRequest commentActionRequest) {
        String token = commentActionRequest.getToken();
        // 用户 TTL 更新
        String key = UserConstant.USER_LOGIN_STATE + token;
        String idStr = stringRedisTemplate.opsForValue().get(key);
        ThrowUtils.throwIf(StringUtils.isBlank(idStr), ErrorCode.INTERACTION_OPERATION_ERROR, "您未登录");
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);

        Long videoId = commentActionRequest.getVideoId();
        Integer actionType = commentActionRequest.getActionType();

        // 2. 从 redis 中根据对应的 token 获取用户数据，判断用户是否存在
//        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
//        if (entries.isEmpty()) {
//            //  1. 不存在则抛出异常
//            throw new BusinessException(ErrorCode.INTERACTION_OPERATION_ERROR, "您未登录");
//        }
        Long userId = Long.valueOf(idStr);
        if (actionType == 1) {
            // 添加评论
            // 获取评论
            String commentText = commentActionRequest.getCommentText();
            ThrowUtils.throwIf(StringUtils.isBlank(commentText), ErrorCode.PARAMS_ERROR);
            stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
            return saveCommentAction(userId, videoId, commentText);
        } else {
            // 删除评论
            // 获取评论id
            Long commentId = commentActionRequest.getCommentId();
            ThrowUtils.throwIf(commentId == null, ErrorCode.PARAMS_ERROR);
            stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
            return deleteCommentAction(userId, videoId, commentId);
        }
    }

    /**
     * 评论列表
     *
     * @param token   令牌
     * @param videoId 视频id
     * @return {@link CommentListVO}
     */
    @Override
    public CommentListVO commentList(String token, Long videoId) {
        // 2. 获取 userId
        String key = UserConstant.USER_LOGIN_STATE + token;
        ThrowUtils.throwIf(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key)), ErrorCode.INTERACTION_OPERATION_ERROR, "您未登录");
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 3. 根据 videoId 在 mongo 获取对应的 CommentList 列表
        Criteria criteria = Criteria.where("video_id").is(videoId);
        List<Comment> commentList = mongoTemplate.find(Query.query(criteria), Comment.class);

        // 4. 根据 videoId 在 redis 获取对应的 CommentList 列表
        Set<String> keys = stringRedisTemplate.keys(CommentConstant.SAVE_COMMENT + videoId + "*");
        if (keys != null) {
            for (String k : keys) {
                String commentStr = stringRedisTemplate.opsForValue().get(k);
                Comment comment = JSONUtil.toBean(commentStr, Comment.class);
                commentList.add(comment);
            }
        }

        // 根据 Comment 的 createTime 来排序
        List<Comment> sortedList = commentList.stream()
                .sorted(Comparator.comparing(Comment::getCreateTime).reversed())
                .collect(Collectors.toList());

        // 6. 从 CommentList 获取一个 Map<Long,Long> userIdMap 和 token 发送给 User 模块，key 为 commentId，value 为 userId。User 模块返回一个 Map<Long,UserVo>， key 为 commentId，value 为 userVo。
        HashMap<Long, Long> userIdMap = new HashMap<>();
        for (Comment comment : sortedList) {
            userIdMap.put(comment.getId(), comment.getUserId());
        }
        Map<Long, UserVO> longUserVOHashMap = innerUserService.getUserMap(userIdMap, token);

        // 7. 创建 List<CommentVO>，将两个 Map 组装，生成结果并返回。
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : sortedList) {
            CommentVO commentVO = new CommentVO();
            commentVO.setId(comment.getId());
            commentVO.setUser(longUserVOHashMap.get(comment.getId()));
            commentVO.setContent(comment.getCommentText());
            commentVO.setCreateDate(new SimpleDateFormat("MM-dd").format(comment.getCreateTime()));
            commentVOList.add(commentVO);
        }
//        PropertyComparator<CommentVO> comparator = new PropertyComparator<CommentVO>.comparing()

        CommentListVO commentListVO = new CommentListVO();
        commentListVO.setCommentList(commentVOList);
        commentListVO.setStatusCode(0);
        commentListVO.setStatusMsg("成功");

        return commentListVO;
    }

    /**
     * 保存评论动作
     *
     * @param userId      用户id
     * @param videoId     视频id
     * @param commentText 评论文本
     * @return {@link CommentActionVO}
     */
    private CommentActionVO saveCommentAction(Long userId, Long videoId, String commentText) {

        // 2. zset 的构成：key【前缀:视频id:评论id】，value【整个 Comment】、score【时间】
        Comment comment = new Comment();
        comment.setId(IdUtil.getSnowflakeNextId());
        comment.setUserId(userId);
        comment.setVideoId(videoId);
        comment.setCommentText(commentText);
        Date date = new Date();
        comment.setCreateTime(date);
        String commentJson = JSONUtil.toJsonStr(comment);
        String key = CommentConstant.SAVE_COMMENT + videoId + ":" + userId;
        stringRedisTemplate.opsForValue().set(key, commentJson);

        // 3. 返回 CommentActionVO
        CommentVO commentVO = new CommentVO();
        commentVO.setId(comment.getId());
        // 在 user 模块里获取对应的用户数据
        UserVO userVO = innerUserService.getUserVO(userId);
        commentVO.setUser(userVO);
        commentVO.setContent(commentText);
        commentVO.setCreateDate(new SimpleDateFormat("MM-dd").format(date));
        CommentActionVO commentActionVO = new CommentActionVO();
        commentActionVO.setComment(commentVO);
        commentActionVO.setStatusCode(0);
        commentActionVO.setStatusMsg("成功");
        return commentActionVO;
    }

    /**
     * 删除评论动作
     *
     * @param userId    用户id
     * @param commentId 评论id
     * @param videoId   视频id
     * @return {@link CommentActionVO}
     */
    private CommentActionVO deleteCommentAction(Long userId, Long videoId, Long commentId) {
        // 2. 在新增评论缓存中删除对应的数据
        if (Boolean.FALSE.equals(stringRedisTemplate.delete(CommentConstant.SAVE_COMMENT + videoId + ":" + commentId))) {
            // 3. 将操作添加到删除评论缓存当中。redis 构成：key【前缀:视频id:评论id】，value【userId】
            String key = CommentConstant.DELETE_COMMENT + videoId + ":" + commentId;
            // 如果操作不存在则添加，存在则直接返回结果
            if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
                stringRedisTemplate.opsForValue().set(key, userId.toString());
            }
        }
        CommentActionVO commentActionVO = new CommentActionVO();
        commentActionVO.setComment(null);
        commentActionVO.setStatusCode(0);
        commentActionVO.setStatusMsg("删除成功");
        return commentActionVO;
    }
}
