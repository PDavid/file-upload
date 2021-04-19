## Upload a file
POST /api/upload
curl -v -i -X POST -H "Content-Type: multipart/form-data"  -F "file=@/home/paksyd-private/Downloads/Titanic_20_Mb_Data.xlsx" http://localhost:8080/api/upload

HTTP 202 Accepted
```json
{
  "id": "ac5a0464-20a3-423d-b9d4-7ecc52cb1504"
}
```

### Get status of a file
Request:

POST /api/status/<id>

```
curl --header "Content-Type: application/json" \
--request POST \
--data '{"id":"5254db51-9efc-4141-b912-8b1cbf192cce"}' \
http://localhost:8080/api/status
```

HTTP 200 OK
```json
{
  "status": "converting"
}
```

Possible states:
- uploading,
- upload_error,
- converting,
- conversion_error,
- converted

### Get file content

GET /api/file/<id>

HTTP 200 OK

```json
{
  "rows":[
    {
      "columns":[
        "0.0",
        "First Name",
        "Last Name",
        "Gender",
        "Country",
        "Age",
        "Date",
        "Id"
      ]
    },
    {
      "columns":[
        "1.0",
        "Dulce",
        "Abril",
        "Female",
        "United States",
        "32.0",
        "15/10/2017",
        "1562.0"
      ]
    }
  ]
}
```