# Cybertron-Android

A native Android port of **Transformers: War for Cybertron** (PC), aiming to run on older
and non-Adreno (e.g. MediaTek Dimensity / Mali) devices, without a Wine/Box64 wrapper.

> **Status: early research.** Only the launcher app shell exists. The game does not run yet.

## Not included / not affiliated
This repository contains **no game code, assets or binaries**. You must supply your own
legitimately obtained copy of the game. This project is not affiliated with or endorsed by
Activision, High Moon Studios, Hasbro or Epic Games.

## Goals
1. Player installs the APK and opens the launcher.
2. Menu: **Play / Settings / Game Files / Exit**.
3. Game files are imported from the user's own PC install (in-app folder import, or copied into
   `Android/data/com.shad0wdem0n.cybertron/files/game/` where the OS allows it).
4. Settings: resolution, graphics preset, frame-rate cap.

## Layout
| Path | Purpose |
|---|---|
| `app/` | Android launcher (Kotlin): menu, settings, game-file importer |
| `cpu/` | Code execution strategy (dynarec bring-up first, static recomp later) |
| `platform/` | Win32 API replacement layer (files, threads, timers, audio, input) |
| `renderer-gles/` | Direct3D 9 to OpenGL ES 3.x renderer |
| `tools/` | Analysis scripts (binary report, game inventory) |
| `docs/` | Roadmap, architecture, research notes |

See [`docs/ROADMAP.md`](docs/ROADMAP.md) and [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

## Building the launcher
Open the project in Android Studio (Giraffe or newer), or let GitHub Actions build a debug APK
(see `.github/workflows/build.yml`). The launcher has no native code yet.

## Contributing / first task
Run the two analysis tools in [`tools/`](tools/README.md) against your own game install and
paste the output into an issue. That decides the shape of the CPU and platform layers.
