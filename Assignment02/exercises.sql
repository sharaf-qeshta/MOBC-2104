/** building the database for testing */

create table buildings
(
  building_name varchar(2) primary key,
  capacity integer
);

create table employees
(
  role varchar(64),
  name varchar(216) primary key,
  building varchar(2),
  years_employed integer,
  foreign key (building) references buildings(building_name)
);

insert into buildings values ('1e', 24);
insert into buildings values ('1w', 32);
insert into buildings values ('2e', 16);
insert into buildings values ('2w', 20);

insert into employees values ('Engineer', 'Becky A.', '1e', 4);
insert into employees values ('Engineer', 'Dan B.', '1e', 2);
insert into employees values ('Engineer', 'Sharon F.', '1e', 6);
insert into employees values ('Engineer', 'Dan M.', '1e', 4);
insert into employees values ('Engineer', 'Malcolm S.', '1e', 1);
insert into employees values ('Artist', 'Tylar S.', '2w', 2);
insert into employees values ('Artist', 'Sherman D.', '2w', 8);
insert into employees values ('Artist', 'Jacob J.', '2w', 6);
insert into employees values ('Artist', 'Lillia A.', '2w', 7);
insert into employees values ('Artist', 'Brandon J.', '2w', 7);
insert into employees values ('Manager', 'Scott K.', '1e', 9);
insert into employees values ('Manager', 'Shirlee M.', '1e', 3);
insert into employees values ('Manager', 'Daria O.', '2w', 6);

/** solutions */

/*
a) Create PROCEDURE to print the list of all buildings that do not have
employees
*/

create or replace procedure print_empty_buildings
        as
            cursor empty_buildings is select * from buildings where building_name not in (select distinct building from employees);
        begin
            for building in empty_buildings loop
                DBMS_OUTPUT.PUT_LINE('Name: ' || building.building_name || ', Capacity: ' || building.capacity);
            end loop;
        end;


-- test
/*
 [2022-05-24 07:01:39] Name: 2e, Capacity: 16
 [2022-05-24 07:01:39] Name: 1w, Capacity: 32
 */
begin
    print_empty_buildings();
end;


/*
b) Create FUNCTION to return the name of employee that have the
maximum employed years
*/

create or replace function get_max_employed_employee return varchar is
            cursor employees is select * from employees;
            max_name varchar(216);
            max_years integer := -1;
        begin
            for employee in employees loop
                if employee.years_employed > max_years then
                    max_name := employee.name;
                    max_years := employee.years_employed;
                end if;
                end loop;
            return max_name;
        end;

-- testing
begin
   DBMS_OUTPUT.PUT_LINE(get_max_employed_employee()); -- Scott K.
end;


/*
c) Create PROCEDURE to print all employees' names and their grades
based on the following rules.
If employee have 1 to 4 years employed his grade will be A
If employee have 5 to 7 years employed his grade will be B
If employee have 8 to 9 years employed his grade will be C
*/

create or replace function get_grade(years_employed in integer) return varchar is
        begin
            if years_employed >= 8 then
                return 'C';
            elsif years_employed >= 5 then
                return 'B';
            else
                return 'A';
            end if;
        end;


create or replace procedure print_all_employees_grades
    as
    begin
        for employee in (select * from employees) loop
            DBMS_OUTPUT.PUT_LINE('Name: ' || employee.name || ' have grade: "' || get_grade(employee.years_employed)
                 || '" based on ' || employee.years_employed || ' years of experience.');
        end loop;
    end;

-- testing
/*
[2022-05-24 07:25:56] Name: Becky A. have grade: "A" based on 4 years of experience.
[2022-05-24 07:25:56] Name: Dan B. have grade: "A" based on 2 years of experience.
[2022-05-24 07:25:56] Name: Sharon F. have grade: "B" based on 6 years of experience.
[2022-05-24 07:25:56] Name: Dan M. have grade: "A" based on 4 years of experience.
[2022-05-24 07:25:56] Name: Malcolm S. have grade: "A" based on 1 years of experience.
[2022-05-24 07:25:56] Name: Tylar S. have grade: "A" based on 2 years of experience.
[2022-05-24 07:25:56] Name: Sherman D. have grade: "C" based on 8 years of experience.
[2022-05-24 07:25:56] Name: Jacob J. have grade: "B" based on 6 years of experience.
[2022-05-24 07:25:56] Name: Lillia A. have grade: "B" based on 7 years of experience.
[2022-05-24 07:25:56] Name: Brandon J. have grade: "B" based on 7 years of experience.
[2022-05-24 07:25:56] Name: Scott K. have grade: "C" based on 9 years of experience.
[2022-05-24 07:25:56] Name: Shirlee M. have grade: "A" based on 3 years of experience.
[2022-05-24 07:25:56] Name: Daria O. have grade: "B" based on 6 years of experience.
*/
begin
    print_all_employees_grades();
end;






