truncate reservation_details restart identity cascade;
truncate reservations restart identity cascade;
truncate site_availability restart identity cascade;
truncate calendar restart identity cascade;
truncate site_rates restart identity cascade;
truncate rate_types restart identity cascade;
truncate sales_tax restart identity cascade;
truncate site_types restart identity cascade;
truncate members restart identity cascade;
truncate roles restart identity cascade;

-- role
insert into roles (
                  key
                  , name
                  , created_at
) values
('ROLE_GENERAL', '一般ユーザ', default )
;

-- member
insert into members (
                    id
                    , name
                    , mail
                    , password
                    , phone_number
                    , role
                    , updated_at
                    , created_at
) values
(default , '藤村忠寿', 'aozora@camp.com', '$2a$10$rJyapIrvsHARwCNgporWLO6QIKXXezOpRrdb..7X0ea0VwZ5IldSy', '09012345678', 'ROLE_GENERAL', default, default)
,(default ,'嬉野雅道','aozora2@camp.com','$2a$10$bWrKNHFJ4bDoUl/hSt6GoeKm1QHdAkkrFDV.Lh5SKEumpg9kAZ8TO','09088888888','ROLE_GENERAL',default, default)
;

-- site_types
insert into site_types (
                        id
                        , name
                        , capacity
                        , list_img_url
                        , created_at
) values
(1, '区画サイト（AC電源なし）', 6, 'https://aozoracamprsv.s3.ap-northeast-1.amazonaws.com/siteType1_300.png', default)
,(2, '区画サイト（AC電源あり）', 6, 'https://aozoracamprsv.s3.ap-northeast-1.amazonaws.com/siteType2_300.png',default)
,(3, 'コテージ', 4, 'https://aozoracamprsv.s3.ap-northeast-1.amazonaws.com/siteType3_300.png', default)
;

-- sales_tax
insert into sales_tax (
                       date_from
                       , date_to
                       , rate
                       , created_at
) values
('2014-04-01', '2014-09-30', 0.08, default)
,('2019-10-01', '3000-01-01', 0.10, default)
;

-- rate_types
insert into rate_types (
                        id
                        , name
                        , created_at
) values
(1, '通常料金', default)
, (2, 'ハイシーズン料金', default)
;

-- site_rates
insert into site_rates (
                        site_type_id
                        , rate_type_id
                        , date_from
                        , date_to
                        , rate
                        , updated_at
                        , created_at
) values
(1, 1, '2010-01-01', '2020-09-30', 2500, '2020-10-01', '2010-01-01')
,(1, 1, '2020-10-01', '3000-01-01', 3000, '2020-10-01', '2020-10-01')
,(1, 2, '2010-01-01', '2020-09-30', 3500, '2020-10-01', '2010-01-01')
,(1, 2, '2020-10-01', '3000-01-01', 4000, '2020-10-01', '2020-10-01')
,(2, 1, '2010-01-01', '3000-01-01', 4500, '2020-10-01', '2020-10-01')
,(2, 2, '2010-01-01', '3000-01-01', 5500, '2020-10-01', '2020-10-01')
,(3, 1, '2010-01-01', '3000-01-01', 5000, '2020-10-01', '2020-10-01')
,(3, 2, '2010-01-01', '3000-01-01', 7000, '2020-10-01', '2020-10-01')
;

-- calendar
insert into calendar (
                       calendar_date
                     , rate_type_id
                     , created_at
)
select
    generate_series
     , case
            when extract(month from generate_series) in (07, 08, 09) then 2
            else 1
    end
     ,to_timestamp('2021-08-01', 'YYYY-MM-DD')
from
    generate_series('2021-08-01'::date, '2022-12-31', '1 day')
;

-- site_availability
insert into site_availability (
                                calendar_date
                              , site_type_id
                              , availability_count
                              , max_count
                              , updated_at
                              , created_at
)
select
    generate_series
     , 1
     , 5
     , 5
     ,to_timestamp('2021-08-01', 'YYYY-MM-DD')
     ,to_timestamp('2021-08-01', 'YYYY-MM-DD')
from
    generate_series('2021-08-01'::date, '2022-12-31', '1 day')
union all
select
    generate_series
     , 2
     , 4
     , 4
     ,to_timestamp('2021-08-01', 'YYYY-MM-DD')
     ,to_timestamp('2021-08-01', 'YYYY-MM-DD')
from
    generate_series('2021-08-01'::date, '2022-12-31', '1 day')
union all
select
    generate_series
     , 3
     , 3
     , 3
     ,to_timestamp('2021-08-01', 'YYYY-MM-DD')
     ,to_timestamp('2021-08-01', 'YYYY-MM-DD')
from
    generate_series('2021-08-01'::date, '2022-12-31', '1 day')
;

-- reservations
insert into "reservations"("id","site_type_id","date_from","stay_days","number_of_people","total_amount_tax_incl","sales_tax","reservation_method","member_id","non_member_name","non_member_mail","non_member_phone_number","canceled_at","created_at")
values
(default ,1,E'2021-09-02',2,1,8800,800,1,1,NULL,NULL,NULL,NULL,E'2021-08-15 13:45:31.934168'),
(default ,3,E'2021-08-25',1,1,7700,700,1,1,NULL,NULL,NULL,NULL,E'2021-08-15 13:46:07.733142'),
(default ,3,E'2021-08-31',1,1,7700,700,1,1,NULL,NULL,NULL,NULL,E'2021-08-15 13:46:33.467437'),
(default ,2,E'2021-09-15',3,3,18150,1650,1,1,NULL,NULL,NULL,NULL,E'2021-08-15 13:48:15.109568'),
(default ,3,E'2021-09-13',1,1,7700,700,1,1,NULL,NULL,NULL,NULL,E'2021-08-15 13:48:50.726346'),
(default ,2,E'2021-09-18',2,1,12100,1100,1,1,NULL,NULL,NULL,NULL,E'2021-08-15 13:49:31.789736'),
(default ,3,E'2021-08-28',1,1,7700,700,1,1,NULL,NULL,NULL,NULL,E'2021-08-15 13:50:34.095119');


-- reservation_details
INSERT INTO "reservation_details"("reservation_id","reservation_date","site_rate","tax_rate","rate_type_name","created_at")
VALUES
(1,E'2021-09-02',4000,0.1,E'ハイシーズン料金',E'2021-08-15 13:45:31.934168'),
(1,E'2021-09-03',4000,0.1,E'ハイシーズン料金',E'2021-08-15 13:45:31.934168'),
(2,E'2021-08-25',7000,0.1,E'ハイシーズン料金',E'2021-08-15 13:46:07.733142'),
(3,E'2021-08-31',7000,0.1,E'ハイシーズン料金',E'2021-08-15 13:46:33.467437'),
(4,E'2021-09-15',5500,0.1,E'ハイシーズン料金',E'2021-08-15 13:48:15.109568'),
(4,E'2021-09-16',5500,0.1,E'ハイシーズン料金',E'2021-08-15 13:48:15.109568'),
(4,E'2021-09-17',5500,0.1,E'ハイシーズン料金',E'2021-08-15 13:48:15.109568'),
(5,E'2021-09-13',7000,0.1,E'ハイシーズン料金',E'2021-08-15 13:48:50.726346'),
(6,E'2021-09-18',5500,0.1,E'ハイシーズン料金',E'2021-08-15 13:49:31.789736'),
(6,E'2021-09-19',5500,0.1,E'ハイシーズン料金',E'2021-08-15 13:49:31.789736'),
(7,E'2021-08-28',7000,0.1,E'ハイシーズン料金',E'2021-08-15 13:50:34.095119');