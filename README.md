# Smart Expense Tracker

## 📱 App Overview
Smart Expense Tracker is an Android app built with **Jetpack Compose**, **Material 3**, and **Room** to manage daily expenses efficiently.  
It supports expense entry with receipt images, viewing/filtering expenses, visual reports for the last 7 days, CSV export, and a light/dark/system theme switcher.

---

## 🤖 AI Usage Summary
I used AI tools (ChatGPT / GitHub Copilot) to speed up scaffolding and boilerplate creation:
- Generated MVVM + Hilt Compose screen templates and navigation wiring.
- Created Room DAO queries and repository methods.
- Produced Compose Canvas chart code for daily and category-based reports.
- Drafted CSV export logic and README structure.  
All generated code was reviewed, tested, and customized to meet the brief requirements.

---

## 📄 Prompt Logs
*(Key prompts given to AI during development)*

> “Got this as an assignment for a job… create a plan by hours from H0–32”  
> “Let’s start on entry screen quickly”  
> “We are using Room — update repo with using this”  
> “Now let’s move on to H7–11 — Screen 2: Expense List”  
> “Add CSV export + share intent to list screen”  
> “H11–15 — Screen 3: Expense Report (last 7 days)”  
> “Theme switcher (Light/Dark) — persist using DataStore”  
> “Now let’s move on to the ReadME file. Also need this”

---

## ✅ Checklist of Features Implemented
- **Expense Entry Screen**
  - Title, amount (₹), category (Staff, Travel, Food, Utility)
  - Optional notes (≤ 100 chars) and receipt image
  - Validation + non-blocking duplicate detection
  - Live “Total Spent Today” indicator
- **Expense List Screen**
  - Default: Today’s expenses
  - Date navigation (prev/next/day picker)
  - Group by time or category with sticky headers
  - Header stats (count, total) + empty state
  - CSV export (today or last 7 days) with share intent
- **Expense Report Screen**
  - Last 7 days daily totals (bar chart)
  - Totals per category (horizontal bars)
  - Axis labels for dates and currency
  - Optional CSV export
- **App-wide**
  - Room persistence, Hilt DI, MVVM
  - Light/Dark/System theme switcher (DataStore)
  - State restoration for list/report
  - Modern Material 3 UI

---

## 📦 APK Download
[📥 Download v1.0 APK (Google Drive)](https://drive.google.com/file/d/17KZ1aNMgqAg-BW57yS1qLRFHA-MKBn6k/view?usp=sharing)

---

## 📸 Screenshots
<img width="1280" height="2856" alt="Screenshot_20250810_222541" src="https://github.com/user-attachments/assets/b5a9bf99-2ed7-466f-9d94-24c0303f2435" />
<img width="1280" height="2856" alt="Screenshot_20250810_222648" src="https://github.com/user-attachments/assets/41e7067d-4c2f-47f8-a4d8-8c3013670c3b" />
<img width="1280" height="2856" alt="Screenshot_20250810_222718" src="https://github.com/user-attachments/assets/47797668-c3df-41a0-bad4-5f3836883149" />
<img width="1280" height="2856" alt="Screenshot_20250810_222738" src="https://github.com/user-attachments/assets/3f926f66-f580-4f0e-a0b0-6252c267a3af" />
<img width="1280" height="2856" alt="Screenshot_20250810_222748" src="https://github.com/user-attachments/assets/f6c46a4e-9c94-4e12-a344-7ab211aece6d" />
<img width="1280" height="2856" alt="Screenshot_20250810_222805" src="https://github.com/user-attachments/assets/66295f7f-e7ef-475d-adc4-208e50d368b1" />
<img width="1280" height="2856" alt="Screenshot_20250810_222815" src="https://github.com/user-attachments/assets/574fd9f5-934a-4e23-90e5-d5613672900b" />
<img width="1280" height="2856" alt="Screenshot_20250810_222826" src="https://github.com/user-attachments/assets/6c64bd16-5239-4bbd-b24f-4ff2ca8bc56a" />
<img width="1280" height="2856" alt="Screenshot_20250810_222841" src="https://github.com/user-attachments/assets/59cc6810-fc41-406d-9654-2d8f1e4aaebd" />
<img width="1280" height="2856" alt="Screenshot_20250810_222851" src="https://github.com/user-attachments/assets/9ed22dc9-0d01-4434-a1a5-2092101639c6" />
<img width="1280" height="2856" alt="Screenshot_20250810_222904" src="https://github.com/user-attachments/assets/edf4bcbb-3ec8-4240-8907-6d66834f25d4" />


---
