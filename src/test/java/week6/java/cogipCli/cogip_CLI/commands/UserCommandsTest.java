package week6.java.cogipCli.cogip_CLI.commands;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@ShellTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserCommandsTest {
    @Autowired
    ShellTestClient client;

    @Test
    void testAddUser() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("adduser", "Test", "Test#1")
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("User has been created");
        });
    }

    @Test
    void testGetAllUsers() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("allusers")
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("id");
        });
    }

    @Test
    void testGetUserById() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("userid", "1")
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("\"id\":1");
        });
    }

    @Test
    void testUpdateUser() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("edituser", "1")
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("User with the id : 1 has been updated");
        });
    }

    @Test
    void testDeleteUser() {
        String id = "21";

        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("deluser", id)
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("User with the id : "+ id +" has been deleted");
        });
    }
}