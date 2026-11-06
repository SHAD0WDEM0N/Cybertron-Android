# Architecture (draft)

```
+------------------------- app/ (Kotlin launcher) -------------------------+
| Menu | Settings | Game Files importer | Play -> starts GameActivity      |
+---------------------------------+----------------------------------------+
                                  | JNI
+---------------------------------v----------------------------------------+
| platform/  Win32 replacement: memory, files, threads, timers, audio, input|
+----------------+-------------------------------+--------------------------+
                 |                               |
+----------------v-----------+     +-------------v--------------------------+
| cpu/  x86 execution        |     | renderer-gles/  D3D9 -> GLES 3.x       |
| dynarec now, static recomp |     | shader translation, state caching       |
| later (swappable)          |     |                                          |
+----------------------------+     +------------------------------------------+
```

## Why GLES first
Mali drivers (MediaTek Dimensity) and older GPUs have far better GLES support than Vulkan.
A Vulkan backend can be added later behind the same renderer interface.

## Storage
Game files live in the app's external files dir: `Android/data/<package>/files/game/`.
On Android 11+ the OS blocks most file managers from writing there, so the in-app importer
(Storage Access Framework folder picker) is the primary route.

## Settings (stored in SharedPreferences, read by the engine at launch)
`resolution`, `graphics`, `fps_cap`
