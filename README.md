# Contents
There are two modules in this project:
* Backend  : To start a REST server
* Frontend : To start a HTML application.

## How to start servers?
cd to Module folders and run
 
 ```cd Backend && gradle bootRun```

## Backend
Starts a rest server at port 8080

### Getting information about a stock:
```curl -X GET http://localhost:8080/api/stock/1```
output:
```{"id":"1","name":"Apple Inc","symbol":"AAPL","price":904.72,"lastUpdateDate":1520278892481}```
### Posting a new stock
```curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/stock/1 -d '{"id":"1","name":"MyCompany","price":14449.2, "symbol": "ABCD"}'```
output:
```{"id":"1","name":"MyCompany","symbol":"ABCD","price":14449.2,"lastUpdateDate":1520283700900}```
### Updating a stock


## Test
Running ```gradle test jacocoTestReport``` will generate test result and coverage
* Test Result : ```build/reports/tests/test/index.html```
* Coverage Report : ```build/jacocoHtml/index.html``` 
