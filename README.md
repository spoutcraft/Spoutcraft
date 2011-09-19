[![][Project Logo]][Website]
Spoutcraft
==========
Spoutcraft is the Minecraft client mod system for the Bukkit server plugin, Spout,  that exposes new API for other plugins to utilize, in an attempt to bring the Minecraft Multiplayer experience to parity with the single player modding experience.

Copyright &copy; 2011, SpoutDev <dev@getspout.org>  
Spoutcraft is licensed under [GNU LESSER GENERAL PUBLIC LICENSE Version 3][License]

Visit our [website][Website].  
Get support on our [Bukkit forum thread][Forum].  
Track and submit issues and bugs on our [issue tracker][Issues].

Follow Spoutcraft on Twitter [@SpoutDev][Twitter]

Source
------
The latest and greatest source of Spoutcraft can be found on [GitHub].  
Download the latest builds from [Jenkins].  

Compiling
---------
* Download and extract the latest compatible version of Minecraft Coder Pack.  
* Copy the latest complete, unmodified Minecraft bin directory from your .minecraft directory.  
* Place the Minecraft bin directory under MCP's jars directory.  
* Run MCP's decompile script (.bat for Windows or .sh for Linux).  
* Checkout or copy the conf directory from the Spoutcraft GitHub repository to MCP's conf directory.  
* Checkout or copy the lib directory from the Spoutcraft GitHub repository to MCP's lib directory.  
* Checkout or copy the latest src directory from the Spoutcraft GitHub repostiory to MCP's src directory.  
* Run MCP's recompile and reobfuscate scripts (.bat for Windows or .sh for Linux).  
* When tasks are finished, the compiled Spout class files will be located in reobf.  
* You'll also need to class files from the jars located in the lib directory on the Spoutcraft GitHub repository to run the client.  

Coding and Pull Request Formatting
----------------------------------
* Generally follows the Oracle coding standards.
* Spoutcraft uses tabs, no spaces.
* No 80 column limit or midstatement newlines.
* Pull requests must compile and work.
* Pull requests must be formatted properly.
* When modifying Notch code (Minecraft vanilla code), include `//Spout start`and `//Spout end`

Please follow the above conventions if you want your pull requests accepted.

[Project Logo]: http://cdn.getspout.org/img/logo/spoutcraft_551x150.png
[License]: http://www.gnu.org/licenses/lgpl.html
[Website]: http://www.getspout.org
[Forum]: http://spout.in/bukkit
[GitHub]: https://github.com/SpoutDev/Spoutcraft
[Jenkins]: http://ci.getspout.org/job/Spoutcraft
[Issues]: https://github.com/SpoutDev/Spout/issues
[Twitter]: http://twitter.com/SpoutDev
[MCP]: http://mcp.ocean-labs.de/index.php/MCP_Releases
