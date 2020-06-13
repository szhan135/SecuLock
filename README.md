CS179J-Team6
============

CS 179J: Senior Design in Computer Architecture and Embedded Systems - SecuLock

Inroduction
-----------

The project that we are proposing is an access control system that can be used for homes, workplaces, safes, and anything that uses a lock. It will prevent any unauthorized people from unlocking the system, and keep the locked area safe. 

The inspiration of this project is for security and convenience. When you carry a bunch of traditional keys, finding the correct one is often a nuisance. The only thing needed here is a phone, a single key tag, and a finger to deploy Multi-Factor Authentication (ex. 2FA). The ultimate goal is that only authorized people have access to certain spaces.

Our project applies an RFID reader with tags and fingerprint recognition and a mobile app to prevent unauthorized users from getting access. We use an LCD screen to show the notifications for users if they access successfully, and information on getting external assistance. Additionally, as an option, if someone breaks in without proper authorization, an IR sensor will trigger an intruder alarm.

Android app
-----------

When the app the is first launched, users will be asked to sign in or sign up for a new account (using email or exisint Google account). Once logged in, users will see the "My locks" screen, which is initially empty. A lock can be added by tapping on the add lock button.

The app will ask for a code, provided with the lock (tentative, may add scanning QR/bar code feature). Once entered, if the code is valid, the lock will be added to the "My locks" screen. The app will ask for a name and location for the newly added lock.

During the inital set up process, users will be asked to scan their RFID tags and fingerprints on the lock's scanners. The app will prompt a message to indicate success or ask the users to try again.

In the lock list screen, select the lock to be locked/unlocked. Tap the "Lock" button to lock, and tap the "Unlock" button to unlock. A text field will display the unlock and lock result (Successful or Failed).

At the bottom of the lock screen, a "History" button appears. Users can select this button to view the lock and unlock history of the lock.

Note: After downloading from Github, the existing AWS Amplify project can be reused by running 'amplify env add' with the existing AWS profile.

That's a wrap
-----------
The senior design project course is finished, and the project will no longer be worked on. It's time for reflection on what we have learned from this project and how to do better in the future.
