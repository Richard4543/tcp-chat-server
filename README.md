# TCP Chat Server

[![CI](https://github.com/Richard4543/tcp-chat-server/actions/workflows/ci.yml/badge.svg)](https://github.com/Richard4543/tcp-chat-server/actions/workflows/ci.yml)

A multi-client chat server and terminal client written in Java 17, built on raw
TCP sockets — no frameworks.

## Why this project exists

Two goals at once: build something that genuinely works over a network, and do it
using the working practices of a professional engineering team — tickets with
estimates, feature branches, pull requests with code review, and an automated
build gate on every change.

The repository is therefore as much a record of *how* the work was done as of
what was built. Every feature arrived through a reviewed pull request; nothing was
pushed straight to `main`.

## Status

Sprint 1 in progress. See the `docs/onboarding/` directory for how the work is
organised.

## Requirements

- JDK 17 or newer
- Maven 3.9+

## Build

```bash
mvn verify
```

## Run

Usage instructions land with the first server ticket (SCRUM-7).

## Architecture

Documented as the code arrives — see SCRUM-12.

## How this project is worked on

- [Developer workflow](docs/onboarding/workflow.md) — the daily loop, commit
  discipline, pull request etiquette, definition of done
- [IDE setup](docs/onboarding/ide-setup.md) — IntelliJ configured for this project
