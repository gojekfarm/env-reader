package com.gojek.de;

import com.gojek.de.exception.ConfigException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Config config;

    @Before
    public void before() throws Exception {
        config = ConfigFactory.load("/application.properties", "/default.properties");
    }

    @Ignore
    @Test
    public void getsFromEnv() throws Exception {
        assertEquals("yes", config.get("ENV_ONLY"));
    }

    @Ignore
    public void shouldReadFromEnvironmentConfigFirst() throws Exception {
        String defined_everywhere = config.get("DEFINED_EVERYWHERE");
        assertEquals("env", defined_everywhere);
    }

    @Test
    public void shouldReadFromAppConfigWhenNotPresentInEnvironmentConfig() throws Exception {
        assertEquals("yes", config.get("APPCONFIG_ONLY"));
    }

    @Test
    public void shouldReadFromDefaultConfigWhenNotPresentInOtherPlaces() throws Exception {
        assertEquals("yes", config.get("DEFAULT_CONFIG_ONLY"));
    }

    @Test
    public void filePriority() {
        assertEquals("application.properties", config.get("FILECONFIG_ONLY"));
    }

    @Test
    public void shouldThrowExceptionWhenConfigIsNotPresent() throws Exception {
        expectedException.expect(ConfigException.class);
        expectedException.expectMessage("NOT_DEFINED config not set");

        config.get("NOT_DEFINED");
    }

    @Test
    public void shouldGiveIntValue() {
        assertEquals(234, config.getInt("INT"));
    }

    @Test
    public void shouldGiveLongValue() {
        assertEquals(9223372036854775807L, config.getLong("LONG"));
    }

    @Test
    public void shouldThrowErrorForBadInt() {
        expectedException.expect(ConfigException.class);
        expectedException.expectMessage("9238458438233829299999192828282828282 is not an int");
        config.getInt("BAD_INT");
    }

    @Test
    public void shouldThrowErrorForBadLong() {
        expectedException.expect(ConfigException.class);
        expectedException.expectMessage("9223372036854775809 is not a long");
        config.getLong("BAD_LONG");
    }

    @Ignore
    @Test
    public void hasGivesTrueForKeyInAnySource() {
        assertTrue(config.has("ENV_ONLY"));
        assertTrue(config.has("APPCONFIG_ONLY"));
        assertTrue(config.has("DEFAULT_CONFIG_ONLY"));
    }

    @Test
    public void hasGivesFalseForKeyNotFound() {
        assertFalse(config.has("NOT_FOUND"));
    }

    @Test
    public void modifiers() {
        config.remap(new ModifierSet(
                "^KAFKA_CONSUMER_CONFIG_.*",
                key -> String.join(".", key.replaceAll("KAFKA_CONSUMER_CONFIG_", "").toLowerCase().split("_")),
                value -> value
        ));
        assertEquals(2147483647, config.getInt("auto.commit.interval.ms"));
        assertEquals("false", config.get("enable.auto.commit"));
    }

    @Test
    public void getMatching() {
        assertEquals("2147483647", config.getMatching("^KAFKA_CONSUMER_CONFIG_.*")
                .get("KAFKA_CONSUMER_CONFIG_AUTO_COMMIT_INTERVAL_MS"));
        assertEquals("false", config.getMatching("^KAFKA_CONSUMER_CONFIG_.*")
                .get("KAFKA_CONSUMER_CONFIG_ENABLE_AUTO_COMMIT"));
        assertEquals(2, config.getMatching("^KAFKA_CONSUMER_CONFIG_.*").size());
    }

    @Ignore
    @Test
    public void getAllGiveUnionOfAllConfigsWithRightPrecedence() {
        Map<String, String> map = config.getAll();

        assertEquals("9238458438233829299999192828282828282", map.get("BAD_INT"));
        assertEquals("yes", map.get("APPCONFIG_ONLY"));
        assertEquals("234", map.get("INT"));
        assertEquals("9223372036854775807", map.get("LONG"));
        assertEquals("yes", map.get("DEFAULT_CONFIG_ONLY"));
        assertEquals("application.properties", map.get("FILECONFIG_ONLY"));
        assertEquals("yes", map.get("ENV_ONLY"));
        assertEquals("env", map.get("DEFINED_EVERYWHERE"));
        assertEquals("9223372036854775809", map.get("BAD_LONG"));
    }

    @Test
    public void modificationToMapReturnedByGetAllDoesNotMutateOriginal() {
        Map<String, String> map = config.getAll();
        String oldValue = map.get("DEFINED_EVERYWHERE");
        String newValue = oldValue + "v2";
        map.put("DEFINED_EVERYWHERE", newValue);
        assertEquals(oldValue, config.getAll().get("DEFINED_EVERYWHERE"));
    }
}
