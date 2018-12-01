# DecentWorkMobile
Mobile App for decent work WEB

## Installation
 
 ```
 git clone https://github.com/TeamYuml/DecentWorkMobile.git
```
### Prerequisites 
``` 
Android Studio v 3.2.1
SDK 26 
Java 8.1 
```

### Google token oauth2:
In order to google sign in working well with web server you have to set up google credentials and receive Google client ID for
mobile application.
When you obtain the client ID you need to find .gradle directory in you OS:
In Windows it maybe in:
```
C:\Users\<username>\.gradle\
```
Linux:
```
/home/<username>/.gradle/
```
After you find this directory go edit gradle.properties file if its not there just create it and then write:
```
DecentWork_GoogleClientID="your_client_id"
```
