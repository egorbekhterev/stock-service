create table stock
(
    id bigserial primary key,
    symbol varchar(50),
    name text,
    is_enabled bool
);

create table quote
(
    id bigserial primary key,
    symbol varchar(50),
    latest_price numeric(9, 4),
    change_percent numeric(8, 6),
    refresh_time timestamp without time zone
);

