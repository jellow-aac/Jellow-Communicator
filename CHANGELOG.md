# Changelog
All notable changes to this project will be documented in this file.

## [1.2]
### Added
- Major additions.
- New content, verbiage, icons into categories Greet and Feel-> Requests, Daily Activities-> Therapy,
  Learning->Money and Help.
- Simple and easy to follow process to download new language.
- Hindi (India), English (India) two new languages.
- User preference are now divided into two sets as:
   1) English (US and UK)
   2) English (India) and Hindi (India)
- User can add/delete languages. Any three languages are allowed.
- User can switch between language.
- Text-to-speech engine will change speech accent as per app language.
- Users below Lollipop required to switch Text-to-speech language manually on the other hand devices from
  Lollipop and higher do not required to switch between Text-to-speech language.
- Added new view which has only three icons per screen.
- User can choose country to insert country code a phone number.
- Hindi serial keyboard
- Glide image loader to load images.
- Added built in package downloader to download language packages.
- Firebase crash reporting, analytics, web storage to store language wise
  icon a packages.
- Added Text-to-speech engine as service in app
- Added default exception handler.
 


### Changed
- Replaced more button feature to scroll in recycler view.
- Changed in the breadcrumb. Now items separated with "/" symbol.
- Moved all strings in app from java code to string.xml
- Moved all string arrays to arrays in string.xml. 
  Converted verbiage string to single JSON.
- About Jellow screen now uses Web view.
- About Jellow credits.
- Updated tutorial image in tutorial screen.
- Renamed multiple java classes and respective xml layout file names, variable name.
- Update in database tables which stores user preferences.
- Added simple view to show border on category icon.


### Removed
- Removed library used to draw border to image icons.


### Fixed
- app stop speaking when screen paused in About Jellow.
- To send feedback user need to send every points.
- App resumes smoothly after user resumes app.
- App by default do not save any blood group.
- UI correction in layouts for various screen size devices.
- App crash in first run on Level 1 screen on new high density phones.


## [1.1.1] - July 25th, 2017
### Changed
- Renamed App name in Google play store from Jellow International to Jellow Communicator

### Fixed
- Nougat users can now download app from Google Play store.

## [1.1.0] - April 3rd, 2017
### Added
- First, public release on the Google play store.
- Simple, intuitive and child friendly icons.
- Organized multilevel structure which enable to keep relevant content together.
- Tapping on expressive/category icon app speak (voice output) about icon.
- Expressive buttons when used in conjunction with category button app speaks sentence.
- Sequences of activities such as Brushing, Toilet, Bathing and more.
- About me category icon is used in conjunction with expressive icons speak about user.
- App remembers user preference. Such as most used category icons used pulled upward 
  in that category
- User preferences are remembered based on number of taps on icon. Also, 
  only specific category have preferences function.
- User can reset user preferences.
- App uses Text-to-speech engine to synthesize the plain text into voice output (speak).
- User can change voice output accent to US or UK.
- User can change speech (voice output) parameters such as speech speed, voice pitch.
- User can change app view either to Pictures and Text or Picture only.
- User can edit profile details.
- Keyboard input to speak text which is not available in app.
- Serial keyboard; this keyboard has letters in alphabetical order.
