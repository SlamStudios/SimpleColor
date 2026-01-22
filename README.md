# SimpleColor

A chat color library for Hytale that brings Spigot/Minecraft-style color codes to Hytale.

## Features

- Legacy color codes (`&0-9`, `&a-f`)
- Format codes (bold, italic, underline, monospace)
- Hex color support (`&#RRGGBB`)
- Multi-color gradients (unlimited colors)
- Built-in rainbow gradient
- Clickable links
- Permission-based access control

---

## Color Codes

### Legacy Colors

| Code | Color | Permission |
|------|-------|------------|
| `&0` | Black | `simplecolor.color.black` |
| `&1` | Dark Blue | `simplecolor.color.dark_blue` |
| `&2` | Dark Green | `simplecolor.color.dark_green` |
| `&3` | Dark Aqua | `simplecolor.color.dark_aqua` |
| `&4` | Dark Red | `simplecolor.color.dark_red` |
| `&5` | Dark Purple | `simplecolor.color.dark_purple` |
| `&6` | Gold | `simplecolor.color.gold` |
| `&7` | Gray | `simplecolor.color.gray` |
| `&8` | Dark Gray | `simplecolor.color.dark_gray` |
| `&9` | Blue | `simplecolor.color.blue` |
| `&a` | Green | `simplecolor.color.green` |
| `&b` | Aqua | `simplecolor.color.aqua` |
| `&c` | Red | `simplecolor.color.red` |
| `&d` | Light Purple | `simplecolor.color.light_purple` |
| `&e` | Yellow | `simplecolor.color.yellow` |
| `&f` | White | `simplecolor.color.white` |

> **Note:** You can also use `§` instead of `&`

### Format Codes

| Code | Format | Permission |
|------|--------|------------|
| `&l` | **Bold** | `simplecolor.format.bold` |
| `&o` | *Italic* | `simplecolor.format.italic` |
| `&n` | <u>Underline</u> | `simplecolor.format.underline` |
| `&m` | `Monospace` | `simplecolor.format.monospace` |
| `&r` | Reset | `simplecolor.format.reset` |

---

## Advanced Features

### Hex Colors

Use any RGB color with the hex format:

```
&#RRGGBB
```

**Example:**
```
&#FF5500This is orange text
&#00FF00This is lime green
```

**Permission:** `simplecolor.color.hex`

---

### Gradients

Create smooth color transitions with unlimited color stops:

```
&#color1:color2:color3:...
```

Colors can be hex codes or color names. Applies to following text until another color code or reset.

**Examples:**
```
&#FF0000:00FF00Red to Green
&#red:yellow:greenTraffic Light
&#FF0000:FF7F00:FFFF00:00FF00:0000FF:8B00FFFull Rainbow
&#dark_purple:light_purple:whitePurple Fade
&#FF0000:0000FFGradient &rNormal &aGreen
```

**Permission:** `simplecolor.color.gradient`

---

### Rainbow

Built-in rainbow gradient shortcut:

```
&*text
```

Applies rainbow colors to all following text until another color code or reset.

**Example:**
```
&*This text is rainbow colored!
&*Rainbow &rNormal &*Rainbow again
```

**Permission:** `simplecolor.color.rainbow`

---

### Clickable Links

Create clickable text that opens URLs:

```
&(url)[display text]
```

**Examples:**
```
&(https://example.com)[Click here to visit]
&(https://discord.gg/example)[&bJoin our Discord!]
```

**Permission:** `simplecolor.link`

---

## Permissions

### Wildcard Permissions

| Permission | Description |
|------------|-------------|
| `simplecolor.bypass` | Bypass all permission checks |
| `simplecolor.color.*` | Access to all colors (legacy + hex + gradient + rainbow) |
| `simplecolor.format.*` | Access to all format codes |

### Color Permissions

| Permission | Description |
|------------|-------------|
| `simplecolor.color.black` | Black color (`&0`) |
| `simplecolor.color.dark_blue` | Dark Blue color (`&1`) |
| `simplecolor.color.dark_green` | Dark Green color (`&2`) |
| `simplecolor.color.dark_aqua` | Dark Aqua color (`&3`) |
| `simplecolor.color.dark_red` | Dark Red color (`&4`) |
| `simplecolor.color.dark_purple` | Dark Purple color (`&5`) |
| `simplecolor.color.gold` | Gold color (`&6`) |
| `simplecolor.color.gray` | Gray color (`&7`) |
| `simplecolor.color.dark_gray` | Dark Gray color (`&8`) |
| `simplecolor.color.blue` | Blue color (`&9`) |
| `simplecolor.color.green` | Green color (`&a`) |
| `simplecolor.color.aqua` | Aqua color (`&b`) |
| `simplecolor.color.red` | Red color (`&c`) |
| `simplecolor.color.light_purple` | Light Purple color (`&d`) |
| `simplecolor.color.yellow` | Yellow color (`&e`) |
| `simplecolor.color.white` | White color (`&f`) |
| `simplecolor.color.hex` | Hex colors (`&#RRGGBB`) |
| `simplecolor.color.gradient` | Gradient colors |
| `simplecolor.color.rainbow` | Rainbow gradient (`&*`) |

