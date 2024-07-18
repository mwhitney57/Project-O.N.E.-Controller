<p align="center">
    <img align="center" src="https://raw.githubusercontent.com/mwhitney57/Project-O.N.E.-Controller/main/src/main/java/dev/mwhitney/images/icon.png">
    <br>
    <h1 align="center">Project O.N.E. - Controller</h1>
</p>
<p align="center">
    A Java application that allows for remote control of the Project O.N.E. system.
    <br><br>
    <a target="_blank" href="https://github.com/mwhitney57/Project-O.N.E."><img src="https://img.shields.io/badge/Project%20O.N.E.-3D556B?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAAFVBMVEUAAAArO0rA6+s0SVw5T2M9VWv///8cYhYEAAAAAXRSTlMAQObYZgAAAFBJREFUGNOFj0EKwEAMAnU0//9yDy3bZXuoOQhDEJV8a3OwSYMBy9iEtgR7AaYESzIOhRlKJJl8QJnnmgOQv48tdIYV6g8wocNb7Kh+jjvnXykdAi0mh4iNAAAAAElFTkSuQmCC" alt="A Project O.N.E. Subproject"></a>
    <img src="https://img.shields.io/badge/designed for-windows-blue?style=flat&logo=windows" alt="Designed for and Tested on Windows">
    <img src="https://img.shields.io/badge/version-1.4.3-blue" alt="Controller Application v1.4.3">
    <img src="https://img.shields.io/badge/language-java-F58219?logo=oracle" alt="Written in Java">
    <a target="_blank" href="https://github.com/mwhitney57/Project-O.N.E.-Controller/blob/main/LICENSE"><img src="https://img.shields.io/badge/license-GPL%203.0-yellow" alt="GPL License v3.0"></a>
</p>

### Table of Contents
- [Description](https://github.com/mwhitney57/Project-O.N.E.-Controller?tab=readme-ov-file#-description)
- [Features](https://github.com/mwhitney57/Project-O.N.E.-Controller?tab=readme-ov-file#-features)
- [Images](https://github.com/mwhitney57/Project-O.N.E.-Controller?tab=readme-ov-file#-images)
- [Libraries](https://github.com/mwhitney57/Project-O.N.E.-Controller?tab=readme-ov-file#-libraries)
- [Additional Information](https://github.com/mwhitney57/Project-O.N.E.-Controller?tab=readme-ov-file#%E2%84%B9%EF%B8%8F-additional-information)

### 📃 Description
This application is a part of  __Project O.N.E.__

It allows for quick and easy control of the Project O.N.E. system by communicating with the Project O.N.E. server.
The application stays in the system tray, and all functions are done through the tray icon.

### ✨ Features
- Send and receive commands, messages, and broadcasts to/from the Project O.N.E. server and system.
- Quickly unlock the door by triple-clicking the tray icon.
- A small tray menu that displays with a right-click. Quickly unlock/lock the door, view the about, open the GUI, or exit the application.
- A hidden, compact GUI with quick command buttons and a small terminal.

### 📸 Images
- Tray Icon

>![trayIcon](https://github.com/user-attachments/assets/f211932b-b635-4469-8bc2-d8cfe9fd5963)

- Tray Menu

>![trayMenu](https://github.com/user-attachments/assets/f7da6f5c-4974-4295-9240-5c8ab08fd14d)

- GUI (as of `v1.4.3`)

>![GUI](https://github.com/user-attachments/assets/2c520d4e-4aea-45c5-82ad-00b73a0a825d)

__The components of the GUI break down as follows:__
- ✅ Open Button
    - Unlocks the door by retracting the solenoid, allowing for the door to be opened.
- ❌ Close Button
    - Locks the door by releasing the solenoid, keeping the door closed.
- 🔓 Unlock Button
    - Unlocks the Project O.N.E. system, allowing for anyone to open the door.
- 🔒 Lock Button
    - Locks the Project O.N.E. system, thereby requiring biometric identity verification to open the door.
- 🟢 Enable Button
    - Enables manual unlocks on the Project O.N.E. system, allowing someone to open the door or change the system lock state in person.
- 🔴 Disable Button
    - Disables manual unlocks on the Project O.N.E. system, preventing anyone from opening the door or changing the system lock state in person.
- 🖥️ Console/Terminal Functionality with a Text Window, Command Line Entry, and an Enter Button

Clicking anywhere outside of the GUI after it is opened will hide it.
This helps to make the GUI feel snappy, convenient, and easy-to-use.
It can be re-opened using the `Open Interface` button in the Tray Menu.

Please note that the images provided above may not be representative of the current product, as they may not be updated with each new version.

*For more information regarding how this application interfaces with the rest of the project, please reference the Project O.N.E. information repository.*

### 📖 Libraries
- `nv-websocket-client` @ <a target="_blank" href="https://github.com/TakahikoKawasaki/nv-websocket-client">https://github.com/TakahikoKawasaki/nv-websocket-client</a>
    - Licensed under <a target="_blank" href="https://github.com/mwhitney57/Project-O.N.E.-Controller/blob/main/LICENSE_Apache">Apache 2.0</a>
    - No changes to library's source code.

### ℹ️ Additional Information
The code for Project O.N.E. is currently designed to work with the one system that is currently in existence.
This means that, although this code and the design models are publicly available, nobody else will be able to fully deploy the project without making the necessary changes.
I plan to make adjustments throughout the subprojects to allow for this in the future.
Once completed, anyone will be able to deploy a Project O.N.E. system that can be controlled by this application.

The application connects to the Project O.N.E. server using an authentication token.
The token is pulled from the system environment variable `PROJECT_ONE_CONTROLLER`.
However, as described in the previous paragraph, none of this has any public use at the moment.
Once the server allows for it, this token will likely be used to connect to a specific Project O.N.E. system.
