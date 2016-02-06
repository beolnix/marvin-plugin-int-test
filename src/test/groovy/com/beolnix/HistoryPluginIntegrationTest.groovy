package com.beolnix

import com.beolnix.marvin.im.api.IMSessionManager
import com.beolnix.marvin.im.api.model.IMIncomingMessageBuilder
import com.beolnix.marvin.im.api.model.IMOutgoingMessage
import com.beolnix.marvin.plugin.OSGIPluginsIntegrationTest
import com.beolnix.marvin.plugins.api.PluginConfig
import jdk.nashorn.internal.ir.annotations.Ignore
import org.apache.log4j.Logger
import org.apache.log4j.spi.LoggerFactory
import org.junit.Test
import org.sonatype.aether.repository.RemoteRepository

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * Created by beolnix on 09/11/15.
 */
@Ignore
class HistoryPluginIntegrationTest extends OSGIPluginsIntegrationTest {

    private static List<RemoteRepository> remotes = Arrays.asList(
            new RemoteRepository(
                    "beolnix-snapshots",
                    "default",
                    "http://nexus.beolnix.com/content/repositories/snapshots/"
            )
    );

    HistoryPluginIntegrationTest() {
        super(remotes, "com.beolnix.marvin", "marvin-history-plugin", "0.1-SNAPSHOT")
    }

    @Test
    public void test() {
        IMOutgoingMessage[] outMsges
        def imSessionManager = [
                sendMessage: { msg ->
                    outMsges = msg
                }
        ] as IMSessionManager

        PluginConfig pluginConfig = new PluginConfig('target',
                new File("target"), Collections.emptyList())

        imPlugin.init(pluginConfig, imSessionManager)
        imPlugin.process(getHelpMsg())

        assertNotNull(outMsges)
        assertTrue(outMsges.length > 0)
    }

    def getHelpMsg() {
        new IMIncomingMessageBuilder()
                .withAutor("Sam")
                .withBotName("testBot")
                .withCommand(true)
                .withCommandName("help")
                .withCommandAttributes(null)
                .withCommandSymbol("!")
                .withConference(true)
                .withConferenceName("test_conf")
                .withProtocol("IRC")
                .withRawMessageBody("!help")
                .build()
    }

}
