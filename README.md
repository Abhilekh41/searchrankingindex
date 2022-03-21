##### **`Search Ranking Index`**
    
This application provides Ranks of keywords and asins provided by users.

Please add accessKey & secretKey in application.properties file to fetch the CSV file from S3 bucket.

You can use `./gradlew bootrun` to bring the application up locally.
The application runs on `port:8081` locally.

    There are 3 REST APIs exposed in this application.
 
    http://localhost:8081/api/v1/ranking/asinKeywordCombo/{keyword}/{asin}

    http://localhost:8081/api/v1/ranking/asin/{asin}

    http://localhost:8081/api/v1/ranking/keyword/{keyword}


    