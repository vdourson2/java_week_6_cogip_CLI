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
class ContactCommandsTest {
    @Autowired
    ShellTestClient client;

    @Test
    void testAddContact() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("addcontact", "Test", "Testing", "0123456789", "test@test.com", "1")
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Contact has been created");
        });
    }

    @Test
    void testGetAllInvoices() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("allcontacts")
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("id");
        });
    }

    @Test
    void testGetInvoiceById() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("contactid", "1")
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("\"id\":1");
        });
    }

    @Test
    void testUpdateInvoice() {
        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("editcontact", "1")
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Contact with the id : 1 has been updated");
        });
    }

    @Test
    void testDeleteUser() {
        String id = "10";

        ShellTestClient.NonInteractiveShellSession session = client
                .nonInterative("delcontact", id)
                .run();

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Contact with the id : "+ id +" has been deleted");
        });
    }
}