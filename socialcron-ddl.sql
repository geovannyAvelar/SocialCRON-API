# SocialCRON-CORE database schema

create table if not exists posts (
  id bigint(20) primary key auto_increment,
  title varchar(50) not null,
  content varchar(1000) not null
);

create table if not exists profiles (
  id bigint(20) primary key auto_increment,
  profile_id varchar(50) not null,
  name varchar(256) not null,
  token varchar(256) not null,
  created_at datetime not null,
  expires datetime not null
);

create table if not exists events (
  id bigint(20) primary key auto_increment,
  initial_date datetime not null,
  limit_date date,
  period int,
  time_interval int
);

create table if not exists schedules (
  id bigint(20) primary key auto_increment,
  date datetime not null,
  completed tinyint(1) not null,
  post_id bigint(20) not null,
  profile_id bigint(20) not null,
  event_id bigint(20) not null,
  foreign key (post_id) references posts(id) on delete cascade,
  foreign key (profile_id) references profiles(id),
  foreign key (event_id) references events(id) on delete cascade
);

create table if not exists user (
  username varchar(50) primary key not null,
  email varchar(50) default null,
  password varchar(500) default null,
  name varchar(50) default null,
  avatar varchar(256) not null,
  activated tinyint(1) default '0',
  activationkey varchar(50) default null,
  resetpasswordkey varchar(50) default null
);

create table if not exists authority (  
  name varchar(50) not null primary key  
);

create table if not exists user_authority (  
  username varchar(50) not null,  
  authority varchar(50) not null,  
  foreign key (username) references user (username),  
  foreign key (authority) references authority (name),
  unique index (username, authority)  
);
   
create table if not exists oauth_access_token (  
  token_id varchar(256) default null,
  token blob,
  authentication_id varchar(256) default null, 
  user_name varchar(256) default null,
  client_id varchar(256) default null,
  authentication blob,
  refresh_token varchar(256) default null
); 
  
create table if not exists oauth_refresh_token (  
  token_id varchar(256) default null,
  token blob,
  authentication blob
);

create table if not exists acl_sid (
  id bigint unsigned not null auto_increment primary key,
  principal boolean not null,
  sid VARCHAR(100) not null,
  unique key unique_acl_sid (sid, principal)
);

create table if not exists acl_class (
  id bigint unsigned not null auto_increment primary key,
  class VARCHAR(100) not null,
  unique key uk_acl_class (class)
);

create table if not exists acl_object_identity (
  id bigint unsigned not null auto_increment primary key,
  object_id_class bigint unsigned not null,
  object_id_identity bigint not null,
  parent_object bigint unsigned,
  owner_sid bigint unsigned,
  entries_inheriting boolean not null,
  unique key uk_acl_object_identity (object_id_class, object_id_identity),
  constraint fk_acl_object_identity_parent
                                  foreign key (parent_object) references acl_object_identity (id),
  constraint fk_acl_object_identity_class foreign key (object_id_class) references acl_class (id),
  constraint fk_acl_object_identity_owner foreign key (owner_sid) references acl_sid (id)
);

create table if not exists acl_entry (
  id bigint unsigned not null auto_increment primary key,
  acl_object_identity bigint unsigned not null,
  ace_order INTEGER not null,
  sid bigint unsigned not null,
  mask INTEGER unsigned not null,
  granting boolean not null,
  audit_success boolean not null,
  audit_failure boolean not null,
  unique key unique_acl_entry (acl_object_identity, ace_order),
  constraint fk_acl_entry_object 
                            foreign key (acl_object_identity) references acl_object_identity (id),
  constraint fk_acl_entry_acl foreign key (sid) references acl_sid (id)
);

insert into authority(name) values('ADMIN');
insert into authority(name) values('USER');

# BCrypt hash
# Password is root
insert into user (username, email, password, activated) 
                    values ('root', 'root@root.com', 
                            '$2a$06$LOO5IrurVYKTAIIH/Ojd/.0FL9oVXRlsmj8OZRb0xh0C/Nr1WEEFm', true);

insert into user_authority(username, authority) values('root', 'ADMIN');
