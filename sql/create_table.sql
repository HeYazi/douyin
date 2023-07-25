-- --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- 基础模块 db_basic
-- --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- 创建库
create database if not exists db_basic;

-- 切换库
use db_basic;

-- 用户表
create table if not exists tb_user
(
    id               bigint auto_increment
        primary key,
    username         varchar(32)                                                                                               not null comment '登录用户名',
    `password`       varchar(32)                                                                                               not null,
    `name`           varchar(16)                                                                                               null comment '用户名称 ',
    follow_count     int          default 0                                                                                    null comment '关注总数',
    follower_count   int          default 0                                                                                    null comment ' 粉丝总数',
    background_image varchar(512) default 'https://tse3-mm.cn.bing.net/th/id/OIP-C.MVUrXzIVHvklsnbTk5XcywHaFd?pid=ImgDet&rs=1' null comment '用户个人页顶部大图 ',
    avatar           varchar(512) default 'https://tse3-mm.cn.bing.net/th/id/OIP-C.MVUrXzIVHvklsnbTk5XcywHaFd?pid=ImgDet&rs=1' null comment '头像',
    signature        varchar(512)                                                                                              null comment '个人简介',
    total_favorited  int          default 0                                                                                    null comment '获赞数量',
    work_count       int          default 0                                                                                    null comment '作品数量',
    create_time      datetime     default CURRENT_TIMESTAMP                                                                    not null comment '创建时间',
    update_time      datetime     default CURRENT_TIMESTAMP                                                                    not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete        tinyint      default 0                                                                                    not null comment '是否删除',
    constraint tb_user_id_uindex
        unique (id),
    constraint tb_user_name_uindex
        unique (name)
)
    comment '用户表' collate = utf8mb4_unicode_ci;



