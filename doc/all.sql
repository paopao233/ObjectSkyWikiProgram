##电子书
select *
from ebook;

insert into ebook (id, name, description)
values (1, "spring boot 入门教程", "零基础入门java开发 企业级应用开发最佳选项");
insert into ebook (id, name, description)
values (2, "spring boot 入门教程", "零基础入门java开发 企业级应用开发最佳选项");
insert into ebook (id, name, description)
values (3, "尚硅谷Mysql", "零基础入门mysql开发 企业级应用开发最佳选项");
insert into ebook (id, name, description)
values (4, "Javascript菜鸟教程", "零基础入门Javascript开发 企业级应用开发最佳选项");
insert into ebook (id, name, description)
values (5, "Python零基础入门教程", "零基础入门m开发 企业级应用开发最佳选项");

## 分类表
drop table if exists `category`;
create table `category`
(
    `id`     bigint      not null comment 'id',
    `parent` bigint      not null default 0 comment '父id',
    `name`   varchar(50) not null comment '名称',
    `sort`   int comment '顺序',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='分类';

insert into `category` (id, parent, name, sort)
VALUES (100, 000, '前端开发', 100);
insert into `category` (id, parent, name, sort)
VALUES (101, 100, 'vue', 101);
insert into `category` (id, parent, name, sort)
VALUES (102, 100, 'html & css', 102);

insert into `category` (id, parent, name, sort)
VALUES (200, 000, 'java', 200);
insert into `category` (id, parent, name, sort)
VALUES (201, 200, '基础应用', 201);
insert into `category` (id, parent, name, sort)
VALUES (202, 200, '框架应用', 202);

insert into `category` (id, parent, name, sort)
VALUES (300, 000, 'python', 300);
insert into `category` (id, parent, name, sort)
VALUES (302, 300, '框架应用', 301);
insert into `category` (id, parent, name, sort)
VALUES (303, 300, '进阶方向应用', 302);

insert into `category` (id, parent, name, sort)
VALUES (400, 000, '数据库', 400);
insert into `category` (id, parent, name, sort)
VALUES (401, 400, 'mysql', 402);
insert into `category` (id, parent, name, sort)
VALUES (402, 400, 'mongodb', 402);

insert into `category` (id, parent, name, sort)
VALUES (500, 000, '其他', 500);
insert into `category` (id, parent, name, sort)
VALUES (501, 500, '服务器', 501);
insert into `category` (id, parent, name, sort)
VALUES (502, 500, '开发工具', 502);
insert into `category` (id, parent, name, sort)
VALUES (503, 500, '热门服务端语言', 503);

select *
from category;


### 文档表
drop table if exists `doc`;
create table `doc`
(
    `id`         bigint      not null comment 'id',
    `ebook_id`   bigint      not null default 0 comment '电子书id',
    `parent`     bigint      not null default 0 comment '父id',
    `name`       varchar(50) not null default 0 comment '名称',
    `sort`       int comment '顺序',
    `view_count` int                  default 0 comment '阅读数',
    `vote_count` int                  default 0 comment '点赞数',
    primary key (`id`)
) engine = innodb
  default charset = utf8mb4 comment ='文档';
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (1, 1, 0, '文档1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (2, 1, 1, '文档1.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (3, 1, 0, '文档2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (4, 1, 3, '文档2.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (5, 1, 3, '文档2.2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (6, 1, 5, '文档2.2.1', 1, 0, 0);

select * from doc;
