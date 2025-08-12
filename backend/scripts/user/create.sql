create table user (
                     id int unsigned primary key auto_increment comment 'ID',
                     username varchar(20) not null unique comment '用户名',
                     password varchar(64) default null comment '密码',
                     name varchar(16) not null comment '昵称'
) comment '用户表';