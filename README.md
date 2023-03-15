# spring-boot-issuetracker

The app is unit/integration/db integration tested, tested with mockMvc and the real endpoints are also tested.

these test can be run by "mvn test -Dgroups=sanity"


The app is also dockerized.

And can be run by 1. downloading the docker-compose.yml file to an empty folder, and then 2. running "docker compose up" from the command line. Obviously docker needs to be installed first.

Once its started - it will download the image of the databse and the image of the app from dockerhub,etc - it can be accessed from "http://localhost:8080" then.

After running it,  clean it up with " docker compose down --rmi all"



