[![Spoutcraft][Project Logo]][Website]
What is Spoutcraft?
-------------------
Spoutcraft is the Minecraft client mod system for the Bukkit server plugin, Spout, that exposes new API for other plugins to utilize, in an attempt to bring the Minecraft multiplayer experience to parity with the single-player modding experience.

Copyright &copy; 2011, SpoutDev <[Website]>  
Spoutcraft is licensed under [GNU LESSER GENERAL PUBLIC LICENSE Version 3][License]

Who is SpoutDev?
----------------
SpoutDev is the team behind Spout, SpoutAPI, Spoutcraft, SpoutcraftAPI, and Spoutcraft Launcher.  
[![Afforess](https://secure.gravatar.com/avatar/ea0be49e1e4deac42ed9204ffd95b56c?d=mm&r=pg&s=48)](http://forums.getspout.org/members/afforess.2/) 
[![alta189](https://secure.gravatar.com/avatar/7a087430b2bf9456b8879c5469aadb95?d=mm&r=pg&s=48)](http://forums.getspout.org/members/alta189.3/) 
[![Wulfspider](https://secure.gravatar.com/avatar/6f2a0dcb60cd1ebee57875f9326bc98c?d=mm&r=pg&s=48)](http://forums.getspout.org/members/wulfspider.1/) 
[![Raphfrk](https://secure.gravatar.com/avatar/68186a30d5a714f6012a9c48d2b10630?d=mm&r=pg&s=48)](http://forums.bukkit.org/members/raphfrk.294/) 
[![narrowtux](https://secure.gravatar.com/avatar/f110a5b8feacea25275521f4efd0d7f2?d=mm&r=pg&s=48)](http://forums.getspout.org/members/narrowtux.5/) 
[![Top_Cat](https://secure.gravatar.com/avatar/defeffc70d775f6df95b68f0ece46c9e?d=mm&r=pg&s=48)](http://forums.getspout.org/members/top_cat.4/) 
[![Olloth](https://secure.gravatar.com/avatar/fa8429add105b86cf3b61dbe15638812?d=mm&r=pg&s=48)](http://forums.getspout.org/members/olloth.6/) 
[![Rycochet](https://secure.gravatar.com/avatar/b06c12e72953e0edd3054a8645d76791?d=mm&r=pg&s=48)](http://forums.getspout.org/members/rycochet.10/)

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
* No 80 column limit or midstatement newlines.
* Pull requests must compile and work.
* Pull requests must be formatted properly.
* If you change a packet or widget's read/write/number of bytes, be sure to increment the version on both the server and client.
* When modifying Notch code (Minecraft vanilla code), include `//Spout start` and `//Spout end`

**Please follow the above conventions if you want your pull request(s) accepted.**

[Project Logo]: http://cdn.getspout.org/img/logo/spoutcraft_551x150.png
[License]: http://www.gnu.org/licenses/lgpl.html
[Website]: http://www.getspout.org
[Forums]: http://forums.getspout.org
[GitHub]: https://github.com/SpoutDev/Spoutcraft
[Jenkins]: http://spout.in/ci
[Issues]: http://spout.in/issues
[Twitter]: http://spout.in/twitter
[Twitter Logo]: http://cdn.getspout.org/img/button/twitter_follow_us.png
[Facebook]: http://spout.in/facebook
[Facebook Logo]: http://cdn.getspout.org/img/button/facebook_like_us.png
[Donate]: https://www.paypal.com/cgi-bin/webscr?hosted_button_id=QNJH72R72TZ64&item_name=Spoutcraft+donation+%28from+github.com%29&cmd=_s-xclick
[Donate Logo]: http://cdn.getspout.org/img/button/donate_paypal_96x96.png
[MCP]: http://mcp.ocean-labs.de/index.php/MCP_Releases
