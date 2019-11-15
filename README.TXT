Hello! Thank you for taking the time to evaluate my project for C195.

My two languages are English and Spanish. You'll notice there is, in fact, a radio button choice for either language.
However, the language should automatically be set to your System's default locale. (See Lines ~90 and on of the
LoginScreenController class).

My program currently has two users with login information here:
Username: test Password: test
Username: test2 Password: test2

The database is in UTC time. Some columns were intentionally left blank per statements from the course chatter board
from instructors.

The buttons below the appointment tableview calendar for sorting by month, week and all gather the appointments for the
next 30 days, 7 days, and all appointments, respectively.

The time change function is based on the systems time zone.

Appointments that are scheduled back to back are considered to overlap. (i.e One ending at 10:00:00 and another
beginning at 10:00:00 would overlap.)

I used 4 instances of 3 different Lambda expressions. They can be found in the MainScreenController, the
LoginScreenController and the ConsultantTableController.

The log.txt file should be within the project folder.

Thanks again for taking the time to go over this.

                                                    ROUND 2

My previous submission was kicked back to me due to the following error per the evaluator comments:
"Appointments are able to be added, updated, and deleted. Appointments could only be added or modified to 15:00 to
16:00, all other times resulted in an out of office hours error."

For the life of me, I could not replicate this bug. What I've done is changed the business hours label to automatically
reflect the time zone of the current user. So, although the business hours are universally 14:00 to 22:00 UTC, a user
in the Central time zone of the United States will see all times (including the business hours) converted to central
time. Before being sent to the database, times are converted to UTC time. Likewise, after being pulled from the DB,
times are converted to the users local time before being displayed. So that means when a user schedules an appointment,
they are doing so in their own time. I have also included a short video to demonstrate how I could not replicate the bug.

Because of this bug, exception controls were not evaluated. Try/catch statements can be seen in the LoginScreenController
file and throws clauses are common.

Thanks for going over this project again. I truly appreciate it and apologize for any confusion.

Lucas Roberts


