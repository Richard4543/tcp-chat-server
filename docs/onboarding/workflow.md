# How We Work — the developer workflow handbook

This is the "how things are done here" doc every decent team has. Ours describes a
one-person team with an AI teammate (Claude: writes and points tickets, reviews
PRs, runs retros). The habits are exactly the ones a real team expects.

## 1. The board is the truth

Jira project **CHAT**, columns **To Do → In Progress → In Review → Done**.

Rules that make a board mean something:
- A ticket is *In Progress* only while you are actually working it — and you work
  **one ticket at a time**. (A PR waiting on review doesn't count as in progress.)
- Nothing reaches *Done* without a merged PR.
- If you discover new work while coding, you don't just do it — you **write a
  ticket** and keep your current change scoped. This single habit is most of what
  separates professional work from hobby work.

## 2. Story points — what they mean here

Points measure **relative size and uncertainty**, not hours:

| Pts | Feels like |
|---|---|
| 1 | Trivial and certain — a doc tweak, a config line |
| 2 | Small, clear, one sitting |
| 3 | A real task; touches a few files; needs tests |
| 5 | Big or uncertain; multiple sittings; design choices to make |
| 8 | Too big — should probably be split before starting |

Ritual: **before** starting a ticket, glance at its points and guess your own.
**After** merging, note whether it felt bigger or smaller. At retro we compare.
That feedback loop — not any formula — is how developers learn estimation.

## 3. The daily loop

1. **Pick** the top ticket from *To Do* you can finish. Drag to *In Progress*.
2. **Branch** from a fresh `main` (IDE: branch widget → main → Update → New
   Branch): `CHAT-4-multi-client-broadcast`. Ticket key first, always — it links
   the branch to the ticket automatically.
3. **Work in small, compiling steps.** Each time a coherent step works — the test
   passes, the method behaves — commit it (see §4). Push at least daily; pushed
   code is backed up code.
4. **PR when the acceptance criteria are met** — not before, not "and one more
   thing after". Fill the template. Re-read your own diff in the Files tab first.
5. Drag the ticket to *In Review*, ask Claude for review, and **start something
   else** (§6) — never sit refreshing the PR.
6. **Address review with new commits** on the same branch. Never force-push a
   branch under review: it rewrites the history the reviewer already read.
7. CI green + review resolved → **squash-merge** (one clean commit lands on
   `main`, your messy WIP history stays on the branch), delete the branch, drag
   the ticket to *Done*.

## 4. Commit discipline — the answer to "more commits?"

**When:** every time a coherent step is done and the code compiles and tests pass.
On a feature branch that's typically every 20–60 minutes of progress. Too many
small commits is a non-problem (squash-merge tidies them); giant end-of-day
commits are a real problem — they can't be reviewed, reverted, or understood.

**What makes a good commit:** one logical change. "Add the connection handler" and
"fix typo in README" are two commits, even if you did them in the same hour —
that's what selective staging in the IDE is for.

**Message anatomy** (enforced by the IDE inspections we configured):

```
type(scope): what applying this commit does [CHAT-n]

Optional body: the why — the constraint, the tradeoff, the bug it prevents.
```

`feat` `fix` `test` `refactor` `docs` `chore` `build` — if no single type fits,
the commit is doing too much; split it.

**main is sacred:** nobody commits to `main` directly — not even in a solo repo.
Branch protection enforces it, but the habit matters more than the enforcement.

## 5. Pull requests — small, described, self-reviewed

- **Size:** a reviewable PR is under ~400 changed lines. If a ticket produces
  more, the ticket was too big — note it for retro.
- **Description:** what changed, why, how you tested it, anything you're unsure
  about. Flagging your own doubts ("not sure this handles disconnect mid-message")
  is a senior habit, not a weakness — it aims the reviewer at the risk.
- **Self-review first:** read your own diff top to bottom before requesting
  review. Fix what embarrasses you. The reviewer's time is the scarcest resource
  on a team; spending your own first is the professional courtesy.
- **Respond to every comment** — fix it, or say why you disagree (politely,
  with a reason). Silently ignoring a comment is the one review behaviour that
  genuinely damages trust on teams.

## 6. Between tickets — what devs do while waiting

A PR is out for review and you're idle? Real developers never are:

- Pull a **chore from the Engineering-health epic**: raise test coverage, extract
  a duplicated block, update a dependency, improve the README.
- **Groom the backlog:** read the next 2–3 tickets; if one is unclear or too big,
  comment on it / split it now — future-you at 9am will be grateful.
- **Read code.** Pick a class you didn't write recently and read it end to end.
  On a team you'd read teammates' merged PRs; here, read your own from two weeks
  ago (it will feel like someone else wrote it — that feeling is the lesson).
- **Write the missing test** you noticed while doing the last ticket but scoped out.

Start the *next feature ticket* only when nothing above is worth doing — juggling
two open feature branches is how solo devs end up with merge conflicts against
themselves.

## 7. Branch model vs fork model — when each is used

- **Feature branches on a shared repo** (what we do): everyone with write access
  branches in the same repository and merges via PR. This is the standard inside
  companies.
- **Fork model:** you don't have write access — open source, or another team's
  repo. You copy ("fork") the repo to your account, work there, and PR *across*
  repos: `your-fork:branch → upstream:main`. Two remotes: `origin` (your fork,
  you push) and `upstream` (theirs, you fetch and rebase onto).

You'll practice the fork model for real against
`firstcontributions/first-contributions` — a repo maintained specifically so
newcomers can make a genuine fork → PR → merged contribution.

## 8. Definition of Done

A ticket is Done when: acceptance criteria met · tests cover the new behaviour ·
CI green · review comments resolved · squash-merged · branch deleted · ticket
moved. Not one item earlier. "Done except..." is the most expensive phrase in
software.
