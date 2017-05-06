# SocialCRON-API
[![Build Status](https://travis-ci.org/geovannyAvelar/SocialCRON-API.svg?branch=master)](https://travis-ci.org/geovannyAvelar/SocialCRON-API) [![Code Climate](https://codeclimate.com/github/geovannyAvelar/SocialCRON-CORE/badges/gpa.svg)](https://codeclimate.com/github/geovannyAvelar/SocialCRON-CORE) [![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)

SocialCRON platform RESTFul API. SocialCRON is an open-source platform to schedule posts on social media sites

## Summary
* 1 Build
    * 1.1 Pre-requisites
    * 1.2 Before build
        * 1.2.1 Database
        * 1.2.2 Facebook
    * 1.3 Build
* 2 License

## 1 Build

### 1.1 Pre-requisites

- [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
- [Git](https://git-scm.com/)
- [Maven](https://maven.apache.org/)
- [MySQL](https://www.mysql.com/)

### 1.2 Before build
#### 1.2.1 Database
This project use MySQL as relational database management system. You can obtain it [here](https://www.mysql.com/downloads/)

To configure the connection of SocialCRON with your MySQL database, open the file **src/main/resources/application.properties** and fill the properties with your username, password and database URL. The application can generate the database tables automatically, just set the property **spring.jpa.hibernate.ddl-auto** to **update**, don't use this in production.

#### 1.2.2 Facebook
To use SocialCRON, you will need a Facebook app to connect to Facebook API. You can create one accessing [Facebook Developers page](https://developers.facebook.com/). After create your app, fill the properties of file **src/main/resources/facebook4j.properties** with the id and the secret of your Facebook application.

### 1.3 Build

1. Get the source typing the command:
**git clone https://github.com/geovannyAvelar/SocialCRON-CORE.git**

2. Enter on project directory:
**cd SocialCRON-CORE**

3. With Maven, compile and package with:
**mvn install -DskipTests**

4. The depedencies will be resolved and a file named socialcron-CORE-X.war will be generated on target directory, 'X' is the version name. Now you should deploy this .war file on an application server.

## 2 License
This project is under The MIT License (MIT) terms.

[<img src="https://i1.wp.com/www.agenciacodeplus.com.br/wp-content/uploads/2017/03/cropped-logoOficial-1.png?w=200">](http://www.agenciacodeplus.com.br/)
