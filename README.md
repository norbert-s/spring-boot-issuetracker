# spring-boot-issuetracker

The app is dockerized.

And can be run by 1. downloading the docker-compose.yml file to an empty folder, and then 2. running "docker compose up" from the command line. Obviously docker needs to be installed first.

Once its started - it will download the image of the databse and the image of the app from dockerhub,etc - it can be accessed from "http://localhost:8080" then.

After running it,  clean it up with " docker compose down --rmi all"

The app is also available on heroku, however since heroku's subscription plan changed I created the dockerized versions, because with the base subscription the pods can onlybe manually switched on and off, and therefore they are not running 24/7. 




https://spring-boot-issue-tracker.herokuapp.com/

findall -get

https://spring-boot-issue-tracker.herokuapp.com/api/issues

findbyid -get

https://spring-boot-issue-tracker.herokuapp.com/api/issues/{issueid}

deletebyid -delete

https://spring-boot-issue-tracker.herokuapp.com/api/issues/{issueid}

create -post

https://spring-boot-issue-tracker.herokuapp.com/api/issues

    {
        "title": "",
        "description": "",
        "assigneeName": "",
        "status": ""
    }

update

https://spring-boot-issue-tracker.herokuapp.com/api/issues
    
    {
        "id":{id},
        "title": "",
        "description": "",
        "assigneeName": "",
        "status": ""
    }

