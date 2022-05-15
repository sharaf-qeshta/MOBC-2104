/*  2- Write a procedure that deletes all duplicated rows from a
    table
*/

create or replace procedure delete_duplicate
as
    begin
        delete from test2 y where (select count(*)
                                 from test2 x
                                 where x.name = y.name
                                  ) >= 2;
    end;


-- testing
create table test2(
  id int primary key,
  name varchar(50)
);

insert into test2 values (1, 'sharaf');
insert into test2 values (2, 'sharaf');
insert into test2 values (3, 'sharaf');
insert into test2 values (4, 'khaled');
insert into test2 values (5, 'khaled');
insert into test2 values (6, 'khaled');
insert into test2 values (7, 'mohammed');


declare
begin
    delete_duplicate();
end;

/*
ID      NAME
7       mohammed
*/
select *
from test2;