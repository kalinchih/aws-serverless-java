## View (S3)

- CloudFront URL: https://d2vr4rokdjnjeb.cloudfront.net/index.html
- S3 Object URL: https://s3-ap-northeast-1.amazonaws.com/kalinchih-my-ip-view/index.html

## API Gateway

- CloudFront URL: https://d1ckkwv3a94kwc.cloudfront.net/dev/my_ip_addresses
  - Origin Custom Headers:
    - myip-authtoken: AuthTokenFromCloudFront
    - auth by Lambda
- Stage URL: https://g0c4lg49ea.execute-api.ap-northeast-1.amazonaws.com/dev/my_ip_addresses
  - Directly access: 403 (forbidden)

## Another CloudFront

http://d1wgiepc0c6db4.cloudfront.net/index.html -> view3
