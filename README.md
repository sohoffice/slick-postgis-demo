# Use PostGis with Slick3

Slick can be a bit mysteric when we have to extend it with new datatypes.

This demo project is looking to provide an example to extend slick with
the ability to work with PostGis on POSTGRESQL.


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