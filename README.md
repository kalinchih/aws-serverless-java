# What's My IP

This service is made by AWS serverless to display user's IP address.

## Demo

- Website URL: https://d3w3g1kfb3wf7q.cloudfront.net/index.html
- Web API URL: https://dgugs846yfp0a.cloudfront.net/dev/my_ip_addresses

## Design

Topology

### Components

- Web API:
  - CloudFront:
    - provide URL for end-user (in front of API Gateway stage URL).
    - no cache TTL.
    - pass the 'access_token' to Web API by 'Origin Custom Headers'.
  - API Gateway:
    - handle the request/response interface.
    - edge optimized type.
    - use 'Mapping Template' to transform the request and pass to Lambda.
  - Lambda Function:
    - authorize the 'access_token' from request.
    - fetch the user and client IP addresses.
- Website:
  - CloudFront:
    - provide URL for end-user (in front of S3 website bucket).
    - enable cache and object compression.
  - S3:
    - contains website artifacts.
    - bucket and object no public.
- Builds Repository
  - S3:
    - contains API and website artifacts.
    - bucket and object no public.

### How to Get IP by Request Header?

1. Get value of request X-Forwarded-For' header. The header value contains multiple IP addresses.
2. The web API endpoint type is 'Edge optimized'. So the header value must contains 3 IP addresses at least.
3. Split the header value by comma and get the index(max_length-3) IP address as the user IP.

### How to Force S3 Requests via CloudFront Only?

1. Set S3 website bucket and object to 'no public'.
2. Creating a CloudFront 'Origin Access Identity' and adding it to the distribution.
3. Granting the 'Origin Access Identity' permission to read files in S3 website bucket.
   > Reference: [Restricting Access to Amazon S3 Content by Using an Origin Access Identity](https://docs.aws.amazon.com/en_us/AmazonCloudFront/latest/DeveloperGuide/private-content-restricting-access-to-s3.html)

### How to Authorize Requests?

Public website

- CloudFront passes a 'AccessToken' by Origin Custom Headers (HeaderName: myip-authtoken).
- Lambda function authorizes this AccessToken. If this AccessToken is not valid, the Lambda function returns 403 statusCode.

---

## Test Report

test_report.pdf

### Unit Test

jacoco

### Functional Test

- S3 Object URL: https://s3-ap-northeast-1.amazonaws.com/kalinchih-my-ip-view/index.html

  - Directly access result: AccessDenied.

- Stage URL: https://3uzizhoukf.execute-api.ap-northeast-1.amazonaws.com/dev/my_ip_addresses

  - Directly access result: 403 (forbidden).

- Cloudfront web
- Cloudfront API
- Directly S3
- Directly api
- Forge X-Forwarded-For
- upload lambda for server ip

### Performance Test

WRK
BlazeMeter
https://www.dotcom-tools.com/website-speed-test.aspx

---

## Source Codes

### Lambda Function for Web API

#### Java Classes

- MyIpApp: the Lambda handler class to retrieve user and server IP addresses.
- MyIpRequest: to wrap the request.
- MyIpResponse: to wrap the response.
- ServerIpNotFoundError: to wrap the inner exception when something wrong in retrieving server IP.
- UserIpNotFoundError: to wrap the error message when something wrong in retrieving user IP.

#### How to Build It?

Use the following [Maven](https://maven.apache.org/) command to run all unit tests and release a build.

```
mvn package
```

### Website Artifacts

- Index.html: the UI to async invoke Web API to display user & server IP addresses.
- favicon.ico: the website icon.

---

## Environment Setup and 1st-time Deployment

### Step 1: Create the Lambda artifact S3 bucket and upload Lambda build

1. Use the [/aws_cloudfront/01.my_ip-code_bucket.yaml](aws_cloudfront/01.my_ip-code_bucket.yaml) to create a CloudFormation stack.
2. Upload the Lambda build to this bucket.

### Step 2: Create API and Lambda function

1. Use the [/aws_cloudfront/02.my_ip-api.yaml](aws_cloudfront/02.my_ip-api.yaml) script to create a CloudFormation stack.

### Step 3: Setup CloudFront for web API

1. Open the [/aws_cloudfront/03.my_ip-api_cloudfront.yaml](aws_cloudfront/03.my_ip-api_cloudfront.yaml) script and modify the 'apiDomainName' parameter by API stage domain name.

2. Run the [/aws_cloudfront/03.my_ip-api_cloudfront.yaml](aws_cloudfront/03.my_ip-api_cloudfront.yaml) script to create a CloudFormation stack.

### Step 4: Create Website S3 bucket with CloudFront and upload website artifacts

1. Use the [/aws_cloudfront/04.my_ip-view_cloudfront.yaml](aws_cloudfront/04.my_ip-view_cloudfront.yaml) script to create a CloudFormation stack.

2. Upload website artifacts to the S3 bucket.

---

## New Feature or Change Deployment

### Deploy Web API

1. Upload the Lambda build to Lambda S3 bucket.
2. Update the Lambda function by the S3 link.

### Deploy Website

1. Upload the new website artifacts to website S3 bucket.
2. Create a CloundFront 'invalidation' to update CloudFront edge caches.
