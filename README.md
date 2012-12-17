Spoutcraft
==========
Spoutcraft is a modified Minecraft client that adds in custom content and unlocks server-side SpoutPlugin features.

Like the project? Feel free to [donate] to help continue development!

## What is Spoutcraft?
Spoutcraft is a modified Minecraft client, which is accessible via the Spoutcraft Launcher, that boosts increased FPS, HD textures/fonts,
minimap, and an overview map. When connecting to a server with SpoutPlugin, custom blocks and items, custom guis, custom sounds, etc are
all available for use. Spoutcraft is a legacy project and will be replaced by the Spout client when finished.

[![Spoutcraft][Logo]][Homepage]  
[Homepage] | [Forums] | [Twitter] | [Facebook]

## The License
Spoutcraft is licensed under the [GNU Lesser General Public License Version 3][License].

Copyright (c) 2011-2012, Spout LLC <<http://www.spout.org/>>

## Getting the Source
The latest and greatest source can be found here on [GitHub][Source].  
Download the latest builds from our [build server][Builds]. [![Build Status](http://build.spout.org/job/Spoutcraft/badge/icon)][Builds]  

## Compiling the Source
Spoutcraft uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)  
* Checkout this repo and run: `mvn clean install`

## Using with Your Project
For those using [Maven](http://maven.apache.org/download.html) to manage project dependencies, simply include the following in your pom.xml:

    <dependency>
        <groupId>org.spoutcraft</groupId>
        <artifactId>spoutcraft</artifactId>
        <version>1.4.5-SNAPSHOT</version>
    </dependency>

If you do not already have repo.spout.org in your repository list, you will need to add this also:

    <repository>
        <id>spout-repo</id>
        <url>http://repo.spout.org</url>
    </repository>

## Coding and Pull Request Conventions
* Generally follow the Oracle coding standards.
* No spaces, only tabs for indentation.
* No trailing whitespaces on new lines.
* 200 column limit for readability.
* Pull requests must compile, work, and be formatted properly.
* Sign-off on ALL your commits - this indicates you agree to the terms of our license.
* No merges should be included in pull requests unless the pull request's purpose is a merge.
* Number of commits in a pull request should be kept to *one commit* and all additional commits must be *squashed*.
* You may have more than one commit in a pull request if the commits are separate changes, otherwise squash them.
* When modifying NMS code (Minecraft code), include `// Spout start` and `// Spout end`
* For clarification, see the full pull request guidelines [here](http://spout.in/prguide).

**Please follow the above conventions if you want your pull request(s) accepted.**

[Logo]: http://cdn.spout.org/spoutcraft-github.png
[Homepage]: http://www.spout.org
[Forums]: http://forums.spout.org
[License]: http://www.gnu.org/licenses/lgpl.html
[Source]: https://github.com/SpoutDev/Spoutcraft
[Builds]: http://build.spout.org/job/Spoutcraft
[Issues]: http://issues.spout.org/browse/SPOUTCRAFT
[Twitter]: http://spout.in/twitter
[Facebook]: http://spout.in/facebook
[Donate]: http://spout.in/donate