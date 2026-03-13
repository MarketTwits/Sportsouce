Desktop
Run the desktop application: ./gradlew :instances:app-desktop:run

Create the unpacked desktop distribution: ./gradlew :instances:app-desktop:createDistributable

Build native installers for the current host OS:

- macOS: `.dmg`
- Windows: `.exe` and `.msi`
- Linux: `.deb`

Use: ./gradlew :instances:app-desktop:packageReleaseDistributionForCurrentOS
