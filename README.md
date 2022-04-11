# spring-boot-issuetracker

this app is hosted on heroku

https://spring-boot-issuetracker.herokuapp.com/

findall -get
https://spring-boot-issuetracker.herokuapp.com/api/issues

findbyid -get
https://spring-boot-issuetracker.herokuapp.com/api/issues/{issueid}

deletebyid -delete
https://spring-boot-issuetracker.herokuapp.com/api/issues/{issueid}

create -post
https://spring-boot-issuetracker.herokuapp.com/api/issues
    {
        "title": "",
        "description": "",
        "assigneeName": "",
        "status": ""
    }

update
https://spring-boot-issuetracker.herokuapp.com/api/issues
    {
        "id":{id}
        "title": "",
        "description": "",
        "assigneeName": "",
        "status": ""
    }

