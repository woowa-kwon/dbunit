create table user (
  user_id bigint auto_increment primary key,
  name varchar(20) not null,
  password varchar(10) not null,
  email varchar(50) unique,
  company_id bigint not null
);

create table company (
  company_id bigint auto_increment primary key,
  name varchar(20) unique not null
);