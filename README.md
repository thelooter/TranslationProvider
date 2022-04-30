<div align="center">
<img src="https://i.ibb.co/890xdWq/translationpriovider.png" alt="TranslationProvider Logo" width="150">

# TranslationProvider

Server Translations for Users
</div>

## Installation

You can use this API by adding the Maven Dependency

### Maven

Repository:

```xml
<repositories>
    <repository>
        <id>tuxcraft-public</id>
        <url>https://maven.tuxcraft.eu/repository/maven-public/</url>
    </repository>
</repositories>
```

Dependency

```xml
<dependencies>
    <dependency>
        <groupId>eu.tuxcraft</groupId>
        <artifactId>translationprovider-spigot</artifactId>
        <version>2.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle

#### Groovy DSL

Repository:

```groovy
repositories {
    maven {
        url "https://maven.tuxcraft.eu/repository/maven-public/"
    }
}
```

Dependency:

```groovy
dependencies {
    implementation 'eu.tuxcraft:translationprovider-spigot:2.0.0'
}
```

#### Kotlin DSL

Repository:

```kotlin
repositories {
    maven {
        url = uri("https://maven.tuxcraft.eu/repository/maven-public/")
    }
}
```

Dependency:

```groovy
dependencies {
    implementation("eu.tuxcraft:translationprovider-spigot:2.0.0")
}
```

## Usage

#### Load Translations in the Project

```java
public class Main implements JavaPlugin {

	@Override
	public void onEnable() {
		loadMessages();
	}

	public void loadMessages() {
		try {
			TranslationProvider.mapAllTranslations(Messages.class, "lobby");
		} catch (Exception ex) {
			throw new RuntimeException("Could not load messages: " + ex.getMessage(), ex);
		}
	}
}
```

#### Create a Class containing all the Messages

Messages are handled in static inner Classes

```java
import eu.tuxcraft.translationprovider.spigot.model.Message;

public class Messages {

	public static class General {

		public static Message internalServerError;
		public static Message playerUnknown;
		public static Message permissionDenied;
		public static Message invalidArgs;
		public static Message title;
		public static Message subTitle;
		public static Message closeMenu;
	}

	public static class Cosmetics {

		public static Message cosmeticsTitle;
		public static Message hatsDisplayName;
		public static Message particlesDisplayName;
		public static Message balloonsDisplayName;
		public static Message gadgetsDisplayName;
		public static Message unlocked;
		public static Message locked;
		public static Message remove;
		public static Message disabled;

		public static class Balloons {
			public static Message inventoryTitle;
			public static Message yellow;
			public static Message red;
			public static Message green;
			public static Message blue;
			public static Message remove;
			public static Message hay;
			public static Message sealantern;
			public static Message bookshelf;
			public static Message noteblock;
			public static Message equiped;
			public static Message removed;
		}
	}
}

```

#### Use Translation in Code

There are 2 ways you can get a Translation.
If you want to send the message directly use Message#sendTo(Player)

```java
public class Command implements CommandExecutor {

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			@NotNull String[] args) {

		Messages.General.internalServerError.sendTo(player);

	}
}
```

If you want to just get the String containing the Translation use

```java
public class Command implements CommandExecutor {

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			@NotNull String[] args) {

		sender.sendMessage(Messages.General.internalServerError.getFor(player));

	}
}
```

#### Replacing Parameters

Sometimes you need to use parameters in your Translations.
In this case both the sendTo() and getFor() Method offer Parameter Replacement
by passing a Map from String to String.

In the actual Translation Entry in the Database you need to prefix the Parameter with a %

```java
import java.util.Map;

public class Command implements Command {

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			@NotNull String[] args) {

		Messages.General.internalServerError.sendTo(player, Map.of("player", sender.getName()));
		sender.sendMessage(Messages.General.internalServerError.getFor(player, Map.of("player", sender.getName())));
	}
}
```

## Authors

- [@thelooter](https://git.plugin-lab.com/thelooter)
- [@Tuxgamer](https://git.plugin-lab.com/TuxGamer)

## Issues

Issue Management can be found at

[Jira](http://jira.tuxcraft.eu/projects/TRANSL/issues/)