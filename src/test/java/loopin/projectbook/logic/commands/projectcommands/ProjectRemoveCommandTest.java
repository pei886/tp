package loopin.projectbook.logic.commands.projectcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

class ProjectRemoveCommandTest {

    @Test
    void execute_removeByName_success() throws Exception {
        Person alice = ProjectMemberTestUtil.mkVolunteer("Alice Tan");
        Project website = ProjectMemberTestUtil.mkProject("Website Revamp");
        website.assignPerson(alice);

        var model = new ProjectMemberTestUtil.ModelStub(
                ProjectMemberTestUtil.peopleList(alice),
                ProjectMemberTestUtil.projectList(website));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand("Alice Tan", new ProjectName("Website Revamp"));
        CommandResult result = cmd.execute(model);

        assertEquals(
                String.format(ProjectRemoveCommand.MESSAGE_SUCCESS, alice.getName(), new ProjectName("Website Revamp")),
                result.getFeedbackToUser()
        );
        var stored = model.findProjectByName("Website Revamp").get();
        org.junit.jupiter.api.Assertions.assertFalse(stored.hasMember(alice));
    }

    @Test
    void execute_removeByIndex_success() throws Exception {
        Person alice = ProjectMemberTestUtil.mkVolunteer("Alice Tan");
        Project website = ProjectMemberTestUtil.mkProject("Website Revamp");
        website.assignPerson(alice);

        var model = new ProjectMemberTestUtil.ModelStub(
                ProjectMemberTestUtil.peopleList(alice),
                ProjectMemberTestUtil.projectList(website));

        ProjectRemoveCommand cmd = new ProjectRemoveCommand(Index.fromOneBased(1), new ProjectName("Website Revamp"));
        CommandResult result = cmd.execute(model);

        assertEquals(
                String.format(ProjectRemoveCommand.MESSAGE_SUCCESS, alice.getName(), new ProjectName("Website Revamp")),
                result.getFeedbackToUser()
        );
        org.junit.jupiter.api.Assertions.assertFalse(model.findProjectByName("Website Revamp").get().hasMember(alice));
    }

    @Test
    void execute_personNameNotFound_throwsCommandException() {
        var model = new ProjectMemberTestUtil.ModelStub(
                ProjectMemberTestUtil.peopleList(),
                ProjectMemberTestUtil.projectList(ProjectMemberTestUtil.mkProject("Website Revamp"))
        );
        ProjectRemoveCommand cmd = new ProjectRemoveCommand("Ghost", new ProjectName("Website Revamp"));
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }
}