### Format Permissions

| Permission | Description |
|------------|-------------|
| `simplecolor.format.bold` | Bold format (`&l`) |
| `simplecolor.format.italic` | Italic format (`&o`) |
| `simplecolor.format.underline` | Underline format (`&n`) |
| `simplecolor.format.monospace` | Monospace format (`&m`) |
| `simplecolor.format.reset` | Reset format (`&r`) |

### Other Permissions

| Permission | Description |
|------------|-------------|
| `simplecolor.link` | Clickable links |

---

## API Usage

SimpleColor can be used by other plugins to parse colored text.

### Basic Parsing

```java
import org.slamstudios.simplecolor.SimpleColor;
import com.hypixel.hytale.server.text.Message;

// Parse without permission checks
Message msg = SimpleColor.parse("&cRed &aGreen &9Blue");

// Send to player
player.sendMessage(msg);
```

### Permission-Based Parsing

```java
// Parse with permission checks (strips codes player doesn't have permission for)
Message msg = SimpleColor.parse("&c&lBold Red Text", player);
```

### Hex Colors

```java
Message msg = SimpleColor.parse("&#FF5500Orange &#00FFFFCyan");
```

### Gradients

```java
// Two-color gradient (applies to following text)
Message msg = SimpleColor.parse("&#FF0000:0000FFRed to Blue gradient");

// Multi-color gradient
Message msg = SimpleColor.parse("&#red:yellow:green:blueFour color gradient");

// Rainbow
Message msg = SimpleColor.parse("&*Rainbow text!");
```

### Links

```java
Message msg = SimpleColor.parse("&(https://example.com)[Click Me!]");
```

### Strip Colors

```java
// Remove all color/format codes from a string
String plain = SimpleColor.strip("&c&lColored Text");
// Result: "Colored Text"
```

### Direct ColorParser Access

```java
import org.slamstudios.simplecolor.ColorParser;

// Same functionality as SimpleColor static methods
Message msg = ColorParser.parse("&aHello World!");
String stripped = ColorParser.stripAll("&cColored");
```

### Using ChatColor Enum

```java
import org.slamstudios.simplecolor.ChatColor;

// Get color by code
ChatColor red = ChatColor.getByCode('c');

// Get color by name
ChatColor blue = ChatColor.getByName("blue");

// Get the java.awt.Color
java.awt.Color awtColor = ChatColor.RED.getColor();

// Parse hex to Color
java.awt.Color custom = ChatColor.parseHex("FF5500");
```

### Using GradientUtil

```java
import org.slamstudios.simplecolor.GradientUtil;
import java.awt.Color;
import java.util.List;

// Generate a two-color gradient
List<Color> gradient = GradientUtil.generateGradient(
    Color.RED,
    Color.BLUE,
    10  // number of steps
);

// Generate a multi-color gradient
List<Color> colors = List.of(Color.RED, Color.YELLOW, Color.GREEN);
List<Color> multiGradient = GradientUtil.generateMultiGradient(colors, 20);
```

---

## Configuration

SimpleColor creates a `config.json` file in its data directory:

```json
{
  "chatParsingEnabled": true,
  "chatFormat": "{player}: {message}"
}
```

| Option | Description |
|--------|-------------|
| `chatParsingEnabled` | Enable/disable color parsing in chat messages |
| `chatFormat` | Chat format with `{player}` and `{message}` placeholders |

**Example formats:**
```
{player}: {message}
[{player}] {message}
&7{player} &8>> &f{message}
&6{player}&7: {message}
```

---

## Installation

1. Build the plugin:
   ```bash
   mvn clean package
   ```

2. Place `SimpleColor-1.0.0.jar` in your server's plugins folder.

3. Start the server.

---

## Examples

### Chat Examples

```
&cThis is red text
&a&lThis is bold green text
&#FF5500This is custom orange
&#red:blueThis fades from red to blue
&#FF0000:FFFF00:00FF00Red to Yellow to Green
&*Rainbow colored text!
&(https://discord.gg/example)[&b&lJoin Discord]
```

### Combined Usage

```
&6&l[Server] &#gold:yellowWelcome to FlameTale! &(https://flametale.net)[&aWebsite]
&*Welcome to &lFlameTale&r &*Network!
```

---

## License

Copyright FlameTale Team. All rights reserved.
