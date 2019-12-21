#!/bin/sh
mysql -h 47.52.88.176 -u $1 -p $2 < schema.sql
mysql -h 47.52.88.176 -u $1 -p $2 < data.sql
mysql -h 47.52.88.176 -u $1 -p $2 < testdata.sql
mysql -h 47.52.88.176 -u $1 -p $2 < commonTest.sql
