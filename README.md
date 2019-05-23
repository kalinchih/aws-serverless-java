# What's My IP

This service is made by AWS serverless to display user's IP address.

## Demo

Scrrenshot

- Website: https://ds5x69x17pp0m.cloudfront.net/index.html
- API Gateway: https://d31ams200lac18.cloudfront.net/dev/my_ip_addresses

## Design Document

Topology

### How to Get IP by Request Header?

### How to Force S3 Requests via CloudfFront Only?

OAI

- S3 Object URL: https://s3-ap-northeast-1.amazonaws.com/kalinchih-my-ip-view/index.html
  - Directly access result: AccessDenied.

### How to Authorize Requests?

Public website

- CloudFront passes a 'AccessToken' by Origin Custom Headers (HeaderName: myip-authtoken).
- Lambda function authorizes this AccessToken. If this AccessToken is not valid, the Lambda function returns 403 statusCode.
- Stage URL: https://q7b56svwmg.execute-api.ap-northeast-1.amazonaws.com/dev/my_ip_addresses
  - Directly access result: 403 (forbidden).

---

## Test Report

test_report.pdf

### Unit Test

jacoco

### Functional Test

- Cloudfront web
- Cloudfront API
- Directly S3
- Directly api
- Forge X-Forwarded-For

### Black Box Test

### Performance Test

BlazeMeter

---

## Source Codes

README.md
comment
unit test

### api-lambda

- Classes:
  - MyIpApp
  - MyIpRequest
  - MyIpResponse
  - ServerIpNotFoundError
  - UserIpNotFoundError

### view

- HTML:
  - index.html:

---

## Deployment Procedures
