# ScureLogin App

## Overview

This Android application demonstrates basic mobile security principles by validating a set of conditions before allowing the user to log in. The app checks for device location, orientation, battery level, device type, and network connectivity (Wi-Fi) to ensure the environment meets certain security requirements. If any of the conditions are not met, access is denied, and the user is presented with a message listing the missing conditions.

## Features

- **Sensor Integration**: The app uses the device's accelerometer and magnetometer sensors to check the device's orientation.
- **Location Verification**: The app verifies if the device is within a specific geographic range using the `FusedLocationProviderClient`.
- **Battery Level Check**: It ensures that the device's battery level is above 50%.
- **Device Validation**: The app ensures that the device is a Google Pixel or similar model.
- **Network Connectivity**: It verifies that the device is connected to Wi-Fi before allowing login.
- **Permissions**: The app requests necessary permissions for location and Wi-Fi access dynamically.
- **User Login**: After all conditions are met, users can log in, and they are redirected to a home activity.

## Key Components

### `MainActivity.java`
- **Function**: The main activity where the user logs in. It checks whether the required conditions (location, orientation, battery, device type, and Wi-Fi) are met before allowing access.
- **Features**: 
  - Requests location and Wi-Fi permissions.
  - Validates device orientation using accelerometer and magnetometer sensors.
  - Validates battery level and device type (Google Pixel).
  - Verifies Wi-Fi connectivity and checks if the location is within a specific range.
  - If all conditions are met, the user is redirected to `HomeActivity`.

### `PermissionUtils.java`
- **Function**: Handles the requesting and managing of runtime permissions for location and Wi-Fi access.
- **Features**: 
  - Checks if the required permissions (`ACCESS_FINE_LOCATION`, `ACCESS_WIFI_STATE`) are granted.
  - Requests permissions if not granted using runtime permission dialogs.
  - Handles the results of permission requests, notifying the user if permission is denied.

### `LocationHelper.java`
- **Function**: Provides functionality to fetch and validate the user's location using the `FusedLocationProviderClient`.
- **Features**:
  - Retrieves the last known location of the device.
  - Validates if the location is within a specific geographic range (e.g., Rishon Lezion, Israel).
  - Checks if location permissions are granted before attempting to fetch the device's location.

### `DeviceOrientationHelper.java`
- **Function**: Monitors the device's orientation by using the accelerometer and magnetometer sensors.
- **Features**:
  - Determines if the device is facing north, which is one of the security conditions for login.

### `HomeActivity.java`
- **Function**: Displays the main content of the app after successful login.
- **Features**:
  - Provides an example of how the app transitions to another activity after successful login.
  - The content can be customized based on the app’s purpose.

