/*
3- Write a trigger that simulates the auto_increment attribute.
*/

create or replace trigger custom_auto_increment
    before insert on TEST2 for each row
    begin
        select count(*) into :new.ID
        from TEST2;
    end;

-- testing
create table test2
(
  id int primary key,
  name varchar(50)
);

/*
before applying the auto increment trigger
cannot insert NULL into ("SYSTEM"."TEST2"."ID")
*/
insert into test2(NAME) values ('name');
insert into test2(NAME) values ('name');
insert into test2(NAME) values ('name');
insert into test2(NAME) values ('name');
insert into test2(NAME) values ('name');
insert into test2(NAME) values ('name');

/*
after applying the auto increment trigger

ID      NAME
0       name
1       name
2       name
3       name
4       name
5       name
*/
select *
from test2;