delete from reservation_details;
delete from reservations;
delete from site_availability;
delete from calendar;
delete from site_rates;
delete from rate_types;
delete from sales_tax;
delete from site_types;
delete from members;
delete from roles;

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
(1, '藤村忠寿', 'aozora@camp.com', '$2a$10$rJyapIrvsHARwCNgporWLO6QIKXXezOpRrdb..7X0ea0VwZ5IldSy', '09012345678', 'ROLE_GENERAL', default, default)
,(2,'嬉野雅道','aozora2@camp.com','$2a$10$bWrKNHFJ4bDoUl/hSt6GoeKm1QHdAkkrFDV.Lh5SKEumpg9kAZ8TO','09088888888','ROLE_GENERAL',default, default)
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


INSERT INTO "reservations"("id","site_type_id","date_from","stay_days","number_of_people","total_amount_tax_incl","sales_tax","reservation_method","member_id","non_member_name","non_member_mail","non_member_phone_number","canceled_at","created_at")
VALUES
(9,1,E'2021-08-31',2,2,9900,900,E'1',1,NULL,NULL,NULL,NULL,E'2021-08-10 00:02:39.238429'),
(10,2,E'2021-09-03',1,1,5500,500,E'1',1,NULL,NULL,NULL,NULL,E'2021-08-10 00:02:53.467315'),
(11,3,E'2021-08-31',2,1,16500,1500,E'1',1,NULL,NULL,NULL,NULL,E'2021-08-10 00:03:18.611791'),
(12,1,E'2021-09-09',1,1,3300,300,E'1',1,NULL,NULL,NULL,NULL,E'2021-08-10 00:03:28.049619'),
(13,2,E'2021-08-12',1,1,8800,800,E'1',1,NULL,NULL,NULL,NULL,E'2021-08-10 00:03:56.011949'),
(14,3,E'2021-08-13',1,1,9900,900,E'1',1,NULL,NULL,NULL,NULL,E'2021-08-10 00:04:04.620321'),
(15,3,E'2021-09-09',2,2,13200,1200,E'1',1,NULL,NULL,NULL,NULL,E'2021-08-10 00:04:32.135804');


INSERT INTO reservation_details("reservation_id","reservation_date","site_rate","tax_rate","rate_type_name","created_at")
VALUES
(9,E'2021-08-31',6000,0.1,E'ハイシーズン料金',E'2021-08-10 00:02:39.238429'),
(9,E'2021-09-01',3000,0.1,E'通常料金',E'2021-08-10 00:02:39.238429'),
(10,E'2021-09-03',5000,0.1,E'通常料金',E'2021-08-10 00:02:53.467315'),
(11,E'2021-08-31',9000,0.1,E'ハイシーズン料金',E'2021-08-10 00:03:18.611791'),
(11,E'2021-09-01',6000,0.1,E'通常料金',E'2021-08-10 00:03:18.611791'),
(12,E'2021-09-09',3000,0.1,E'通常料金',E'2021-08-10 00:03:28.049619'),
(13,E'2021-08-12',8000,0.1,E'ハイシーズン料金',E'2021-08-10 00:03:56.011949'),
(14,E'2021-08-13',9000,0.1,E'ハイシーズン料金',E'2021-08-10 00:04:04.620321'),
(15,E'2021-09-09',6000,0.1,E'通常料金',E'2021-08-10 00:04:32.135804'),
(15,E'2021-09-10',6000,0.1,E'通常料金',E'2021-08-10 00:04:32.135804');