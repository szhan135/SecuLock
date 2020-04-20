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

When the app the is first launched, users will be asked to sign in or sign up for a new account (using email or exisint Google account). Once logged in, users will see the "My locks" screen, which is initially empty. A lock can be added by selecting "Add lock".

The app will ask for a code, provided with the lock (tentative, may add scanning QR/bar code feature). Once entered, if the code is valid, the lock will be added to the "My locks" screen. The app will ask for a label for the newly added lock.

During the inital set up process, users will be asked to scan their RFID tags and fingerprints on the lock's scanners. The app will prompt a message to indicate success or ask the users to try again.

In the "My locks" screen, select the lock to be locked/unlocked. A large button will apear on the lock page. In the center of the button, the locked/unlocked status is displayed. A prompt will suggest "Hold to unlock" or "Hold to lock" depending on the lock status.

When the center button is held for 3 seconds, a prompt will display "Unlocking, please proceed with the fingerprint and RFID scanners.

At the bottom of the lock screen, a "History" button appears. Users can select this button to view the lock and unlock history of the lock.
