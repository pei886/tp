layout: default.md
title: "User Guide"
pageNav: 3
---

# Loopin User Guide

Loopin is a desktop app that helps project leaders **track team members, organisation partners, volunteers, and project updates** in one place. It is designed for project leads and coordinators, and friendly for first-time users via a CLI (only typing required) interface.

--------------------------------------------------------------------------------------------------------------------

## Command Summary

Action | Format, Examples
---|---
[**Add volunteer**](#addv--addt--addo-adding-a-person-volunteer-teammember-organisationmember) | `addv n/NAME [p/PHONE] e/EMAIL [t/TELEGRAM]` <br> e.g., `addv n/James Ho p/22224444 e/jamesho@example.com t/jamesho123`
[**Add team member**](#addv--addt--addo-adding-a-person-volunteer-teammember-organisationmember) | `addt n/NAME c/COMMITTEE [p/PHONE] e/EMAIL [t/TELEGRAM]` <br> e.g. `addt n/Sarah Ng c/Logistics p/12345678 e/sarahng@example.com t/sarahlogistics`
[**Add organisation member**](#addv--addt--addo-adding-a-person-volunteer-teammember-organisationmember) | `addo n/NAME o/ORGANISATION [p/PHONE] e/EMAIL [t/TELEGRAM]` <br> e.g. `addo n/Timothy Lee o/Example Company p/98371896 e/partner@example.com t/timlee`
[**Clear**](#clear-clearing-all-entries) | `clear`
[**Delete**](#delete-deleting-a-person) | `delete INDEX`<br> e.g., `delete 3`
[**Edit**](#edit-editing-a-person) | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [t/TELEGRAM]`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com t/jameslee123`
[**Remark**](#remark-adding-a-remark) | `remark INDEX u/UPDATE` <br> e.g. `remark 2 u/add to shared drive`
[**Resolve**](#resolve-resolving-a-remark) | `resolve PERSON_INDEX REMARK_INDEX ` <br> e.g. `resolve 2 1`
[**Find**](#find-finding-persons) | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
[**List**](#list-listing-all-persons) | `list`
[**Add project**](#project-add-adding-a-project) | `project add n/NAME d/DESCRIPTION` <br> e.g., `project add n/Beach Cleanup d/Beach cleaning at Siloso Beach`
[**List projects**](#project-list-listing-all-projects) | `project list`
[**View project**](#project-view-viewing-a-project) | `project view project/PROJECTNAME` <br> e.g., `project view project/Beach Cleanup`
[**Delete project**](#project-delete-deleting-a-project) | `project delete project/PROJECTNAME` <br> e.g., `project delete project/Fundraising Drive`
[**Assign person to project**](#project-assign-assigning-a-person-to-a-project) | `project assign INDEX project/PROJECTNAME` <br> `project assign n/NAME project/PROJECTNAME` <br> e.g., `project assign 3 project/Artshow`
[**Remove person from project**](#project-remove-removing-a-person-from-a-project) | `project remove INDEX project/PROJECTNAME` <br> `project remove n/NAME project/PROJECTNAME` <br> e.g., `project remove 4 project/Wellbeing`
[**Help**](#help-viewing-help) | `help`
[**Exit**](#exit-exiting-the-program) | `exit`

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Getting Started

### 1. Installation

1.  Ensure you have Java `17` or above installed in your Computer.<br>
    **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2.  Download the latest `.jar` file from our GitHub page.

3.  Copy the file to the folder you want to use as the *home folder* for your ProjectBook.

4.  Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar projectBook.jar` command to run the application.<br>
    A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.

    ![Ui](images/Ui.png)

### 2. Your First Commands (A Quick Tutorial)

1.  Type commands into the command box and press Enter to execute them. For example, typing **`help`** and pressing Enter will open the help window.

2.  The app starts with sample data. Let's clear it by typing **`clear`** and pressing Enter.

3.  Now, let's add your first volunteer. Type:
    `addv n/John Doe p/98765432 e/johnd@example.com`

4.  Next, create a project for John to be a part of. Type:
    `project add n/Beach Cleanup d/Weekly cleanup at Siloso Beach`

5.  You should see John as Item 1 in the person list. Let's assign him to your new project. Type:
    `project assign 1 project/Beach Cleanup`

6.  To see a summary of the project and who's in it, type:
    `project view project/Beach Cleanup`

7.  To see all contacts in your book, type `list`.

8.  To exit the app, type `exit`.

Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `addv n/NAME`, `NAME` is a parameter which can be used as `addv n/John Doe`.

* Items in square brackets `[]` are optional.<br>
  e.g `n/NAME [p/PHONE]` can be used as `n/John Doe p/98613698` or as `n/John Doe`.

* `INDEX` refers to the index number shown in the displayed person list. The index must be a positive integer e.g., `1`, `2`, `3`...

* Items with `…` after them can be used multiple times including zero times.<br>
  e.g. `[u/UPDATE]…` can be used as ` ` (i.e. 0 times), `u/add to group chat`, `u/register in portal` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE`, `p/PHONE n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

</box>

<box type="tip" seamless>

**Pro Tip: Command Chaining**

The `INDEX` for commands like `edit` or `delete` refers to the *currently displayed list*.

This means you can "chain" commands. For example:
1.  Use `find Alex` to show only people named Alex.
2.  The list might show two people: `1. Alex Yeoh` and `2. Alexandra Tan`.
3.  You can then use `delete 1` to delete **Alex Yeoh**, who is the 1st person *in that filtered list*.

</box>

### `help`: Viewing help

Shows a message explaining how to access the help page. The link will bring you to a condensed cheatsheet of commands for your future reference!

![help message](images/helpMessage.png)

Format: `help`

---
### `addv` / `addt` / `addo`: Adding a Person (Volunteer, TeamMember, OrganisationMember)

Adds a person of a specific role to the project book. There are 3 roles a person can play: volunteer, committee member, and organisation contact.

Feature | Command word | Command format | Example
---|---|---|---
Add a volunteer | `addv` | `addv n/NAME [p/PHONE] e/EMAIL [t/TELEGRAM]` | `addv n/James Ho p/22224444 e/jamesho@example.com t/jamesho123`
Add a team member | `addt` | `addt n/NAME c/COMMITTEE [p/PHONE] e/EMAIL [t/TELEGRAM]` | `addt n/Sarah Ng c/Logistics p/12345678 e/sarahng@example.com t/sarah_ng`
Add an organisation member | `addo` | `addo n/NAME o/ORGANISATION [p/PHONE] e/EMAIL [t/TELEGRAM]` | `addo n/Timothy Lee o/Example Company p/98371896 e/partner@example.com t/timlee`

![img.png](img.png)

---
### `remark`: Adding a Remark

Adds a remark to a person. Remarks are used as a "to-do" list or reminder to liaise with a person about a certain update.

Format: `remark INDEX u/UPDATE`

Example: `remark 2 u/add to shared drive`

---
### `resolve`: Resolving a Remark

Removes a remark from a person after the task is done. After having updated the person, the reminder is no longer needed.

Format: `resolve PERSON_INDEX REMARK_INDEX`

Example: `resolve 2 1` (Removes the 1st remark from the 2nd person)

---
### `list`: Listing All Persons

Shows a list of all persons in the project book.

Format: `list`

---
### `edit`: Editing a Person

Edits the specified fields of an existing person in the project book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [t/TELEGRAM]`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list.
* The `INDEX` **must be a positive integer** (1, 2, 3, …).
* At least one of the optional fields must be provided.
* The given value will replace the existing value for that field.

Examples:
* `edit 1 p/91234567 e/johndoe@example.com` Edits the phone and email of the 1st person.
* `edit 2 n/Betsy Crower` Edits the name of the 2nd person.

<box type="tip" seamless>
**Tip:** Use `list` or `find` first to get the correct `INDEX` of the person you want to edit!
</box>

---
### `find`: Finding Persons

Locates persons by matching their name to a sequence of keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is **case-insensitive** (e.g., `hans` will match `Hans`).
* The search matches **any part of the name** (e.g., `Han` will match `Hans`, `Hannah`, or `Johanson`).
* The order of the keywords does not matter.

Examples:
* `find John` returns `John Doe`, or `Johnson Tan`.
* `find david alex` returns `Alex Yeoh David`, `Alexandra Davidson`.

![result for 'find alex david'](images/findcommandresult.png)

---
### `delete`: Deleting a Person

Deletes the specified person from the project book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX` from the *currently displayed list*.
* The `INDEX` **must be a positive integer** (1, 2, 3, …).

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the project book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

---
### `clear`: Clearing All Entries

Clears all entries (all persons and all projects) from the project book.

Format: `clear`

<box type="warning" seamless>
**Warning:** This action cannot be reversed.
</box>

---
### `project list`: Listing All Projects

Shows a list of all projects in the project book.

Format: `project list`

---
### `project add`: Adding a Project

Adds a project to the project book.

Format: `project add n/PROJECTNAME d/DESCRIPTION`

Example:
`project add n/Beach Cleanup d/Beach cleaning at Siloso Beach`
* Adds a new project named "Beach Cleanup" with the given description.
* No one is assigned to it yet.

<box type="tip" seamless>
**Tip:** Project names are your unique identifiers. Choose clear, unique names like "Annual Gala 2025" instead of just "Gala".
</box>

---
### `project delete`: Deleting a Project

Deletes the specified project from the project book.

Format: `project delete project/PROJECTNAME`

Example:
`project delete project/Fundraising Drive`

* Deleting a project does *not* delete the persons who were associated with it. They will just no longer be assigned to this project.

---
### `project view`: Viewing a Project

Views the summary of an existing project and its participants.

Format: `project view project/PROJECTNAME`

Example:
`project view project/Beach Cleanup`
* Lists the name and description of the project.
* Lists all contacts (volunteers, team, orgs) assigned to it.

---
### `project assign`: Assigning a Person to a Project

Assigns a specified volunteer, team member, or organisation member to a project.

**Formats:**
* `project assign INDEX project/PROJECTNAME`
* `project assign n/NAME project/PROJECTNAME`

* Assigns the person at the specified `INDEX`, or with the specified `NAME`, to the specified `PROJECT`.
* The `INDEX` refers to the index number shown in the displayed person list. It **must be a positive integer** (1, 2, 3, …).
* The `NAME` search is **case-insensitive**.
* When you view the project details (`project view`), the assigned person will show up in its details.

**Examples:**
* `project assign 4 project/Startup Showcase`
* `project assign n/Jackson project/Startup Showcase`

---
### `project remove`: Removing a Person from a Project

Removes a specified volunteer, team member, or organisation member from a project.

**Formats:**
* `project remove INDEX project/PROJECTNAME`
* `project remove n/NAME project/PROJECTNAME`

* Removes the person at the specified `INDEX`, or with the specified `NAME`, from the specified `PROJECT`.
* The `INDEX` refers to the index number shown in the displayed person list. It **must be a positive integer** (1, 2, 3, …).
* The `NAME` search is **case-insensitive**.
* This only *un-assigns* them; it does not delete the person from the project book.

**Examples:**
* `project remove 4 project/Science Fair`
* `project remove n/Marianne project/Science Fair`

---
### `exit`: Exiting the Program

Exits the program.

Format: `exit`

---
### Saving the data

Loopin's data is saved in the hard disk automatically after any command that modifies data. There is no need to save manually!

---
### Editing the data file

Loopin data are saved automatically as a JSON file `[JAR file location]/data/projectbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>
**Caution:**
If your changes to the data file makes its format invalid, ProjectBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1.  **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2.  **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.