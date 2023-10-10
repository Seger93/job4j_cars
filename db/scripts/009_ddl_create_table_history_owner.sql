create table history_owner
(
    id       serial primary key,
    owner_id int not null references owner (id),
    car_id   int not null references car (id),
    UNIQUE (owner_id, car_id)
);