# Aerith User Guide

<img src="Ui.png" alt="Screenshot of the app" width="500"/>

Aerith is a desktop to-do list app, optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). 

## Command Summary
| Action | Format |
| ------- | ------ |
| Add to-do | `todo DESCRIPTION` |
| Add deadline task | `deadline DESCRIPTION /by DATE [TIME]` |
| Add event | `event DESCRIPTION /from START /to END` |
| View list | `list` |
| Mark task | `mark INDEX` |
| Unmark task | `unmark INDEX` |
| Delete task | `delete INDEX` |
| Edit task | `edit [/desc DESCRIPTION] [/by DEADLINE] [/from START] [/to END]` |
| Search list | `find KEYWORD` |
| Exit program | `bye` |

## Adding to-dos: `todo`
Adds a todo to your list.

Format: `todo DESCRIPTION`

## Adding deadlines: `deadline`
Adds a deadline task to your list.

Format: `deadline DESCRIPTION /by DATE [TIME]`

`DATE` should be in the format `dd-MM-yyyy`.

`TIME` should be in the format `hh:ss`.

`TIME` is optional.

## Adding events: `event`
Adds an event to your list.

Format: `event DESCRIPTION /from START /to END`

There are no format restrictions for `START` or `END`.

## Viewing your list: `list`
Displays your list of tasks.

Format: `list`

## Marking a task: `mark`
Marks a task as done.

Format: `mark INDEX`

`INDEX` is the index number of the task in the list.

## Unmarking a task: `unmark`
Marks a task as not done yet.

Format: `unmark INDEX`

`INDEX` is the index number of the task in the list.

## Deleting a task: `delete`
Removes a task from your list.

Format: `delete INDEX`

`INDEX` is the index number of the task in the list.

## Editing a task: `edit`
Updates a task.

Format: `edit [/desc DESCRIPTION] [/by DEADLINE] [/from START] [/to END]`

At least one of the fields must be provided.
Existing values will be updated to the input values.

## Searching your list: `find`
Finds tasks with descriptions that contain a given keyword.

Format: `find KEYWORD`

`KEYWORD` may be one or more words. Only tasks that contain the exact string provided will be returned.

## Exiting the program: `bye`
Exits the program.

Format: `bye`
