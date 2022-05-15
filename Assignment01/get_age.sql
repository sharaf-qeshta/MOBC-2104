/* 1- Write a function called get_age that returns the age out of
birthday.
*/
create or replace function get_age(birthday in date)
return integer is
    begin
        return (current_date  - birthday) / 365;
    end;

-- testing

declare
BEGIN
       DBMS_OUTPUT.PUT_LINE(get_age(to_date('13-MAY-2001'))); -- 21
       DBMS_OUTPUT.PUT_LINE(get_age(to_date('13-MAY-2002'))); -- 20
       DBMS_OUTPUT.PUT_LINE(get_age(to_date('13-MAY-2003'))); -- 19
end;
