 drop table if exists flow;
 drop table if exists transport;
 drop table if exists vessel;
 drop table if exists habour;
 pragma foreign_keys = ON;
 create table habour(id Integer primary key, name Text);
 insert into habour
 values(1, 'Jawaharlal Nehru');
 insert into habour
 values(2, 'Tanjung Pelepas');
 insert into habour
 values(3, 'Dar Es Salaam');
 insert into habour
 values(4, 'Mombasa');
 insert into habour
 values(5, 'Zanzibar');
 insert into habour
 values(6, 'Jebel Ali Dubai');
 insert into habour
 values(7, 'Salalah');
 create table vessel(
     id Integer primary key,
     name Text,
     capacity integer
 );
 insert into vessel
 values(1, 'Maren', 12000);
 insert into vessel
 values(2, 'Misse', 5000);
 insert into vessel
 values(3, 'Mette', 8000);
 insert into vessel
 values(4, 'Musse', 10000);
 insert into vessel
 values(5, 'Mugge', 8000);
 insert into vessel
 values(6, 'Marle', 10000);
 insert into vessel
 values(7, 'Minne', 10000);
 insert into vessel
 values(8, 'Maryk', 10000);
 insert into vessel
 values(9, 'Melle', 10000);
 insert into vessel
 values(10, 'Manna', 10000);
 insert into vessel
 values(11, 'Mynte', 10000);
 insert into vessel
 values(12, 'Munja', 10000);
 create table transport(
     id Integer primary key,
     vessel Integer references vessel(id),
     fromhabour Integer references habour(id),
     tohabour Integer references habour(id)
 );
 insert into transport
 values (1, 1, 1, 4);
 insert into transport
 values (2, 2, 1, 3);
 insert into transport
 values (3, 3, 2, 4);
 insert into transport
 values (4, 4, 2, 3);
 insert into transport
 values (5, 5, 2, 5);
 insert into transport
 values (6, 6, 2, 6);
 insert into transport
 values (7, 7, 2, 7);
 insert into transport
 values (8, 8, 3, 2);
 insert into transport
 values (9, 9, 3, 1);
 insert into transport
 values (10, 10, 3, 6);
 insert into transport
 values (11, 11, 4, 7);
 insert into transport
 values (12, 12, 4, 6);
 create table flow(
     id Integer primary key,
     transport Integer references transport(id),
     containers integer
 );
 insert into flow
 values (1, 1, 2000);
 insert into flow
 values (2, 2, 2000);
 insert into flow
 values (3, 3, 5000);
 insert into flow
 values (4, 4, 3000);
insert into flow
values (5, 5, 2000);
insert into flow
values (6, 6, 7000);
insert into flow
values (7, 7, 7000);
insert into flow
values (8, 8, 5000);
insert into flow
values (9, 9, 3000);
insert into flow
values (10, 10, 2000);
insert into flow
values (11, 11, 2000);
insert into flow
values (12, 12, 10000);
--Brug den her til at insert containers in i flow tabellen istedet for update
insert into flow(transport, containers)
values (12, 1500);
select v.id as id,
    h1.name as fromport,
    h2.name as toport,
    v.name as vessel,
    Sum(f.containers) as containers,
    v.capacity
from transport t
    inner join vessel v on t.vessel = v.id
    inner join habour h1 on t.fromhabour = h1.id
    inner join habour h2 on t.tohabour = h2.id
    left outer join flow f on t.id = f.transport
GROUP BY t.id;
-- Tjekker om containers er større end capacity
select v.id as id,
    h1.name as fromport,
    h2.name as toport,
    v.name as vessel,
    Sum(f.containers) as containers,
    v.capacity
from transport t
    inner join vessel v on t.vessel = v.id
    inner join habour h1 on t.fromhabour = h1.id
    inner join habour h2 on t.tohabour = h2.id
    left outer join flow f on t.id = f.transport
GROUP BY t.id
HAVING f.containers > v.capacity;
select t.id as id,
    h1.name as fromport,
    h2.name as toport,
    v.name as vessel,
    Sum(f.containers) as containers,
    v.capacity
from transport t
    inner join vessel v on t.vessel = v.id
    inner join habour h1 on t.fromhabour = h1.id
    inner join habour h2 on t.tohabour = h2.id
    left outer join flow f on t.id = f.transport
group by t.id;
select t.id as id,
    h1.name as fromport,
    h2.name as toport,
    v.name as vessel,
    Sum(f.containers) as containers,
    v.capacity
from transport t
    inner join vessel v on t.vessel = v.id
    inner join habour h1 on t.fromhabour = h1.id
    inner join habour h2 on t.tohabour = h2.id
    left outer join flow f on t.id = f.transport
WHERE fromport = "Jawaharlal Nehru"
GROUP by t.id;
select t.id as id,
    h1.name as fromport,
    h2.name as toport,
    v.name as vessel,
    Sum(f.containers) as containers,
    v.capacity
from transport t
    inner join vessel v on t.vessel = v.id
    inner join habour h1 on t.fromhabour = h1.id
    inner join habour h2 on t.tohabour = h2.id
    left outer join flow f on t.id = f.transport
WHERE fromport = "Jawaharlal Nehru"
    AND toport = "Mombasa";