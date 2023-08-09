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
    latest_price numeric(20, 10),
    change_percent numeric(20, 10),
    refresh_time timestamp without time zone
);
