CaT (Call and Talk)
================

CaT is an Android Voice over IP Application to make Video- or Audio-Calls. Currently CaT is an Prototype which includes an Video- and Audio-Call feature.

### Mockup Data

To Mockup an sample users to call modify the file under app/src/assets/config.example.properties and save it as config.properties.

Sample configuration
```
# SIP account mockup
username=Doe
password=doe
domain=192.168.2.186 # SIP Server IP
# SIP mockup friend information
friendUsername=John
```

### Build

Download or clone from GIT and then open this Project with Android Studio or build APK.
```
gradle build
```

### Used Libs
CaT used [linphone-android](https://github.com/BelledonneCommunications/linphone-android) SDK to create audio- or video-calls.

### License

This program is an Voice over IP client for Android devices.
Copyright (C) 2016 Andreas Sekulski, Dimitri Kotlovsky

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
