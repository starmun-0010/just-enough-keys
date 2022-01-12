## JEK 0.5.1 to 0.5.3
- Fixed crash when launching dedicated server.

## JEK 0.5.1 to 0.5.2
- Added/Fixed Compatibility for mods:
  1. Don't Drop It
  2. Interactic
  3. ItemPhysic
- Fixed modifier only vanilla keys resetting on restart
- Fixed forge keys with default modifiers resetting on restart
- Updated keybinding to let keys with no modifier pass through
  when a modifier key is pressed, but no keymapping with the modifier
  is triggered.
  
## JEK 0.4.3 to 0.5.1

- Implemented separate drop stack key from drop item
- Fixed modifiers not working in certain containers (e.g. jei)
- Fixed forge conflict checks
- Updated the search algorithm, results should be more relevant and better sorted
- Implemented JEKKeyMapping api for multi-modifier keys
- RU translation, thanks to [DrHesperus](https://github.com/DrHesperus?tab=repositories)

## JEK 0.4.2 to 0.4.3
- Removed accidental log spam
- Fixed 1.18 not working with modded keybindings

## JEK 0.4.1 to 0.4.2
- Added help text for advanced search tokens`
- Fixed incompatibility with
    - Mouse Wheelie
    - BackSlot
    - Embedded Amecs Api

## JEK 0.4.1 

### [1.16, ->]
- Fixed open GL Errors in log when closing containers
- Replaced platform specific render tooltip with screen.renderTooltip
- Fixed mixin targets

### [1.17, ->]
- Fixed keys not working with forge modded keybindings

### [1.18]
- Ported to 1.18
