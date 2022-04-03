
## spring cloud gatewag [Actuator API](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#actuator-api)

##1. Verbose Actuator Format
```shell
curl --location --request GET 'http://localhost:8080/actuator/gateway/routes'
```
返回内容
```json
[{"predicate":"Paths: [/get], match trailing slash: true","route_id":"path_route","filters":[],"uri":"http://localhost:8082","order":0},{"predicate":"Hosts: [*.myhost.org]","route_id":"host_route","filters":[],"uri":"http://httpbin.org:80","order":0},{"predicate":"Hosts: [*.rewrite.org]","route_id":"rewrite_route","filters":["[[RewritePath /foo/(?<segment>.*) = '/${segment}'], order = 0]"],"uri":"http://httpbin.org:80","order":0},{"predicate":"Hosts: [*.circuitbreaker.org]","route_id":"circuitbreaker_route","filters":["[[SpringCloudCircuitBreakerResilience4JFilterFactory name = 'slowcmd', fallback = [null]], order = 0]"],"uri":"http://httpbin.org:80","order":0},{"predicate":"Hosts: [*.circuitbreakerfallback.org]","route_id":"circuitbreaker_fallback_route","filters":["[[SpringCloudCircuitBreakerResilience4JFilterFactory name = 'slowcmd', fallback = forward:/circuitbreakerfallback], order = 0]"],"uri":"http://httpbin.org:80","order":0},{"predicate":"(Hosts: [*.limited.org] && Paths: [/anything/**], match trailing slash: true)","route_id":"limit_route","filters":["[org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory$$Lambda$705/871153004@4db537bd, order = 0]"],"uri":"http://httpbin.org:80","order":0},{"predicate":"Paths: [/echo], match trailing slash: true","route_id":"websocket_route","filters":[],"uri":"ws://localhost:9000","order":0}]%   
```

##2. Creating Route
```shell
curl --location --request POST 'http://localhost:8080/actuator/gateway/routes/grape' \
--header 'JwtToken: token' \
--header 'Content-Type: application/json' \
--data-raw '    {
        "predicates": [
                {
                    "name": "Path",
                    "args": {
                        "pattern": "/grape"
                    }
                }
            ],
        "route_id": "grape",
        "filters": [],
        "uri": "http://www.graprfruit.com.cn/grape/",
        "order": 0
    }'
```
gateway log
```log
2022-04-04 06:47:16.868 DEBUG 37480 --- [ctor-http-nio-3] o.s.c.g.a.GatewayControllerEndpoint      : Saving route: RouteDefinition{id='grape', predicates=[PredicateDefinition{name='Path', args={pattern=/grape}}], filters=[], uri=http://www.graprfruit.com.cn/grape/, order=0, metadata={}}
```

##3. refresh routes
```shell
curl --location --request POST 'http://localhost:8080/actuator/gateway/refresh'
```
```log
2022-04-04 06:48:53.330 DEBUG 37480 --- [     parallel-4] o.s.c.g.r.RouteDefinitionRouteLocator    : RouteDefinition grape applying {pattern=/grape} to Path
2022-04-04 06:48:53.392 DEBUG 37480 --- [     parallel-4] o.s.c.g.r.RouteDefinitionRouteLocator    : RouteDefinition matched: grape
```

##4.route
```shell
curl --location --request GET 'http://127.0.0.1:8080/grape' \
--header 'JwtToken: token12345'
```
```html
<!DOCTYPE html>
<html lang="">

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width,initial-scale=1.0">
	<link rel="icon" href="/grape/favicon.ico">
	<title>jenkinsweb8082</title>
	<link href="/grape/about.1646869027961.js" rel="prefetch">
	<link href="/grape/app.1646869027961.js" rel="preload" as="script">
	<link href="/grape/chunk-vendors.1646869027961.js" rel="preload" as="script">
</head>

<body>
	<noscript>
		<strong>We're sorry but jenkinsweb doesn't work properly without JavaScript enabled. Please enable it to continue.</strong>
	</noscript>
	<div id="app"></div>
	<!-- built files will be auto injected -->
	<script type="text/javascript" src="/grape/chunk-vendors.1646869027961.js"></script>
	<script type="text/javascript" src="/grape/app.1646869027961.js"></script>
</body>

</html> 
```

