# spring-boot-issuetracker

It is a simple spring-boot demo application
the focus is on the Junit testing and the pipeline

When pushing to a branch then a docker image is being built, connected to and all the sanity tests are running against the newly built image.
On pull request the same tests are run and if the tests pass the image is pushed to dockerhub.
If the tests fail then its not possible to accept the pull request and the image is discarded.
https://github.com/norbert-s/spring-boot-issuetracker/actions

----------------------------------------------------------------------
Information about the repo

The app is unit tested - testing the service and dao layer methods in isolation by mocking, and integration/db integration tested, and the real endpoints are also tested.

these test can be run by "mvn test -Dgroups=sanity"

https://github.com/norbert-s/spring-boot-issuetracker/tree/master/src/test/java/com/issuetracker/tests

----------------------------------------------------------------------

The app is also dockerized and connected to a db. The compose setup takes care of that.






