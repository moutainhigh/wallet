#!/usr/bin/python3
#coding:utf-8

import pymysql


class DB:

    def connectdb(self, dbName, readOnly=True):
        mysqlTag = "mysql_read"
        if not readOnly:
            mysqlTag = "mysql_write"

        if dbName == "rfmember2":
            dbName = "rfmember"

        DB_CONFIG = self.envConfig.properties['db_config'][self.env][mysqlTag]['mysql'][dbName]

        if self.env == "test":
            dbName = dbName + "_test"

        # print("mysql 连接信息:{}".format(DB_CONFIG))
        connect = pymysql.Connect(
            host=DB_CONFIG.get("mysql.host"),
            port=DB_CONFIG.get("mysql.port"),
            user=DB_CONFIG.get("mysql.user"),
            passwd=DB_CONFIG.get("mysql.passwd"),
            db=dbName,
            charset='utf8'
        )
        return connect

    def connectRedis(self):
        REDIS_CONFIG = self.envConfig.properties['db_config'][self.env]["redis"]
        # print("redis 连接信息:{}".format(REDIS_CONFIG))
        return redis.Redis(REDIS_CONFIG.get("redis.host"), REDIS_CONFIG.get("redis.port"))

    def __table_exists__(self, con, table_name):
        sql = "show tables;"
        con.execute(sql)
        tables = [con.fetchall()]
        table_list = re.findall('(\'.*?\')',str(tables))
        table_list = [re.sub("'",'',each) for each in table_list]
        if table_name in table_list:
            return True
        else:
            return False





if __name__ == '__main__':
    db = DB()
    # r = db.connectRedis()
    # print(r.get("aacc"))
    c = db.connectdb("rfmember2")
