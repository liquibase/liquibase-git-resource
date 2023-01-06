# Liquibase Git Resource
Liquibase extension to clone a remote Git repository and add it to the [Liquibase Search Path](https://docs.liquibase.com/concepts/changelogs/how-liquibase-finds-files.html).

## Supported product editions
<a href="https://www.liquibase.com/download" target="_blank"><img alt="Liquibase Open Source" src="https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Fmcred%2Fliquibase-header-footer%2Ffeature%2Fbadges%2Fbadges%2Fcommunity.json"></a>
<a href="https://www.liquibase.com/pricing/pro" target="_blank"><img alt="Liquibase Pro" src="https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Fmcred%2Fliquibase-header-footer%2Ffeature%2Fbadges%2Fbadges%2Fpro.json"></a>

## Requirements
* Liquibase 4.18.0 + 

## Installation
The easiest way to install this extension is with `lpm` [liquibase package manager](https://github.com/liquibase/liquibase-package-manager).

```shell
lpm update
lpm add liquibase-git-resource
```

## Setup
If a valid [Git URL](https://www.git-scm.com/docs/git-clone#_git_urls) is added to the [Liquibase Search Path](https://docs.liquibase.com/concepts/changelogs/how-liquibase-finds-files.html) this extension will clone the repository to a local temporary directory. The location can be specified with `liquibase.git.path`
 and by default will be removed after liquibase has finished the command.

### Optional parameters
```
--git-branch=PARAM
    Git branch to clone into git.path
    (liquibase.git.branch)
    (LIQUIBASE_GIT_BRANCH)
    [deprecated: --gitBranch]

--git-cleanup=PARAM
    Remove local repository path after run
    DEFAULT: true
    (liquibase.git.cleanup)
    (LIQUIBASE_GIT_CLEANUP)
    [deprecated: --gitCleanup]

--git-password=PARAM
    Git password for searchPath repository URL
    (liquibase.git.password)
    (LIQUIBASE_GIT_PASSWORD)
    [deprecated: --gitPassword]

--git-path=PARAM
    Path in which to clone repository
    DEFAULT: .tmp
    (liquibase.git.path)
    (LIQUIBASE_GIT_PATH)
    [deprecated: --gitPath]

--git-username=PARAM
    Git username for searchPath repository URL
    (liquibase.git.username)
    (LIQUIBASE_GIT_USERNAME)
    [deprecated: --gitUsername]
```

## Example
Cloning a Public Repository:
```properties
liquibase.searchPath= https://github.com/liquibase/liquibase-github-action-example.git
changeLogFile= example/changelogs/samplechangelog.h2.sql
```
Cloning a Private Repository:
```properties
liquibase.searchPath= https://github.com/private-org/private-repository.git
changeLogFile= example/changelogs/samplechangelog.h2.sql

git.username= username
git.password= password or personal_access_token
```

## Feedback and Issues
Please submit all feedback and issues [here]().