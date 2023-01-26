# spring-boot-issuetracker

!!This app is hosted on heroku (Since heroku's plan has changed the pods can be only manually be switched on and off, therefore they are only running  when I am taking interviews )

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

