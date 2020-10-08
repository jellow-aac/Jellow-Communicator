# Contributing to Jellow Communicator

Welome to Jellow family and **thank you** for helping with Jellow Communicator

This contributing guide is specifically about contributing to the Jellow Communicator app. Their are many ways you can contribute to Jellow.

# Table of Contents

* [Code of Conduct](#code-of-conduct)
* [Submitting Content Changes](#submitting-content-changes)
* [Cloning or Forking Code for first time](#cloning-or-forking-code-for-first-time)
* [Submitting Code Changes](#submitting-code-changes)
    - [Git Workflow](#git-workflow)
    - [Issues](#issues)
    - [Style (Java)](#style-java)
    - [Pull Requests](#pull-requests)
## Code of Conduct

Help us keep Jellow Communicator team welcoming. Please read and abide by the [Code of Conduct][coc].

## Submitting Content Changes

Please email us at <jellowcommunicator@gmail.com>

## Cloning or Forking Code for first time

To setup project locally follow below steps:
1. Clone repository or Fork the repository then clone to local machine.
2. Open Android Studio then choose option "Open an existing Android Studio project". Give the path to cloned folder and press "OK".
3. Android studio will download automatically the necessary libraries and dependancies for the project.
4. Once project successfuly is built, then try running it on any physical device or emulator.

## Submitting Code Changes

If you're new to contributing to open source on GitHub, this next section should help you get started.
If you get stuck, open an issue to ask us for help and we'll get you sorted out (and improve these instructions) [discussions][].

### Git Workflow

1. Fork and clone.
2. Add the upstream Jellow repository as a new remote to your clone.
   `git remote add upstream https://github.com/jellow-aac/Jellow-Communicator.git`
3. Create a new branch (with name of functionality )
   `git checkout -b name-of-branch`
   example: `git checkout -b language-updator-tool`
4. Commit and push as usual on your branch.
5. After you pushed the code. Discuss, review and ensured it will work correctly.
5. When you're ready, submit a pull request to staging branch. If everything is intact then we'll 
   merge it on master branch.

### Issues

We keep track of bugs, enhancements and support requests in the repository using GitHub [issues][].

### Style (Java)

We use [AOSP Java Code Style for Contributors][coding-style] 
to enforce a few style-related conventions.

### Pull Requests

When submitting a pull request, sometimes we'll ask you to make changes before
we accept the patch.

Please do not close the first pull request and open a second one with these
changes. If you push more commits to a branch that you've opened a pull
request for, it automatically updates the pull request.

As with adding more commits, you do not need to close your pull request and open a new one.
If you change the history (`rebase`, `squash`, `amend`), use `git push --force` to update the branch on your fork.
The pull request points to that branch, not to specific commits.

Here's a guide on [how to squash commits in a GitHub pull request][squash-commits].

### Deployment

- Usually, we ship/deploy code to play store. We manually sign and  create release build.
- Contributors work will get added to next public release.


[coc]: https://github.com/jellow-aac/Jellow-Communicator/blob/master/CODE_OF_CONDUCT.md
[coding-style]: https://source.android.com/setup/contribute/code-style
[squash-commits]: http://blog.steveklabnik.com/posts/2012-11-08-how-to-squash-commits-in-a-github-pull-request
[issues]: https://github.com/jellow-aac/Jellow-Communicator/issues
[discussions]: https://github.com/jellow-aac/Jellow-Communicator/issues
