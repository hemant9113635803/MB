# Service1 has two project inside - service1 and service2
start Kafka server i.e zookeeper and kafka
Start both services
Specify csv and xml file path

use below url:
CSV:
GET:http://localhost:9002/api/read/all/csv
POST:http://localhost:9001/api/save/csv
{
    "name": "purta",
    "dob": "1992-02-01",
    "salary": 10000,
    "age": 56
}
PUT:
http://localhost:9001/api/update/5/csv
{
    "name": "MB",
    "dob": "1992-08-01",
    "salary": 78,
    "age": 57
}
