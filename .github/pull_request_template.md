<!--
Title format: type(scope): imperative summary [SCRUM-n]
e.g.  feat(server): accept a single client connection [SCRUM-7]
-->

## What changed

<!-- One or two sentences. What does this PR do, from a user's point of view? -->

## Why

<!-- Link the ticket and give the reason. The ticket key auto-links to Jira. -->

Closes SCRUM-

## How I tested it

<!--
Be specific. "Tested it" tells a reviewer nothing.
e.g. "Ran the server, connected with telnet on 5000, typed three lines and saw
each echoed. Disconnected with Ctrl+] and confirmed the server exited cleanly."
-->

## Checklist

- [ ] Acceptance criteria in the ticket are all met
- [ ] `mvn verify` passes locally
- [ ] I re-read my own diff before requesting review
- [ ] Public classes and methods have Javadoc
- [ ] No commented-out code, no debug prints left behind
- [ ] Resources (sockets, streams) are closed on every path

## Anything you're unsure about

<!--
Optional, and a sign of seniority rather than weakness. Flagging your own doubts
aims the reviewer at the risky part.
e.g. "Not sure the executor bound of 50 is right — open to a different number."
-->
