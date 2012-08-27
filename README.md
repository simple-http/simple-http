# Simple-HTTP
## Quickly get up and running with HTTP

A simple way to build and configure a HTTP client and work with the HTTP verbs;

``` java
HttpResponse response = anApacheClient().get(url,
    headers(
        header("Accept", "application/json")
    )
);
```

Will create a HTTP client, perform a `GET` with an `Accept` header and populate a simple `HttpResponse` object. Simple-HTTP currently wraps the [Apache HTTP client](http://hc.apache.org/) but does so in a API agnostic way. Think of it as a smaller, simpler way to use Apache without all the boiler plate code.

See this [blog post](http://baddotrobot.com/blog/2012/06/10/http-simple/) for an overview.

## Features

1. No noise. Build and work with HTTP clients simply.
1. Supports the basic HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`).
1. Easy to configure. Comes out of the box with sensible defaults but is easy to customise.
1. Supplied with [Hamcrest](http://code.google.com/p/hamcrest/) `Matcher`s making testing your clients straight forward.
1. Supports typical HTTP settings; SSL, follow redirects, timeouts, proxies etc.

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

## Proxy support

You can set things up to go throw a proxy (e.g. `localhost:8888`) like this.

``` java
anApacheClient().with(proxy(new URL("http://localhost:8888"))).get(new URL("http://baddotrobot.com"));
```


## Adding Basic Auth

You could add the `Authorization` header to your requests manually or just do the following,

``` java
AuthorisationCredentials credentials = basicAuth(username, password, new URL("http://example.com")));
anApacheClient().with(credentials).get(new URL("http://example.com/something"));
```

This will add basic auth headers, similar to below

    Authorization: Basic aGVsbG86d29ybGQNCg==

to every request made to `http://example.com` (but not to other URLs).

## Adding OAuth Bearer Token

In a similar way to the basic auth headers above, you can add `Bearer` authorisation,

``` java
AuthorisationCredentials credentials = oAuth(accessToken("XystZ5ee"), new URL("http://example.com"));
HttpResponse response = anApacheClient().with(credentials).get(new URL("http://example.com/foo"));
```

This will add the

    Authorization: Bearer XystZ5ee

header to every request to `http:\\example.com`.


## Adding Different Authorisation Headers

You can even mix the schemes

``` java
AuthorisationCredentials basicAuthCredentials = basicAuth(username("username"), password("secret"), new URL("http://robotooling.com"));
AuthorisationCredentials oAuthCredentials = oAuth(accessToken("XystZ5ee"), new URL("http://baddotrobot.com"));
HttpClient http = anApacheClient().with(basicAuthCredentials).with(oAuthCredentials);
http.get(new URL("http://baddotrobot.com"));
http.get(new URL("http://robotooling.com"));
```

which will use basic auth for requests to `http://robotooling.com` and bearer token auth for requests to `http://baddotrobot.com`.


# Download

Available via my [Maven repository](http://robotooling.com/maven/).

For more tools, see [robotooling.com](http://www.robotooling.com) and visit my [blog](http://baddotrobot.com).