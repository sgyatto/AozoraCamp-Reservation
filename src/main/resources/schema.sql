-- trigger function
create or replace function trigger_set_updated_at()
returns trigger as '
begin
  new.updated_at := ''now'';
return new;
end;
' language plpgsql;

-- table
-- role
create table if not exists roles (
    key text primary key,
    name text not null,
    created_at timestamp not null default current_timestamp
);

-- members
create table if not exists members (
    id serial primary key,
    name varchar(100) not null,
    mail text unique,
    password text not null,
    phone_number varchar(15),
    role text references roles(key),
    updated_at timestamp not null default current_timestamp,
    created_at timestamp not null default current_timestamp
);

-- site_types
create table if not exists site_types (
    id int primary key,
    name text not null,
    capacity int not null,
    list_img_url text not null,
    created_at timestamp not null default current_timestamp
);

-- sales_tax
create table if not exists sales_tax (
    date_from date primary key,
    date_to date,
    rate numeric(3, 2) not null,
    created_at timestamp not null default current_timestamp
);

-- rate_types
create table if not exists rate_types (
    id int primary key,
    name text not null,
    created_at timestamp not null default current_timestamp
);

-- site_rates
create table if not exists site_rates (
    site_type_id int references site_types(id),
    rate_type_id int references rate_types(id),
    date_from date,
    date_to date not null,
    rate int not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    primary key (site_type_id, rate_type_id, date_from)
);

-- calendar
create table if not exists calendar (
    calendar_date date primary key,
    rate_type_id int not null,
    created_at timestamp not null default current_timestamp
);

-- site_availability
create table if not exists site_availability (
    calendar_date date references calendar(calendar_date),
    site_type_id int references site_types(id),
    availability_count smallint not null,
    max_count smallint not null,
    updated_at timestamp not null  default current_timestamp,
    created_at timestamp not null  default current_timestamp,
    primary key (calendar_date, site_type_id)
);

-- reservations
create table if not exists reservations (
    id serial primary key,
    site_type_id int not null references site_types(id),
    date_from date not null,
    stay_days int not null,
    number_of_people int not null,
    total_amount_tax_incl int not null,
    sales_tax int not null,
    reservation_method varchar(15) not null,
    member_id int  references members(id) check (
    case
        when non_member_name is null
            and non_member_mail is null
            and non_member_phone_number is null
            and member_id is not null then true
        when non_member_name is not null
            and non_member_mail is not null
            and non_member_phone_number is not null
            and member_id is null then true
        else false
    end),
    non_member_name varchar(100),
    non_member_mail text,
    non_member_phone_number varchar(15),
    canceled_at timestamp,
    created_at timestamp not null default current_timestamp
);

-- reservation_details
create table if not exists reservation_details (
    reservation_id int references reservations(id),
    reservation_date date not null,
    site_rate int not null,
    tax_rate numeric(3, 2) not null,
    rate_type_name text not null,
    created_at timestamp not null default current_timestamp,
    primary key (reservation_id, reservation_date)
);

-- create trigger
-- members
drop trigger if exists set_updated_at on members;
create trigger set_updated_at before update on members for each row
execute procedure trigger_set_updated_at();

-- site_rates
drop trigger if exists set_updated_at on site_rates;
create trigger set_updated_at before update on site_rates for each row
execute procedure trigger_set_updated_at();

-- site_availability
drop trigger if exists set_updated_at on site_availability;
create trigger set_updated_at before update on site_availability for each row
execute procedure trigger_set_updated_at();