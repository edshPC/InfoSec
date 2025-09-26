# Работа 2: Анализ и устранение уязвимости на примере реального CVE с использованием Vulhub

Уязвимость: [CVE-2021-44228](https://github.com/vulhub/vulhub/tree/master/log4j/CVE-2021-44228)

[Оригинальный Dockerfile](https://github.com/vulhub/vulhub/blob/master/base/solr/8.11.0/Dockerfile)

[Примененный патч](https://github.com/edshPC/InfoSec/commit/077aa51a7f859fa4c5c98c8aed2092be18de577a):

```dockerfile
ENV LOG4J_MODULES="log4j-1.2-api log4j-api log4j-core log4j-layout-template-json log4j-slf4j-impl log4j-web"

RUN for i in $LOG4J_MODULES; do \
    wget --no-check-certificate \
    -O /opt/solr/server/lib/ext/$i-2.14.1.jar \
    https://repository.apache.org/content/repositories/releases/org/apache/logging/log4j/$i/2.16.0/$i-2.16.0.jar; \
done
```
