import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import eu.tuxcraft.translationprovider.engine.model.Language;
import eu.tuxcraft.translationprovider.spigot.TranslationProvider;
import eu.tuxcraft.translationprovider.spigot.model.LazyLoadingMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LazyLoadingMessageTest {

  ServerMock server;
  TranslationProvider plugin;

  LazyLoadingMessage message;

  @BeforeEach
  public void setUp() {

    server = MockBukkit.mock();
    plugin = MockBukkit.load(TranslationProvider.class);

    message = new LazyLoadingMessage("lobby.scoreBoard.rankTitle");
  }

  @Test
  void testLazyLoadingMessage() {
    assertThat(message, instanceOf(LazyLoadingMessage.class));
  }

  @Test
  void testGetFor() {
    PlayerMock player =
        new PlayerMock(
            server, "TestPlayer", UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    assertThat(message.getFor(player), instanceOf(String.class));
    assertThat(message.getFor(player), equalToIgnoringCase("Rank:"));
  }

  @Test
  void testGetForConsoleSender() {

    assertThat(message.getFor(Bukkit.getConsoleSender()), equalToIgnoringCase("Rang:"));
  }

  @Test
  void testSendTo() {
    PlayerMock player =
        new PlayerMock(
            server, "TestPlayer", UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

    server.addPlayer(player);

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    message.sendTo(player);

    player.assertSaid("Rank:");
  }

  @Test
  void testSendToWithNewLine() {
    LazyLoadingMessage testMessage = new LazyLoadingMessage("test.newline");
    PlayerMock player =
        new PlayerMock(
            server, "TestPlayer", UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

    server.addPlayer(player);

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    testMessage.sendTo(player);

    player.assertSaid("Test");
    player.assertSaid("Test");
  }

  @Test
  void testSendToConsoleSender() {
    ConsoleCommandSenderMock consoleCommandSenderMock = new ConsoleCommandSenderMock();

    message.sendTo(consoleCommandSenderMock);

    consoleCommandSenderMock.assertSaid("Rang:");
  }

  @Test
  void testGetComponentFor() {
    PlayerMock player =
        new PlayerMock(
            server, "TestPlayer", UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    assertThat(message.getComponentFor(player, true), instanceOf(Component.class));
    assertThat(message.getComponentFor(player, true), equalToObject(Component.text("Rank:")));
  }

  @Test
  void testGetComponentForConsoleSender() {
    ConsoleCommandSenderMock consoleCommandSenderMock = new ConsoleCommandSenderMock();

    assertThat(
        message.getComponentFor(consoleCommandSenderMock, true),
        equalToObject(Component.text("Rang:")));
  }

  @Test
  void testGetComponentForNoMiniMessage() {
    PlayerMock player =
        new PlayerMock(
            server, "TestPlayer", UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    assertThat(message.getComponentFor(player, false), instanceOf(Component.class));
    assertThat(message.getComponentFor(player, false), equalToObject(Component.text("Rank:")));
  }

  @Test
  void getComponentForWithParametersNoMiniMessage() {
    PlayerMock player =
        new PlayerMock(
            server, "TestPlayer", UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    message = new LazyLoadingMessage("test.parameter");

    assertThat(
        message.getComponentFor(player, new HashMap<>(Map.of("%layer", player.getName())), false),
        instanceOf(Component.class));

    assertThat(
        message.getComponentFor(player, new HashMap<>(Map.of("player", player.getName())), false),
        equalToObject(Component.text("Playername: " + player.getName())));
  }

  @Test
  void testGetComponentForWithParametersIsMinimessage() {
    PlayerMock player =
        new PlayerMock(
            server, "TestPlayer", UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("English"));

    message = new LazyLoadingMessage("test.parameterWithMiniMessage");

    assertThat(
        message.getComponentFor(player, new HashMap<>(Map.of("player", player.getName())), true),
        instanceOf(Component.class));

    assertThat(
        message.getComponentFor(player, new HashMap<>(Map.of("player", player.getName())), true),
        equalToObject(
            MiniMessage.miniMessage().deserialize("<red>Playername:<red> " + player.getName())));
  }

  @Test
  void testGetComponentForConsoleCommandSenderWithParameterNoMiniMessage() {
    ConsoleCommandSenderMock consoleCommandSenderMock = new ConsoleCommandSenderMock();

    message = new LazyLoadingMessage("test.parameter");

    assertThat(
        message.getComponentFor(
            consoleCommandSenderMock,
            new HashMap<>(Map.of("%layer", consoleCommandSenderMock.getName())),
            false),
        instanceOf(Component.class));

    assertThat(
        message.getComponentFor(
            consoleCommandSenderMock,
            new HashMap<>(Map.of("player", consoleCommandSenderMock.getName())),
            false),
        equalToObject(Component.text("Spielername: " + consoleCommandSenderMock.getName())));
  }

  @Test
  void testGetComponentForConsoleCommandSenderWithParametersIsMinimessage() {

    ConsoleCommandSenderMock consoleCommandSenderMock = new ConsoleCommandSenderMock();

    message = new LazyLoadingMessage("test.parameterWithMiniMessage");

    assertThat(
        message.getComponentFor(
            consoleCommandSenderMock,
            new HashMap<>(Map.of("player", consoleCommandSenderMock.getName())),
            true),
        instanceOf(Component.class));

    assertThat(
        message.getComponentFor(
            consoleCommandSenderMock,
            new HashMap<>(Map.of("player", consoleCommandSenderMock.getName())),
            true),
        equalToObject(
            MiniMessage.miniMessage()
                .deserialize("<red>Spielername:<red> " + consoleCommandSenderMock.getName())));
  }

  @AfterEach
  public void tearDown() {

    PlayerMock player =
        new PlayerMock(
            server, "TestPlayer", UUID.fromString("82b9b78e-e807-478e-b212-1c53c4cd1cfd"));

    server.addPlayer(player);

    TranslationProvider.playerLanguage(player, Language.fromDisplayName("Deutsch"));

    MockBukkit.unmock();
  }
}
