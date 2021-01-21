# Life Planner

## Proposal

This application will allow you keep track of your appointments, todo list, and any habits you may want to track. You will be able to add and remove appointments and tasks, as well as filter any that should be viewed today. Furthermore, you will also be able to track multiple different habits. This will work best for things easily forgotten, such as taking medication.

*Anyone* can use this application, but it will specifically work best for those who would like to keep everything they have to do in a day all in one place. **This includes students, like me!**

This project is of special interest to me, since a lot of people I know have trouble keeping up with their routines and schedules as everything has migrated online for a bit. This includes important tasks like eating and taking medication as well. In fact, this whole project started as a mostly "wellness tracker" (which has been incorporated renamed habit tracker to encompass more things) in order to make sure I could provide a way for those I care about to be able to quickly make sure they have done what they need to.

## User Stories

As of the moment, I have the following realized user stories for my project.

Overall:
- As a user, I want to be able to navigate a menu system that includes appointments, tasks, and habit trackers.
- As a user, I want to be able to save my schedule and habits to a file.
- As a user, I want to be able to load my schedule and habits from a file.

For appointments: 
- As a user, I want to be able to add appointments to my schedule.
- As a user, I want to be able to remove appointments from my schedule.
- As a user, I want to be able to view all appointments in my schedule.
- As a user, I want to be able to view today's appointments.

For tasks:
- As a user, I want to be able to add tasks to my todo list.
- As a user, I want to be able to remove tasks from my todo list.
- As a user, I want to be able to view all tasks in my todo list.
- As a user, I want to be able to view tasks due today.

For habits:
- As a user, I want to be able to add new habits to track.
- As a user, I want to be able to remove tracked habits.
- As a user, I want to be able to mark a habit as completed today.
- As a user, I want to be able to undo marking a habit as completed today.

## Phase 4: Task 2

- Include a type hierarchy in your code other than the one that uses the Saveable interface introduced in Phase 2.  You must have more than one subclass and your subclasses must have distinct functionality.  They must therefore override at least one method inherited from a super type and override it in different ways in each of the subclasses.

I have created a Scheduleable interface and utilized the Comparable interface with my classes Appointments and Tasks.

## Phase 4: Task 3
I definitely think had I learned more in the course earlier and had more time, I would have definitely refactored a lot of methods in most of the classes since I'm sure there are duplicates. I think I may have even refactored Tracker, Appointment, and Task to share potentially an abstract superclass or interface due to this. Appointment and Task in particular overlap a lot more than I had initially intended, and their Scheduleable interface could have been expanded. I feel like there is a fair amount of redundant code in particularly the UI (not GUI) class that I could have cut out as well.