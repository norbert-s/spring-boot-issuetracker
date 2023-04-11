# spring-boot-issuetracker


When pushing to a branch then a docker image is being built, connected to and all the sanity tests are running against the newly built image.
If the tests fail then its not possible to accept the pull request


----------------------------------------------------------------------
Information about the repo

The app is unit tested - testing the service and dao layer methods in isolation by mocking, and integration/db integration tested, and the real endpoints are also tested.

these test can be run by "mvn test -Dgroups=sanity"


The app is also dockerized and connected to a db. The compose setup takes care of that.

In the docerized version at this point the tests are not being run automatically.

And can be run by 1. downloading the docker-compose.yml file to an empty folder, and then 2. running "docker compose up" from the command line. 

Once its started - it will download the image of the databse and the image of the app from dockerhub,etc - it can be accessed from "http://localhost:8080" then.

After running it,  clean it up with " docker compose down --rmi all"



