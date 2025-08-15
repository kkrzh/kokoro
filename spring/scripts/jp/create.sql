create table jp_word (
                      id int unsigned primary key auto_increment comment 'ID',
                      word varchar(20) not null unique comment '单词',
                      cn varchar(20) not null comment '翻译',
                      part_speech  varchar(20) not null comment '词性'
                      root varchar(20) not null comment '根词汇'
) comment '词表';