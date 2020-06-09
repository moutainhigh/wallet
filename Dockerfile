from hub.thinkinpower.net/platform/server-jre:8u191-20200518
maintainer nzm "niezengming@rfchina.com"

workdir %{app_path}
copy  %{jar_path}/%{jar_name} %{app_path}

run adduser rfchina -g rfchina -D
run chown rfchina  %{app_path}
user rfchina

entrypoint java -jar $JAVA_OPTS %{jar_name}