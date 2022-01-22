# Aozora Camp Reservation

https://aozoracamprsv.com ※停止中

<img src="https://i.gyazo.com/dfa9b268f772662ec5d299dfe161edd9.png" width="600px">

## 想定する要件

- キャンプ場のWeb予約機能
- 既存の静的コンテンツから呼び出されるアプリケーション
- 会員、非会員ともに予約可能
- 会員は登録情報を更新可能
- 会員は予約内容の確認、キャンセルが可能
- 料金は現地精算だが、予約時に計算は行う
- スマートフォンでも閲覧可能

## 機能

- キャンプ予約
  - サイト一覧
  - スケジュール
  - 各種宿泊情報入力
  - 予約（会員）
  - 予約（非会員）
- マイページ
  - ログイン
  - 会員登録
  - 基本情報更新
  - メールアドレス変更
  - パスワード変更
  - 予約一覧
  - 予約詳細
  - 予約キャンセル
  
## 構成

### Application

- Java SE 11
- Spring Boot 2.5.2
- Spring Security 5.5.1
- Thymeleaf 3.0.4
- Bootstrap 4.5.3
- Tomcat(Embedded) 9.0.48
- MyBatis 3.5.7
- PostgreSQL 13.3

### Infrastructure

- AWS Elastic Beanstalk
  - ALB
  - RDS
  - CloudWatch
- Amazon Route 53
- AWS Certificate Manager
- Amazon S3

## ER図
<img src="https://i.gyazo.com/a919c7356918121707f09b6b48ed5689.png" width="800px">

## Demo

- 予約
  
[![Image from Gyazo](https://i.gyazo.com/c4168e6c4ed8ce21b5b36ecad03bc852.gif)](https://gyazo.com/c4168e6c4ed8ce21b5b36ecad03bc852)

- キャンセル
  
[![Image from Gyazo](https://i.gyazo.com/7bbc327278481cb809654542df338830.gif)](https://gyazo.com/7bbc327278481cb809654542df338830)

- スマートフォン
  
[![Image from Gyazo](https://i.gyazo.com/18ff92a86c1428f1ea270d8b6696dd7c.gif)](https://gyazo.com/18ff92a86c1428f1ea270d8b6696dd7c)
