# SimpleColor

A chat color library for Hytale that brings Spigot/Minecraft-style color codes to Hytale.

## Features

- Legacy color codes (`&0-9`, `&a-f`)
- Format codes (bold, italic, underline, monospace)
- Hex color support (`&#RRGGBB`)
- Multi-color gradients (unlimited colors)
- Built-in rainbow gradient (`&*`)
- Clickable links (`&(url)[text]`)
- Permission-based access control
- **Class aliases** (`ChatColor`, `CC`, `Color`) for flexible coding styles

## Quick Start

```java
import org.slamstudios.simplecolor.aliases.CC;
import com.hypixel.hytale.server.core.Message;

// Simple colors
player.sendMessage(Message.raw(CC.GREEN + "Success!"));
player.sendMessage(Message.raw(CC.RED + "Error!"));

// Parse color codes from strings
player.sendMessage(CC.translate("&aGreen &cRed &#FF5500Orange &*Rainbow"));
```

## Chat Examples

```
&cRed text
&a&lBold green
&#FF5500Custom orange
&#red:blueGradient
&*Rainbow text!
&(https://example.com)[Click here]
```

## Documentation

For complete documentation, see the **[Wiki](../../wiki)**:

- [Overview](../../wiki/overview) - Features and quick start
- [Color Codes](../../wiki/color-codes) - Legacy colors and formats
- [Advanced Features](../../wiki/advanced-features) - Hex, gradients, rainbow, links
- [Permissions](../../wiki/permissions) - Permission nodes
- [API Usage](../../wiki/api-usage) - Complete API reference
- [Configuration](../../wiki/configuration) - Plugin settings
- [Installation](../../wiki/installation) - Maven, Gradle, manual setup
- [Examples](../../wiki/examples) - Code examples

## Installation

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.SlamStudios</groupId>
        <artifactId>SimpleColor</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.SlamStudios:SimpleColor:1.0.0'
}
```

## License

Copyright SlamStudios. All rights reserved.
