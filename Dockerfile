from openjdk:8-jre-alpine
maintainer nzm "niezengming@rfchina.com"

workdir #{app_path}
copy  %{jar_path}/%{jar_name} %{app_path}

entrypoint ["java","-jar","%{jar_name}"]