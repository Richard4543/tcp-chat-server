# IDE Setup — IntelliJ IDEA, configured like a professional's machine

Work through this top to bottom once. Everything after this is muscle memory.
(Settings is `Ctrl+Alt+S` everywhere below.)

## 1. JDK and project SDK

A machine can hold many JDKs; the *project* declares which one it uses.

1. Settings → **Build, Execution, Deployment → Build Tools → Maven** — confirm
   Maven home. IntelliJ's bundled Maven is fine inside the IDE; our CLI Maven at
   `D:\Dev\tools\apache-maven-3.9.16` is what CI-like terminal builds use. Same
   `pom.xml` drives both — that's the point of Maven: **the build lives in the
   project, not in the IDE.**
2. When you create/open a project: **File → Project Structure → Project**:
   - SDK: `ms-17.0.20` (add it via *Add SDK → JDK* → `C:\Users\ricar\.jdks\ms-17.0.20` if missing)
   - Language level: **17**
3. Rule that saves hours of confusion: if IntelliJ and `mvn` on the command line
   disagree, trust `mvn` — it's what CI runs — and fix the IDE to match.

## 2. Git inside the IDE

Settings → **Version Control → Git**:

- **Update method: Rebase** (this makes the IDE's *Update Project* do
  `git pull --rebase`, the enterprise default — no accidental merge commits from
  pulling).
- Tick **Use credential helper** (Windows credential manager stores your GitHub
  token once).

Settings → **Version Control → Commit**:

- Enable **"Use non-modal commit interface"** if not already on (gives you the
  Commit tool window on the left sidebar).
- Under *Commit message inspections*: enable **blank line between subject and
  body** and **limit subject to 72 characters** — the IDE now enforces the same
  message rules a strict team lints for.

Optional but recommended: Settings → Version Control → Git → **Enable staging
area**. This makes IntelliJ behave like standard Git (an explicit staging step)
instead of its own changelist model — what you learn transfers 1:1 to any team.

## 3. Log the IDE into GitHub

Settings → **Version Control → GitHub** → `+` → **Log In via GitHub…** → browser
opens → authorize. After this the IDE can push, and the **Pull Requests** tool
window (left sidebar) can open and browse PRs without leaving the editor.

## 4. How professionals actually commit (the Commit tool window)

Open it with **`Ctrl+K`** (or the ✔ icon, left sidebar). This is the answer to
"not just through CMD":

1. **Review your own diff first.** Click each changed file — the diff viewer shows
   exactly what you're about to commit. Devs catch half their own mistakes here,
   before any reviewer sees them. Never commit a file you haven't re-read.
2. **Stage selectively.** Tick whole files, or — with the staging area on — stage
   *individual chunks* from the diff view. This is how one messy working session
   becomes two clean commits ("the feature" and "the unrelated typo fix").
3. **Write the message in two parts:**
   ```
   feat(server): accept a single client connection [CHAT-2]

   Opens a ServerSocket on the configured port and echoes each line
   back until the client disconnects.
   ```
   - Subject: `type(scope): imperative summary` — *"what applying this commit does"*.
     Types: `feat`, `fix`, `test`, `refactor`, `docs`, `chore`, `build`.
   - The `[CHAT-2]` key is what links the commit to the Jira ticket automatically.
   - Body (optional): the *why*, wrapped, after a blank line.
4. **Commit** (`Ctrl+K` again) — *not* "Commit and Push" every time. Committing is
   local and cheap; push when a coherent chunk is done or at least daily.
5. **Amend** (checkbox in the commit panel) — fixes "oops, forgot a file" on your
   *last, unpushed* commit. Never amend a commit that's already pushed to a PR
   under review.

Equivalents, because you should always know what the buttons do:

| IDE action | Git command |
|---|---|
| Tick file in Commit window | `git add <file>` |
| Stage one chunk | `git add -p` |
| Commit button | `git commit` |
| Amend checkbox | `git commit --amend` |
| Push (`Ctrl+Shift+K`) | `git push` |
| Update Project (`Ctrl+T`) | `git pull --rebase` |
| Branch widget → New Branch | `git switch -c <name>` |
| Git tool window → Log tab | `git log --graph` |

## 5. Branching from the IDE

The **branch widget** sits in the main toolbar (shows your current branch name).
Click it → **New Branch** → name it after the ticket: `CHAT-2-echo-server`.
Always branch from a fresh `main`: widget → `main` → *Update* → then new branch.
Switching branches is the same widget — IntelliJ stashes/restores open changes
automatically if you let it.

## 6. Opening a pull request from the IDE

Push the branch (`Ctrl+Shift+K`), then either:
- the notification IntelliJ pops ("Create Pull Request"), or
- **Pull Requests** tool window → `+`.

Fill title (`feat(server): echo server for a single client [CHAT-2]`) and the
description from the PR template. Review the *Files* tab — that's your last
self-review before a teammate sees it.

## 7. SonarQube for IDE (formerly SonarLint) — your pre-reviewer

Settings → **Plugins** → Marketplace → install **SonarQube for IDE** → restart.
It underlines bugs, code smells and security issues live in the editor — the same
rule engine many companies run server-side. Habit to build: **the squiggles are
review comments you get to fix before anyone else reads the code.** Don't suppress
a rule you can't explain.

## 8. Run and debug (you'll need this by CHAT-4)

- First run: right-click the class with `main` → **Run**. IntelliJ saves it as a
  run configuration (top toolbar) — edit it to add program args like a port number.
- Debugger, the 20% you'll use daily:
  - Click the gutter to set a **breakpoint**; run with the 🐞 icon.
  - `F8` step over · `F7` step into · `F9` continue.
  - The *Variables* pane shows live state; `Alt+F8` evaluates any expression.
  - For the chat server: put a breakpoint in the connection handler, connect a
    client, and watch a request travel through your code once. That one exercise
    teaches more than a day of print statements.

## 9. Daily keymap (learn these nine, ignore the rest for now)

| Keys | Does |
|---|---|
| `Ctrl+K` | Commit window |
| `Ctrl+Shift+K` | Push |
| `Ctrl+T` | Update project (pull --rebase) |
| `Alt+9` | Git log |
| `Shift Shift` | Search everywhere |
| `Alt+Enter` | Quick-fix whatever is underlined |
| `Ctrl+Alt+L` | Reformat file |
| `Shift+F10` / `Shift+F9` | Run / Debug last configuration |
| `Ctrl+E` | Recent files |
