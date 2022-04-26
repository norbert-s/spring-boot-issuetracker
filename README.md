# spring-boot-issuetracker

this app is hosted on heroku (please be patient the fist time you start the app because it will take about 15-20 seconds to load for it is in sleeping mode - it is obviously not a production app)

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

