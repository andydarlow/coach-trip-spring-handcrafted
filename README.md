# coach-trip-spring-handcrafted

I want to see how Claude will build a spring boot app verses how i might build it the 'old handcrafted way'.
This is the handcrafted version (it is  also a good refresher on spring boot 4 for myself!)

I'll repeat the same project using claude so i can compare and give my thoughts on the LLM generated code and where i can 
improve my CLAUDE.md/SKILLS files to improve the development experience while reducing the token count

# The Problem

A simple site for booking trips on coaches. This is a good test of the many to many relationship/transactions on traveler and coachtrip (I can travel on many tips and many traveler travel on a coach trip)

## Business rules
1. Traveler travel on several coach trips. Travelers travel on many coach trips. Traveler has name/surname
2. Coach schedule represents when and where a trip occurs. The schedule represnets what day of the week and the time the trip occurs.
3. Coach trip only travel from City to City at a time under one schedule.
3. Coach trip has many Travelers on it.

# REST API

## Posts

4. POST /coachSchedules - creates a new schedule. It contains when trips occurs (time and which days of week), max number of travellrs allows to travel on trip and start city/end city (assume it's the trip will be direct with no stops to keep the example simpler.)
2. POST /coachTrips - creates a new trip for a specific schedule.
3. POST /travelers - creates a profile of a traveler
4. POST /coachTrips/<id>/travelers - add a traveler to a trip if space available - can't add a traveler if the trip has already started or ended. also, adding the same traveler multiple times will have no effect.

## Gets

1. GET /coachTrips/<coachtrips/<id>  displays the travelers on a trip (id and name), to/from city, day time of trip 

## Deletes

1. DELETE /coachTrips/<id>/travelers/<id> - removed traveler from trip
2. DELETE /coachTrips/<id>  - delete the coach trip only if no travelers have been booked on it

*NOTE: not a complete list but a good sample to test out development process

# Security
1. Two roles... 
    1.1 coachAdmin:people who manages setting schedule and trips.
    1.2 Traveler role. Can book a trip/reomve themselve from a trip
2. App protected by JWT token obtained by Congnito or Autho (will decide when coding)
3. Should be https
4. coachAdmin has authorization to perform: POST /coachSchedules, POST /coachTrips, DELETE /coachTrips/<id>
5. Traveler authorization to perform: POST /travelers
6. only the traveler who is books/booked the travel can call POST /coachTrips/<id>/travelers or  DELETE /coachTrips/<id>

# architecture
1. spring boot 4 microservice
2. Use Postgress DB to store data. Use a Progress docker container for local testing 
