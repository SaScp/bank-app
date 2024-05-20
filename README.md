# Bank API 
API для банковских операций.
<br>
Вы можете получить доступ к Swagger и просмотреть все доступные конечные точки, посетив http://localhost:8080/swagger-doc/swagger-ui/index.html или http://localhost/swagger-doc/swagger-ui/index.html, если вы запускаекте проект через контейнер
## Переменные среды

Чтобы запустить это приложение, вам необходимо создать <code>.env</code> файл в корневом каталоге со следующими средами:

<code>HOST</code>- хост базы данных Postgresql
<br>
<code>PORT</code>— порт базы данных Postgresql
<br>
<code>POSTGRES_USERNAME</code>- имя пользователя для базы данных Postgresql
<br>
<code>POSTGRES_PASSWORD</code>- пароль для базы данных Postgresql
<br>
<code>POSTGRES_DATABASE</code>- имя базы данных Postgresql
<br>

<code>MONGO_DATABASE</code> - имя базы данны MongoDB
<br>
<code>MONGO_PASSWORD</code> - пароль для базы данных MongoDB
<br>
<code>MONGO_USERNAME</code> - имя пользователя для базы данных MongoDB 
<br>
<code>MONGO_HOST</code> - имя хоста для базы данных MongoDB
<br>
<code>MONGO_PORT</code> - порт хоста для базы данных MongoDB

## Docker
После создания <code>.env</code> файла нужно запустить Docker compose в консоли в основном каталоге данного API с помощью команды: <code>docker-compose --profile <profile_name> up</code> за место <profile_name> можно подставить main для запуска приложение из контейнера или dev для запуска из IDEA(по умолчанию стоит dev)
<br>
<br>
Для того, чтобы обновить основной контейнер приложения в Docker нужно воспользоваться командой
<br>
<code>docker-compose --profile <profile_name> bulid</code>
