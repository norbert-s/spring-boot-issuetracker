# spring-boot-issuetracker

Since heroku's subscription plan has changed and the pods can onlybe manually switched on and off, they are not running 24/7.

As a workaround the app can be run by running "docker compose up" from the command line. Obviously docker needs to be installed first.

Once its started - it will download the image of the app from dockerhub,etc - it can be accessed from "http://localhost:8080"

After running it,  clean up with " docker compose down --rmi all"


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

