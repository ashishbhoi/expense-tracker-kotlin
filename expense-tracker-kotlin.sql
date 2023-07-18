drop database etktdb;
drop user etktuser;

create user etktuser with password 'etktpass';
create database etktdb with template=template0 owner etktuser;

\connect etktdb;
alter default privileges grant all on tables to etktuser;
alter default privileges grant all on sequences to etktuser;

create table et_users(
                         user_id integer primary key not null,
                         first_name varchar(30) not null,
                         last_name varchar(30) not null,
                         email varchar(50) not null,
                         password text not null
);

create table et_categories(
                              category_id integer primary key not null,
                              user_id integer not null,
                              title varchar(20) not null,
                              description varchar(50) not null
);
alter table et_categories add constraint cat_user_fk foreign key(user_id) references et_users(user_id);

create table et_transactions(
                                transaction_id integer primary key not null,
                                category_id integer not null,
                                user_id integer not null,
                                amount numeric(10,2) not null,
                                note varchar(50) not null,
                                transaction_date bigint not null
);
alter table et_transactions add constraint tran_cat_fk foreign key(category_id) references et_categories(category_id);
alter table et_transactions add constraint tran_user_fk foreign key(user_id) references et_users(user_id);

create sequence et_users_seq increment 10 start 10;
create sequence et_categories_seq increment 10 start 15;
create sequence et_transactions_seq increment 1 start 1000;