# Use PostGis with Slick3

Slick can be a bit mysteric when we have to extend it with new datatypes.

This demo project is looking to provide an example to extend slick with
the ability to work with PostGis on POSTGRESQL.


## Overview

This is a play 2.6 project. Slick is integrated via the play-slick project.
The other relevant dependencies are as below:

- Slick 3.2.0.
- postgis-jdbc 2.2.1
- postgresql driver 42.2.2

The test runs on JDK 1.8, scala 2.12.2. Using postgresql 10 + PostGis 2.4.

## Preparation

We need to have a postgresql database with postgis installed.

Install vagrant and use `vagrant up` to provision a testing postgresql.

Manually prepare the database by creating a new user and database.

```sql
-- connect with postgres/postgres
CREATE USER test WITH CREATEDB LOGIN INHERIT PASSWORD 'test';
CREATE DATABASE test OWNER test;

-- connect with postgres/postgres with database test. Ex: \c test in psql
CREATE EXTENSION postgis;
```

If you wish to use an existing postgresql database, please change
`conf/application.conf` accordingly.

## Running

The project has no api or frontend, execute the few test cases from sbt.

```
sbt test
```