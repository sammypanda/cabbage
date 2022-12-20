## Project Outline
- ~~Working file layout for minecraft plugins~~
- ~~/cabbage join command that allows you to choose a colour and remembers it~~
- ~~/cabbage join command classify a player into the team~~
- ~~/cabbage start command that teleports the joined users to a predefined location~~
- ~~/cabbage forcefinish command that forces a finish function and finishes a game~~
- ~~Cancel hits from other team members (no friendly fire)~~
- ~~Create "Cabbage Slice" item using bonemeal as the base item~~
- ~~A hit from an enemy drops one of your cabbage slices~~
- ~~No throwing cabbage slices on the ground~~
- Fix improper exiting from arena editor (when multiple participants) 
- ~~Stop players from joining multiple teams at once~~
- Add Material.DYE-chatColor translating methods
- Refactor copied loop through materials for identifying teams
- ~~When one team has all the cabbage slices end and say [team] won~~
- Rescue any abandoned cabbage slices!
- Translate the spigot boundary ideas to functioning code
- ...

<!-- OLD POINTS:
- Add a command /cabbage chest that spawns a chest somewhere like how chests would spawn at the start of a game
- Right clicking doesn't open the spawned chest and tells an expiry date (could use locked chest)
- Locked chest only opens when chest is expired
- [optional] Add effect for when chest is expired (open chest or similar)
- Add random number (under 30) of cabbage slices to expired chests
- Chest should disappear when cabbage taken
- Freeze all players who did not take that cabbage for 3 seconds
-->

## Dependencies
- Maven
  - current build command: ``mvn -f [path/to/cabbage/root/directory] clean package -U -e -q -DskipTests``

### Standard Directory Layout
> https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html
