MGFrame
========

Prototype Flow:
====
- Preload
  - Find and cache all worldnames (in the world folder) somewhere
  - Initialize all Bukkit-specific interfaces
  - Initialize configuration
  - Final check method to test if everything is loaded correctly, disable if it fails

- Adding Maps (maps/)
  - Configured maps rendered unusuable if the world it was made for doesn't exist

  - Command to mark a world as a map by name; doing this will most likely render the world unusuable by normal means
    - Maps will never save an inventory
    - Maps will never automatically world save

  - Command to mark a map as edit mode, in-game tools or config editing will be used
    - Possibly gamemode-specific world configurations, meaning you can only play a certain gamemode on a map if it has a configuration for it
    - Basic Map Editing Functions
      - Set Map Name
      - Set Map Author
      - Add spawnpoint (Optional team preference)
      - Add teleporter (In and Out)
      - Add region (Specify IDs, possibly for other gamemodes to retrieve in-game)
    - Possible Advanced Map Editing Functions (in the future)
      - Trigger-based Region state changing (Using WorldGuard regions as triggers to temporarily erase all blocks in another region)
      - Jumppads
- Adding Gamemodes
  - Static gamemode behavior
    - Pregame (Warmup)
      - A freshly started gamemode on a map will start in a "warmup" mode, where score and points are always 0. (setting the points/score is ignored)
      - During warmup mode, players would use a /ready command or similar, where >50% of players being ready would start the game
      - Optional timer, so the game starts whether they're ready or not (only starts if the minimal player size for that game is met)
      - A method could be made to let gamemodes have different behaviors on whether it is warmup mode or not.
    - Gameplay
      - Game is divided in "rounds", where the winner of a "round" earns a "point" (winners can be teams if the game is team-based, or player-specific)
      - Players can earn "score" depending on what the gamemode is, for example for killing another player.
      - The gamemode decides who wins depending on what their score is, or possibly not depending on their score at all.
      - The visibility of rounds and scores should be configurable, in case a gamemode never uses either.
      - If someone/a team wins the round, a post-round timer will countdown before the next round starts
    - Postgame
      - Possible map voting, or just semi-random map choosing (another map using the same gamemode or something)
      - Post-game timer until the actual map change happens

  - Separated by folders, with the folder names serving as gamemode IDs

  - Configuration (via config.yml)
    - "name" specifying formatted name of the gamemode
    - "author" specifying author of the gamemode

  - Events
    - mg_game_init: Runs when the game starts, before any players join (pre-game)
    - mg_game_start: Runs right before the first round starts
    - mg_player_join: Runs when a player joins the game
    - mg_player_quit: Runs when a player quits the game
    - mg_player_hurt: Runs when a player gets hurt (by heart or shield)
    - mg_player_die: Runs when a player dies
    - mg_player_kill: Runs when a player kills a player
    - mg_region_enter: Runs when a player enters a named region
    - mg_region_leave: Runs when a player leaves a named region
    - mg_team_switch: Runs when a player joins a team or a player switches teams.
    - mg_round_init: Runs before the start of each round (pre-round)
    - mg_round_start: Runs at the start of each round
    - mg_round_end: Runs at the end of each round (post-round)
    - mg_game_end: Runs at the end of a game (post-game)

  - CommandHelper
    - Companion CH extension to be made to interface with the game events
    - "game.ms" file to be the base of the game, and will execute when the game starts up.

- Player Behavior (Classes)
  - Static Player Behavior
    - Player hurt/deaths will be overridden by the plugin as long as they're in a game
    - Inventories are never saved as long as they're in a game
    - When a player "dies", they will instantly respawned as a temporary spectator
  - Configuration
    - Class Name
    - Walking Speed
    - Default Weapons (by IDs)
    - Default Health (hearts)
    - Default Shield (exp bar); set to 0 if a gamemode will set the exp bar
  - Scripting
    - Function run on player right click (while not holding any weapon)

- Teams
  - Static team "0", which is for spectators (not to be confused by temporarily spectating while in a team)
  - Team 1 defaults to "Red" with red as the team color
  - Team 2 defaults to "Blue" with blue as the team color, etc.
  - Modular formatting to allow additional teams in the future

- Weapons
  - Configuration
    - Weapon Name (in your hand)
    - Item ID (what you're holding)
    - Initial Ammo
    - Show Ammo
    - Ammo Display method (show next to the item name in your hand, or use a scoreboard, the exp bar or something)
  - Scripting
    - Function run on player left click
    - Function run on player right click

- General Functions
  - Shooting a projectile from any location at any angle at any speed (fireball block damage should be blocked)
    - Should ignore spectators (pretty much invisible players)
    - A hit should deal damage
  - Shooting a projectile like above, but the location it hits will cause splash damage
    - A hit should deal "direct" damage, and being caught in the splash radius should deal "splash" damage
    - Configurable rate of force depending on how far away the player is from the hit location
  - Jumppad launch algorithm (from point A to B in a perfect arc)
