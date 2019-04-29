# WallpaperPro
Wallpaper App (for CodeCanyon)

Competition: [Walper - Wallpaper Android Application 1.1](https://codecanyon.net/item/walper-wallpaper-android-application-10/23448932?s_rank=2)

Mockups-Low Fidelity : [Check Mockup](./20190416_081423.jpg)

<h2>Features:</h2>

- Admin Panel:
  - Add Categories
  - Add Pictures
  - Add Admins (Moderators) + Manage Admins
  - App Settings
  - App Notifications (Send Notifications)
- App:
  - Big Features:
    - Explore Wallpapers
    - Categories
    - Save Wallpapers (to Phone)
    - Save Wallpapers into Collections (Work w/ connection)
      + Make Collections
    - Settings Page:
      + Dark/Light Mode
      + Notifications Settings
      + GDPR Settings
  - Pictures:
    - Infos:
      - Category
      - Tags
      - Views
      - Rating
      - Downloads
    - Pictures Filter:
      - Recent
      - Popular
    - Search Picture (by Tags)
    
<h2>Steps (App UI Structure):</h2>
- [ ] Splash Screen
  + [ ] App Logo
- [ ] Main Activity:
  - [ ] View Pager:
    - [ ] Explore Fragment
      - [ ] Filters:
        - [ ] Featured
        - [ ] Recent
        - [ ] Popular
      - [ ] Images (Recycler View)
    - [ ] Categories Fragment:
      - [ ] Categories (Recycler View)
    - [ ] Favorites Fragment:
      - [ ] Collections (Recycler View)
  - [ ] Drawer Menu:
    - [ ] Categroies (Dynamique Drawer Menu)
    - [ ] Others:
      - [ ] Settings
      - [ ] FAQ
      - [ ] ...
- [ ] Wallpaper Activity:
  - [ ] View Pager (Images)
  - [ ] Toolbar + Favorites Button
  - [ ] Actions PopUpMenu
    - [ ] Index
    - [ ] Content:
      - [ ] Title
      - [ ] Actions:
        - [ ] Rate
        - [ ] Download
        - [ ] Share
      - [ ] Donwloads Info
      - [ ] Rating Info
      - [ ] Categories
      - [ ] Tags
      - [ ] Set Wallpaper Button
    + [ ] Rating PopUp:
      - [ ] Dismiss Button
      - [ ] RatingBar
      - [ ] Rating Button
    + [ ] Favorite PopUp
      - [ ] Dismiss Button
      - [ ] Picture is saved Message
      - [ ] Collections List + Add Collection Button
- [ ] ImagesActivity:
  - [ ] Toolbar (Category/Collection Name)
  - [ ] Collection Infos:
    - [ ] Items Info
    - [ ] Edit Button
    - [ ] Remove Button
  - [ ] Recycler View (Images)
- [ ] Settings Activity:
  - ...

<h2>Steps (App UI Events):</h2>

<h2>Steps (App Logic):</h2>

<h2>Steps (Admin Panel):</h2>

<h2>Steps (Link App with Admin Panel):</h2>
