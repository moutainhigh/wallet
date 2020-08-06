#!/usr/bin/python3
#coding:utf-8

import pymysql


class DB:

    def connectdb(self, dbName, readOnly=True):

        connect = pymysql.Connect(
            host="conn.thinkinpower.net",
            port=23306,
            user="root",
            passwd="mysql-root-password",
            db=dbName,
            charset='utf8'
        )
        return connect

    def query(self, con, sql):

        with con.cursor() as cursor:
           cursor.execute(sql)
           data = cursor.fetchall()

           cursor.close()
        return data



if __name__ == '__main__':
    db = DB()
    con = db.connectdb("rfwallet_super_test")
    result = db.query(con,"select * from `rf_gateway_log` where method_name = 'setCompanyInfo' and is_auth = 1");
    print(result)

