[![Spout][Project Logo]][Website]
What is Spoutcraft?
-------------------
Spoutcraft is the Minecraft client mod system for the Bukkit server plugin, Spout, that exposes new API for other plugins to utilize, in an attempt to bring the Minecraft multiplayer experience to parity with the single-player modding experience.

Copyright (c) 2011-2012, SpoutDev <http://www.spout.org>
 
Spoutcraft is licensed under [GNU LESSER GENERAL PUBLIC LICENSE Version 3][License], but with a provision that files are released under the MIT license 180 days after they are published. Please see the `LICENSE.txt` file for details.

Who is SpoutDev?
----------------
SpoutDev is the team behind Spout, SpoutAPI, SpoutNBT, Spoutcraft, SpoutcraftAPI, Spoutcraft Launcher and LauncherAPI, SpoutPlugin, SpoutPluginAPI, Vanilla, and BukkitBridge.  
[![Afforess](https://secure.gravatar.com/avatar/ea0be49e1e4deac42ed9204ffd95b56c?d=mm&r=pg&s=48)](http://forums.spout.org/members/afforess.2/) 
[![alta189](https://secure.gravatar.com/avatar/7a087430b2bf9456b8879c5469aadb95?d=mm&r=pg&s=48)](http://forums.spout.org/members/alta189.3/) 
[![Wulfspider](https://secure.gravatar.com/avatar/6f2a0dcb60cd1ebee57875f9326bc98c?d=mm&r=pg&s=48)](http://forums.spout.org/members/wulfspider.1/) 
[![raphfrk](https://secure.gravatar.com/avatar/68186a30d5a714f6012a9c48d2b10630?d=mm&r=pg&s=48)](http://forums.spout.org/members/raphfrk.601/) 
[![narrowtux](https://secure.gravatar.com/avatar/f110a5b8feacea25275521f4efd0d7f2?d=mm&r=pg&s=48)](http://forums.spout.org/members/narrowtux.5/) 
[![Top_Cat](https://secure.gravatar.com/avatar/defeffc70d775f6df95b68f0ece46c9e?d=mm&r=pg&s=48)](http://forums.spout.org/members/top_cat.4/) 
[![Olloth](https://secure.gravatar.com/avatar/fa8429add105b86cf3b61dbe15638812?d=mm&r=pg&s=48)](http://forums.spout.org/members/olloth.6/) 
[![Rycochet](https://secure.gravatar.com/avatar/b06c12e72953e0edd3054a8645d76791?d=mm&r=pg&s=48)](http://forums.spout.org/members/rycochet.10/) 
[![RoyAwesome](https://secure.gravatar.com/avatar/6d258213c33a16465021daa8df299a0d?d=mm&r=pg&s=48)](http://forums.spout.org/members/royawesome.8/) 
[![zml2008](https://secure.gravatar.com/avatar/2320ab48d0715a4e9c73b7ec13fd6f3a?d=mm&r=pg&s=48)](http://forums.spout.org/members/zml2008.14/) 
[![Zidane](https://secure.gravatar.com/avatar/99532c7f117c8dac751422376116fb38?d=mm&r=pg&s=48)](http://forums.spout.org/members/zidane.7/) 

Visit our [website][Website] or get support on our [forums][Forums].  
Track and submit issues and bugs on our [issue tracker][Issues].

[![Follow us on Twitter][Twitter Logo]][Twitter][![Like us on Facebook][Facebook Logo]][Facebook][![Donate to the Spout project][Donate Logo]][Donate]

Source
------
The latest and greatest source can be found on [GitHub].  
Download the latest builds from [Jenkins].  

Compiling
---------
Spoutcraft requires MCP (Minecraft Coder Pack) for decompiling, deobfuscating, recompiling, and reobfuscating Minecraft's source.
There are a lot of steps involved to compile Spoutcraft, so knowing how to use MCP is a good place to start.

* Download and extract the latest compatible version of Minecraft Coder Pack.  
* Copy the latest complete, unmodified Minecraft bin directory from your .minecraft directory.  
* Place the Minecraft bin directory under MCP's jars directory.  
* Checkout or copy the conf directory from the Spoutcraft GitHub repository to MCP's conf directory.  
* Run MCP's decompile script (.bat for Windows or .sh for Linux).  
* Checkout or copy the lib directory from the Spoutcraft GitHub repository to MCP's lib directory.  
* Copy the latest SpoutcraftAPI jar into the lib directory. 
* Checkout or copy the latest src directory from the Spoutcraft GitHub repostiory to MCP's src directory.  
* Run MCP's recompile and reobfuscate scripts (.bat for Windows or .sh for Linux).  
* When tasks are finished, the compiled Spout class files will be located in reobf.  
* You'll also need to class files from the jars located in the lib directory on the Spoutcraft GitHub repository to run the client.  

**NOTE:** You will need to copy the Minecraft resources folder to the MCP root in order to launch the game using MCP.  
**NOTE:** You will need to copy the res folder to the minecraft.jar along with the compile Spoutcraft source and contents of the SpoutcraftAPI.jar for those custom resources to show.

Coding and Pull Request Formatting
----------------------------------
* Generally follow the Oracle coding standards.
* Use tabs, no spaces.
* No trailing whitespaces.
* No 80 column limit or midstatement newlines.
* Pull requests must compile and work.
* Pull requests must be formatted properly.
* Number of commits in a pull request should be kept to a minimum.
* No merges should be included in pull requests unless the pull request's purpose is a merge.
* If you change a packet or widget's read/write/number of bytes, be sure to increment the version on both the server and client.
* When modifying Notch code (Minecraft vanilla code), include `//Spout start` and `//Spout end`

**Please follow the above conventions if you want your pull request(s) accepted.**

[Project Logo]: http://cdn.spout.org/img/logo/spoutcraft_551x150.png
[License]: http://www.spout.org/SpoutDevLicenseV1.txt
[Website]: http://www.spout.org
[Forums]: http://forums.spout.org
[GitHub]: https://github.com/SpoutDev/Spoutcraft
[Jenkins]: http://spout.in/ci
[Issues]: http://spout.in/issues
[Twitter]: http://spout.in/twitter
[Twitter Logo]: http://cdn.spout.org/img/button/twitter_follow_us.png
[Facebook]: http://spout.in/facebook
[Facebook Logo]: http://cdn.spout.org/img/button/facebook_like_us.png
[Donate]: https://www.paypal.com/cgi-bin/webscr?hosted_button_id=QNJH72R72TZ64&item_name=Spoutcraft+donation+%28from+github.com%29&cmd=_s-xclick
[Donate Logo]: http://cdn.spout.org/img/button/donate_paypal_96x96.png
[MCP]: http://mcp.ocean-labs.de/index.php/MCP_Releases
