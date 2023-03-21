# spring-boot-issuetracker

UP NEXT

Because initially I was not satisfied with my approach of using optional-s in the tests, I started looking into it how it would be better.

Also based on this article  (https://stackoverflow.com/questions/38954742/assert-that-optional-has-certain-value).

And I found a different approch which is more to my liking in terms of usability, readability and also more concise.


I will be creating Suppliers to throw and will be using those in the Optional "orElseThrow()" clause.

then it is going to look like this

List<Issue> issues = Optional.ofNullable(issueDao.findAllByAssignee(issue.getAssignee())).orElseThrow(ThrowsWhenIssue.isNotPresent);




---------------------------------------------------------------------

Also I will be creating tests to check the exceptions in isolation. Currently they are only checked in integration. It would have been better to check them in isolation in the first place.

----------------------------------------------------------------------
Information about the repo

The app is unit tested - testing the service and dao layer methods in isolation by mocking, and integration/db integration tested, and the real endpoints are also tested.

these test can be run by "mvn test -Dgroups=sanity"


The app is also dockerized and connected to a db. The compose setup takes care of that.

In the docerized version at this point the tests are not being run automatically.

And can be run by 1. downloading the docker-compose.yml file to an empty folder, and then 2. running "docker compose up" from the command line. 

Once its started - it will download the image of the databse and the image of the app from dockerhub,etc - it can be accessed from "http://localhost:8080" then.

After running it,  clean it up with " docker compose down --rmi all"



