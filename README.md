# Simple-HTTP
### Quickly get up and running with HTTP

A simple way to build and configure a HTTP client and work with the HTTP verbs;

``` java
HttpResponse response = anApacheClient().get(url,
    headers(
        header("Accept", "application/json"),
    )
);
```

Will create a HTTP client, perform a `GET` with an `Accept` header and populate a simple `HttpResponse` object. Simple-HTTP currently wraps the [Apache HTTP client](http://hc.apache.org/) but does so in a API agnostic way. Think of it as a smaller, simpler API to using Apache with less boiler plate.

See this [blog post](http://baddotrobot.com/blog/2012/06/10/http-simple/) for an overview.

## Features

1. No noise. Build and work with HTTP clients simply.
1. Supports the basic HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`).
1. Easy to configure. Comes out of the box with sensible defaults but is easy to customise.
1. Supplied with [Hamcrest](http://code.google.com/p/hamcrest/) `Matcher`s making testing your clients straight forward.
1. Supports SSL and basic auth.

# Getting Started

## Create a HTTP Client

``` java
HttpClient http = HttpClients.anApacheClient();
```

That's all there is to it. Now start making HTTP requests,

## Making Requests

``` java
http.get(new URL("http://baddotrobot.com"));
```

and if you want to make the request with specific headers,

``` java
http.get(new URL("http://baddotrobot.com"),
    headers(
        header("Accept", "application/json"),
    ));
```


## Adding Basic Auth

You could add the `Authorization` header to your requests manually or just do the following,

``` java
anApacheClient().with(basicAuth(username, password, new URL("http://example.com")))).get(new URL("http://example.com/something"));
```

This will add the basic auth headers to requests made to `http://example.com` but not to other URLs.


# Download

Available via my [Maven repository](http://robotooling.com/maven/).

For more tools, see [robotooling.com](http://www.robotooling.com) and visit my [blog](http://baddotrobot.com).