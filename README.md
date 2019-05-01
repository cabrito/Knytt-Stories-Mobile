# Knytt Stories Mobile

![Image](./docs/img/KSM_beta_nojuni.png)

Knytt Stories Mobile (KSM) is a port of the Windows game [Knytt Stories by Nifflas](https://nifflas.ni2.se/games/) based on the source code of [Knytt Stories DS by Rodrigo Roman](http://www.rodrigoroman.com/rrc2soft/nds_dsknytt.html).

### How to Play

Currently, regular Knytt Stories levels and the tutorial can be played. The tutorial level is built-in to the game, meaning you'll be able to immediately start playing!

#### Desktop:

If you would like to play a custom level, create a directory in your user folder (In `~/` on Linux, and your personal folder on Windows) called `Knytt Stories Mobile` (if it isn't already created). In this directory, place extracted Knytt Stories level folders. At this point, you may start the game and play the level.

Use the arrow keys on the keyboard to navigate the screens.

#### Android:

If you would like to play a custom level, create a directory in your device's root folder called `Knytt Stories Mobile` (if it isn't already created). In this directory, place extracted Knytt Stories level folders. At this point, you may start the game and play the level.

Use the arrow keys on the keyboard to navigate the screens.

### To do list:

Currently, the most that can be done is viewing of the levels and the music that accompanies it. There are actively on-going plans to flesh out the "gameplay" aspect of this game, don't worry! The following items are not an exhaustive list, but things that I think are of importance for game functionality. The list will be updated from time to time, so check back to see what is and isn't working.

- ~~Finish base modularization of all currently-completed components. Right now, tilesets and data assembly need to be modularized.~~ **(Done!)**
- Upscale the resolution of the game and menu screen to be 720p+
- ~~Implement on-screen controls for Android and iOS.~~ **(Done!)**
- ~~Implement menu screen with level selection~~, and level ~~extraction~~ (downloading capabilities may be of immediate priority as well). **Partly done. Extraction is functional, and so the next step is to implement networking to download levels.**
- Implement Juni.
- Revise onscreen controls for Juni.
- Implement per-pixel collision of layer-3 with Juni.
- Implement objects (VERY time-consuming, and will take some considerable time to accomplish).
