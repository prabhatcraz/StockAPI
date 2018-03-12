# Contents
There are two modules in this project:
* Backend  : To start a REST server
* Frontend : To start an HTML application.

## Starting Servers
There are two ways to start servers, after which one can find frontend at [http://localhost:8000/](http://localhost:8000/)
and backend at port 8080.

### Executing jars
Considering you are the root folder of this repository, there are two packaged jars which could be run

```bash
java -jar Frontend/libs/stock-api-1.0.0.jar
java -jar Backend/libs/stocks-api-1.0.0.jar
```

### Using gradle bootRun
```bash
  cd Frontend
  gradle bootRun
```

```bash
cd Backend
gradle bootRun
```

## Making requests to Backend 
### Get information about a stock:
```bash
curl -X GET http://localhost:8080/api/stocks/1
```
Sample Output:
```json
{"id":"1","name":"Apple Inc","symbol":"AAPL","price":904.72,"lastUpdateDate":1520278892481}
```

### Get all stocks in paginated manner
```bash
curl -X GET http://localhost:8080/api/stocks
```

Sample Output:
```json
{
  "page": 1,
  "items": [
    {
      "id": "11",
      "name": "Cisco Systems Inc",
      "symbol": "CSCO",
      "price": 217.82,
      "lastUpdateDate": 1520841327041
    },
    {
      "id": "12",
      "name": "Berkshire Hathaway Inc",
      "symbol": "BRK.B",
      "price": 269.17,
      "lastUpdateDate": 1520841327041
    },
    {
      "id": "1",
      "name": "Apple Inc",
      "symbol": "AAPL",
      "price": 904.72,
      "lastUpdateDate": 1520841327041
    },
    {
      "id": "2",
      "name": "Amazon.com Inc",
      "symbol": "AMZN",
      "price": 722.93,
      "lastUpdateDate": 1520841327041
    },
    {
      "id": "88964552-ebf4-4580-893d-5b9da909def1",
      "name": "MyCompany",
      "symbol": "ABCD",
      "price": 14449.2,
      "lastUpdateDate": 1520841439756
    }
  ],
  "maxPage": 3
}
```

### Create a new stock

```bash
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/stocks -d '{
    "name":"MyCompany",
    "price":14449.2, 
    "symbol": "ABCD"
}'
```

Sample output:
```json
{"id":"1","name":"MyCompany","symbol":"ABCD","price":14449.2,"lastUpdateDate":1520283700900}
```

### Update price of a stock
```bash
curl -H "Content-Type: application/json" -X PUT http://localhost:8080/api/stocks/1 -d "300"
```


## Getting Test Results
For each module (Backend and Frontend), running ```gradle test jacocoTestReport``` will generate unit test result and coverage

* Unit Test Result : ```build/reports/tests/test/index.html```
* Unit Test Coverage Report : ```build/jacocoHtml/index.html``` 
