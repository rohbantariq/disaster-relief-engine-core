# Disaster Relief Management System - Sandbox Environment

**Design and Analysis of Algorithms — Semester Project**
Shaheed Zulfikar Ali Bhutto Institute of Science & Technology

Team: Naushaba Asif, Rohban Tariq (BSCS 4I)

## Overview

During a multi-site humanitarian crisis, relief operations are often crippled by poor resource distribution — low-risk areas absorb stock while high-severity zones go under-served, and there's no reliable way to audit or roll back a bad dispatch decision.

This project is an in-memory disaster relief coordination sandbox that:
- automatically ranks disaster-hit regions by severity using a custom Max-Priority Queue,
- recommends supply quantities using a bounded greedy allocation algorithm,
- lets an operator confirm or override those recommendations,
- and tracks every dispatch on a history stack so the last batch can be undone in constant/linear time.

Everything runs in RAM for a single session — there is no disk I/O in the hot path, which keeps the system fast and keeps the focus on the algorithms and data structures rather than persistence.

## Features

- **Priority-based area intake** — register a disaster area with population, injury count, and a 1–10 severity score; it's automatically inserted into sorted position.
- **Greedy supply recommendations** — food, first-aid, and water quantities are computed from population/injuries and scaled by an "Emergency Urgency Ratio," then capped at available warehouse stock.
- **Manual override with validation** — operators can accept the suggested numbers or enter custom amounts; invalid or over-stock entries are rejected without touching the queue.
- **Undo / rollback** — the most recent dispatch batch can be reverted, restoring both inventory levels and the area's position in the queue.
- **Inventory management** — view current stock and restock any supply item.
- **Two interfaces** — a console menu (`DisasterReliefSystem`) and a Swing GUI (`DisasterReliefGUI`) built on the same backend classes.
- **Audit logging** — every dispatch, undo, and inventory change is timestamped and printed via `DataLogger`.

## Project Structure

```
├── DisasterReliefSystem.java   # Console entry point (main menu loop)
├── DisasterReliefGUI.java      # Swing GUI entry point
├── AreaManager.java            # Wraps the PriorityQueue of DisasterAreas
├── DispatchManager.java        # Greedy allocation engine + dispatch history stack
├── InventoryManager.java       # Warehouse stock tracking
├── DataLogger.java             # Timestamped audit/dispatch/inventory logging
├── DisasterArea.java           # Region model (name, population, injuries, severity)
├── Supply.java                 # Supply item model (name, quantity)
├── DispatchRecord.java         # Record of a single dispatched supply, tagged with a batch ID
├── PriorityQueue.java          # Custom linked-list-based max-priority queue
├── PriorityNode.java           # Node type for PriorityQueue
├── Queue.java                  # Custom singly-linked queue
├── Stack.java                  # Custom singly-linked LIFO stack (dispatch history)
└── Node.java                   # Generic linked-list node used by Queue/Stack
```

## Data Structures & Algorithms

### Custom Max-Priority Queue (`PriorityQueue.java`)
A linked chain of `PriorityNode<T>` objects. `enqueuePriority` walks the list and inserts each new node into its exact sorted position by severity score, so the highest-severity area is always at the head. `dequeue`/`peek` are O(1) since the list is kept pre-sorted.

### Custom Stack (`Stack.java`)
A singly-linked LIFO structure used as the dispatch history timeline. Each `push`/`pop` is O(1), and consecutive records sharing a batch ID are popped together to undo an entire dispatch transaction at once.

### Greedy Priority Scheduling
The system always serves the region at the head of the priority queue — the highest severity score — with no additional bookkeeping.

### Bounded Greedy Resource Allocation (`DispatchManager.calculateRecommendedSupplies`)
Recommendations are computed from an **Emergency Urgency Ratio** = `severityScore / 10.0`, applied as:

| Supply | Formula |
|---|---|
| Food Boxes | `population × 0.1 × urgencyRatio` |
| First Aid Kits | `injuries × 2.0 × urgencyRatio` |
| Water Bottles | `population × 0.3 × urgencyRatio` |

Each result is then clamped: `min(recommendedQuantity, availableWarehouseStock)`. Manual overrides that exceed availability throw an exception and leave the queue untouched.

## Time & Space Complexity

| Operation | Complexity | Notes |
|---|---|---|
| `addArea` (insertion) | O(N) worst, O(1) best | Worst case: lowest severity, traverses full list. Best case: highest severity, updates head directly |
| `serveNextArea` (extraction) | O(1) | List is pre-sorted, no scanning needed |
| `calculateRecommendedSupplies` | O(1) | Fixed arithmetic, no loops |
| `undoLastDispatch` | O(B) | B = number of records in the most recent batch |
| Queue storage | O(N) space | N = active disaster areas |
| History stack storage | O(H) space | H = dispatch records processed this session |

## How to Run

**Console version:**
```bash
javac *.java
java DisasterReliefSystem
```

**GUI version:**
```bash
javac *.java
java DisasterReliefGUI
```

Both entry points share the same `AreaManager`, `InventoryManager`, and `DispatchManager` backend, so they behave identically underneath.

### Console Menu Options
1. Add Disaster Area
2. Process Next Pending Disaster Area (view greedy recommendations, dispatch or override)
3. Undo Last Dispatch
4. Show Inventory State
5. Restock Supplies
6. Show Pending Disaster Areas (Queue State)
7. Exit

## Testing Summary

The system was validated against a preloaded dataset (Lahore: severity 8, Korangi: severity 7, Larkana: severity 4, Landhi: severity 2, plus others):

- **Priority ordering** — Lahore correctly surfaced at position 1 due to its highest severity score.
- **Greedy recommendation + validation gate** — generated correct suggested quantities for Lahore; an intentionally oversized manual entry (600,000 food boxes) was correctly rejected without disturbing the queue.
- **Undo/rollback** — reverting a dispatch correctly restored both inventory counts and the area's position at the head of the queue.

## Limitations / Notes

- The session is fully in-memory — no persistence between runs (`saveHistory`, `loadHistory`, `saveInventoryState`, `loadInventory` are present as stubs for future extension).
- Demo data (regions and starting inventory) is seeded on startup for testing purposes.

## References

1. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.
2. Sahni, S. (2005). *Data Structures, Algorithms, and Applications in Java* (2nd ed.). Silicon Press.
