# bad.robot.simple-http

A simple way to build a HTTP client and work with the HTTP verbs;

        HttpResponse response = anApacheClient().with(username, password).get(url,
            headers(
                header("Accept", "application/json"),
            )
        );

Will create a HTTP client, perform a `GET` with an `Accept` header and populate a simple `HttpResponse` object that can be interrogated. We have `Matchers` to help with your testing too!

## features

1. No noise. Build and work with HTTP clients simply.
1. Supports the basic HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`)
1. Easy to configure. Comes out of the box with sensible defaults but is easy to customise
1. Baked in support for instrumentation (view timings, request/responses including underlying TCP messages) 
1. Supplied with [Hamcrest](http://code.google.com/p/hamcrest/) `Matcher`s making testing your clients straight forward

## more.tools

For more tools and to download, see [robotooling.com](http://www.robotooling.com)