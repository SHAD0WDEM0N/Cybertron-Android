#!/usr/bin/env python3
"""Summarise a game install folder: top-level layout, file types, sizes, executables/DLLs.

Read-only, metadata only. Usage: python game_inventory.py <install_folder>
"""
import os
import sys
from collections import defaultdict


def human(n: int) -> str:
    for unit in ("B", "KB", "MB", "GB"):
        if n < 1024 or unit == "GB":
            return f"{n:.1f} {unit}"
        n /= 1024
    return str(n)


def main() -> None:
    if len(sys.argv) != 2 or not os.path.isdir(sys.argv[1]):
        sys.exit(__doc__)
    root = sys.argv[1]
    by_ext = defaultdict(lambda: [0, 0])
    top = defaultdict(lambda: [0, 0])
    binaries = []
    total = [0, 0]
    for dirpath, _, files in os.walk(root):
        rel = os.path.relpath(dirpath, root)
        top_name = "." if rel == "." else rel.split(os.sep)[0]
        for f in files:
            p = os.path.join(dirpath, f)
            try:
                size = os.path.getsize(p)
            except OSError:
                continue
            ext = os.path.splitext(f)[1].lower() or "(none)"
            by_ext[ext][0] += 1
            by_ext[ext][1] += size
            top[top_name][0] += 1
            top[top_name][1] += size
            total[0] += 1
            total[1] += size
            if ext in (".exe", ".dll", ".sys"):
                binaries.append((os.path.relpath(p, root), size))

    print(f"Install root: {root}")
    print(f"Total: {total[0]:,} files, {human(total[1])}\n")
    print("Top-level folders (files, size):")
    for k, (c, s) in sorted(top.items(), key=lambda kv: -kv[1][1]):
        print(f"  {k:<30} {c:>7,} files  {human(s):>10}")
    print("\nFile types (count, size):")
    for k, (c, s) in sorted(by_ext.items(), key=lambda kv: -kv[1][1])[:40]:
        print(f"  {k:<10} {c:>7,} files  {human(s):>10}")
    print("\nExecutables / libraries:")
    for rel, size in sorted(binaries):
        print(f"  {rel}  ({human(size)})")


if __name__ == "__main__":
    main()
