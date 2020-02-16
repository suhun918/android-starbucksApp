# android-starbucksApp

#### 1. MySQL 한글 설정,  (my.ini)
```ini
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
collation-server = utf8_unicode_ci
init-connect='SET NAMES utf8'
init_connect='SET collation_connection = utf8_general_ci'
character-set-server=utf8
lower_case_table_names = 1
```

#### 2. 사용자 생성 및 권한 주기 및 DB 생성
- create user 'starbucks'@'%' identified by 'bitc5600';
- GRANT ALL PRIVILEGES ON 별.별 TO 'starbucks'@'%';
- create database starbucks;
- use starbucks;

```sql
CREATE TABLE coffee(
    id int auto_increment primary key,
    name varchar(100) not null unique,
    detail varchar(200) not null,
    price int not null,
    flavor varchar(100) CHECK(flavor IN('짭짤한 맛','과일 맛','구운 맛','달콤한(캐러멜) 맛')),
    feel varchar(100) CHECK(feel IN('상쾌하고 부드러운 느낌','오랫동안 여운이 남는 느낌')),
    strong varchar(100) CHECK(strong IN('은은하고 부드러움','강도가 세고 강렬함')),
    roast varchar(100) CHECK(roast IN('블론드 로스트','미디엄 로스트','다크 로스트')),
    createDate timestamp,
    image varchar(300) default '/image/coffee/default.png'
) engine=InnoDB default charset=utf8;
```

```sql
CREATE TABLE beverage(
    id int auto_increment primary key,
    name varchar(100) not null unique,
    price int not null,
    category varchar(100) CHECK(category IN('콜드 브루 커피','브루드 커피','에스프레소','프라프치노','블렌디드','스타벅스 피지오','티(티바나)','기타 제조 음료','스타벅스 주스(병음료)')),
    createDate timestamp,
    image varchar(300) default '/images/beverage/default.png'
) engine=InnoDB default charset=utf8;
```

```sql
CREATE TABLE food(
    id int auto_increment primary key,
    name varchar(100) not null unique,
    price int not null,
    category varchar(100) CHECK(category IN('베이커리','케이크','샌드위치와 샐러드','따뜻한 푸드','과일과 요거트','스낵과 미니 디저트','아이스크림')),
    createDate timestamp,
    image varchar(300) default '/images/beverage/default.png'
) engine=InnoDB default charset=utf8;
```

```sql
create table card(
    id int auto_increment primary key,
    name varchar(30) not null unique,
    image varchar(300) default "/image/card/default.png",
    createDate timestamp
)engine=InnoDB default charset=utf8;
```

```sql
create table user(
    id int auto_increment primary key,
    username varchar(30) not null unique,
    name varchar(30) not null,
    password varchar(100),
    money int default 0,
    email varchar(100),
    provider varchar(100),
    providerId varchar(100),
    level varchar(30)  default 'Bronze',
    createDate timestamp
)engine=InnoDB default charset=utf8;
```

```sql
create table trade(
	id int auto_increment primary key,
    userId int,
    name varchar(100) not null,
	price int not null,
	amount int not null,
	createDate timestamp,
    foreign key(userId) references user(id)
)engine=InnoDB default charset=utf8;
```

```sql
create table cart(
	id int auto_increment primary key,
    userId int,
    name varchar(100) not null,
	price int not null,
	createDate timestamp,
    foreign key(userId) references user(id)
)engine=InnoDB default charset=utf8;
```

```sql
create table user_card(
    id int auto_increment primary key,
    userId int,
    cardId int,
    cardName varchar(30) not null,
    cardImage varchar(300),
    point int default 0,
    createDate timestamp,
    foreign key(userId) references user(id),
    foreign key(cardId) references card(id)
)engine=InnoDB default charset=utf8;
```

```sql
create table myCafe(
    id int auto_increment primary key,
    userId int,
    name varchar(50) not null,
    address varchar(50),
    tel varchar(20),
    lat varchar(30),
    lng varchar(30),
    createDate timestamp
)engine=InnoDB default charset=utf8;
```

```sql
create table myBeverage(
    id int auto_increment primary key,
    userId int,
    beverageId int,
    beverageName varchar(100) not null,
    price int not null,
    createDate timestamp,
    foreign key(userId) references user(id),
    foreign key(beverageId) references beverage(id) on delete cascade
)engine=InnoDB default charset=utf8;
```

```sql
create table myCoffee(
    id int auto_increment primary key,
    userId int,
    coffeeId int,
    coffeeName varchar(100) not null,
     price int not null,
    createDate timestamp,
    foreign key(userId) references user(id),
    foreign key(coffeeId) references coffee(id) on delete cascade
)engine=InnoDB default charset=utf8;
```

```sql
create table board(
    id int auto_increment primary key,
    title varchar(100) not null,
    content longtext,
    userId int,
    createDate timestamp
) engine=InnoDB default charset=utf8;
```

#### 3. 환경설정

```Gradle
dependencies {
    implementation 'com.squareup.picasso:picasso:2.71828'
    implementation 'com.squareup.retrofit2:retrofit:2.1.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.1.0'
    implementation group: 'org.apache.httpcomponents', name: 'httpcore', version: '4.4.12'
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation 'com.google.android.material:material:1.2.0-alpha03'
    compileOnly 'org.projectlombok:lombok:1.18.10'
    annotationProcessor 'org.projectlombok:lombok:1.18.10'
    implementation files('src\\main\\jniLibs\\libDaumMapAndroid.jar')
    implementation group: 'com.kakao.sdk', name: 'usermgmt', version: project.KAKAO_SDK_VERSION
    implementation 'com.github.bumptech.glide:glide:4.11.0'
}
```

```Manifest
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.cos.mystarbucks">
    <!-- 네트워크 사용에 대한 퍼미션 -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

    <application
        android:name=".GlobalApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme"
        android:usesCleartextTraffic="true">
        <activity android:name=".PurchaseDoneActivity"></activity>
        <activity android:name=".PayActivity">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="iamporttest" />
            </intent-filter>
        </activity>
        <activity android:name=".CartActivity" />

        <meta-data
            android:name="com.kakao.sdk.AppKey"
            android:value="@string/kakao_app_key" />

        <activity
            android:name=".SplashActivity"
            android:theme="@style/SplashTheme">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".KJoinActivity" />
        <activity android:name=".PurchaseActivity" />
        <activity
            android:name=".LoginActivity"
            android:configChanges="orientation|screenSize" />
        <activity android:name=".MenuActivity" />
        <activity android:name=".WhatsNewActivity" />
        <activity android:name=".StoreActivity" />
        <activity android:name=".SirenOrderActivity" />
        <activity android:name=".CardActivity" />
        <activity android:name=".MainActivity" />
        <activity android:name=".JoinActivity" />
        <activity android:name=".MyPageActivity" />
        <activity android:name=".WNDetailActivity" />
    </application>

</manifest>
```
#### 4. Blog 주소
<https://blog.naver.com/suhun918/221762904652>

#### 5. Youtube 주소
<https://www.youtube.com/watch?v=fbDf84QPq9g>
