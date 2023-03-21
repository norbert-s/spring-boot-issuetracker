# spring-boot-issuetracker

update-2023.03.21

updated quality checks

---------------------------------------------------------------------
UP NEXT

I will probably be refactoring part of the test based on this article (https://stackoverflow.com/questions/38954742/assert-that-optional-has-certain-value) that i have looked into before but found that some parts might be for change. I will have a look.

Also I will be creating tests to check the exceptions in isolation. Currently they are only checked in integration. It would have been better to check them in isolation.

----------------------------------------------------------------------
Information about the repo

The app is unit tested - testing the service and dao layer methods in isolation by mocking, and integration/db integration tested, and the real endpoints are also tested.

these test can be run by "mvn test -Dgroups=sanity"


The app is also dockerized and connected to a db. The compose setup takes care of that.

In the docerized version at this point the tests are not being run automatically.

And can be run by 1. downloading the docker-compose.yml file to an empty folder, and then 2. running "docker compose up" from the command line. 

Once its started - it will download the image of the databse and the image of the app from dockerhub,etc - it can be accessed from "http://localhost:8080" then.

After running it,  clean it up with " docker compose down --rmi all"



