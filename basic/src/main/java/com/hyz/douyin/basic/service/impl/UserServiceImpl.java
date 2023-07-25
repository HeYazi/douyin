package com.hyz.douyin.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyz.douyin.basic.model.entity.User;
import com.hyz.douyin.basic.service.UserService;
import com.hyz.douyin.basic.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author heguande
* @description 针对表【tb_user(用户表)】的数据库操作Service实现
* @createDate 2023-07-25 12:43:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




