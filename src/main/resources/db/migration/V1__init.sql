create table stock
(
    id bigserial primary key,
    symbol varchar(50) unique,
    name text,
    is_enabled bool
);

create table quote
(
    id uuid primary key,
    stock_id bigint references stock(id),
    latest_price numeric(9, 4),
    change_percent numeric(8, 6),
    refresh_time timestamp without time zone
);

create table change
(
    id bigserial primary key,
    symbol varchar(50) unique,
    change numeric(8, 6)
);
