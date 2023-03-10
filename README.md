# spring-boot-issuetracker

The app is dockerized.

And can be run by 1. downloading the docker-compose.yml file to an empty folder, and then 2. running "docker compose up" from the command line. Obviously docker needs to be installed first.

Once its started - it will download the image of the databse and the image of the app from dockerhub,etc - it can be accessed from "http://localhost:8080" then.

After running it,  clean it up with " docker compose down --rmi all"

The app is also unit/integration, system tested using junit.

