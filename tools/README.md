# Analysis tools

Run these on **your own** game install and paste the text output into `docs/ANALYSIS.md`
(or an issue). They only read file metadata; nothing is modified or uploaded.

```
pip install pefile
python tools/pe_report.py  "C:\path\to\GameExecutable.exe"
python tools/game_inventory.py "C:\path\to\game\install\folder"
```
Add `--full` to `pe_report.py` to list every imported function name.
