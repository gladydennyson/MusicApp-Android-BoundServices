# MusicApp-Android-BoundServices
A music player app. This project showcases the concepts of Foreground Services and Bound Services in Android. It contains a Music Server and a Music Client Application. The Music Client app fetches the song information from the Server app and plays the music in the Client app. The Music Client UI provides a list of all tracks and options to play, pause, resume or stop the music.

## Project Overview
### Music Central App
This is the service app that contains all the song information. In order to establish communication between the music service and the client application an AIDL interface has been implemented.  
### Music Client App
This is the the Client Application that communicates with the service using the AIDL interface. Once the client is bound to the service it displays the song information and performs the _Music Player_ functionalities (play, pause, resume, stop).  

* [AIDL](https://developer.android.com/guide/components/aidl?authuser=2)  
* [Services](https://developer.android.com/guide/components/services)  
* [Bound Services](https://developer.android.com/guide/components/bound-services)

## Installation
These applications can run on API level 28(Pie) and above.  
1) Clone the project and import the project in Android Studio.
2) Run the _MusicCentral_ android application first.
3) Run the _MusicClient_ android application.  

## Credits
All images are taken from _Google Images_
