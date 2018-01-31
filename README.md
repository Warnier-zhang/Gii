Gii
===

与[MyBatis Generator][2]的作用类似，Gii是一个用来帮助MyBatis 3用户快速、高效地生成DOMAIN类和MAPPER映射的小工具。
Gii是一个Web App，采用耳熟能详的SSM（Struts 2、Spring 3、MyBatis 3）技术栈实现，它脱胎于 [Yii 2 Gii Extension][1]，沿袭了后者的设计思想、编程思路和使用方式，甚至某些方面直接`fork`了后者的代码。

快速开始
======

#### 1、安装gii.jar；

安装方式非常简单，可以选择自己喜欢的下载工具从https://github.com/Warnier-zhang/Gii/releases处下载各个版本的jar文件，然后复制到当前项目的`WEB-INF/lib`目录即可！

Maven用户可以参考下面的脚本把gii.jar安装到本地仓库：

```
mvn install:install-file -Dfile="C:\**\gii-*.*.*.jar" 
-DgroupId="org.warnier.zhang.gii" 
-DartifactId="gii" 
-Dversion="*.*.*" 
-Dpackaging="jar"
```

#### 2、配置web.xml；

```
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath*:applicationContext-gii.xml</param-value>
</context-param>
...
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

#### 3、配置struts.properties；

```
struts.enable.DynamicMethodInvocation = true
struts.serve.static=true
struts.convention.action.includeJars=.*?/gii-*.*?jar(!/)?
```

#### 4、配置gii.properties；

gii.properties是Gii的核心配置文件，与生成DOMAIN类和MAPPER映射相关的参数设定、默认值以及Gii特性的开启方法都在这个文件中。具体如何配置，参考文中注释；

```
# DB 连接参数；
# 目前情况下，仅支持Oracle，陆续会增加支持MySQL！
gii.dba.driverClassName=oracle.jdbc.driver.OracleDriver
gii.dba.url=
gii.dba.username=
gii.dba.password=

# DOMAIN类相关；
# 配置DOMAIN类包名；
gii.pkg.domain.packageName=

# 配置DOMAIN类路径；
gii.src.domain.absoluteURL=

# 配置是否使用注解（Getter && Setter），默认值为否；
gii.cfg.domain.addAnnotation=0

# 配置是否添加注释，默认值为是；
gii.cfg.domain.enableComment=1

# 配置是否自动把BLOB字段值转成字符串，默认值为是；
gii.cfg.domain.convertBinary=1

# MAPPER类相关；
# 配置MAPPER类包名；
gii.pkg.mapper.packageName=

# 配置MAPPER类路径；
gii.src.mapper.absoluteURL=

# 配置是否同时生成Mapper映射文件，默认值为是；
gii.cfg.mapper.generateQuery=1

# 配置是否继承Mapper接口，默认值为是；
gii.cfg.mapper.extendsMapper=1
```

常见问题
======

1. 选用Spring MVC而不是Struts 2的项目如何使用？
目前情况下，Gii没有打包适合Spring MVC的jar文件以供下载，有需要的话请自行修改下面的`Resource`过滤脚本，并参考[pom.xml][3]导入必须的依赖文件。

```
<resources>
    <resource>
        <directory>src/main/resources</directory>
        <excludes>
            <exclude>struts.properties</exclude>
            <exclude>gii.properties</exclude>
        </excludes>
    </resource>
    <resource>
        <directory>src/main/webapp</directory>
        <excludes>
            <exclude>WEB-INF/web.xml</exclude>
        </excludes>
        <includes>
            <include>index.ftl</include>
            <include>css/*.css</include>
            <include>img/*.jpg</include>
            <include>img/*.png</include>
            <include>layouts/*.ftl</include>
            <include>lib/**/*.*</include>
            <include>modules/**/*.ftl</include>
        </includes>
        <targetPath>/static/</targetPath>
    </resource>
</resources>
```

许可证书
======

    Copyright (C) 2017 Warnier-zhang.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

参与贡献
======



开发人员
======

* [Warnier-zhang<warnier.zhang@gmail.com>](https://github.com/Warnier-zhang/)

[1]:https://github.com/yiisoft/yii2-gii
[2]:https://github.com/mybatis/generator
[3]:https://github.com/Warnier-zhang/Gii/blob/master/pom.xml
