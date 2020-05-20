get All Meals
<br>
$ curl -s localhost:8080/rest/meals/

get Meal (100004)
<br>
$ curl -s localhost:8080/rest/meals/get/{100004}

delete Meal (100004)
<br>
$ curl -I localhost:8080/rest/meals/delete/100004

get between
<br>
$  curl -S localhost:8080/rest/meals/filter?startDate=2020-01-31&endDate=2020-01-31&startTime=08:00&endTime=14:00

create Meal
<br>
$ curl -s -X POST -d '{"dateTime":"2020-02-01T12:00","description":"Created lunch","calories":300}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/meals/create 

update Meal
<br>
$ curl -s -X POST -d '{"dateTime":"2020-07-01T12:00","description":"fix","calories":300}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/m
eals/update/100004 