# stories-publisher
This project opens an endpoint on /stories. 
This endpoint will expose all the jira stories that have been worked on since the latest release.

The code expects that all the commit messages are of the form: "[<user>] <projectLabel>-<issuenumber> comments"

The code will only run in a spring-boot project. Import StoriesConfig.java in your main configuration to get things running/

## Required properties

github.user                 --> owner of the repository
github.repo                 --> repository name
github.url                  --> url to your team
github.authorization.token  --> authorization token

jira.url                    --> url to your jira
jira.project.label          --> 3 letter name of your jira-project
jira.authorization.token    --> base64 encoding of "username:pwd"

## Optional propertie
github.latest.commit        --> hash of your latest commit


