# User API Spec

## Register User

Endpoint : POST /api/users

Request Body :

```json
{
  "username" : "Username",
  "password" : "password",
  "name" : "your firstname lastname" 
}
```

Response Body (Success) :

```json
{
  "data" : "OK"
}
```

Response Body (Failed) :

```json
{
  "errors" : "Username must not blank, ???"
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body :

```json
{
  "username" : "Username",
  "password" : "password" 
}
```

Response Body (Success) :

```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 2342342423423 // milliseconds
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Username or password wrong"
}
```

## Get User

Endpoint : GET /api/users/current

Request Header :

- x-api-header : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : {
    "username" : "Username",
    "name" : "your firstname lastname"
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Unauthorized"
}
```

## Update User

Endpoint : PATCH /api/users/current

Request Header :

- x-api-header : Token (Mandatory)

Request Body :

```json
{
  "name" : "your name", // put if only want to update name
  "password" : "new password" // put if only want to update password
}
```

Response Body (Success) :

```json
{
  "data" : {
    "username" : "Username",
    "name" : "your firstname lastname"
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Unauthorized"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

- x-api-header : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : "OK"
}
```