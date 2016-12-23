# stories-publisher
This project will analyse the commit messages and return a list of jira issues based on that analysis
The code expects that all the commit messages are of the form: "[\<user\>] \<projectLabel\>-\<issuenumber\> comments"

Following calls are supported
* ``GET stories/issue?since=yyyy-MM-dd``
* ``GET stories/issue/sinceLatestRelease``

The code will only run in a spring-boot project. Import StoriesConfig.java in your main configuration to get things running
Your versioning system has to be either github or gitlab

## Required properties

* ``git.repository``           --> repository name e.g. ``vdab/vrijstellingenbeleid``
* ``git.url``                  --> url to your git api e.g. ``gitlab.com/api/v3`` or ``http://api.github.com``
* ``git.authorization.token``  --> authorization token for your git (private token for gitlab)
* ``git.isGithub``             --> is your git on github or gitlab?
 

* ``jira.url``                    --> url to your jira
* ``jira.authorization.token``    --> base64 encoding of "username:pwd"

## Optional property
- ``github.latest.commit``        --> hash of your latest commit


