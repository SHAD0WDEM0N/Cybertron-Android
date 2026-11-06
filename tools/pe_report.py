#!/usr/bin/env python3
"""Summarise a Windows PE executable: hashes, headers, sections, entropy, imports.

Read-only. Helps decide how a binary can be analysed/ported (packed? which APIs?).
Usage: python pe_report.py <file.exe> [--full]
"""
import hashlib
import math
import sys
from collections import Counter

try:
    import pefile
except ImportError:
    sys.exit("Missing dependency: pip install pefile")


def entropy(data: bytes) -> float:
    if not data:
        return 0.0
    counts = Counter(data)
    n = len(data)
    return -sum(c / n * math.log2(c / n) for c in counts.values())


def main() -> None:
    args = [a for a in sys.argv[1:] if not a.startswith("--")]
    full = "--full" in sys.argv
    if len(args) != 1:
        sys.exit(__doc__)
    path = args[0]
    raw = open(path, "rb").read()
    pe = pefile.PE(data=raw, fast_load=False)

    print(f"File:      {path}")
    print(f"Size:      {len(raw):,} bytes")
    print(f"MD5:       {hashlib.md5(raw).hexdigest()}")
    print(f"SHA-256:   {hashlib.sha256(raw).hexdigest()}")
    machine = pe.FILE_HEADER.Machine
    print(f"Machine:   {hex(machine)} ({'x86 32-bit' if machine == 0x14c else 'x64' if machine == 0x8664 else 'other'})")
    print(f"Subsystem: {pe.OPTIONAL_HEADER.Subsystem}")
    print(f"ImageBase: {hex(pe.OPTIONAL_HEADER.ImageBase)}")
    print(f"EntryPt:   {hex(pe.OPTIONAL_HEADER.AddressOfEntryPoint)}")
    print(f"Timestamp: {pe.FILE_HEADER.TimeDateStamp}")
    print(f"LargeAddrAware: {bool(pe.FILE_HEADER.Characteristics & 0x20)}")
    print(f"ASLR (DYNAMIC_BASE): {bool(pe.OPTIONAL_HEADER.DllCharacteristics & 0x40)}")

    print("\nSections (name, virtual size, raw size, entropy, flags):")
    suspicious = []
    for s in pe.sections:
        name = s.Name.rstrip(b"\x00").decode(errors="replace")
        e = entropy(s.get_data())
        flags = []
        if s.Characteristics & 0x20000000: flags.append("X")
        if s.Characteristics & 0x40000000: flags.append("R")
        if s.Characteristics & 0x80000000: flags.append("W")
        print(f"  {name:<10} vsize={s.Misc_VirtualSize:>10,} raw={s.SizeOfRawData:>10,} entropy={e:5.2f} {''.join(flags)}")
        if e > 7.2 and s.SizeOfRawData > 4096:
            suspicious.append(name)
    if suspicious:
        print(f"\n  NOTE: high-entropy sections {suspicious} suggest packing/encryption/wrapping.")

    tls = getattr(pe, "DIRECTORY_ENTRY_TLS", None)
    print(f"\nTLS directory present: {tls is not None}")

    print("\nImports (DLL: function count):")
    imports = getattr(pe, "DIRECTORY_ENTRY_IMPORT", [])
    for entry in imports:
        dll = entry.dll.decode(errors="replace")
        print(f"  {dll}: {len(entry.imports)}")
        if full:
            for imp in entry.imports:
                nm = imp.name.decode(errors="replace") if imp.name else f"ordinal_{imp.ordinal}"
                print(f"      {nm}")
    if not imports:
        print("  (none found - may be packed)")

    delay = getattr(pe, "DIRECTORY_ENTRY_DELAY_IMPORT", [])
    if delay:
        print("\nDelay-load imports:")
        for entry in delay:
            print(f"  {entry.dll.decode(errors='replace')}: {len(entry.imports)}")


if __name__ == "__main__":
    main()
