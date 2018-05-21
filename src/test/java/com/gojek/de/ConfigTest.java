package com.gojek.de;

import com.gojek.de.exception.ConfigException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigTest {
    @Before
    public void before() throws Exception {
        SystemEnv.reset();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReadFromEnvironmentConfigFirst() throws Exception {
        SystemEnv.set("TEST", "123");

        Config config = new Config("application.properties");

        assertEquals("123", config.get("TEST"));
    }

    @Test
    public void shouldReadFromAppConfigWhenNotPresentInEnvironmentConfig() throws Exception {
        Config config = new Config("application.properties");

        assertEquals("application.properties", config.get("TEST"));
    }

    @Test
    public void shouldGiveNullValueWhenEnvironmentConfigAndAppConfigDoesNotHaveKey() throws Exception {
        SystemEnv.set("TEST","123");

        Config config = new Config("application.properties");

        assertEquals(null, config.get("invalid", null));
    }

    @Test
    public void shouldThrowExceptionWhenEnvironmentConfigAndAppConfigDoesNotHaveKey() throws Exception {
        expectedException.expect(ConfigException.class);
        expectedException.expectMessage("No config found");

        SystemEnv.set("TEST","123");

        Config config = new Config("application.properties");

        config.get("invalid");
    }

    @Test
    public void shouldReturnValueFromEnvironmentIfAppConfigFileIsMissing() throws Exception {
        SystemEnv.set("TEST","123");

        Config config = new Config("missing_file.conf");

        assertEquals("123", config.get("TEST"));
    }

    @Test
    public void shouldReturnNullValueIfAppConfigIsMissingAndKeyNotPresentInEnvironment() throws Exception {

        Config config = new Config("missing_file.conf");

        assertEquals(null, config.get("TEST", null));
    }

    @Test
    public void shouldThrowExceptionIfAppConfigIsMissingAndKeyNotPresentInEnvironment() throws Exception {
        expectedException.expect(ConfigException.class);
        expectedException.expectMessage("No config found");

        Config config = new Config("missing_file.conf");

        config.get("TEST");
    }

    @Test
    public void shouldReturnValueFromEnvironmentIfAppConfigFileIsInvalid() throws Exception {
        SystemEnv.set("TEST","123");

        Config config = new Config("test.txt");

        assertEquals("123", config.get("TEST"));
    }

    @Test
    public void shouldReturnNullValueIfAppConfigIsInvalidAndKeyNotPresentInEnvironment() {
        Config config = new Config("test.txt");

        assertEquals(null, config.get("invalid", null));
    }

    @Test
    public void shouldThrowExceptionIfAppConfigIsInvalidAndKeyNotPresentInEnvironment() {
        expectedException.expect(ConfigException.class);
        expectedException.expectMessage("No config found");

        Config config = new Config("test.txt");

        assertEquals(null, config.get("invalid"));
    }

    @Test
    public void shouldGetTheSystemConfigs() throws Exception {

        SystemEnv.set("TEST_ONE","1234321");

        Config systemConfig = new Config();

        Map<String, String> original = System.getenv();

        Map<String, String> expectedConfigs = systemConfig.getAll();
        assertEquals(original.size(), expectedConfigs.size());
        assertEquals(original,expectedConfigs);
    }

    @Test
    public void shouldGetBothConfigs() throws Exception {

        SystemEnv.set("TEST_ONE","1234321");

        Config mergedConfig = new Config("application.properties");

        Map<String, String> systemConfig = System.getenv();
        Map<String, String> expectedConfigs = mergedConfig.getAll();

        for (Map.Entry<String, String> systemProperty: systemConfig.entrySet()){
            assertEquals(systemProperty.getValue(),expectedConfigs.get(systemProperty.getKey()));
        }
        assertEquals("application.properties", mergedConfig.get("TEST"));
    }

    @Test
    public void shouldOverrideAppConfigFromSystemConfig() throws Exception {

        SystemEnv.set("TEST","1234321");

        Config mergedConfig = new Config("application.properties");

        Map<String, String> expectedConfigs = mergedConfig.getAll();

        assertEquals("1234321", expectedConfigs.get("TEST"));
    }

    @Test
    public void shouldReadIntValueFromConfig() throws Exception {
        SystemEnv.set("INT_VALUE","123");

        Config config = new Config("application.properties");

        assertEquals(123, config.getInt("INT_VALUE"));
    }

    @Test
    public void shouldThrowExceptionWhenConfigIsMissingForAnIntValueFromConfig() throws Exception {
        expectedException.expect(ConfigException.class);
        expectedException.expectMessage("No config found");

        Config config = new Config("application.properties");

        config.getInt("invalid_key");
    }

    @Test
    public void shouldThrowExceptionWhenConfigIsNotInt() throws Exception {
        expectedException.expect(ConfigException.class);
        expectedException.expectMessage("Config value is not a number");

        SystemEnv.set("NON_INT","abc");

        Config config = new Config("application.properties");

        config.getInt("NON_INT");
    }
    @Test
    public void shouldGiveFalseIfKeyDoesNotExist() {
        Config config = new Config("application.properties");

        assertFalse(config.has("invalid_key"));
    }
    
    @Test
    public void shouldGiveTrueIfKeyDoesExist() throws Exception {
        SystemEnv.set("key", "value");
        Config config = new Config("application.properties");

        assertTrue(config.has("key"));
    }

}
