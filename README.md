# What's My IP

This service is made by AWS serverless to display user's IP address.

## Demo Website

- CloudFront URL: https://ds5x69x17pp0m.cloudfront.net/index.html
- S3 Object URL: https://s3-ap-northeast-1.amazonaws.com/kalinchih-my-ip-view/index.html
  - Directly access result: AccessDenied.

---

## Demo API Gateway

- CloudFront URL: https://d31ams200lac18.cloudfront.net/dev/my_ip_addresses
  - CloudFront passes a 'AccessToken' by Origin Custom Headers (HeaderName: myip-authtoken).
  - Lambda function authorizes this AccessToken. If this AccessToken is not valid, the Lambda function returns 403 statusCode.
- Stage URL: https://q7b56svwmg.execute-api.ap-northeast-1.amazonaws.com/dev/my_ip_addresses
  - Directly access result: 403 (forbidden).

## Design Document

### How to Get IP by Request Header?

### How to Force S3 Requests to Go Trough CloudfFont?

### How to Authorize Requests?

---

## Test Report

unit test, function test, and black box test, performance test

---

## Source Codes

### api-lambda

- Classes:
  - MyIpApp
  - MyIpRequest
  - MyIpResponse
  - ServerIpNotFoundError

### view

- HTML:
  - index.html:

---

## Deployment Procedures
