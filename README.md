# Twilio powered Payment Center

- In order to make this application work, a basic understanding of the following is assumed:
  - Java 11
  - Spring boot
  - Angular >= 9
  - mySql connectors
  - Twilio API.

- Backend: Open and change application.yml
```yml
twilio:
  account_sid: # your account sid
  auth_token: # your auth token
  trial_number: # your trial number
  url: # your url for your backend. The twilio application needs an accessible url that can be either on a host server or with the usage of the ngrok service.
  company: # your company name for the IVR
  alpha: # your alphanumeric (In case you are not sending to alpha just use a Twilio long code)
  
datasource:
  url: # your mysql url
  username: # your sql username
  password: # your sql password
```
  
- Frontend: Open and change payment.service.ts
```ts
getBackendUrl():
``` 
  substitute the <BACKEND_DOMAIN> with the domain the Java service is hosted
  
