create table stock
(
    id serial primary key,
    symbol varchar(50) unique,
    exchange varchar(50),
    exchange_suffix varchar(255),
    exchange_name text,
    exchange_segment varchar(255),
    exchange_segment_name text,
    name text,
    date date,
    type varchar(50),
    iex_id varchar(50),
    region varchar(50),
    currency varchar(20),
    is_enabled bool,
    figi varchar(50),
    cik varchar(50),
    lei varchar(50)
);

create table quote
(
    id uuid primary key,
    symbol varchar(20),
    latest_price numeric(20, 10),
    change_percent numeric(20, 10),
    refresh_time timestamp without time zone
);
