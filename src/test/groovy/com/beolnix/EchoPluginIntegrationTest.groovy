package com.beolnix

import com.beolnix.marvin.im.api.IMSessionManager
import com.beolnix.marvin.im.api.model.IMIncomingMessageBuilder
import com.beolnix.marvin.im.api.model.IMOutgoingMessage
import com.beolnix.marvin.plugin.OSGIPluginsIntegrationTest
import org.junit.Test
import org.sonatype.aether.repository.RemoteRepository

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * Created by beolnix on 09/11/15.
 */
class EchoPluginIntegrationTest extends OSGIPluginsIntegrationTest {

    private static List<RemoteRepository> remotes = Arrays.asList(
            new RemoteRepository(
                    "beolnix-snapshots",
                    "default",
                    "http://nexus.beolnix.com/content/repositories/snapshots/"
            )
    );

    EchoPluginIntegrationTest() {
        super(remotes, "com.beolnix.marvin", "marvin-echo-plugin", "0.2")
    }

    @Test
    public void test() {
        IMOutgoingMessage[] outMsges
        def imSessionManager = [
                sendMessage: { msg ->
                    outMsges = msg
                }
        ] as IMSessionManager

        imPlugin.setIMSessionManager(imSessionManager)
        imPlugin.process(getHelpMsg())

        assertNotNull(outMsges)
        assertTrue(outMsges.length > 0)
    }

    def getHelpMsg() {
        new IMIncomingMessageBuilder()
                .withAutor("Sam")
                .withBotName("testBot")
                .withCommand(false)
                .withCommandSymbol("!")
                .withConferenceName("test_conf")
                .withProtocol("IRC")
                .withRawMessageBody("yo")
                .build()
    }

}
