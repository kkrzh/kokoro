create table emp (
                     id int unsigned primary key auto_increment comment 'ID',
                     username varchar(20) not null unique comment '用户名',
                     password varchar(32) default '123456' comment '密码',
                     name varchar(10) not null comment '姓名',
                     gender tinyint unsigned not null comment '性别, 说明: 1 男, 2 女',
                     image varchar(300) comment '图像',
                     job tinyint unsigned comment '职位, 说明: 1 班主任,2 讲师, 3 学工主管, 4 教研主管',
                     entrydate date comment '入职时间',
                     create_time datetime not null comment '创建时间',
                     update_time datetime not null comment '修改时间'
) comment '员工表';