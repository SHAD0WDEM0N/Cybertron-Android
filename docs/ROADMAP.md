# Roadmap

## M0 - Analysis (current)
- [ ] Run `tools/pe_report.py` on the game exe; record in `docs/ANALYSIS.md`
- [ ] Run `tools/game_inventory.py` on the install; record file types, middleware, sizes
- [ ] Decide: lifter approach for x86 (e.g. LLVM-based) vs dynarec-first
- [ ] List every imported Win32 / D3D9 / audio / video API (the "API surface")

## M1 - Launcher shell
- [x] Menu, settings storage, game-file importer (this repo's `app/`)
- [ ] First on-device test (any ARM64 phone)

## M2 - Platform layer
- [ ] PE loader (map the exe into memory at its preferred base)
- [ ] Minimal Win32 HLE: memory, files, threads, timers, TLS, critical sections
- [ ] Logging of every unimplemented import hit

## M3 - Renderer
- [ ] D3D9 device interface to GLES 3.0
- [ ] SM3 shader bytecode to GLSL ES translation
- [ ] Mali / Adreno / PowerVR test matrix

## M4 - CPU
- [ ] Run x86 code via an existing dynarec for bring-up
- [ ] Static recompilation prototype of one subsystem; measure vs dynarec

## M5 - Audio, input, video, polish
- [ ] Audio output, touch/gamepad input, cutscene playback
- [ ] Settings wired to the engine (resolution, graphics preset, FPS cap)

## Principles
- No game data or copyrighted binaries in the repo, ever.
- Keep CPU, platform and renderer independently testable.
- Do not add anything that defeats copy protection.
